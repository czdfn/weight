package com.chengsi.weightcalc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.listener.OnContinuousClickListener;
import com.chengsi.weightcalc.widget.JDLoadingView;
import com.chengsi.weightcalc.widget.PreferenceRightDetailView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ArchivesActivity extends BaseActivity {

    @InjectView(R.id.item_my_archives)
    PreferenceRightDetailView itemArchives;
    @InjectView(R.id.item_my_case)
    PreferenceRightDetailView itemCase;
    @InjectView(R.id.item_apply_history)
    PreferenceRightDetailView itemHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archives);
        ButterKnife.inject(this);

        itemArchives.setOnClickListener(new OnContinuousClickListener() {
            @Override
            public void onContinuousClick(View v) {
                Intent intent = new Intent(ArchivesActivity.this, MyArchivesActivity.class);
                startActivity(intent);
            }
        });
        itemCase.setOnClickListener(new OnContinuousClickListener() {
            @Override
            public void onContinuousClick(View v) {
                Intent intent = new Intent(ArchivesActivity.this, MyCaseActivity.class);
                startActivity(intent);
            }
        });
        itemHistory.setOnClickListener(new OnContinuousClickListener() {
            @Override
            public void onContinuousClick(View v) {
                Intent intent = new Intent(ArchivesActivity.this, SendArchiveHistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_archives, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_send) {
            setLoadingViewState(JDLoadingView.STATE_LOADING);
//            JDHttpClient.getInstance().sendArchive(this, application.userManager.getUserBean().getHospital().getId(), new JDHttpResponseHandler<String>(new TypeReference<BaseBean<String>>(){}){
//
//                @Override
//                public void onRequestCallback(BaseBean<String> result) {
//                    super.onRequestCallback(result);
//                    dismissLoadingView();
//                    if (result.isSuccess()){
//                        showToast("发送档案成功！");
//                    }else{
//                        showToast(result.getMessage());
//                    }
//                }
//            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
