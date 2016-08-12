package com.chengsi.weightcalc.activity.search;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chengsi.weightcalc.activity.BaseActivity;
import com.chengsi.weightcalc.activity.measure.BackMeasureActivity;
import com.chengsi.weightcalc.activity.measure.ConstantActivity;
import com.chengsi.weightcalc.activity.measure.FrontMeasureActivity;
import com.chengsi.weightcalc.activity.measure.MidMeasureActivity;
import com.chengsi.weightcalc.db.DBHelper;
import com.chengsi.weightcalc.utils.MeasureType;
import com.chengsi.weightcalc.R;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    public static String shipName;
    private ListView mListView;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private List<MyShipSearch> mSearchs;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        shipName = getIntent().getStringExtra("Name");
        setTitle(shipName);
        mListView = (ListView) findViewById(R.id.search_listview);
        dbHelper = new DBHelper(this, "weightdatabase.db", null, 1);
        queryShip();
    }

    private static String[] check_type = {"", "前测", "中测", "后测","常数计算"};

    private void queryShip() {
        db = dbHelper.getReadableDatabase();
//        Cursor cursor = db.query("weightcalc", new String[]{"check_id", "check_time", "measure_type"}, "ship_name like " + shipName, null, null, null, null, null);
        Cursor cursor = db.rawQuery("select * from weightcalc where ship_name=?", new String[]{shipName});
        mSearchs = new ArrayList<MyShipSearch>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MyShipSearch mShipSearch = new MyShipSearch();
                mShipSearch.setTabel_id(Integer.valueOf(cursor.getString(cursor.getColumnIndex("_id"))));
                mShipSearch.setMeasure_id(cursor.getString(cursor.getColumnIndex("check_id")));
                mShipSearch.setMeasure_time(cursor.getString(cursor.getColumnIndex("check_time")));
                mShipSearch.setMeasure_type(Integer.valueOf(cursor.getString(cursor.getColumnIndex("check_type"))));
                mSearchs.add(mShipSearch);
            }
            mListView.setAdapter(new MyAdapter());
            mListView.setOnItemClickListener(SearchActivity.this);
        }
        db.close();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, GroupActivity.class);
        intent.putExtra("Name", shipName);
        intent.putExtra("check_id", mSearchs.get(i).getMeasure_id() + "");
        intent.putExtra("check_type", mSearchs.get(i).getMeasure_type());
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == MeasureType.MEASURE_CHANGE) {
            queryShip();
        }
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mSearchs.size();
        }

        @Override
        public Object getItem(int i) {
            return mSearchs.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHodler hodler;
            if (view == null) {
                view = LayoutInflater.from(SearchActivity.this).inflate(R.layout.item_search_list_b, null);
                hodler = new ViewHodler();
                hodler.mNum = (TextView) view.findViewById(R.id.search_num);
                hodler.mId = (TextView) view.findViewById(R.id.search_id);
                hodler.mTime = (TextView) view.findViewById(R.id.search_time);
                hodler.mType = (TextView) view.findViewById(R.id.search_type);
                hodler.mChange = (Button) view.findViewById(R.id.search_change);
                hodler.mDelete = (Button) view.findViewById(R.id.search_delete);
                view.setTag(hodler);
            } else {
                hodler = (ViewHodler) view.getTag();
            }
            final MyShipSearch myShipSearch = mSearchs.get(i);
            hodler.mNum.setText(i + 1 + "");
            hodler.mId.setText(myShipSearch.getMeasure_id() + "");
            hodler.mTime.setText(myShipSearch.getMeasure_time());
            hodler.mType.setText(check_type[Integer.valueOf(myShipSearch.getMeasure_type())]);
            hodler.mChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Class activity;
                    switch (myShipSearch.getMeasure_type()) {
                        case 1:
                            activity = FrontMeasureActivity.class;
                            break;
                        case 2:
                            activity = MidMeasureActivity.class;
                            break;
                        case 3:
                            activity = BackMeasureActivity.class;
                            break;
                        case 4:
                            activity = ConstantActivity.class;
                            break;
                        default:
                            Toast.makeText(SearchActivity.this, "出现未知错误", Toast.LENGTH_SHORT).show();
                            return;
                    }
                    Intent intent = new Intent(SearchActivity.this, activity);
                    intent.putExtra("type", MeasureType.MEASURE_CHANGE);
                    intent.putExtra("check_type",myShipSearch.getMeasure_type());
                    intent.putExtra("id", myShipSearch.getTabel_id() + "");
                    startActivityForResult(intent, MeasureType.MEASURE_CHANGE);
                }
            });
            hodler.mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(SearchActivity.this, android.R.style.Theme_Light)
                            .setTitle("删除")//
                            .setMessage("是否确认删除？")//
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db = dbHelper.getReadableDatabase();
                                    db.delete("weightcalc", "_id=?", new String[]{myShipSearch.getTabel_id() + ""});
                                    db.close();
                                    queryShip();
                                }
                            })//
                            .setNegativeButton("取消", null).show();
                }
            });
            return view;
        }

        class ViewHodler {
            TextView mNum;
            TextView mId;
            TextView mTime;
            TextView mType;
            Button mChange;
            Button mDelete;
        }
    }

    class MyShipSearch {
        private int tabel_id;
        private String measure_id;
        private int measure_type;
        private String measure_time;

        public int getTabel_id() {
            return tabel_id;
        }

        public void setTabel_id(int tabel_id) {
            this.tabel_id = tabel_id;
        }

        public String getMeasure_id() {
            return measure_id;
        }

        public void setMeasure_id(String measure_id) {
            this.measure_id = measure_id;
        }

        public int getMeasure_type() {
            return measure_type;
        }

        public void setMeasure_type(int measure_type) {
            this.measure_type = measure_type;
        }

        public String getMeasure_time() {
            return measure_time;
        }

        public void setMeasure_time(String measure_time) {
            this.measure_time = measure_time;
        }

        @Override
        public String toString() {
            return "MyShipSearch{" +
                    "measure_id=" + measure_id +
                    ", measure_type='" + measure_type + '\'' +
                    ", measure_time='" + measure_time + '\'' +
                    '}';
        }
    }
}
