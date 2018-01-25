package net.doyouhike.app.wildbird.biz.model.event;

/**
 * Created by zaitu on 15-12-9.
 */
public class EventUploadRecord {

    boolean isSuc;
    String strErrMsg;
    private String node_id;

    public boolean isSuc() {
        return isSuc;
    }

    public void setIsSuc(boolean isSuc) {
        this.isSuc = isSuc;
    }

    public String getStrErrMsg() {
        return strErrMsg;
    }

    public void setStrErrMsg(String strErrMsg) {
        this.strErrMsg = strErrMsg;
    }

    public String getNode_id() {
        return node_id;
    }

    public void setNode_id(String node_id) {
        this.node_id = node_id;
    }

    public static EventUploadRecord getErrEvent(String msg){
        EventUploadRecord errEvent=new EventUploadRecord();
        errEvent.setIsSuc(false);
        errEvent.setStrErrMsg(msg);
        return errEvent;
    }
}
