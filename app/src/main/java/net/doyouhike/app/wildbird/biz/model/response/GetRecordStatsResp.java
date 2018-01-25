package net.doyouhike.app.wildbird.biz.model.response;

import net.doyouhike.app.wildbird.biz.model.bean.Species;

import java.util.List;

/**
 * Created by ${luochangdong} on 15-12-7.
 */
public class GetRecordStatsResp{

    /**
     * species : [{"imageUrl":"http://static.test.doyouhike.net/files/2015/11/17/e/efb7c7dff7b5c2385dd8043ad7abbeef.jpg","speciesName":"白鹡鸰","speciesID":"1207","starNum":2},{"imageUrl":"http://static.test.doyouhike.net/files/2015/11/17/c/c29ed7e19d60109d10e2a80935c86961.jpg","speciesName":"[树]麻雀","speciesID":"1198","starNum":2},{"imageUrl":"http://static.test.doyouhike.net/files/2015/11/17/4/4ce52088f4df657343da097bd18159ad.jpg","speciesName":"乌鸫","speciesID":"707","starNum":2},{"imageUrl":"http://static.test.doyouhike.net/files/2015/11/17/e/e71a8784299d7d8e89ab309a4855be3f.jpg","speciesName":"普通翠鸟","speciesID":"171","starNum":2},{"imageUrl":"http://static.test.doyouhike.net/files/2015/11/17/2/20731e7cd83c17e3771551b8de8def22.jpg","speciesName":"（小）白鹭","speciesID":"535","starNum":2},{"imageUrl":"http://static.test.doyouhike.net/files/2015/11/17/f/fdacd1789a14a2f9ad508db2cca0774f.jpg","speciesName":"鹊鸲","speciesID":"778","starNum":2},{"imageUrl":"http://static.test.doyouhike.net/files/2015/11/17/d/d022530046395623adddc25ff17b3be5.jpg","speciesName":"暗绿绣眼鸟","speciesID":"925","starNum":2},{"imageUrl":"http://static.test.doyouhike.net/files/2015/11/17/e/e35fad21141a119f6c234d73eda5557c.jpg","speciesName":"大白鹭","speciesID":"542","starNum":2},{"imageUrl":"http://static.test.doyouhike.net/files/2015/11/17/d/d266d7fb085a0d6b0a316ea2bd6546ec.jpg","speciesName":"绿翅鸭","speciesID":"96","starNum":2},{"imageUrl":"http://static.test.doyouhike.net/files/2015/11/17/9/9e6364623f42255dfe0f41e76fe05853.jpg","speciesName":"红耳鹎","speciesID":"896","starNum":2},{"imageUrl":"http://static.test.doyouhike.net/files/2015/11/17/b/b34e835219b0ba2ae5589c7400430df2.jpg","speciesName":"红嘴鸥","speciesID":"424","starNum":2},{"imageUrl":"http://static.test.doyouhike.net/files/2015/11/17/f/f7793852b577af1fccfca160b3efd27c.jpg","speciesName":"池鹭","speciesID":"545","starNum":2},{"imageUrl":"http://static.test.doyouhike.net/files/2015/11/17/1/14d5ade4710072e140433b6b5bca3ca3.jpg","speciesName":"长尾缝叶莺","speciesID":"968","starNum":2},{"imageUrl":"http://static.test.doyouhike.net/files/2015/11/17/6/644eade57d3c7b9b13b892d52652f6be.jpg","speciesName":"矶鹬","speciesID":"351","starNum":2},{"imageUrl":"http://static.test.doyouhike.net/files/2015/11/17/8/8c3716bd248617c5b49d881bcf5c15a3.jpg","speciesName":"白胸翡翠","speciesID":"176","starNum":2},{"imageUrl":"http://static.test.doyouhike.net/files/2015/11/17/e/e1b9db57bbd2eb4b11189cc62ec0b8b8.jpg","speciesName":"黄腹鹪莺","speciesID":"921","starNum":2},{"imageUrl":"http://static.test.doyouhike.net/files/2015/11/17/d/dd28fc7ed0cbdb64143ebd04691b5d77.jpg","speciesName":"斑鱼狗","speciesID":"180","starNum":2},{"imageUrl":"http://static.test.doyouhike.net/files/2015/11/17/4/4146caacf7a7f5ac967d014601d550e3.jpg","speciesName":"赤颈鸭","speciesID":"87","starNum":2},{"imageUrl":"http://static.test.doyouhike.net/files/2015/11/17/9/9b1e53f69e5239117831c2e8419780c2.jpg","speciesName":"黑领椋鸟","speciesID":"823","starNum":2},{"imageUrl":"http://static.test.doyouhike.net/files/2015/11/17/3/3c538a9bb75881318154d043ccb6577a.jpg","speciesName":"叉尾太阳鸟","speciesID":"1188","starNum":2}]
     * recCount : 20
     */
    private String share_url;
    private int recCount;
    /**
     * imageUrl : http://static.test.doyouhike.net/files/2015/11/17/e/efb7c7dff7b5c2385dd8043ad7abbeef.jpg
     * speciesName : 白鹡鸰
     * speciesID : 1207
     * starNum : 2
     */

    private List<Species> species;

    public void setRecCount(int recCount) {
        this.recCount = recCount;
    }

    public void setSpecies(List<Species> species) {
        this.species = species;
    }

    public int getRecCount() {
        return recCount;
    }

    public List<Species> getSpecies() {
        return species;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    @Override
    public String toString() {
        return "GetRecordStatsResp{" +
                "recCount=" + recCount +
                ", species=" + species +
                ", share_url=" + share_url +
                '}';
    }
}
