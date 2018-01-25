package net.doyouhike.app.wildbird.ui.main.birdinfo.detail;

import net.doyouhike.app.wildbird.ui.base.IBasePresenter;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-11.
 */
public interface IBirdDetailPresenter extends IBasePresenter{
    /**
     * @param speciesID 鸟种id
     */
    void getDetail(String speciesID);

    /**
     * @param speciesID 鸟种id
     */
    void getOfflineSpecies(String speciesID);

}
