package net.doyouhike.app.wildbird.biz.model.request.get;

import net.doyouhike.app.wildbird.biz.model.base.BaseGetRequest;

/**
 * Created by zaitu on 15-12-3.
 */
public class GetBirdDetailParam extends BaseGetRequest {
    private String speciesID;
    @Override
    protected void setMapValue() {
        map.put("speciesID",speciesID);

    }

    public String getSpeciesID() {
        return speciesID;
    }

    public void setSpeciesID(String speciesID) {
        if (null==speciesID){
            speciesID="";
        }
        this.speciesID = speciesID;
    }
}
