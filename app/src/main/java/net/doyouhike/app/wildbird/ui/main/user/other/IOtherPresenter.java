package net.doyouhike.app.wildbird.ui.main.user.other;

import net.doyouhike.app.wildbird.ui.base.IBasePresenter;

/**
 * 功能：他人主页
 *
 * @author：曾江 日期：16-4-14.
 */
public interface IOtherPresenter extends IBasePresenter {
    /**
     * @param userId 用户id
     */
    void getUserInfo(String userId);
}
