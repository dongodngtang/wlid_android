package net.doyouhike.app.wildbird.biz.model.event;

import net.doyouhike.app.wildbird.biz.service.net.UploadRecordService;

/**
 * Created by zengjiang on 16/6/6.
 */
public class RemoveUploadTaskEvent {
    private UploadRecordService service;

    public RemoveUploadTaskEvent(UploadRecordService service) {
        this.service = service;
    }

    public UploadRecordService getService() {
        return service;
    }

    public void setService(UploadRecordService service) {
        this.service = service;
    }
}
