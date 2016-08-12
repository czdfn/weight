package com.chengsi.weightcalc.http;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
//import LoginActivity;
import com.chengsi.weightcalc.bean.BaseBean;
import com.chengsi.weightcalc.utils.LogUtil;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jiadao.corelibs.utils.TimeUtils;

/**
 * Created by yingjianxu on 15/6/17.
 */
public class JDHttpResponseHandler<T> extends TextHttpResponseHandler{

    private static final String LOG_TAG = "JDHttpResponseHandler";


    private boolean useRFC5179CompatibilityMode = true;

    private TypeReference<BaseBean<T>> typeReference;

    /**
     * Creates new JsonHttpResponseHandler, with JSON String encoding UTF-8
     */
    public JDHttpResponseHandler(TypeReference<BaseBean<T>> typeReference) {
        super(DEFAULT_CHARSET);
        this.typeReference = typeReference;
    }

    /**
     * Creates new JsonHttpResponseHandler with given JSON String encoding
     *
     * @param encoding String encoding to be used when parsing JSON
     */
    public JDHttpResponseHandler(String encoding, TypeReference<BaseBean<T>> typeReference) {
        super(encoding);
        this.typeReference = typeReference;
    }

    /**
     * Creates new JsonHttpResponseHandler with JSON String encoding UTF-8 and given RFC5179CompatibilityMode
     *
     * @param useRFC5179CompatibilityMode Boolean mode to use RFC5179 or latest
     */
    public JDHttpResponseHandler(boolean useRFC5179CompatibilityMode, TypeReference<BaseBean<T>> typeReference) {
        super(DEFAULT_CHARSET);
        this.useRFC5179CompatibilityMode = useRFC5179CompatibilityMode;
        this.typeReference = typeReference;
    }

    /**
     * Creates new JsonHttpResponseHandler with given JSON String encoding and RFC5179CompatibilityMode
     *
     * @param encoding                    String encoding to be used when parsing JSON
     * @param useRFC5179CompatibilityMode Boolean mode to use RFC5179 or latest
     */
    public JDHttpResponseHandler(String encoding, boolean useRFC5179CompatibilityMode, TypeReference<BaseBean<T>> typeReference) {
        super(encoding);
        this.useRFC5179CompatibilityMode = useRFC5179CompatibilityMode;
        this.typeReference = typeReference;
    }

    /**
     * Returns when request succeeds
     *
     * @param statusCode http response status line
     * @param headers    response headers if any
     * @param response   parsed response if any
     */
    public void onSuccess(int statusCode, Header[] headers, BaseBean<T> response) {
        Log.w(LOG_TAG, "onSuccess(int, Header[], T) was not overriden, but callback was received");
        if (response == null){
            response = new BaseBean();
        }
        onRequestCallback(response);
    }

    /**
     * Returns when request failed
     *
     * @param statusCode    http response status line
     * @param headers       response headers if any
     * @param throwable     throwable describing the way request failed
     * @param errorResponse parsed response if any
     */
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, BaseBean<T> errorResponse) {
        if (throwable != null){
            throwable.printStackTrace();
        }
        if (errorResponse == null){
            errorResponse = new BaseBean();
            errorResponse.setInfoCode(1);
            errorResponse.setMessage("无法连接网络服务器");
        }
        onRequestCallback(errorResponse);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        if (throwable != null){
            throwable.printStackTrace();
        }
        BaseBean err = new BaseBean();
        err.setInfoCode(1);
        err.setMessage("无法连接网络服务器");
        onRequestCallback(err);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        Log.w(LOG_TAG, "onSuccess(int, Header[], String) was not overriden, but callback was received");
        BaseBean result = new BaseBean();
        result.setMessage(responseString);
        onRequestCallback(result);
    }

    @Override
    public final void onSuccess(final int statusCode, final Header[] headers, final byte[] responseBytes) {
        if (statusCode != HttpStatus.SC_NO_CONTENT) {
            Runnable parser = new Runnable() {
                @Override
                public void run() {
                    try {
                        final Object jsonResponse = parseResponse(responseBytes);
                        postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                // In RFC5179 a null value is not a valid JSON
                                if (!useRFC5179CompatibilityMode && jsonResponse == null) {
                                    onSuccess(statusCode, headers, (String) jsonResponse);
                                } else if (jsonResponse instanceof BaseBean) {
                                    onSuccess(statusCode, headers, (BaseBean) jsonResponse);
                                }else if (jsonResponse instanceof String) {
                                    // In RFC5179 a simple string value is not a valid JSON
                                    if (useRFC5179CompatibilityMode) {
                                        onFailure(statusCode, headers, (String) jsonResponse, new JSONException("Response cannot be parsed as JSON data"));
                                    } else {
                                        onSuccess(statusCode, headers, (String) jsonResponse);
                                    }
                                }
                            }
                        });
                    } catch (final JSONException ex) {
                        postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                onFailure(statusCode, headers, ex, (BaseBean) null);
                            }
                        });
                    }
                }
            };
            if (!getUseSynchronousMode() && !getUsePoolThread()) {
                new Thread(parser).start();
            } else {
                // In synchronous mode everything should be run on one thread
                parser.run();
            }
        } else {
            BaseBean jdResult = new BaseBean();
            jdResult.setInfoCode(1);
            onSuccess(statusCode, headers, jdResult);
        }
    }

    @Override
    public final void onFailure(final int statusCode, final Header[] headers, final byte[] responseBytes, final Throwable throwable) {
        if (throwable != null){
            throwable.printStackTrace();
            String jsonString = "";
            if (responseBytes != null){
                jsonString = getResponseString(responseBytes, getCharset());
                LogUtil.e("OnFailure URL: " + getRequestURI().toString() + "\n error Message: " + throwable.getMessage() + "\n" +
                        "response String : " + jsonString);
            }else{
                LogUtil.e("OnFailure URL: " + getRequestURI().toString() + "\n error Message: " + throwable.getMessage());
            }
        }
        if (responseBytes != null) {
            Runnable parser = new Runnable() {
                @Override
                public void run() {
                    try {
                        final Object jsonResponse = parseResponse(responseBytes);
                        postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                // In RFC5179 a null value is not a valid JSON
                                if (!useRFC5179CompatibilityMode && jsonResponse == null) {
                                    onFailure(statusCode, headers, (String) jsonResponse, throwable);
                                } else if (jsonResponse instanceof BaseBean) {
                                    onFailure(statusCode, headers, throwable, (BaseBean) jsonResponse);
                                }  else if (jsonResponse instanceof String) {
                                    onFailure(statusCode, headers, (String) jsonResponse, throwable);
                                }
                            }
                        });

                    } catch (final JSONException ex) {
                        postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                onFailure(statusCode, headers, ex, (BaseBean) null);
                            }
                        });

                    }
                }
            };
            if (!getUseSynchronousMode() && !getUsePoolThread()) {
                new Thread(parser).start();
            } else {
                // In synchronous mode everything should be run on one thread
                parser.run();
            }
        } else {
            Log.v(LOG_TAG, "response body is null, calling onFailure(Throwable, JSONObject)");
            onFailure(statusCode, headers, throwable, (BaseBean) null);
        }
    }

    /**
     * Returns Object of type {@link JSONObject}, {@link JSONArray}, String, Boolean, Integer, Long,
     * Double or {@link JSONObject#NULL}, see {@link org.json.JSONTokener#nextValue()}
     *
     * @param responseBody response bytes to be assembled in String and parsed as JSON
     * @return Object parsedResponse
     * @throws JSONException exception if thrown while parsing JSON
     */
    protected Object parseResponse(byte[] responseBody) throws JSONException {
        if (null == responseBody)
            return null;
        BaseBean<T> result = null;
        //trim the string to prevent start with blank, and test if the string is valid JSON, because the parser don't do this :(. If JSON is not valid this will return null
        String jsonString = getResponseString(responseBody, getCharset());
        LogUtil.i(TimeUtils.getCurrentTimeInString() + "       " + JDHttpResponseHandler.LOG_TAG + "   Request url:" + getRequestURI().toString());
        LogUtil.i(JDHttpResponseHandler.LOG_TAG + "   Response data:" + jsonString);
        try{
            if (this.typeReference != null){
                result = JSON.parseObject(jsonString, this.typeReference);
            }else {
                result = com.alibaba.fastjson.JSONObject.parseObject(jsonString, new TypeReference<BaseBean<T>>() {
                });
            }
        }catch (Exception e){
            LogUtil.e(e.getMessage());
            e.printStackTrace();
        }
        if (result == null) {
            result = new BaseBean();
            result.setInfoCode(1);
            result.setData(null);
            result.setMessage("数据解析出错");
            return jsonString;
        }
        return result;
    }

    public boolean isUseRFC5179CompatibilityMode() {
        return useRFC5179CompatibilityMode;
    }

    public void setUseRFC5179CompatibilityMode(boolean useRFC5179CompatibilityMode) {
        this.useRFC5179CompatibilityMode = useRFC5179CompatibilityMode;
    }

    public void onRequestCallback(BaseBean<T> result){
        if (result.getInfoCode() == 401 || result.getInfoCode() == 1007){
            //用户token过期
//            MyApplication.getInstance().userManager.resetUser(null);
//            Intent intent = new Intent(MyApplication.getInstance(), LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            MyApplication.getInstance().startActivity(intent);
        }
    };
}
