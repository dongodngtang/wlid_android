package net.doyouhike.app.wildbird.biz.model.response;

import net.doyouhike.app.wildbird.biz.db.bean.DbWildBird;
import net.doyouhike.app.wildbird.biz.model.bean.SpeciesInfo;

import java.util.List;

/**
 * 获取鸟种数据更新
 * Created by zengjiang on 16/6/7.
 */
public class GetUpdateBirdResponse {

    private List<DbWildBird> bird_list;


    public List<DbWildBird> getBird_list() {
        return bird_list;
    }

    public void setBird_list(List<DbWildBird> bird_list) {
        this.bird_list = bird_list;
    }
}
