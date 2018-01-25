package net.doyouhike.app.wildbird.biz.model.request.get;

import net.doyouhike.app.wildbird.biz.model.base.BaseListGetParam;

/**
 * Created by zaitu on 15-12-3.
 */
public class GetCommentsParam extends BaseListGetParam {

    private int speciesID;

    @Override
    protected void setMapValue() {
        super.setMapValue();
        map.put("speciesID",speciesID+"");
    }

    public int getSpeciesID() {
        return speciesID;
    }

    public void setSpeciesID(int speciesID) {
        this.speciesID = speciesID;
    }
}
