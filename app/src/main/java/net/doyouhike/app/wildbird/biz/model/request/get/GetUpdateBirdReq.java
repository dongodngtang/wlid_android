package net.doyouhike.app.wildbird.biz.model.request.get;

import android.support.annotation.NonNull;

import net.doyouhike.app.wildbird.biz.model.base.BaseGetRequest;

/**
 * 获取最新鸟种
 * Created by zengjiang on 16/6/7.
 */
public class GetUpdateBirdReq extends BaseGetRequest{


    @Override
    protected void setMapValue() {
        map.put("species_id",species_id);
    }

    public GetUpdateBirdReq(@NonNull String species_id) {
        this.species_id = species_id;
    }

    private String species_id;

    public String getSpecies_id() {
        return species_id;
    }

    public void setSpecies_id(String species_id) {
        this.species_id = species_id;
    }

}
