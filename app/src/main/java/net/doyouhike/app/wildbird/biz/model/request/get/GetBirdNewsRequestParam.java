package net.doyouhike.app.wildbird.biz.model.request.get;

import net.doyouhike.app.wildbird.biz.model.base.BaseListGetParam;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-11.
 */
public class GetBirdNewsRequestParam extends BaseListGetParam {

    @Override
    protected void setMapValue() {
        map.put("species_id",getSpecies_id());
    }


    private String species_id;//鸟种id


    public String getSpecies_id() {
        return species_id;
    }

    public void setSpecies_id(String species_id) {
        this.species_id = species_id;
    }
}
