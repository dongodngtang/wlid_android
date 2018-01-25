package net.doyouhike.app.wildbird.biz.model.base;

/**
 * Created by zaitu on 15-12-3.
 */
public class BaseListGetParam extends BaseGetRequest{

    private int skip=1;// 类型: int, 从第x条开始取
    private int  count=20;// 类型: int,  共取 y 条数据

    @Override
    protected void setMapValue() {
        map.put("skip",skip+"");
        map.put("count",count+"");
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
