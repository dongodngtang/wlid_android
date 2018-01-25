package net.doyouhike.app.wildbird.biz.model.response;

import net.doyouhike.app.wildbird.biz.model.bean.RecordEntity;

import java.util.List;

/**
 * Created by zaitu on 15-12-4.
 */
public class GetMyRecordResp {

    /**
     * records : [{"hasImage":0,"time":"1449216965","location":"广东省深圳市南山区汕头街东e-2栋","speciesName":"白颊山鹧鸪","recordID":"6001303","cityID":null,"number":"6","speciesID":"18"},{"hasImage":0,"time":"1449215003","location":"广东省深圳市南山区汕头街东e-2栋","speciesName":"白颊山鹧鸪","recordID":"6001299","cityID":null,"number":"3","speciesID":"18"},{"hasImage":3,"time":"1448002549","location":"广东省深圳市南山区汕头街东e-2栋","speciesName":"红胸山鹧鸪","recordID":"6000736","cityID":null,"number":"4","speciesID":"20"},{"hasImage":0,"time":"1447994230","location":"广东省深圳市南山区汕头街东e-2栋","speciesName":"褐胸山鹧鸪","recordID":"6000714","cityID":null,"number":"4","speciesID":"21"},{"hasImage":0,"time":"1447993993","location":"广东省深圳市南山区汕头街东e-2栋","speciesName":"环颈山鹧鸪","recordID":"6000712","cityID":null,"number":"3","speciesID":"16"}]
     * recCount : 5
     */

    private int recCount;
    private List<RecordEntity> records;

    public void setRecCount(int recCount) {
        this.recCount = recCount;
    }

    public void setRecords(List<RecordEntity> records) {
        this.records = records;
    }

    public int getRecCount() {
        return recCount;
    }

    public List<RecordEntity> getRecords() {
        return records;
    }

}
