package com.chengsi.weightcalc.http;

public final class HttpConstants {

    public static final String HTTP_BASE_URL = "http://192.168.1.117:8009/";//home
   // public static final String HTTP_BASE_URL = "http://192.168.199.170/";//office
//    public static final String HTTP_BASE_URL = "http://58.51.90.231:8009/"; //test

//    public static final String HTTP_BASE_URL = "http://192.168.51.2:8080/";//formal
//    public static final String HTTP_BASE_URL = "https://120.24.79.125/";//formal
    public static final String HTTP_HOST_URL = HTTP_BASE_URL + "app/";

    /**
     * 用户相关
     */
    public static final String USER_REG_URL = "patient/register.do";
    public static final String USER_SEND_SMS_URL = "patient/validateCode.do";
    public static final String USER_VALIFY_SMS_URL = "patient/valify.do";
    public static final String USER_LOGIN_URL = "patient/login.do";
    public static final String USER_INFO_URL = "patient/userInfo.do";
    public static final String USER_MODIFY_PWD = "patient/updatepwd.do";
    public static final String USER_MODIFY_INFO = "patient/updateinfo.do";
    public static final String INDEX_ROLL_PIC_LIST = "patient/rollPiclist.do";


    public static final String USER_FILE_UPLOAD = "attach/upload.do";
    public static final String PUSH = "patient/push.do";


    public static final String HOSPITAL_LIST_URL = "patient/hospitallist.do";
    public static final String DOCTOR_LIST_URL = "patient/doctorlist.do";
    public static final String DOCTOR_ATTENTION_LIST_URL = "patient/attenDoctorList.do";
    public static final String DOCTOR_ADD_ATTENTION_URL = "patient/attenDoctor.do";
    public static final String DOCTOR_CANCEL_ATTENTION_URL = "patient/cancelAttenDoctor.do";
    public static final String REQUEST_SEND_CONSULT_URL = "patient/consult.do";
    public static final String REQUEST_CONSULT_LIST_URL = "patient/allConsultationList.do";
    public static final String REQUEST_MY_CONSULT_LIST_URL = "patient/consultlist.do";

    public static final String REQUEST_RESERVATION_INFO_URL = "patient/order.do";
    public static final String REQUEST_RESERVATION_DOCTOR_INFO_URL = "patient/orderDoctor.do";
    public static final String REQUEST_RESERVATION_TIME_INFO_URL = "patient/orderDoctorArrange.do";
    public static final String REQUEST_RESERVATION_ADD_URL = "patient/addOrder.do";
    public static final String REQUEST_RESERVATION_UNTREATED_URL = "patient/orderList.do";

    public static final String REQUEST_OUT_RESERVATION_DOCTOR_INFO_URL = "patient/getHISDoctor.do";
    public static final String REQUEST_OUT_RESERVATION_TIME_INFO_URL = "HospitalWebService/HISAPI/getHISRequest.do";
    public static final String REQUEST_OUT_RESERVATION_ADD_URL = "HospitalWebService/HISAPI/orderHISReg.do";
    public static final String REQUEST_OUT_RESERVATION_PAY_URL = "HospitalWebService/HISAPI/payHISReg.do";
    public static final String REQUEST_OUT_RESERVATION_CANCEL_URL = "HospitalWebService/HISAPI/cancleHISGh.do";
    public static final String REQUEST_OUT_RESERVATION_HISTORY_URL = "patient/reqOutReservationHistory.do";
    public static final String REQUEST_OUT_HIS_URL = "patient/reqAllHis.do";

    public static final String REQUEST_ATTEND_HOSPITAL_URL = "patient/attenHospital.do";
    public static final String REQUEST_ATTEND_HOSPITAL_LIST_URL = "patient/attenHospitalList.do";
    public static final String REQUEST_CHAT_GROUP_LIST_URL = "patient/groupList.do";
    public static final String REQUEST_DYNAMIC_LIST_URL = "patient/dynamiclist.do";
    public static final String REQUEST_CONSULT_DOCTOR_LIST_URL = "patient/consultDoctorList.do";


    public static final String REQUEST_DAY_TASK_LIST_URL = "patient/getDayTask.do";
    public static final String REQUEST_DEAL_TASK_URL = "patient/dealTask.do";
    public static final String REQUEST_DAILY_TASK_LIST_URL = "patient/getDailyTask.do";
    public static final String REQUEST_REQ_ADVICE_URL = "patient/getDoctorAdvices.do";
    public static final String REQUEST_REQ_ADVICE_DETAIL_URL = "patient/getDoctorAdvice.do";
    public static final String REQUEST_REQ_CHECK_DETAIL_URL = "patient/getCheckDetail.do";

    public static final String REQUEST_CHECK_LIST_URL = "patient/getCheck.do";
    public static final String REQUEST_REQ_ARCHIVES_URL = "patient/getArchive.do";
    public static final String REQUEST_REQ_ADD_ARCHIVES_URL = "patient/addArchive.do";
    public static final String REQUEST_REQ_CASES_URL = "patient/getCase.do";
    public static final String REQUEST_REQ_ADD_CASES_URL = "patient/addCase.do";

    public static final String REQUEST_REQ_BIND_HOSPITAL_URL = "patient/bindHospital.do";
    public static final String REQUEST_REQ_UNBIND_HOSPITAL_URL = "patient/unBindmCardNo.do";

    public static final String REQUEST_REQ_GROUP_MEMBERS_URL = "patient/groupMemberList.do";
    public static final String REQUEST_REQ_OVULATION_URL = "patient/ovulation.do";
    public static final String REQUEST_REQ_GOOD_NEWS_URL = "patient/getGoodNews.do";


    public static final String REQUEST_REQ_ADD_COLLECT_URL = "patient/collection.do";
    public static final String REQUEST_REQ_ADD_CANCEL_COLLECT_URL = "patient/cancelCollection.do";
    public static final String REQUEST_REQ_COLLECT_LIST_URL = "patient/collectionlist.do";
    public static final String REQUEST_REQ_TREAT_HISTORY_LIST_URL = "patient/getTreatHis.do";

    public static final String REQUEST_REQ_SEND_ARCHIVE_URL = "patient/addArchiveLog.do";
    public static final String REQUEST_REQ_GET_ARCHIVE_HISTORY_URL = "patient/getArchiveLog.do";
    public static final String REQUEST_REQ_GET_HOSPITAL_URL = "patient/getHospital.do";

    public static final String REQUEST_ADD_USER_HXGROUP_URL = "patient/addUserToHXGroup.do";
    public static final String REQUEST_DELETE_USER_HXGROUP_URL = "patient/deleteUserFromHXGroup.do";
    public static final String REQUEST_GET_HXGROUPS_URL = "patient/findHospitalChatRooms.do";
    public static final String REQUEST_GET_USER_BANNED_URL = "patient/getUserBannedState.do";
    public static final String REQUEST_GET_PUSHNOTIFICATION = "patient/getPush.do";
    public static final String REQUEST_UPDATE_READSTATUS = "patient/setNotificationReadStatus.do";
}
