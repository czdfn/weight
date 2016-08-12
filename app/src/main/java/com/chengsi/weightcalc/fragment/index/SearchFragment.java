package com.chengsi.weightcalc.fragment.index;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.chengsi.weightcalc.activity.search.SearchActivity;
import com.chengsi.weightcalc.db.DBHelper;
import com.chengsi.weightcalc.fragment.BaseFragment;
//import com.chengsi.pregnancy.sreach.ChineseSpelling;
import com.chengsi.weightcalc.utils.search.PinyinTransformation;
import com.chengsi.weightcalc.utils.search.ShipName;
import com.chengsi.weightcalc.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Hook on 2016/2/17.
 */
public class SearchFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView mList;
    private Context context;
    private List<String> shipNames;
    private MyAdapter mAdapter;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private EditText mSreachEt;
    private List<String> mSreachList;
    private List<Map<String, Objects>> mShipNames;
    private boolean isFrist = true;
    private List<String> longname;
    private List<String> shortname;
    private PinyinTransformation mPinyin;

    @Override
    public View getContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        context = this.getActivity().getApplicationContext();
        mPinyin = new PinyinTransformation();
        mSreachEt = (EditText) view.findViewById(R.id.search_et);
        mList = (ListView) view.findViewById(R.id.search_listview);
        mList.setOnItemClickListener(this);
        queryAllShipName();
        view.findViewById(R.id.search_btn).setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFrist) {
            queryAllShipName();
        }
    }

    private void queryAllShipName() {
        dbHelper = new DBHelper(context, "weightdatabase.db", null, 1);
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("weightcalc", new String[]{"ship_name"}, null, null, null, null, null, null);
        if (cursor != null) {
            shipNames = new ArrayList();
            longname = new ArrayList<>();
            shortname = new ArrayList<>();
            while (cursor.moveToNext()) {
                String shipName = cursor.getString(cursor.getColumnIndex("ship_name"));
                if (shipNames.indexOf(shipName) == -1) {
                    shipNames.add(shipName);
                    ShipName shipnames = mPinyin.toPinyin(shipName);
                    longname.add(shipnames.getLongname());
                    shortname.add(shipnames.getShortname());
                }
            }
            Log.i("TAG", longname.toString() + "----------" + shortname.toString());
            mSreachList = shipNames;
            Message msg = new Message();
            msg.what = 0;
            if (mAdapter == null) {
                mAdapter = new MyAdapter();
                mList.setAdapter(mAdapter);
                isFrist = !isFrist;
            } else
                mAdapter.notifyDataSetChanged();
        }
        db.close();

    }

//    /**
//     * 根据拼音搜索
//     */
//    public static boolean contains(String search) {
//        if (TextUtils.isEmpty(search)) {
//            return false;
//        }
//
//        // 全拼匹配
//        ChineseSpelling finder = ChineseSpelling.getInstance();
//        finder.setResource(search);
//        // 不区分大小写
//        Pattern pattern = Pattern
//                .compile(search, Pattern.CASE_INSENSITIVE);
//        Matcher matcher = pattern.matcher(finder.getSpelling());
//        flag = matcher.find();
//
//        return flag;
//    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        startActivity(new Intent(context, SearchActivity.class).putExtra("Name", mSreachList.get(i)));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.search_btn) {
            String sreach = mSreachEt.getText().toString();
            if (sreach.length() < 1) {
                mSreachList = shipNames;
                mAdapter.notifyDataSetChanged();
                return;
            }
            mSreachList = new ArrayList<String>();
            for (int i = 0; i < shipNames.size(); i++) {
                if (shipNames.get(i).indexOf(sreach) != -1) {
                    mSreachList.add(shipNames.get(i));
                }
            }
            if (sreach.matches("^[a-zA-Z]*")) {
                sreach = sreach.toLowerCase();
                Log.i("TAG", "str=" + sreach);
                for (int i = 0; i < longname.size(); i++) {
                    Log.i("TAG", "(longname.get(i).indexOf(sreach)=" + (longname.get(i).indexOf(sreach)));
                    if ((longname.get(i).indexOf(sreach) != -1 || shortname.get(i).indexOf(sreach) != -1) && (mSreachList.indexOf(sreach) == -1)) {
                        mSreachList.add(shipNames.get(i));
                    }
                }
            }
            if (mSreachList.size() > 0) {
                mAdapter.notifyDataSetChanged();
            } else {
                mSreachList = shipNames;
                mAdapter.notifyDataSetChanged();
                Toast.makeText(context, "没有搜索到符合船只", Toast.LENGTH_SHORT).show();

            }
        }
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mSreachList.size();
        }

        @Override
        public Object getItem(int i) {
            return mSreachList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_search_list, null);
                holder = new ViewHolder();
                holder.mImg = (ImageView) view.findViewById(R.id.item_search_img);
                holder.mName = (TextView) view.findViewById(R.id.item_search_name);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.mName.setText(mSreachList.get(i).toString());
            return view;
        }

        class ViewHolder {
            ImageView mImg;
            TextView mName;
        }
    }
}
