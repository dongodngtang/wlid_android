package net.doyouhike.app.wildbird.biz.model.response;

import net.doyouhike.app.wildbird.biz.model.bean.Version;

/**
 * 版本更新
 * Created by zengjiang on 16/6/14.
 */
public class CheckVersionResponse {
    private Version version;

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }
}
