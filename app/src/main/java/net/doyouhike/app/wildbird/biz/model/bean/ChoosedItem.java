package net.doyouhike.app.wildbird.biz.model.bean;

/**
 * Created by zaitu on 15-11-12.
 */
public class ChoosedItem {
    private String key;
    private String content;

    public ChoosedItem(String key, String content) {
        this.key = key;
        this.content = content;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object obj) {

        boolean flag = obj instanceof ChoosedItem;
        if(flag == false){
            return false;
        }
        ChoosedItem item = (ChoosedItem)obj;
        if(this.getKey().equals(item.getKey()) && this.getContent().equals(item.getContent())){
            return true;
        }else {
            return false;
        }
    }
}
