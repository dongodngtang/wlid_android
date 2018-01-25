package net.doyouhike.app.wildbird.biz.model.bean;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-15.
 */
public class AreaRank {
    private String province_id;   // 省份id
    private String species_count; // 统计的鸟种总数
    private String rank;   // 排名


    public String getSpecies_count() {
        return species_count;
    }

    public void setSpecies_count(String species_count) {
        this.species_count = species_count;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
