package net.doyouhike.app.wildbird.biz.model.request.post;

import net.doyouhike.app.wildbird.biz.model.bean.RecordEntity;

/**
 * Created by zaitu on 15-12-9.
 */
public class PublishRecordParam extends BaseUploadRecordParam {
    public PublishRecordParam() {
    }

    public PublishRecordParam(RecordEntity entity) {
        super(entity);
    }
}
