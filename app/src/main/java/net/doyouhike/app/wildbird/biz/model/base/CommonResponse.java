package net.doyouhike.app.wildbird.biz.model.base;

/**
 * Created by zaitu on 15-11-27.
 */
public class CommonResponse<T> extends BaseResponse {
    private T t;

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
