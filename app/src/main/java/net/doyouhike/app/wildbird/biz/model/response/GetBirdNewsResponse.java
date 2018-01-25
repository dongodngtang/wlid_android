package net.doyouhike.app.wildbird.biz.model.response;

import net.doyouhike.app.wildbird.biz.model.bean.BirdNewsItem;

import java.util.List;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-11.
 */
public class GetBirdNewsResponse {

    private List<BirdNewsItem> news_list;

    public List<BirdNewsItem> getNews_list() {
        return news_list;
    }

    public void setNews_list(List<BirdNewsItem> news_list) {
        this.news_list = news_list;
    }
}
