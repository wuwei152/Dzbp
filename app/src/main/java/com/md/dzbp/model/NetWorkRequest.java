package com.md.dzbp.model;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.data.BaseBean;
import com.md.dzbp.utils.Log4j;

import java.util.Iterator;
import java.util.Map;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpCycleContext;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.MediaType;

/**
 * Created by Administrator on 2016/12/21.
 */
public class NetWorkRequest {
    private Context context;
    private UIDataListener uiDataListener;
    private String currentUrl = "";
    private String TAG = "NetWorkRequest";

    public NetWorkRequest(Context context, UIDataListener uiDataListener) {
        this.context = context;
        this.uiDataListener = uiDataListener;
    }

    /**
     * Get请求
     *
     * @param map
     */
    public void doGetRequest(final int flag, String url, final boolean isShowDialog, Map map) {
        RequestParams params = new RequestParams((HttpCycleContext) context);//请求参数
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String key;
            String value;
            key = it.next().toString();
            value = (String) map.get(key);
            params.addFormDataPart(key, value);
        }
        currentUrl = url;
//        LogUtils.d(currentUrl + "?" + params);
        Log4j.d(TAG,currentUrl + "?" + params);
        HttpRequest.get(url, params, new BaseHttpRequestCallback<String>() {

            //请求网络前
            @Override
            public void onStart() {
                if (isShowDialog)
                    uiDataListener.showDialog();
            }

            @Override
            protected void onSuccess(String response) {
                LogUtils.d(flag + "/" + response);
                if (TextUtils.isEmpty(response)) {
                    return;
                } else {
                    BaseBean result = JSON.parseObject(response, new TypeReference<BaseBean>() {
                    });
                    if (result != null && result.getStatus() == 1) {
                        uiDataListener.loadDataFinish(flag, result.getData());
                    } else if (result != null) {
                        uiDataListener.showToast(result.getMsg());
                    }
                }
            }

            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                Log4j.e(TAG,"onFailure" + errorCode + ""+ msg);
                uiDataListener.onError(flag, msg);
            }

            //请求网络结束
            @Override
            public void onFinish() {
                uiDataListener.dismissDialog();
            }
        });
    }

    /**
     * Post请求
     *
     * @param map
     */
    public void doPostRequest(final int flag, String url, final boolean isShowDialog, Map map) {
        RequestParams params = new RequestParams((HttpCycleContext) context);//请求参数
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String key;
            String value;
            key = it.next().toString();
            value = (String) map.get(key);
            params.addFormDataPart(key, value);
        }
        currentUrl = url;
//        LogUtils.d(currentUrl + "?" + params);
        Log4j.d(TAG,currentUrl + "?" + params);
        HttpRequest.post(url, params, new BaseHttpRequestCallback<String>() {

            //请求网络前
            @Override
            public void onStart() {
                if (isShowDialog)
                    uiDataListener.showDialog();
            }

            @Override
            protected void onSuccess(String response) {
                LogUtils.d(flag + "/" + response);
                if (TextUtils.isEmpty(response)) {
                    return;
                } else {
                    BaseBean result = JSON.parseObject(response, new TypeReference<BaseBean>() {
                    });

                    if (result != null && result.getStatus() == 1) {
                        uiDataListener.loadDataFinish(flag, result.getData());
                    } else if (result != null) {
                        uiDataListener.showToast(result.getMsg());
                    }
                }
            }

            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                Log4j.e(TAG,"onFailure" + errorCode + ""+ msg);
                uiDataListener.onError(flag, msg);
            }

            //请求网络结束
            @Override
            public void onFinish() {
                uiDataListener.dismissDialog();
            }
        });
    }
    /**
     * Json Post请求
     */
    public void doPostRequest(final int flag, String url, final boolean isShowDialog,String json) {
        RequestParams params = new RequestParams((HttpCycleContext) context);//请求参数
        params.addHeader("Content-type","application/json; charset=utf-8");
//        params.setRequestBodyString(json);
        params.setRequestBody("application/json; charset=utf-8",json);
        currentUrl = url;
//        LogUtils.d(currentUrl + "?" + json);
        Log4j.d(TAG,currentUrl + "?" + params);
        HttpRequest.post(url, params, new BaseHttpRequestCallback<String>() {

            //请求网络前
            @Override
            public void onStart() {
                if (isShowDialog)
                    uiDataListener.showDialog();
            }

            @Override
            protected void onSuccess(String response) {
                LogUtils.d(flag + "/" + response);
                if (TextUtils.isEmpty(response)) {
                    return;
                } else {
                    BaseBean result = JSON.parseObject(response, new TypeReference<BaseBean>() {
                    });

                    if (result != null && result.getStatus() == 1) {
                        uiDataListener.loadDataFinish(flag, result.getData());
                    } else if (result != null) {
                        uiDataListener.showToast(result.getMsg());
                    }
                }
            }

            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                Log4j.e(TAG,"onFailure" + errorCode + ""+ msg);
                uiDataListener.onError(flag, msg);
            }

            //请求网络结束
            @Override
            public void onFinish() {
                uiDataListener.dismissDialog();
            }
        });
    }

    /**
     * 取消请求
     */
    public void CancelPost() {
        HttpRequest.cancel(currentUrl);
    }
}
