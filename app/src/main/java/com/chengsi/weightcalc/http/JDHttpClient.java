package com.chengsi.weightcalc.http;

import android.content.Context;
import android.os.Build;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chengsi.weightcalc.MyApplication;
import com.chengsi.weightcalc.bean.AdviceBean;
import com.chengsi.weightcalc.bean.ArchiveHistoryBean;
import com.chengsi.weightcalc.bean.ChatGroup;
import com.chengsi.weightcalc.bean.CheckResultBean;
import com.chengsi.weightcalc.bean.ConsultBean;
import com.chengsi.weightcalc.bean.ConsultDoctorList;
import com.chengsi.weightcalc.bean.DailyTaskBean;
import com.chengsi.weightcalc.bean.DoctorBean;
import com.chengsi.weightcalc.bean.DynamicBean;
import com.chengsi.weightcalc.bean.FileUploadBean;
import com.chengsi.weightcalc.bean.GoodNewsBean;
import com.chengsi.weightcalc.bean.HospitalBean;
import com.chengsi.weightcalc.bean.ListBaseBean;
import com.chengsi.weightcalc.bean.MyArchivesBean;
import com.chengsi.weightcalc.bean.Notificationhistory;
import com.chengsi.weightcalc.bean.OplanInforBean;
import com.chengsi.weightcalc.bean.OutDoctorArrangeBean;
import com.chengsi.weightcalc.bean.OutDoctorBean;
import com.chengsi.weightcalc.bean.OutReservationHistoryBean;
import com.chengsi.weightcalc.bean.RequestCaseBean;
import com.chengsi.weightcalc.bean.ReservationDoctorListBean;
import com.chengsi.weightcalc.bean.ReservationHistoryBean;
import com.chengsi.weightcalc.bean.ReservationInfo;
import com.chengsi.weightcalc.bean.ReservationTimeListBean;
import com.chengsi.weightcalc.bean.UserBean;
import com.chengsi.weightcalc.constants.StorageConstants;
import com.chengsi.weightcalc.storage.JDStorage;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * Created by yingjianxu on 15/6/17.
 */
public class JDHttpClient {

    private AsyncHttpClient httpClient;

    public static String hostUrl = HttpConstants.HTTP_HOST_URL;

    private JDHttpClient() {
        httpClient = new AsyncHttpClient();
        httpClient.setURLEncodingEnabled(true);
        httpClient.setConnectTimeout(60 * 1000);
        try{
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            httpClient.setSSLSocketFactory(sf);
        }catch (Exception e){
            e.printStackTrace();
        }

        hostUrl = JDStorage.getInstance().getStringValue(StorageConstants.KEY_HOST_URL, HttpConstants.HTTP_HOST_URL);
    }

    public static JDHttpClient getInstance() {
        return JDHttpClientHandler.INSTANCE;
    }

    private static class JDHttpClientHandler {
        public static final JDHttpClient INSTANCE = new JDHttpClient();
    }

    public AsyncHttpClient getHttpClient() {
        return httpClient;
    }

    public <T> void sendHttpGetRequest(Context context, String relativeURL, Map<String, Object> params, JDHttpResponseHandler<T> callback) {
        RequestParams rp = new RequestParams();
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                rp.put(key, params.get(key));
            }
        }
        addGlobalParams(rp);
        httpClient.get(context, hostUrl.concat(relativeURL), rp, callback);
    }

    private void addGlobalParams(RequestParams rp) {
//        if (MyApplication.getInstance().userManager.isLogin()){
//            rp.put("token", MyApplication.getInstance().userManager.getUserBean().getToken());
//        }
        rp.put("deviceType", "android");
    }


    public <T> void sendHttpPostRequest(Context context, String relativeURL, JDHttpResponseHandler<T> callback) {
        RequestParams params = new RequestParams();
        addGlobalParams(params);
        httpClient.post(context, hostUrl.concat(relativeURL), params, callback);
    }

    public <T> void sendHttpPostRequest(Context context, String relativeURL, Map<String, Object> params, JDHttpResponseHandler<T> callback) {
//        sendHttpGetRequest(context,relativeURL,params,callback);
        RequestParams rp = new RequestParams();
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                if (params.get(key) instanceof  List){
                    List list = (List) params.get(key);
                    for (Object obj : list){
                        if (obj instanceof  File){
                            File file = (File) obj;
                            try {
                                rp.put(key + list.indexOf(obj), file, file.getName());
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }else if (params.get(key) instanceof File) {
                    File file = (File) params.get(key);
                    try {
                        rp.put(key, file, file.getName());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    rp.put(key, params.get(key));
                }
            }
        }
        addGlobalParams(rp);
        httpClient.post(context, hostUrl.concat(relativeURL), rp, callback);
    }

    public <T> void sendOutHttpPostRequest(Context context, String url, Map<String, Object> params, JDHttpResponseHandler<T> callback) {
//        sendHttpGetRequest(context,relativeURL,params,callback);
        RequestParams rp = new RequestParams();
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                if (params.get(key) instanceof  List){
                    List list = (List) params.get(key);
                    for (Object obj : list){
                        if (obj instanceof  File){
                            File file = (File) obj;
                            try {
                                rp.put(key + list.indexOf(obj), file, file.getName());
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }else if (params.get(key) instanceof File) {
                    File file = (File) params.get(key);
                    try {
                        rp.put(key, file, file.getName());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    rp.put(key, params.get(key));
                }
            }
        }
        addGlobalParams(rp);
        httpClient.post(context, url, rp, callback);
    }

    public void cancelRequest(Context context, boolean mayInterruptIfRunning) {
        if (httpClient != null) {
            httpClient.cancelRequests(context, mayInterruptIfRunning);
        }
    }

    public void reqRegister(Context context, String username, String pwd, JDHttpResponseHandler<UserBean> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("uname", username);
        map.put("pwd", pwd);
        map.put("pwd2", pwd);
        sendHttpPostRequest(context, HttpConstants.USER_REG_URL, map, responseHandler);
    }

    public void reqSendSms(Context context, String phone, JDHttpResponseHandler<String> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        sendHttpPostRequest(context, HttpConstants.USER_SEND_SMS_URL, map, responseHandler);
    }


    public void reqValifySms(Context context, String phone, String code, JDHttpResponseHandler<String> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        map.put("code", code);
        sendHttpPostRequest(context, HttpConstants.USER_VALIFY_SMS_URL, map, responseHandler);
    }

    public void reqUploadPhoto(Context context, File file, JDHttpResponseHandler<List<FileUploadBean>> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("type", "3");
        map.put("file", file);
        sendHttpPostRequest(context, HttpConstants.USER_FILE_UPLOAD, map, responseHandler);
    }

    public void reqUploadPhotos(Context context, List<File> file, JDHttpResponseHandler<List<FileUploadBean>> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("type", "3");
        map.put("file", file);
        sendHttpPostRequest(context, HttpConstants.USER_FILE_UPLOAD, map, responseHandler);
    }

    public void reqLogin(Context context, String username, String pwd, JDHttpResponseHandler<UserBean> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("uname", username);
        map.put("pwd", pwd);
        Map<String, String> rp = new HashMap<>();
        rp.put("mobile_ver", Build.VERSION.SDK_INT + "");
        rp.put("app_ver", MyApplication.getVersionCode() + "");
        rp.put("version_name", MyApplication.getVersionName());
        rp.put("mobile_brand", Build.BRAND);

        String deviceInfo = "";
        for (String key : rp.keySet()){
            deviceInfo += key + ":" + rp.get(key);
            deviceInfo += "||";
        }
        map.put("deviceInfo", deviceInfo);
        sendHttpPostRequest(context, HttpConstants.USER_LOGIN_URL, map, responseHandler);
    }

    public void reqUserInfo(Context context, JDHttpResponseHandler<UserBean> responseHandler){
        Map<String, Object> map = new HashMap<>();
        sendHttpPostRequest(context, HttpConstants.USER_INFO_URL, map, responseHandler);
    }

    public void reqModifyPwd(Context context, String username, String pwd, JDHttpResponseHandler<UserBean> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("pwd", pwd);
        map.put("phone", username);
        sendHttpPostRequest(context, HttpConstants.USER_MODIFY_PWD, map, responseHandler);
    }

    public void reqRollingPicList(Context context, JDHttpResponseHandler<List<String>> responseHandler){
        sendHttpPostRequest(context, HttpConstants.INDEX_ROLL_PIC_LIST, null, responseHandler);
    }

    public void reqHospitalList(Context context, String name, JDHttpResponseHandler<ListBaseBean<List<HospitalBean>>> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        sendHttpPostRequest(context, HttpConstants.HOSPITAL_LIST_URL, map, responseHandler);
    }

    public void reqModifyInfo(Context context,String imgUrl, String nickname, String realname, int sex, String idNo, JDHttpResponseHandler<String> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("nickname", nickname);
        map.put("imgUrl", imgUrl);
        map.put("realname", realname);
        map.put("sex", String.valueOf(sex));
        map.put("idNo", idNo);
        sendHttpPostRequest(context, HttpConstants.USER_MODIFY_INFO, map, responseHandler);
    }

    public void pushmsg(Context context,String appid, String title, String content, JDHttpResponseHandler<String> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("appid", appid);
        map.put("title", title);
        map.put("content", content);
//        map.put("extra", extra);
        sendHttpPostRequest(context, HttpConstants.PUSH, map, responseHandler);
    }

    public void reqDoctorList(Context context, String name, String hospitalId, int pageNo, JDHttpResponseHandler<ListBaseBean<List<DoctorBean>>> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("hospitalId", hospitalId);
        map.put("pageNo", pageNo);
        sendHttpPostRequest(context, HttpConstants.DOCTOR_LIST_URL, map, responseHandler);
    }

    public void reqAttentDoctorList(Context context, JDHttpResponseHandler<List<DoctorBean>> responseHandler){
        Map<String, Object> map = new HashMap<>();
        sendHttpPostRequest(context, HttpConstants.DOCTOR_ATTENTION_LIST_URL, map, responseHandler);
    }

    public void reqAddAttentDoctor(Context context, long doctorId, JDHttpResponseHandler<String> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("doctorId", String.valueOf(doctorId));
        sendHttpPostRequest(context, HttpConstants.DOCTOR_ADD_ATTENTION_URL, map, responseHandler);
    }

    public void reqCancelAttentDoctor(Context context, long doctorId, JDHttpResponseHandler<String> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("doctorId", String.valueOf(doctorId));
        sendHttpPostRequest(context, HttpConstants.DOCTOR_CANCEL_ATTENTION_URL, map, responseHandler);
    }

    public void reqSendConsult(Context context, String doctorId, String title, String content, int status, String urlList, JDHttpResponseHandler<String> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("doctorId", doctorId);
        map.put("content", content);
        map.put("title", title);
        map.put("urlList", urlList);
        map.put("status", String.valueOf(status));
        sendHttpPostRequest(context, HttpConstants.REQUEST_SEND_CONSULT_URL, map, responseHandler);
    }

    public void reqConsultDetail(Context context, String consultId, JDHttpResponseHandler<String> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("consultId", consultId);
        sendHttpPostRequest(context, HttpConstants.REQUEST_SEND_CONSULT_URL, map, responseHandler);
    }

    public void reqConsultDoctorList(Context context, JDHttpResponseHandler<ConsultDoctorList> responseHandler){
        Map<String, Object> map = new HashMap<>();
        sendHttpPostRequest(context, HttpConstants.REQUEST_CONSULT_DOCTOR_LIST_URL, map, responseHandler);
    }

    public void reqConsultList(Context context, int pageNo, JDHttpResponseHandler<ListBaseBean<List<ConsultBean>>> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("pageNo", pageNo);
        sendHttpPostRequest(context, HttpConstants.REQUEST_CONSULT_LIST_URL, map, responseHandler);
    }

    public void reqMyConsultList(Context context, int pageNo, JDHttpResponseHandler<ListBaseBean<List<ConsultBean>>> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("pageNo", pageNo);
        sendHttpPostRequest(context, HttpConstants.REQUEST_MY_CONSULT_LIST_URL, map, responseHandler);
    }

    public void reqReservationInfo(Context context, String hospitalId, JDHttpResponseHandler<ReservationInfo> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("hospitalId", hospitalId);
        sendHttpPostRequest(context, HttpConstants.REQUEST_RESERVATION_INFO_URL, map, responseHandler);
    }

    public void reqReservationDoctorInfo(Context context, String time, String itemId, String typeId, JDHttpResponseHandler<ReservationDoctorListBean> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("itemId", itemId);
        map.put("typeId", typeId);
        map.put("time", time);
        sendHttpPostRequest(context, HttpConstants.REQUEST_RESERVATION_DOCTOR_INFO_URL, map, responseHandler);
    }


    public void reqOutReservationDoctorInfo(Context context, String req_begin_date, String req_end_date, String unit_type, String clinic_flag, JDHttpResponseHandler<List<OutDoctorBean>> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("clinic_flag", clinic_flag);
        map.put("unit_type", unit_type);
        map.put("req_begin_date", req_begin_date);
        map.put("req_end_date", req_end_date);
        Map<String, Object> params = new HashMap<>();
        params.put("params", new JSONObject(map).toJSONString());
        sendHttpPostRequest(context, HttpConstants.REQUEST_OUT_RESERVATION_DOCTOR_INFO_URL, params, responseHandler);
    }

    public void reqReservationTimeInfo(Context context, String time, String doctorId, JDHttpResponseHandler<ReservationTimeListBean> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("time", time);
        map.put("doctorId", doctorId);
        sendHttpPostRequest(context, HttpConstants.REQUEST_RESERVATION_TIME_INFO_URL, map, responseHandler);
    }

    public void reqOutReservationTimeInfo(Context context, String request_date, String unit_type, String clinic_flag, String doctor_sn, JDHttpResponseHandler<List<OutDoctorArrangeBean>> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("clinic_flag", clinic_flag);
        map.put("unit_type", unit_type);
        map.put("req_begin_date", request_date);
        map.put("req_end_date", request_date);
        map.put("doctor_sn", doctor_sn);
        Map<String, Object> params = new HashMap<>();
        params.put("url", HttpConstants.REQUEST_OUT_RESERVATION_TIME_INFO_URL);
        params.put("params", new JSONObject(map).toJSONString());
        sendHttpPostRequest(context, HttpConstants.REQUEST_OUT_HIS_URL, params, responseHandler);
    }

    public void reqOutCancelReservation(Context context, String patient_id, String gh_sequence, String request_date, String record_sn, String card_no, String order_id, String his_order_id, JDHttpResponseHandler<OutReservationHistoryBean> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("patient_id", patient_id);
        map.put("gh_sequence", gh_sequence);
        map.put("request_date", request_date);
        map.put("record_sn", record_sn);
        map.put("card_no", card_no);
        map.put("order_id", order_id);
        map.put("his_order_id", his_order_id);

        Map<String, Object> params = new HashMap<>();
        params.put("url", HttpConstants.REQUEST_OUT_RESERVATION_CANCEL_URL);
        params.put("params", new JSONObject(map).toJSONString());
        sendHttpPostRequest(context, HttpConstants.REQUEST_OUT_HIS_URL, params, responseHandler);
    }

    public void reqAddReservation(Context context, String doctorId, String itemId, String date, String timeId, String arrangeId, String typeId, JDHttpResponseHandler<ReservationTimeListBean> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("itemId", itemId);
        map.put("doctorId", doctorId);
        map.put("date", date);
        map.put("arrangeId", arrangeId);
        map.put("typeId", typeId);
        sendHttpPostRequest(context, HttpConstants.REQUEST_RESERVATION_ADD_URL, map, responseHandler);
    }

    public void reqOutAddReservation(Context context, Map<String, Object> params, JDHttpResponseHandler<OutReservationHistoryBean> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("url", "HospitalWebService/HISAPI/orderHISReg.do");
        map.put("params", new JSONObject(params).toJSONString());
        sendHttpPostRequest(context, HttpConstants.REQUEST_OUT_HIS_URL, map, responseHandler);
    }

    public <T> void reqHisRest(Context context, String url, Map<String, Object> params, JDHttpResponseHandler<T> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("url", url);
        map.put("params", new JSONObject(params).toJSONString());
        sendHttpPostRequest(context, HttpConstants.REQUEST_OUT_HIS_URL, map, responseHandler);
    }

    public void reqOutReservationHistory(Context context, String query, String card_no, JDHttpResponseHandler<List<OutReservationHistoryBean>> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("card_no", card_no);
        map.put("query", query);
        sendHttpPostRequest(context, HttpConstants.REQUEST_OUT_RESERVATION_HISTORY_URL, map, responseHandler);
    }

    public void reqUnTreatedReservation(Context context, int curPageNo, JDHttpResponseHandler<ListBaseBean<List<ReservationHistoryBean>>> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("pageNo", String.valueOf(curPageNo));
        sendHttpPostRequest(context, HttpConstants.REQUEST_RESERVATION_UNTREATED_URL, map, responseHandler);
    }

    /**
     *
     * @param context
     * @param hospitalId
     * @param type 0取消关注 1关注
     * @param responseHandler
     */
    public void reqAttendHospital(Context context, long hospitalId, int type, JDHttpResponseHandler<String> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("hospitalId", String.valueOf(hospitalId));
        map.put("type", String.valueOf(type));
        sendHttpPostRequest(context, HttpConstants.REQUEST_ATTEND_HOSPITAL_URL, map, responseHandler);
    }

    public void reqAttendHospitalList(Context context, JDHttpResponseHandler<List<HospitalBean>> responseHandler){
        Map<String, Object> map = new HashMap<>();
        sendHttpPostRequest(context, HttpConstants.REQUEST_ATTEND_HOSPITAL_LIST_URL, map, responseHandler);
    }

    public void reqHXChatGroupList(Context context, String type, JDHttpResponseHandler<List<ChatGroup>> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        sendHttpPostRequest(context, HttpConstants.REQUEST_CHAT_GROUP_LIST_URL, map, responseHandler);
    }

    public void reqDynamicList(Context context, int pageNo, JDHttpResponseHandler<ListBaseBean<List<DynamicBean>>> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("pageNo", String.valueOf(pageNo));
        sendHttpPostRequest(context, HttpConstants.REQUEST_DYNAMIC_LIST_URL, map, responseHandler);
    }

    public void reqTaskList(Context context, JDHttpResponseHandler<List<DailyTaskBean>> responseHandler){
        Map<String, Object> map = new HashMap<>();
        sendHttpPostRequest(context, HttpConstants.REQUEST_DAILY_TASK_LIST_URL, map, responseHandler);
    }

    public void reqTodayTaskList(Context context, JDHttpResponseHandler<List<DailyTaskBean>> responseHandler){
        Map<String, Object> map = new HashMap<>();
        sendHttpPostRequest(context, HttpConstants.REQUEST_DAY_TASK_LIST_URL, map, responseHandler);
    }

    public void reqCheckResultList(Context context,String type,JDHttpResponseHandler<List<CheckResultBean>> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("type",type);
        sendHttpPostRequest(context, HttpConstants.REQUEST_CHECK_LIST_URL, map, responseHandler);
    }

    public void reqArchives(Context context, JDHttpResponseHandler<MyArchivesBean> responseHandler){
        Map<String, Object> map = new HashMap<>();
        sendHttpPostRequest(context, HttpConstants.REQUEST_REQ_ARCHIVES_URL, map, responseHandler);
    }

    public void reqAddArchives(Context context, MyArchivesBean myArchivesBean, JDHttpResponseHandler<String> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("params", JSON.toJSONString(myArchivesBean));
        sendHttpPostRequest(context, HttpConstants.REQUEST_REQ_ADD_ARCHIVES_URL, map, responseHandler);
    }

    public void reqCases(Context context, JDHttpResponseHandler<RequestCaseBean> responseHandler){
        Map<String, Object> map = new HashMap<>();
        sendHttpPostRequest(context, HttpConstants.REQUEST_REQ_CASES_URL, map, responseHandler);
    }

    public void reqAddCases(Context context, RequestCaseBean requestCaseBean, JDHttpResponseHandler<String> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("params", JSON.toJSONString(requestCaseBean));
        sendHttpPostRequest(context, HttpConstants.REQUEST_REQ_ADD_CASES_URL, map, responseHandler);
    }

    public void reqBindHospital(Context context, long hosId, String medCard, String idCard, JDHttpResponseHandler<String> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("medCard", medCard);
        map.put("hospitalId", String.valueOf(hosId));
        map.put("idNo", idCard);
        sendHttpPostRequest(context, HttpConstants.REQUEST_REQ_BIND_HOSPITAL_URL, map, responseHandler);
    }

    public void reqGroupMembers(Context context, String groupId, JDHttpResponseHandler<List<UserBean>> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("groupid", groupId);
        sendHttpPostRequest(context, HttpConstants.REQUEST_REQ_GROUP_MEMBERS_URL, map, responseHandler);
    }

    public void reqGoodNews(Context context,JDHttpResponseHandler<List<GoodNewsBean>> responseHandler){
        Map<String, Object> map = new HashMap<>();
        sendHttpPostRequest(context, HttpConstants.REQUEST_REQ_GOOD_NEWS_URL, map, responseHandler);
    }

    public void reqAdviceList(Context context,JDHttpResponseHandler<List<AdviceBean>> responseHandler){
        Map<String, Object> map = new HashMap<>();
        sendHttpPostRequest(context, HttpConstants.REQUEST_REQ_ADVICE_URL, map, responseHandler);
    }

    public void reqAdviceDetail(Context context, String num, long id, JDHttpResponseHandler<List<AdviceBean>> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("num", num);
        map.put("id", id);
        sendHttpPostRequest(context, HttpConstants.REQUEST_REQ_ADVICE_DETAIL_URL, map, responseHandler);
    }


    public void reqCheckDetail(Context context, String checkId,String type,JDHttpResponseHandler<List<CheckResultBean>> responseHandler){
        Map<String, Object> map = new HashMap<>();
        if(checkId != null)
            map.put("checkId", checkId);
            map.put("type", type);
        sendHttpPostRequest(context, HttpConstants.REQUEST_REQ_CHECK_DETAIL_URL, map, responseHandler);
    }

    public void reqOvulation(Context context,JDHttpResponseHandler<List<CheckResultBean>> responseHandler){
        Map<String, Object> map = new HashMap<>();
        sendHttpPostRequest(context, HttpConstants.REQUEST_REQ_OVULATION_URL, map, responseHandler);
    }

    public void reqAddCollect(Context context, long id, JDHttpResponseHandler<String> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("dynamicId", id);
        sendHttpPostRequest(context, HttpConstants.REQUEST_REQ_ADD_COLLECT_URL, map, responseHandler);
    }

    public void reqDelCollect(Context context, long id, JDHttpResponseHandler<String> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("dynamicId", id);
        sendHttpPostRequest(context, HttpConstants.REQUEST_REQ_ADD_CANCEL_COLLECT_URL, map, responseHandler);
    }

    public void reqCollectList(Context context, int pageNo, JDHttpResponseHandler<ListBaseBean<List<DynamicBean>>> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("pageNo", String.valueOf(pageNo));
        sendHttpPostRequest(context, HttpConstants.REQUEST_REQ_COLLECT_LIST_URL, map, responseHandler);
    }

    public void reqTreatHistory(Context context, JDHttpResponseHandler<List<OplanInforBean>> responseHandler){
        Map<String, Object> map = new HashMap<>();
        sendHttpPostRequest(context, HttpConstants.REQUEST_REQ_TREAT_HISTORY_LIST_URL, map, responseHandler);
    }

    public void unBindHospital(Context context, Long hospitalId, String idNo, String medCard, JDHttpResponseHandler<String> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("hospitalId", hospitalId);
        map.put("idNo", idNo);
        map.put("medCard", medCard);
        sendHttpPostRequest(context, HttpConstants.REQUEST_REQ_UNBIND_HOSPITAL_URL, map, responseHandler);
    }

    public void reqDealTask(Context context, Long taskId, int type, JDHttpResponseHandler<String> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("id", taskId);
        sendHttpPostRequest(context, HttpConstants.REQUEST_DEAL_TASK_URL, map, responseHandler);
    }

    public void sendArchive(Context context, Long hospitalId, JDHttpResponseHandler<String> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("hospitalId", String.valueOf(hospitalId));
        sendHttpPostRequest(context, HttpConstants.REQUEST_REQ_SEND_ARCHIVE_URL, map, responseHandler);
    }

    public void getArchiveHistory(Context context, JDHttpResponseHandler<List<ArchiveHistoryBean>> responseHandler){
        Map<String, Object> map = new HashMap<>();
        sendHttpPostRequest(context, HttpConstants.REQUEST_REQ_GET_ARCHIVE_HISTORY_URL, map, responseHandler);
    }

    public void getHospitalDetail(Context context, Long hospitalId, JDHttpResponseHandler<HospitalBean> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("id", String.valueOf(hospitalId));
        sendHttpPostRequest(context, HttpConstants.REQUEST_REQ_GET_HOSPITAL_URL, map, responseHandler);
    }

    /**
     * @param context
     * @param responseHandler
     */
    public void reqAddUserToHXGroup(Context context, String grouopId, String HXAccount, JDHttpResponseHandler<String> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("groupId", String.valueOf(grouopId));
        map.put("HxCount", String.valueOf(HXAccount));
        sendHttpPostRequest(context, HttpConstants.REQUEST_ADD_USER_HXGROUP_URL, map, responseHandler);
    }

    /**
     * @param context
     * @param responseHandler
     */
    public void reqDeleteUserFromGroup(Context context, String grouopId, String HXAccount, JDHttpResponseHandler<String> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("groupId", String.valueOf(grouopId));
        map.put("HxCount", String.valueOf(HXAccount));
        sendHttpPostRequest(context, HttpConstants.REQUEST_DELETE_USER_HXGROUP_URL, map, responseHandler);
    }

    public void reqGetHospitalChatRooms(Context context,String phone ,String hospitalId,JDHttpResponseHandler<List<ChatGroup>> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("phone",phone);
        map.put("hospitalId", String.valueOf(hospitalId));
        sendHttpPostRequest(context, HttpConstants.REQUEST_GET_HXGROUPS_URL, map, responseHandler);
    }

    public void reqGetUserBanned(Context context,String phone,JDHttpResponseHandler<String> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("phone",phone);
        sendHttpPostRequest(context, HttpConstants.REQUEST_GET_USER_BANNED_URL, map, responseHandler);
    }

    public void reqGetPushNotification(Context context,String id,int type,JDHttpResponseHandler<List<Notificationhistory>> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("id",id);
        map.put("type",type);
        sendHttpPostRequest(context, HttpConstants.REQUEST_GET_PUSHNOTIFICATION, map, responseHandler);
    }

    public void setNotificationReadyStatus(Context context,Long id,String status,JDHttpResponseHandler<List<Notificationhistory>> responseHandler){
        Map<String, Object> map = new HashMap<>();
        map.put("id",id);
        map.put("status",status);
        sendHttpPostRequest(context, HttpConstants.REQUEST_UPDATE_READSTATUS, map, responseHandler);
    }
}
