package net.doyouhike.app.wildbird.biz.model.bean;

import android.support.annotation.NonNull;

import net.doyouhike.app.wildbird.util.GetCityIDUtils;

import java.text.SimpleDateFormat;

/**
 * 功能：
 *
 * @author：曾江 日期：16-3-11.
 */
public class MyRecord implements Comparable<MyRecord> {
    private boolean isTitle = false;
    private RecordEntity record;
    private String dateTime;
    private String dateTimeFormatShareTitle;
    private String dateTimeFormatItemContent;


    public MyRecord() {

    }
    public MyRecord(RecordEntity record) {
        this.record = record;
        setTitleTime(record.getTime());
        this.record.setCityName(GetCityIDUtils.getCity(record.getCityID()));
    }


    public MyRecord copy() {
        MyRecord record = new MyRecord();
        record.setRecord(getRecord());
        record.setTitleTime(getRecord().getTime());
        return record;
    }

    public boolean isTitle() {
        return isTitle;
    }

    public void setIsTitle(boolean isTitle) {
        this.isTitle = isTitle;
    }

    public RecordEntity getRecord() {
        return record;
    }

    public void setRecord(RecordEntity record) {
        this.record = record;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setTitleTime(long time) {
        time=time* 1000;
        this.dateTime = new SimpleDateFormat("yyyy-MM-dd").format(time);
        this.dateTimeFormatShareTitle = new SimpleDateFormat("yyyy年MM月dd日").format(time);
        this.dateTimeFormatItemContent = new SimpleDateFormat("HH时mm分").format(time);
//        this.dateTimeFormatItemContent = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分 E").format(time);
    }

    public String getDateTimeFormatShareTitle() {
        return dateTimeFormatShareTitle;
    }

    public void setDateTimeFormatShareTitle(String dateTimeFormatShareTitle) {
        this.dateTimeFormatShareTitle = dateTimeFormatShareTitle;
    }

    public String getDateTimeFormatItemContent() {
        return dateTimeFormatItemContent;
    }

    public void setDateTimeFormatItemContent(String dateTimeFormatItemContent) {
        this.dateTimeFormatItemContent = dateTimeFormatItemContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyRecord record1 = (MyRecord) o;

        if (isTitle != record1.isTitle) return false;
        if (!dateTime.equals(record1.getDateTime())) return false;
        if (isTitle)return true;
        return record.equals(record1.record);

    }

    @Override
    public int hashCode() {
        int result = (isTitle ? 1 : 0);
        result = 31 * result + record.hashCode();
        return result;
    }

    @Override
    public int compareTo(@NonNull MyRecord another) {

        if (isTitle){
            return dateTime.compareTo(another.getDateTime());
        }else {
            return record.compareTo(another.getRecord());
        }

    }

}
