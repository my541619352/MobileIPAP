package com.tjsinfo.mobileipap.util;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanbo on 2016/9/7.
 */
public class JacksonRequest<T> extends Request<T> {

    /** Default charset for JSON request. */
    protected static final String PROTOCOL_CHARSET = "utf-8";

    private final Response.Listener<T> mListener;

    private static ObjectMapper objectMapper = new ObjectMapper();

    private Class<T> mClass;

    private String mRequestBody;

    private TypeReference<T> mTypeReference;//提供解析复杂JSON数据支持

    public JacksonRequest(int method, String url, Class<T> clazz, String requestBody, Response.Listener<T> listener,
                          Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mClass = clazz;
        mRequestBody = requestBody;
        mListener = listener;
        setMyRetryPolicy();
    }

    public JacksonRequest(int method, String url, TypeReference<T> typeReference, String requestBody, Response.Listener<T> listener,
                          Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mTypeReference = typeReference;
        mRequestBody = requestBody;
        mListener = listener;
        setMyRetryPolicy();
    }

    /**
     * 设置超时时间
     */
    private void setMyRetryPolicy() {
        setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Log.v("mTAG", "json");
            if (mTypeReference == null)//使用Jackson默认的方式解析到mClass类对象

                return (Response<T>) Response.success(
                        objectMapper.readValue(jsonString, TypeFactory.rawClass(mClass)),
                        HttpHeaderParser.parseCacheHeaders(response));
            else//通过构造TypeReference让Jackson解析成自定义的对象类型
                return (Response<T>) Response.success(objectMapper.readValue(jsonString, mTypeReference),
                        HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                    mRequestBody, PROTOCOL_CHARSET);
            return null;
        }
    }
}