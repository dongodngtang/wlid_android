package net.doyouhike.app.wildbird.biz.dao.base;


import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import net.doyouhike.app.wildbird.biz.dao.net.ErrState;
import net.doyouhike.app.wildbird.biz.dao.net.NetException;
import net.doyouhike.app.wildbird.biz.model.Content;
import net.doyouhike.app.wildbird.biz.model.base.BaseResponse;
import net.doyouhike.app.wildbird.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 15-5-21.
 */
public abstract class BaseResponseProcess<T extends BaseResponse> implements IResponseProcess<T> {
    protected T t;
    protected Gson gson =new GsonBuilder().serializeNulls().create();

    public BaseResponseProcess(T t) {
        setT(t);
    }

    protected abstract void parserDataJson(String strJson) ;

    protected abstract void doNext(JSONObject jsonObject) ;


    @Override
    public T getResponse(String response) {
        parserJSON(response);
        return t;
    }

    @Override
    public void setExtraTag(Object tag) {
        t.setTag(tag);
    }

    @Override
    public Object getExtraTag() {
        return t.getTag();
    }

    public void setT(T t) {
        this.t = t;
    }

    private void parserJSON(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);

            t.setStrJSON(jsonObject.toString());
            t.setRet(1);//设置网络响应的ret默认值为1
            if (jsonObject.has(Content.NET_RESP_RET)) {
                t.setRet(jsonObject.getInt(Content.NET_RESP_RET));
            }

            if (t.getRet() == 0)
                t.setIsSuccess(true);

            if (jsonObject.has(Content.NET_RESP_LOG)) {

                String log = jsonObject.getString(Content.NET_RESP_LOG);
                t.setLog(log);

                t.setErrMsg(log);
                if (log.contains("100008")) {
                    t.setIsSuccess(false);
                    t.setErrMsg("请重新登录。");
                    t.setCode(100008);
                    t.setState(ErrState.DATA_ERR);
                }
                if (log.contains("token is invalidate")) {
                    t.setIsSuccess(false);
                    t.setErrMsg("登录失效，请重新登录。");
                    t.setCode(100008);
                    t.setState(ErrState.DATA_ERR);
                }
            }

            if (jsonObject.has(Content.NET_RESP_CODE)) {
                String strCode = jsonObject.getString(Content.NET_RESP_CODE);
                int logCode;
                if (TextUtils.isEmpty(strCode)) {
                    logCode = 0;
                } else {
                    logCode = jsonObject.getInt(Content.NET_RESP_CODE);
                }
                t.setCode(logCode);

                if (logCode == 100008) {
                    t.setIsSuccess(false);
                    t.setErrMsg("登录失效，请重新登录。");
                    t.setCode(100008);
                    t.setState(ErrState.DATA_ERR);
                }
            }
            //解析“data”部分
            if (jsonObject.has(Content.NET_RESP_DATA)) {
                String oData=String.valueOf(jsonObject.get(Content.NET_RESP_DATA));
                if (oData.equals("null")){
                    LogUtil.d("LogUtil NET_GET","null==oData");
                    t.setIsSuccess(false);
                    t.setErrMsg("解析出错，data=null");
                    t.setState(ErrState.PARSE_ERR);
                }else {
                    parserDataJson(jsonObject.getString(Content.NET_RESP_DATA));
                }
            }
            doNext(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
            t.setIsSuccess(false);
            t.setErrMsg(e.getMessage());
            t.setState(ErrState.PARSE_ERR);
        }
    }


    protected String getJSONStr(JSONObject object, String key) throws JSONException, NetException {

        if (object.has(key)) {
            return object.getString(key);
        } else {
            throw new NetException("缺少参数：" + key);
        }
    }

    protected int getJsonInt(JSONObject object, String key)throws JSONException, NetException{

        if (object.has(key)){
            String value=object.getString(key);

            if (!TextUtils.isEmpty(value)){
                return object.getInt(key);
            }
        }
        return -1;
    }
}
