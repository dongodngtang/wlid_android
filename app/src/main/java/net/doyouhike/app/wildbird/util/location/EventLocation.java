package net.doyouhike.app.wildbird.util.location;

import net.doyouhike.app.wildbird.biz.model.bean.WbLocation;

/**
 * 地理位置信息事件
 * Created by zaitu on 15-11-18.
 */
public class EventLocation {
    private EnumLocation enumLocation;
    private WbLocation location;
    private String msg;

    public EventLocation() {
    }

    public EnumLocation getEnumLocation() {
        return enumLocation;
    }

    public void setEnumLocation(EnumLocation enumLocation) {
        this.enumLocation = enumLocation;
    }

    public WbLocation getLocation() {
        return location;
    }

    public void setLocation(WbLocation location) {
        this.location = location;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
