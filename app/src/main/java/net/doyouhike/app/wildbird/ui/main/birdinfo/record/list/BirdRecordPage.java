package net.doyouhike.app.wildbird.ui.main.birdinfo.record.list;

import android.content.Context;

import net.doyouhike.app.wildbird.biz.model.base.BaseListGetParam;
import net.doyouhike.app.wildbird.biz.model.bean.BirdRecordTotalItem;
import net.doyouhike.app.wildbird.biz.model.request.get.GetBirdRecordRequestParam;
import net.doyouhike.app.wildbird.ui.main.birdinfo.record.list.BirdRecordAdapter;
import net.doyouhike.app.wildbird.ui.main.discovery.ranking.page.PageBase;
import net.doyouhike.app.wildbird.util.TimeUtil;

import java.util.List;

/**
 * 功能：观鸟记录列表
 *
 * @author：曾江 日期：16-4-12.
 */
public class BirdRecordPage extends PageBase<BirdRecordTotalItem> {

    public BirdRecordPage(Context context) {
        super();
        mRequest = new GetBirdRecordRequestParam();
        adapter = new BirdRecordAdapter(context, items);

        String tenDayAgo = TimeUtil.getStringFromTime(TimeUtil.getDateBefore(null, 0), "yyyy-MM-dd");
        getRequestParam().setDate(tenDayAgo);
        mRequest.setCount(0);
    }

    @Override
    protected void setEmptyMsg() {

    }

    @Override
    public void updateItem(List item, boolean isRefresh) {
        super.updateItem(item, isRefresh);
        sethasMore(!item.isEmpty());
    }

    @Override
    public BaseListGetParam getRequestParam(boolean isRefresh) {

        if (isRefresh) {
//            String today = TimeUtil.getStringFromTime(TimeUtil.getDateBefore(null, 0), "yyyy-MM-dd");
//            ((GetBirdRecordRequestParam) mRequest).setDate(today);
            ((GetBirdRecordRequestParam) mRequest).setDate(null);



        } else if (!getItems().isEmpty()) {

            String strDate = getItems().get(getItems().size() - 1).getTime();
            String date = strDate.replace(".", "-");

            ((GetBirdRecordRequestParam) mRequest).setDate(date);
        }


        return getRequestParam();
    }

    @Override
    public GetBirdRecordRequestParam getRequestParam() {
        return (GetBirdRecordRequestParam) mRequest;
    }
}
