package net.doyouhike.app.wildbird.ui.main.user.other;

import android.content.Context;

import net.doyouhike.app.wildbird.biz.model.bean.MyRecord;
import net.doyouhike.app.wildbird.biz.model.request.get.GetpersonRecordRequestParam;
import net.doyouhike.app.wildbird.ui.main.discovery.ranking.page.PageBase;
import net.doyouhike.app.wildbird.ui.main.user.mine.MyRecordAdapter;
import net.doyouhike.app.wildbird.ui.main.user.mine.MyRecordList;

/**
 * 功能：他人主页
 *
 * @author：曾江 日期：16-4-14.
 */
public class OtherPager extends PageBase<MyRecord> {
    public OtherPager(Context context) {

        items = new MyRecordList();
        mRequest = new GetpersonRecordRequestParam();
        adapter = new MyRecordAdapter(context, (MyRecordList) items);
    }

    @Override
    protected void setEmptyMsg() {

    }

    @Override
    public GetpersonRecordRequestParam getRequestParam() {
        return (GetpersonRecordRequestParam) mRequest;
    }
}