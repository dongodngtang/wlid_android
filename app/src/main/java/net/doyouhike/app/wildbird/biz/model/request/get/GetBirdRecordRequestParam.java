package net.doyouhike.app.wildbird.biz.model.request.get;

import android.text.TextUtils;

import net.doyouhike.app.wildbird.biz.model.base.BaseListGetParam;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-12.
 */
public class GetBirdRecordRequestParam extends BaseListGetParam {

    @Override
    protected void setMapValue() {
        map.put("species_id", species_id);
        if (!TextUtils.isEmpty(date)) {
            map.put("date", date);
        }
    }

    String species_id; // 类型: int, 鸟种id
    String date;// 类型: string,格式“2016-3-15” 取指定日之后十天的数据

    public String getSpecies_id() {
        return species_id;
    }

    public void setSpecies_id(String species_id) {
        this.species_id = species_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
