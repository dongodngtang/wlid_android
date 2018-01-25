package net.doyouhike.app.wildbird.biz.model.bean;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-8.
 */
public class PersonRank {
    private String rank;  // 类型: int,排名
    private String species_count;  // 类型: int ,统计的鸟种总数

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getSpecies_count() {
        return species_count;
    }

    public void setSpecies_count(String species_count) {
        this.species_count = species_count;
    }
}
