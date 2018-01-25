package net.doyouhike.app.wildbird.biz.model.request.get;

import net.doyouhike.app.wildbird.biz.model.base.BaseGetRequest;

/**
 * Created by zaitu on 15-12-3.
 */
public class CheckDataUpdateGetParam extends BaseGetRequest {

    private String dataVer;

    @Override
    protected void setMapValue() {
        map.put("dataVer",dataVer);
    }

    public String getDataVer() {
        return dataVer;
    }

    public void setDataVer(String dataVer) {
        this.dataVer = dataVer;
    }
}
