package net.doyouhike.app.wildbird.biz.model.bean;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-8.
 */
public class RankAreaItem {

    private int province_id;   // 类型: int,省份id
    private String province_name;   // 类型: int,省份id
    private String species_count; // 类型: int ,统计的鸟种总数
    private String rank;   // 类型: int,排名

    public int getProvince_id() {
        return province_id;
    }

    public void setProvince_id(int province_id) {
        this.province_id = province_id;
    }

    public String getSpecies_count() {
        return species_count;
    }

    public void setSpecies_count(String species_count) {
        this.species_count = species_count;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }
}
