package net.doyouhike.app.wildbird.biz.model.request.post;

import com.google.gson.annotations.Expose;

import net.doyouhike.app.wildbird.biz.model.bean.RecordEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zaitu on 15-12-9.
 */
public class ModifyRecordParam extends BaseUploadRecordParam {
    @Expose
    private int recordID;
    @Expose
    private List<Integer> delImages=new ArrayList<>();

    public ModifyRecordParam() {
    }

    public ModifyRecordParam(RecordEntity entity) {
        super(entity);
        setContent(entity);
    }

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    public List<Integer> getDelImages() {
        return delImages;
    }

    public void setDelImages(List<Integer> delImages) {
        this.delImages = delImages;
    }

    private void setContent(RecordEntity entity) {
        setRecordID(entity.getRecordID());
        setDelImages(entity.getDelImages());
    }
}
