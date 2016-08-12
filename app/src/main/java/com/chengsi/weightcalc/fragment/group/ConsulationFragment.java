package com.chengsi.weightcalc.fragment.group;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.TypeReference;
import com.chengsi.weightcalc.MyApplication;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.activity.ConsultDoctorListActivity;
import com.chengsi.weightcalc.activity.ConsultListActivity;
import com.chengsi.weightcalc.activity.HeadPhotoActivity;
import com.chengsi.weightcalc.bean.BaseBean;
import com.chengsi.weightcalc.bean.DoctorBean;
import com.chengsi.weightcalc.bean.FileUploadBean;
import com.chengsi.weightcalc.bean.LocalImageInfo;
import com.chengsi.weightcalc.fragment.BaseFragment;
import com.chengsi.weightcalc.http.JDHttpClient;
import com.chengsi.weightcalc.http.JDHttpResponseHandler;
import com.chengsi.weightcalc.listener.CaptureImageFinishedListener;
import com.chengsi.weightcalc.listener.CaptureImageLisenter;
import com.chengsi.weightcalc.listener.OnContinuousClickListener;
import com.chengsi.weightcalc.manager.AlbumManager;
import com.chengsi.weightcalc.utils.BitmapUtils;
import com.chengsi.weightcalc.utils.CaptureMedia;
import com.chengsi.weightcalc.utils.JDUtils;
import com.chengsi.weightcalc.utils.UIUtils;
import com.chengsi.weightcalc.utils.imageloader.ImageLoaderUtils;
import com.chengsi.weightcalc.widget.JDLoadingView;
import com.chengsi.weightcalc.widget.PreferenceRightDetailView;
import com.chengsi.weightcalc.widget.dialog.SimpleSelectDialog;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.jiadao.corelibs.utils.ListUtils;

public class ConsulationFragment extends BaseFragment implements CaptureImageFinishedListener{

    private static final int REQUEST_CODE_SELECT_DOCTOR = 0x1000;

    @InjectView(R.id.tv_consult_doctor)
    PreferenceRightDetailView itemConsultDoctorName;
    @InjectView(R.id.tv_consult_privacy)
    PreferenceRightDetailView itemConsultPrivacy;

    private Dialog loadingDialog;

    GridAdapter gridAdapter;

    private List<LocalImageInfo> localImageList = new ArrayList<>();
    private List<String> remoteImageList = new ArrayList<>();

    private int status = 1;

    private static final List<String> privacyList = new ArrayList<>();

    @InjectView(R.id.et_consult_content)
    EditText etContent;
    @InjectView(R.id.et_consult_title)
    EditText etTitle;

    private DoctorBean doctorBean;

    public ConsulationFragment() {
        if (ListUtils.isEmpty(privacyList)){
            privacyList.add("公开");
            privacyList.add("其他人不可见");
        }
    }

    @Override
    public View getContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consulation, container, false);
        ButterKnife.inject(this, view);
        itemConsultDoctorName.setOnClickListener(new OnContinuousClickListener() {
            @Override
            public void onContinuousClick(View v) {
                Intent intent = new Intent(getActivity(), ConsultDoctorListActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT_DOCTOR);
            }
        });
        itemConsultPrivacy.setOnClickListener(new OnContinuousClickListener() {
            @Override
            public void onContinuousClick(View v) {
                final SimpleSelectDialog dialog = SimpleSelectDialog.getInstance();
                dialog.showSelect(getFragmentManager(), privacyList, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialog.dismiss();
                        itemConsultPrivacy.setContent(privacyList.get(position));
                        if (position == 0) {
                            status = 1;
                        } else {
                            status = 0;
                        }
                    }
                });
            }
        });

//        if (fanrApp.userManager.getUserBean().getDoctorBean() != null){
//            doctorBean = fanrApp.userManager.getUserBean().getDoctorBean();
//            itemConsultDoctorName.setContent(doctorBean.getRealName());
//        }
        gridAdapter = new GridAdapter();
        return view;
    }

    @OnClick(R.id.btn_submit)
    void submit(){
        JDUtils.hideIME(getActivity());
        if (doctorBean == null){
            showToast("请选择您要咨询的医生");
            return;
        }
        if (TextUtils.isEmpty(etContent.getText().toString().trim())){
            showToast("请输入您要咨询的内容");
            return;
        }
        if (TextUtils.isEmpty(etTitle.getText().toString().trim())){
            showToast("请输入您要咨询的标题");
            return;
        }
        setLoadingState(JDLoadingView.STATE_LOADING);
        if (!ListUtils.isEmpty(localImageList)){
            fanrApp.getWorkHandler().post(new Runnable() {
                @Override
                public void run() {
                     for (LocalImageInfo info : localImageList){
                         try{
                             File file = BitmapUtils.compressFile(info.imagePath, -1);
                             if (file != null && file.exists()){
                                 info.imagePath = file.getAbsolutePath();
                             }
                         }catch (Exception e){

                         }
                     }

                    fanrApp.getMainHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            List<File> fileList = new ArrayList<>();
                            for (LocalImageInfo info : localImageList){
                                fileList.add(new File(info.imagePath));
                            }
                            JDHttpClient.getInstance().reqUploadPhotos(getActivity(), fileList, new JDHttpResponseHandler<List<FileUploadBean>>(new TypeReference<BaseBean<List<FileUploadBean>>>() {
                            }) {
                                @Override
                                public void onRequestCallback(BaseBean<List<FileUploadBean>> result) {
                                    super.onRequestCallback(result);
                                    if (result.isSuccess()) {
                                        remoteImageList.clear();
                                        String url = "";
                                        for (FileUploadBean bean : result.getData()){
                                            remoteImageList.add(bean.getPath());
                                            url += bean.getPath();
                                            url += ",";
                                        }
                                        if (url.contains(",")){
                                            url = url.substring(0, url.length() - 1);
                                        }
                                        submitConsult(url);
                                    } else {
                                        dismissLoadingView();
                                        showToast(result.getMessage());
                                    }
                                }
                            });
                        }
                    });
                }
            });
        }else{
            submitConsult("");
        }

    }

    private void submitConsult(String imageUrl){
        JDHttpClient.getInstance().reqSendConsult(getActivity(), String.valueOf(doctorBean.getId()), etTitle.getText().toString(), etContent.getText().toString(), status, imageUrl, new JDHttpResponseHandler<String>(new TypeReference<BaseBean<String>>() {
        }) {
            @Override
            public void onRequestCallback(BaseBean<String> result) {
                super.onRequestCallback(result);
                dismissLoadingView();
                if (result.isSuccess()) {
                    localImageList.clear();
                    remoteImageList.clear();
                    gridAdapter.notifyDataSetChanged();
                    etContent.setText("");
                    etTitle.setText("");
                    itemConsultDoctorName.setContent("");
                    doctorBean = null;
                    itemConsultPrivacy.setContent(privacyList.get(0));
                    status = 1;
                    showHistory();
                }
                showToast(result.getMessage());
            }
        });
    }

    @OnClick(R.id.tv_show_history)
    void showHistory(){
        Intent intent = new Intent(getActivity(), ConsultListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_DOCTOR && resultCode == Activity.RESULT_OK) {
            doctorBean = (DoctorBean) data.getSerializableExtra(ConsultDoctorListActivity.DOCTOR_ITEM_SELECTED);
            if (doctorBean != null) {
                itemConsultDoctorName.setContent(doctorBean.getRealName());
            }
        }
    }

    private class GridAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return localImageList.size() + 1 > 12 ? 12 : localImageList.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_consult_image, null);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_pic);
            ImageView ivDel = (ImageView) convertView.findViewById(R.id.iv_del);

            if (position == localImageList.size()){
                imageView.setImageResource(R.drawable.zone_add_pic_nor);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showSelectPhotoDialog();
                    }
                });
                ivDel.setVisibility(View.GONE);
            }else{
                ivDel.setVisibility(View.VISIBLE);
                ImageLoaderUtils.displayImageForIv(imageView, ImageDownloader.Scheme.FILE.wrap(localImageList.get(position).imagePath));
                ivDel.setOnClickListener(new OnContinuousClickListener() {
                    @Override
                    public void onContinuousClick(View v) {
                        localImageList.remove(position);
                        gridAdapter.notifyDataSetChanged();
                    }
                });
            }
            return convertView;
        }
    }

    public final class ActivityCaptureImageListenter extends CaptureImageLisenter {

        private int picWidth;
        private Uri fileUri;
        private Context context;
        private CaptureImageFinishedListener listener;
        public ActivityCaptureImageListenter(Context activity, CaptureImageFinishedListener listener) {
            context = activity;
            this.listener = listener;
        }

        public int getPicWidth() {
            return picWidth;
        }

        public CaptureImageLisenter setPicWidth(int picSize) {
            this.picWidth = picSize;
            return this;
        }

        public Uri getFileUri() {
            return fileUri;
        }

        // controller.onCaptureImageFinishedListener(myBitmap);
        public CaptureImageLisenter setFileUri(Uri fileUri) {
            this.fileUri = fileUri;
            return this;
        }

        @Override
        public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
            //String errorMsg = null;

            switch (resultCode) {
                case BitmapUtils.RESULT_OK:
                    switch (requestCode) {
                        case CaptureMedia.KEY_REQUEST_ALBUM:// 相册
                            Intent ALBUMIntent  = new Intent(context, HeadPhotoActivity.class);
                            ALBUMIntent.putExtra(BitmapUtils.BITMATSRC, data.getData());
                            ALBUMIntent.putExtra(BitmapUtils.OBTAINTYPE, BitmapUtils.ALBUM);
                            ALBUMIntent.putExtra(BitmapUtils.RATIO, (double) 1);//表示相册，预览显示重选
                            startActivityForResult(ALBUMIntent, BitmapUtils.ALBUM);
                            break;
                        case CaptureMedia.KEY_REQUEST_CAREMA:
//                            Intent CAREMAIntent  = new Intent(context, HeadPhotoActivity.class);
//                            CAREMAIntent.putExtra(BitmapUtils.BITMATSRC, this.fileUri);
//                            CAREMAIntent.putExtra(BitmapUtils.OBTAINTYPE, BitmapUtils.CAMERA);//表示相机，预览显示重拍
//                            startActivityForResult(CAREMAIntent,BitmapUtils.CAMERA);
                            String filePath = JDUtils.getRealFilePath(getActivity(), this.fileUri);
                            listener.onCaptureImageFinishedListener(filePath);
                            break;
                        case BitmapUtils.ALBUM:
                        case BitmapUtils.CAMERA:
                            String path = data.getStringExtra(BitmapUtils.BITMATSRC);
                            listener.onCaptureImageFinishedListener(path);
                            break;
                        case CaptureMedia.RESULT_CANCELED:
                            break;


                        default:
                            //errorMsg = context.getString(R.string.get_pic_falied);
                            break;
                    }

                case BitmapUtils.RESULT_CANCELED:
                    //取消
                    break;
                //重选
                case BitmapUtils.RESULT_RE_SELECT_PHOTO :
                    if(requestCode == BitmapUtils.ALBUM){
                        CaptureMedia.captureImage(CaptureMedia.KEY_TYPE_ALBUM, JDUtils.getRootActivity((Activity)context), new ActivityCaptureImageListenter(context, ConsulationFragment.this));
                    }else{
                        CaptureMedia.captureImage(CaptureMedia.KEY_TYPE_CAREMA, JDUtils.getRootActivity((Activity) context), new ActivityCaptureImageListenter(context, ConsulationFragment.this));
                    }
                    break;
            }
            return true;
        }

    }

    private AlertDialog selectPhotoDialog;
    private View selectPhotoContentView;
    private void showSelectPhotoDialog(){
        if (selectPhotoDialog == null) {
            selectPhotoContentView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_select_photo, null);
            selectPhotoDialog = new AlertDialog.Builder(getActivity()).create();
            selectPhotoContentView.findViewById(R.id.iv_album_selected).setOnClickListener(onDialogBtnClick);
            selectPhotoContentView.findViewById(R.id.iv_camera_selected).setOnClickListener(onDialogBtnClick);
            selectPhotoDialog.setCanceledOnTouchOutside(true);
        }

        selectPhotoDialog.show();
        selectPhotoDialog.setContentView(selectPhotoContentView);
        selectPhotoDialog.getWindow().setLayout(UIUtils.getScreenSize(getActivity()).x - 60, UIUtils.convertDpToPixel(180, getActivity()));
    }

    @Override
    public void onCaptureImageFinishedListener(final String path) {
        setLoadingState(JDLoadingView.STATE_LOADING);
        fanrApp.getWorkHandler().post(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(path)) {
                    return;
                }
                String mPath = null;
                String compress = null;
                try{
                    File file = BitmapUtils.compressFile(mPath, -1);
                    if (file != null && file.exists()){
                        compress = file.getAbsolutePath();
                    }
                }catch (Exception e){

                }
                if (compress != null && !TextUtils.isEmpty(compress)) {
                    mPath = compress;
                } else {
                    mPath = path;
                }
                final String imagePath = mPath;
                File file = new File(imagePath);
                if (file.exists()) {
                    LocalImageInfo info = new LocalImageInfo();
                    info.imagePath = imagePath;
                    info.isSelected = true;
                    info.dateModified = String.valueOf(file.lastModified());
                    fanrApp.albumManager.addPicFromCamera(info);
                    localImageList = fanrApp.albumManager.getSelectList();
                }
                dismissLoadingView();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (gridAdapter != null && !isDetached()){
                            gridAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });

    }
    private View.OnClickListener onDialogBtnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_album_selected:
                    MyApplication.getInstance().albumManager.selectMultiPic(getActivity(), 12, true, new AlbumManager.OnSelectMultiPicListener() {
                        @Override
                        public void onFinished(boolean cancel, ArrayList<LocalImageInfo> selectList) {
                            localImageList = selectList;
                            gridAdapter.notifyDataSetChanged();
                            selectPhotoDialog.dismiss();
                        }
                    });
                    break;
                case R.id.iv_camera_selected:
                    CaptureMedia.captureImage(CaptureMedia.KEY_TYPE_CAREMA,
                            JDUtils.getRootActivity(getActivity()), new ActivityCaptureImageListenter(getActivity(), ConsulationFragment.this));
                    selectPhotoDialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
