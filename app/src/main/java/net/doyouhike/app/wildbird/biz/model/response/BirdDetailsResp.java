package net.doyouhike.app.wildbird.biz.model.response;

import net.doyouhike.app.wildbird.biz.model.bean.SpeciesInfo;

/**
 * Created by zaitu on 15-12-3.
 */
public class BirdDetailsResp {

    /**
     * speciesID : 1
     * localName : 雪鹑
     * engName : Snow Partridge
     * latinName : Lerwa lerwa
     * ordo : 鸡形目
     * familia : 雉科
     * genus : 雪鹑属
     * images : [{"url":"http://static.test.doyouhike.net/files/2015/11/17/6/61cdef9d35cf512907e83d9cb16a05c4.jpg","author":"mlchiang"},{"url":"http://static.test.doyouhike.net/files/2015/11/17/f/f5eb69fcdb74a59dbe22783f3952f734.jpg","author":"图库-老爷子"},{"url":"http://static.test.doyouhike.net/files/2015/11/17/7/7039a84076d1dfc9c19d9c259759bb52.jpg","author":"图库-老爷子"},{"url":"http://static.test.doyouhike.net/files/2015/11/17/3/36c226c665f8aebc752bd0825d265763.jpg","author":"KC Lee"},{"url":"http://static.test.doyouhike.net/files/2015/11/17/8/8cf54f0bfa8badae4abcbca1dc5a61f4.jpg","author":"KC Lee"}]
     * comments : [{"commentID":"6","isLike":"0","userName":"","avatar":"","likeNum":"0","content":"习性：常群居；受惊时，两翼极力振动而升空并四散。栖息生境与淡腹雪鸡相同但栖居范围略?２簧蹙迳?"},{"commentID":"5","isLike":"0","userName":"","avatar":"","likeNum":"0","content":"分布状况：常见于海拔2900～5000米林线以上的高山草甸及碎石地带。指名亚种分布在西藏南部；major 分布于四川，callipygia在甘肃南部及四川北部。"},{"commentID":"4","isLike":"0","userName":"","avatar":"","likeNum":"0","content":"分布范围：喜马拉雅山脉、青藏高原至中国中部。"},{"commentID":"3","isLike":"0","userName":"","avatar":"","likeNum":"0","content":"叫声：繁殖期叫声似灰山鹑。受惊时叫声为低哨音，危急时转为尖厉。"},{"commentID":"2","isLike":"0","userName":"","avatar":"","likeNum":"0","content":"虹膜－红褐；嘴－绯红；脚－橙红。"}]
     */

    private SpeciesInfo speciesInfo;

    public void setSpeciesInfo(SpeciesInfo speciesInfo) {
        this.speciesInfo = speciesInfo;
    }

    public SpeciesInfo getSpeciesInfo() {
        return speciesInfo;
    }

}
