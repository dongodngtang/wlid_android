package net.doyouhike.app.wildbird.biz.model.bean;

/**
 * Created by zaitu on 15-11-20.
 */
public class RecStatsEntity {
    private int recordNum;// 数据类型: int, 记录总数
    private int speciesNum;//: "1" // 数据类型: int , 总鸟种
    private int thisYearRecordNum;//: "0" // 数据类型: int , 今年总记录数
    private int thisYearSpeciesNum;//: "0" // 数据类型: int, 今年总鸟种数

    public int getRecordNum() {
        return recordNum;
    }

    public void setRecordNum(int recordNum) {
        this.recordNum = recordNum;
    }

    public int getSpeciesNum() {
        return speciesNum;
    }

    public void setSpeciesNum(int speciesNum) {
        this.speciesNum = speciesNum;
    }

    public int getThisYearRecordNum() {
        return thisYearRecordNum;
    }

    public void setThisYearRecordNum(int thisYearRecordNum) {
        this.thisYearRecordNum = thisYearRecordNum;
    }

    public int getThisYearSpeciesNum() {
        return thisYearSpeciesNum;
    }

    public void setThisYearSpeciesNum(int thisYearSpeciesNum) {
        this.thisYearSpeciesNum = thisYearSpeciesNum;
    }
}
