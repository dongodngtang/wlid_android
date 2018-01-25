package net.doyouhike.app.wildbird.biz.dao.net;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

/**
 * Created by zaitu on 15-11-18.
 */
public class NetException extends VolleyError {


    private Object tag;
    private ErrState state;
    private String msg;
    private int code;

    public NetException() {
    }

    public NetException(NetworkResponse response) {
        super(response);
    }

    public NetException(String s) {
        super(s);
    }

    public NetException(String exceptionMessage, Throwable reason) {
        super(exceptionMessage, reason);
    }

    public NetException(Throwable cause) {
        super(cause);
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
