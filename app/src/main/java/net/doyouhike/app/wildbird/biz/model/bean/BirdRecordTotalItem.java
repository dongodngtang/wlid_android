package net.doyouhike.app.wildbird.biz.model.bean;

import java.util.List;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-12.
 */
public class BirdRecordTotalItem {

    private String date;
    private List<BirdRecordCityItem> area_list;

    public String getTime() {
        return date;
    }

    public void setTime(String time) {
        this.date = time;
    }

    public List<BirdRecordCityItem> getArea_list() {
        return area_list;
    }

    public void setArea_list(List<BirdRecordCityItem> area_list) {
        this.area_list = area_list;
    }



}
