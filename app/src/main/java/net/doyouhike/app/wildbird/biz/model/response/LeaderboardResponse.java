package net.doyouhike.app.wildbird.biz.model.response;

import net.doyouhike.app.wildbird.biz.model.bean.LeaderboardItem;

import java.util.List;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-8.
 */
public class LeaderboardResponse {

    private List<LeaderboardItem> person_list;

    public List<LeaderboardItem> getPerson_list() {
        return person_list;
    }

    public void setPerson_list(List<LeaderboardItem> person_list) {
        this.person_list = person_list;
    }
}
