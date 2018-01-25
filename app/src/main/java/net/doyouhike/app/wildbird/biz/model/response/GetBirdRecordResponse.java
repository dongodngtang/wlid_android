package net.doyouhike.app.wildbird.biz.model.response;

import net.doyouhike.app.wildbird.biz.model.bean.BirdRecordTotalItem;

import java.util.List;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-12.
 */
public class GetBirdRecordResponse {

    List<BirdRecordTotalItem> species_records;

    public List<BirdRecordTotalItem> getSpecies_records() {
        return species_records;
    }

    public void setSpecies_records(List<BirdRecordTotalItem> species_records) {
        this.species_records = species_records;
    }
}
