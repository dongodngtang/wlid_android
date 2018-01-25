package net.doyouhike.app.wildbird.biz.model.base;

import net.doyouhike.app.wildbird.biz.dao.net.ErrState;

/**
 * Created by zaitu on 15-11-18.
 */
public class BaseResponse {
    private int ret;
    private boolean isSuccess;
    private String strJSON;
    private String log;
    private int code;
    private String errMsg;
    private Object tag;
    private ErrState state;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getStrJSON() {
        return strJSON;
    }

    public void setStrJSON(String strJSON) {
        this.strJSON = strJSON;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public ErrState getState() {
        return state;
    }

    public void setState(ErrState state) {
        this.state = state;
    }
}
