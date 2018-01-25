package net.doyouhike.app.wildbird.biz.model.response;

import net.doyouhike.app.wildbird.biz.model.bean.Comment;

import java.util.List;

/**
 * Created by zaitu on 15-12-4.
 */
public class GetCommentsResp {

    /**
     * comments : [{"commentID":"5","userName":"","avatar":"","isLike":"0","likeNum":"0","content":"分布状况：常见于海拔2900～5000米林线以上的高山草甸及碎石地带。指名亚种分布在西藏南部；major 分布于四川，callipygia在甘肃南部及四川北部。"},{"commentID":"4","userName":"","avatar":"","isLike":"0","likeNum":"0","content":"分布范围：喜马拉雅山脉、青藏高原至中国中部。"},{"commentID":"3","userName":"","avatar":"","isLike":"0","likeNum":"0","content":"叫声：繁殖期叫声似灰山鹑。受惊时叫声为低哨音，危急时转为尖厉。"},{"commentID":"2","userName":"","avatar":"","isLike":"0","likeNum":"0","content":"虹膜－红褐；嘴－绯红；脚－橙红。"},{"commentID":"1","userName":"","avatar":"","isLike":"0","likeNum":"0","content":"描述：中等体型(35厘米)，通体灰色，上体、头、颈及尾具黑色及白色细条纹，背及两翼淡染棕褐色，胸白且具宽的矛状栗色特征性条纹。各亚种野外难辨。"}]
     * recCount : 5
     */

    private int recCount;
    private List<Comment> comments;

    public void setRecCount(int recCount) {
        this.recCount = recCount;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getRecCount() {
        return recCount;
    }

    public List<Comment> getComments() {
        return comments;
    }

}
