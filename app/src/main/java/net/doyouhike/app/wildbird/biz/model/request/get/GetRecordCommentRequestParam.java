package net.doyouhike.app.wildbird.biz.model.request.get;

import net.doyouhike.app.wildbird.biz.model.base.BaseListGetParam;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-12.
 */
public class GetRecordCommentRequestParam extends BaseListGetParam {

    @Override
    protected void setMapValue() {
        super.setMapValue();
        map.put("record_id",record_id);
    }

    private String record_id;

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }
}
