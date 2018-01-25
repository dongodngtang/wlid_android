package net.doyouhike.app.wildbird.biz.model.base;

import java.util.HashMap;

/**
 * 每一个请求接口都必须继承此接口
 * Created by zaitu on 15-11-18.
 */
public abstract class BaseGetRequest {

    private Object tag;
    protected HashMap<String, String> map = new HashMap<>();

    /**
     * 设置具体的参数值
     */
    protected abstract void setMapValue();

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    /**
     *
     * @return
     */
    public HashMap<String, String> toHashMap() {
        map.clear();
        setMapValue();
        return map;
    }

}
