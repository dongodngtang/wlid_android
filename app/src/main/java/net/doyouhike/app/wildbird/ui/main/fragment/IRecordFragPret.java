package net.doyouhike.app.wildbird.ui.main.fragment;

import net.doyouhike.app.wildbird.biz.model.request.get.GetRecordStats;

/**
 * Created by ${luochangdong} on 15-12-7.
 */
public interface IRecordFragPret {
    void getRecord(GetRecordStats bean);

    void searchRecord(GetRecordStats bean);
}
