package com.chengsi.weightcalc.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.adapter.SocialStreamAdapter;
import com.chengsi.weightcalc.bean.AlbumInfo;
import com.chengsi.weightcalc.bean.LocalImageInfo;
import com.chengsi.weightcalc.manager.AlbumManager;
import com.chengsi.weightcalc.utils.imageloader.ImageLoaderUtils;
import com.chengsi.weightcalc.utils.imageloader.WSImageDisplayOpts;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectPhotoActivity extends BaseActivity {
	private List<LocalImageInfo> dataList = new ArrayList<LocalImageInfo>();
	
	private GridView gridview;
	private TextView previewBtn;
	private TextView finishBtn;
	private SocialStreamAdapter adapter;
	private int[] resource = {R.layout.image_grid_item};
	private String[] from = {ImageLoaderUtils.IMAGE_CONTENT_PATH, "imageClick", "isSelect", "selectBg"};
	private int[] to = {R.id.image_grid_item_image, R.id.image_grid_item_bg, R.id.image_grid_item_isselected, R.id.image_grid_item_bg};
	private List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
	private int positon;
	private String limitStr;
	private int selectMode;
	private boolean canRechoose;
	private AlbumManager albumManager;
	private AlbumManager.OnAlbumChangedListener onAlbumChangedListener = new AlbumManager.OnAlbumChangedListener() {

		@Override
		public void OnChanged(List<AlbumInfo> list) {
			data.clear();
			dataList.clear();
			dataList.addAll(list.get(positon).imageList);
			for (LocalImageInfo imageInfo : dataList) {
				data.add(parseMap(imageInfo));
			}
			adapter.notifyDataSetChanged();
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isInAbnormalState(savedInstanceState)) {
			return;
		}
		setContentView(R.layout.activity_select_photo);
		setMyTitle(R.string.photo);
		albumManager = application.albumManager;
		limitStr = albumManager.getLimit();
		albumManager.addOnAlbumChangedListener(onAlbumChangedListener);
		positon = getIntent().getIntExtra(AlbumListActivity.EXTRA_ALBUM_POSITION, 0);
		selectMode = getIntent().getIntExtra(albumManager.KEY_MODE, albumManager.MODE_MULTI);
		canRechoose = getIntent().getBooleanExtra(albumManager.KEY_RECHOOSE, false);
		if (selectMode == albumManager.MODE_SINGLE) {
			View bottomPanel = findViewById(R.id.button_panel);
			bottomPanel.setVisibility(View.GONE);
		}
		previewBtn = (TextView) findViewById(R.id.preview_selected);
		previewBtn.setText(getString(R.string.preview));
		previewBtn.setEnabled(false);
		finishBtn = (TextView) findViewById(R.id.finish_select);
		finishBtn.setText(getString(R.string.ok));
		finishBtn.setEnabled(false);
		finishBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				albumManager.onComplete();
				
			}
		});
		gridview = (GridView) findViewById(R.id.select_photo_gridview);
		gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		Map<Integer, String[]> fromMap = new HashMap<Integer, String[]>();
		fromMap.put(resource[0], from);
		Map<Integer, int[]> toMap = new HashMap<Integer, int[]>();
		toMap.put(resource[0], to);
		int width = (getWindowManager().getDefaultDisplay().getWidth() - 3 * 3)/4;
		adapter = new PhotoAdapter(this, data, resource, fromMap, toMap, width, 0);
		adapter.setViewBinder(new SocialStreamAdapter.ViewBinder() {
			@Override
			public boolean setViewValue(View view, Object data, Object comment) {
				if (view.getId() == R.id.image_grid_item_isselected && data instanceof Boolean) {
					view.setVisibility((Boolean)data ? View.VISIBLE : View.GONE);
					return true;
				}else if (view.getId() == R.id.image_grid_item_bg && data instanceof Boolean) {
					view.setSelected((Boolean)data);
					return true;
				}
				return false;
			}
		});
		gridview.setAdapter(adapter);
		gridview.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					ImageLoaderUtils.resume();
				}else {
					ImageLoaderUtils.pause();
				}
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
		albumManager.getImagesBucketList(false, new AlbumManager.OnGetAlbumListListener() {
			@Override
			public void OnFinished(List<AlbumInfo> list) {
				dataList.addAll(list.get(positon).imageList);
				for (LocalImageInfo imageInfo : dataList) {
					imageInfo.isSelected = albumManager.getSelectList().contains(imageInfo);
					if (imageInfo.isSelected) {
						albumManager.getSelectList().remove(imageInfo);
						albumManager.getSelectList().add(imageInfo);
					}
					data.add(parseMap(imageInfo));
				}
				adapter.notifyDataSetChanged();
			}
		});
		albumManager.registRelatedActivity(this);
	}
	@Override
	protected void onResume() {
		super.onResume();
		data.clear();
		for (LocalImageInfo imageInfo : dataList) {
			data.add(parseMap(imageInfo));
		}
		adapter.notifyDataSetChanged();
		if (albumManager.getSelectCount() == 0) {
			previewBtn.setText(getString(R.string.preview));
			finishBtn.setText(getString(R.string.ok));
			previewBtn.setEnabled(false);
			finishBtn.setEnabled(false);
		}else {
			previewBtn.setText(getString(R.string.preview));
			finishBtn.setText(getString(R.string.finish_count, albumManager.getSelectCount() + "", limitStr));
			previewBtn.setEnabled(true);
			finishBtn.setEnabled(true);
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (albumManager != null) {
			albumManager.removeOnAlbumChangedListener(onAlbumChangedListener);
			albumManager.unregistRelatedActivity(this);
		}
	}
//	@Override
//	protected View createRightView() {
//		View view = generateTextRightView(R.string.cancel);
//		view.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (canRechoose) {
//					albumManager.onCompleteForRechoose();
//				}else {
//					albumManager.onCancel();
//				}
//			}
//		});
//		return view;
//	}
	
	private Map<String, Object> parseMap(final LocalImageInfo imageInfo){
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put(from[0], imageInfo.imagePath);
		if (selectMode == AlbumManager.MODE_SINGLE) {
			map.put(from[1], new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (canRechoose) {
						albumManager.onCompleteAllowRechoose(imageInfo.imagePath);
					}else {
						albumManager.onComplete(imageInfo.imagePath);
					}
				}
			});
			map.put(from[2], false);
			map.put(from[3], false);
		}else{
			map.put(from[1], new OnClickListener(){
				@Override
				public void onClick(View v) {
					if (!imageInfo.isSelected) {
						if (!albumManager.selectPic(imageInfo)) {
							return;
						}
						map.put(from[3], true);
						previewBtn.setEnabled(true);
						finishBtn.setEnabled(true);
						previewBtn.setText(getString(R.string.preview));
						finishBtn.setText(getString(R.string.finish_count, albumManager.getSelectCount() + "", limitStr));
					}else {
						map.put(from[3], false);
						albumManager.removePic(imageInfo);
						int selectCount = albumManager.getSelectCount();
						if (selectCount == 0) {
							previewBtn.setText(R.string.preview);
							finishBtn.setText(R.string.ok);
							previewBtn.setEnabled(false);
							finishBtn.setEnabled(false);
						}else {
							previewBtn.setText(getString(R.string.preview));
							finishBtn.setText(getString(R.string.finish_count, selectCount + "", limitStr));
							previewBtn.setEnabled(true);
							finishBtn.setEnabled(true);
						}
					}
					imageInfo.isSelected = !imageInfo.isSelected;
					map.put(from[2], imageInfo.isSelected);
					adapter.notifyDataSetChanged();
				}
			});
			map.put(from[2], imageInfo.isSelected);
			if (imageInfo.isSelected) {
				map.put(from[3], true);
			}else {
				map.put(from[3], false);
			}
		}
		return map;
	}
	private class PhotoAdapter extends SocialStreamAdapter{
		private Context mContext;
		private DisplayImageOptions options;
		private int width;
		public PhotoAdapter(Context context, List<Map<String, Object>> data,
				int[] resource, Map<Integer, String[]> from,
				Map<Integer, int[]> to, int size, int radius) {
			super(context, data, resource, from, to, size, radius);
			mContext = context;
			width = size;
			options = new WSImageDisplayOpts(R.drawable.ic_launcher, radius)
			.setImageWidth(size).setImageHeight(size).toImageLoaderOpts();
		}
		@Override
		protected void setImageView(View v, Object data, Map<String, Object> dataMap) {
				ImageView iv = (ImageView)v;
				String path =  Scheme.FILE.wrap((String)data);
				LayoutParams params = iv.getLayoutParams();
				params.width = params.height = width;
				ImageLoaderUtils.displayImageForIv(iv, path, options);
		}
	}
}
