package com.chengsi.weightcalc.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.chengsi.weightcalc.bean.BaseBean;
import com.chengsi.weightcalc.bean.Notificationhistory;
import com.chengsi.weightcalc.fragment.notification.SysNotification;
import com.chengsi.weightcalc.http.JDHttpClient;
import com.chengsi.weightcalc.http.JDHttpResponseHandler;
import com.chengsi.weightcalc.utils.JDUtils;
import com.chengsi.weightcalc.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NotificationDetailActivity extends BaseActivity {

    @InjectView(R.id.tv_notification_date)
    TextView tv_notification_date;
    @InjectView(R.id.tv_notification_title)
    TextView tv_notification_title;
    @InjectView(R.id.tv_notification_content)
    TextView tv_notification_content;
    Notificationhistory bean = new Notificationhistory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);
        ButterKnife.inject(this);
        setMyTitle("通知详情");
        initView();
        NotificationReadStatus();
    }

    public void initView(){
        bean = (Notificationhistory)getIntent().getSerializableExtra(SysNotification.KEY_NOTIFICATION_BEAN);
        tv_notification_title.setText(bean.getTitle());
        tv_notification_content.setText(bean.getContent());
        tv_notification_date.setText(JDUtils.formatDate(bean.getPushtime(), "yyyy-MM-dd"));
    }

    public void NotificationReadStatus(){
        JDHttpClient.getInstance().setNotificationReadyStatus(this,bean.getId(),"1",new JDHttpResponseHandler<List<Notificationhistory>>(new TypeReference<BaseBean<List<Notificationhistory>>>(){}){
            @Override
            public void onRequestCallback(BaseBean<List<Notificationhistory>> result) {
                super.onRequestCallback(result);
                if(result.isSuccess()){
                }
            }
        });
    }

}
