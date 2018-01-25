package net.doyouhike.app.wildbird.biz.model.bean;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-8.
 */
public class LeaderboardItem {
//
//    user_id: 'xxx',   // 类型: int,广告图片地址
//    avatar: 'xxx'  // 类型: string ,用户头像地址
//    user_name: 'xxx'  // 类型: string ,用户昵称
//    species_count: 'xxx'  // 类型: int ,统计的鸟种总数
//    rank: 'xxx'  // 类型: int ,排名


    /**
     * user_id : xxx
     * avatar : xxx
     * user_name : xxx
     * species_count : xxx
     * rank : xxx
     */

    private String user_id;
    private String avatar;
    private String user_name;
    private String species_count;
    private String rank;

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setSpecies_count(String species_count) {
        this.species_count = species_count;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getSpecies_count() {
        return species_count;
    }

    public String getRank() {
        return rank;
    }
}
