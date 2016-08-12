package com.chengsi.weightcalc.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.chengsi.weightcalc.db.DBHelper;
import com.chengsi.weightcalc.widget.PreferenceRightDetailView;
import com.chengsi.weightcalc.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jiadao.corelibs.utils.ToastUtils;

public class SynActivity extends BaseActivity implements View.OnClickListener{

    @InjectView(R.id.switch1)
    Switch switch1;
    @InjectView(R.id.time_sync)
    PreferenceRightDetailView syncTime;
    @InjectView(R.id.manual_sync)
    PreferenceRightDetailView manualSync;
    	public static String DB_NAME = "weightdatabase.db";
	public static String MEASURE_TABLE = "weightcalc";
	public static String COMPANY_TABLE = "company";
	public static String USER_TABLE = "user";
	public static String MATERIAL_TABLE = "material";
	public static int DB_VERSION = 1;
	public DBHelper dbHelper = null;
    private SQLiteDatabase db;
    private Cursor cr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syn);
        setMyTitle("同步设置");
        ButterKnife.inject(this);
        switch1.setOnClickListener(this);
        syncTime.setOnClickListener(this);
        manualSync.setOnClickListener(this);
        dbHelper= new DBHelper(this, DB_NAME, null, DB_VERSION);
        db = dbHelper.getReadableDatabase();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cr.close();
        db.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.switch1:

                break;
            case R.id.time_sync:

                break;
            case R.id.manual_sync:
                cr = db.rawQuery("select * from "+COMPANY_TABLE,null);
                AVObject company = new AVObject("company");
                cr.moveToFirst();
                for(int i = 1;i<=cr.getCount();i++){

                    company.put("id1",cr.getString(0));
                    company.put("companyname",cr.getString(1));
                    company.put("createtime", cr.getString(2));
                    company.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                ToastUtils.show(SynActivity.this, "ok");
                            } else {
                                ToastUtils.show(SynActivity.this, "error");
                                Log.i("ERROR", e.toString());
                            }
                        }
                    });
                }
                break;
        }
    }
}
