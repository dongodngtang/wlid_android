package net.doyouhike.app.wildbird.biz.model.event;

import net.doyouhike.app.wildbird.biz.model.bean.BirdRecordDetailItem;

/**
 * Created by zengjiang on 16/6/8.
 */
public class DelRecordEvent {
    BirdRecordDetailItem item;

    public DelRecordEvent(BirdRecordDetailItem item) {
        this.item = item;
    }

    public BirdRecordDetailItem getItem() {
        return item;
    }

    public void setItem(BirdRecordDetailItem item) {
        this.item = item;
    }
}
