package net.doyouhike.app.wildbird.ui.main.user.mine;

import net.doyouhike.app.wildbird.biz.model.bean.BirdRecordDetailItem;
import net.doyouhike.app.wildbird.biz.model.bean.BirdRecordTotalItem;
import net.doyouhike.app.wildbird.biz.model.bean.MyRecord;
import net.doyouhike.app.wildbird.ui.base.IBasePresenter;

import java.util.List;

/**
 * Created by zaitu on 15-12-4.
 */
public interface IMeFragPresenter extends IBasePresenter {
    void updRecordCount();
    void deleteRecord(BirdRecordDetailItem item);
    void modifyAvatar(String filePath);

    void updateDelItem(List<BirdRecordTotalItem> items, BirdRecordDetailItem item);
}
