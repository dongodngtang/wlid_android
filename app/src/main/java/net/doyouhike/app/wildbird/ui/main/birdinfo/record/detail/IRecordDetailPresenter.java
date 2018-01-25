package net.doyouhike.app.wildbird.ui.main.birdinfo.record.detail;

import net.doyouhike.app.wildbird.ui.base.IBasePresenter;

/**
 * 功能：观鸟记录详情
 *
 * @author：曾江 日期：16-4-13.
 */
public interface IRecordDetailPresenter extends IBasePresenter {
    /**
     * @param recordId 观鸟记录id
     */
    void getRecordDetail(String recordId);

    /**
     * @param recordId 观鸟记录id
     * @param strComment 评论内容
     */
    void sendComment(String recordId,String strComment);

    /**
     * @param recordId 观鸟记录id
     */
    void doLike(String recordId);
}
