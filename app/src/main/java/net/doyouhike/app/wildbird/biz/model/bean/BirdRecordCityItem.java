package net.doyouhike.app.wildbird.biz.model.bean;

import java.util.List;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-12.
 */
public class BirdRecordCityItem {
//    "city_id":"-1",
//            "city_name":"其他",
//            "record_list":

    private String city_id;
    private String city_name;
    private int  count;// 2,//统计
    private List<BirdRecordDetailItem> record_list;

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public List<BirdRecordDetailItem> getRecord_list() {
        return record_list;
    }

    public void setRecord_list(List<BirdRecordDetailItem> record_list) {
        this.record_list = record_list;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
