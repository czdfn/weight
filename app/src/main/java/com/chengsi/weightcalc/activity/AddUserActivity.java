    package com.chengsi.weightcalc.activity;

    import android.app.AlertDialog;
    import android.content.ContentValues;
    import android.content.DialogInterface;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.os.Bundle;
    import android.view.ContextMenu;
    import android.view.LayoutInflater;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.AdapterView;
    import android.widget.EditText;
    import android.widget.ListView;
    import android.widget.RadioButton;
    import android.widget.SimpleCursorAdapter;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.chengsi.weightcalc.db.DBHelper;
    import com.chengsi.weightcalc.R;

    import java.text.SimpleDateFormat;
    import java.util.Date;

    import butterknife.ButterKnife;
    import butterknife.InjectView;

    public class AddUserActivity extends BaseActivity {

    SimpleCursorAdapter adapter;
    @InjectView(R.id.user_lv)
    ListView listView;
    public SQLiteDatabase database;
    private Cursor cr;
        public static String DB_NAME = "weightdatabase.db";
        public static String MEASURE_TABLE = "weightcalc";
        public static int DB_VERSION = 1;
        public DBHelper dbHelper = new DBHelper(this, DB_NAME, null, DB_VERSION);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        ButterKnife.inject(this);
        registerForContextMenu(listView);
        database = dbHelper.getWritableDatabase();
        setMyTitle("添加用户");
        initView();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.menu.menu_delete:
            /* 在此处，我们关键引入 AdapterView.AdapterContextMenuInfo来获取单元的信息。在有三个重要的信息。
            1、id：The row id of the item for which the context menu is being displayed ，
            在cursorAdaptor中，实际就是表格的_id序号；
            2、position 是list的元素的顺序；
            3、view就可以获得list中点击元素的View，通过view可以获取里面的显示的信息   */
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                delete(info.id);
                return true;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }
    private void delete(final long  rowId){
        if(rowId>0){
            new AlertDialog.Builder(this)
                    .setTitle("删除")
    //                    .setTitle("删除" + getNameById(rowId))
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            deleteData(rowId);
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }
    }

    private void deleteData(long rowId){
        String[] str = {String.valueOf(rowId)};
        database.delete("user", "_id=?", str);
    //        new RefreshList().execute();  //采用后台方式，当然也可以用crusor.requery()来处理。
        initView();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.add(Menu.NONE,R.menu.menu_delete,Menu.NONE,"Delete");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        System.out.println("resume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
        System.out.println("start");
    }


    private void initView(){
        cr = database.query("user", null, null, null, null, null, null, null);
        adapter = new SimpleCursorAdapter(this,R.layout.item_user,cr,new String[]{cr.getColumnNames()[0],cr.getColumnNames()[1],cr.getColumnNames()[2],cr.getColumnNames()[3],cr.getColumnNames()[5]},new int[]{R.id.tv_userid,R.id.tv_nickname,R.id.tv_username,R.id.tv_authority,R.id.tv_createtime},1);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_user, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.adduser) {
            LayoutInflater factory = LayoutInflater.from(this);
            final View textEntryView = factory.inflate(R.layout.dialog, null);

            AlertDialog dlg = new AlertDialog.Builder(this,R.style.tipsDialog)
                    .setView(textEntryView)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            System.out.println("-------------->6");
                            TextView tv_title = (TextView) textEntryView.findViewById(R.id.tv_username);
                            tv_title.setText("用户名");
                            TextView tv_message = (TextView) textEntryView.findViewById(R.id.tv_psw);
                            tv_message.setText("密码");
                            EditText et_username = (EditText) textEntryView.findViewById(R.id.et_username);
                            EditText et_psw = (EditText) textEntryView.findViewById(R.id.et_psw);
                            EditText et_nick = (EditText) textEntryView.findViewById(R.id.et_nick);
                            RadioButton rb1 = (RadioButton)textEntryView.findViewById(R.id.rb1);
                            RadioButton rb2 = (RadioButton)textEntryView.findViewById(R.id.rb2);
                            RadioButton rb3 = (RadioButton)textEntryView.findViewById(R.id.rb3);
                            String authority;
                            if(rb1.isChecked()){
                                authority = "管理员";
                            }else if(rb2.isChecked()){
                                authority = "游客";
                            }else{
                                authority = "操作员";
                            }
                            final ContentValues cv = new ContentValues();
                            cv.put("username", et_username.getText().toString());
                            cv.put("password", et_psw.getText().toString());
                            cv.put("nickname", et_nick.getText().toString());
                            cv.put("authority", authority);
                            cv.put("createtime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                            cr = database.query("user", null, null, null, null, null, null, null);
                            int count = 0;
                            while(cr.moveToNext()){
                                if(et_username.getText().toString().equals(cr.getString(cr.getColumnIndex("username")))){
                                    Toast.makeText(AddUserActivity.this,"用户名重复",Toast.LENGTH_SHORT).show();
                                break;
                                }else{
                                    count++;
                                }
                            }
                            if(count ==cr.getCount()){
                                try {
                                    database.insert("user", "", cv);
                                }catch (Exception e){
                                    Toast.makeText(AddUserActivity.this,"写入数据库出错",Toast.LENGTH_SHORT).show();
                                }
                                initView();
                            }

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            System.out.println("-------------->2");
                        }
                    })
                    .create();
            dlg.show();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
        cr.close();
    }
    }
