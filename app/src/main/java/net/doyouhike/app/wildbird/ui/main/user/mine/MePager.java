package net.doyouhike.app.wildbird.ui.main.user.mine;

import android.content.Context;
import android.text.TextUtils;

import net.doyouhike.app.wildbird.biz.model.Content;
import net.doyouhike.app.wildbird.biz.model.bean.MyRecord;
import net.doyouhike.app.wildbird.biz.model.request.get.GetMyRecordsParam;
import net.doyouhike.app.wildbird.ui.main.discovery.ranking.page.PageBase;

/**
 * 功能：我的页面
 *
 * @author：曾江 日期：16-4-13.
 */
public class MePager extends PageBase<MyRecord> {
    public MePager(Context context) {

        items = new MyRecordList();
        mRequest = new GetMyRecordsParam();
        adapter = new MyRecordAdapter(context, (MyRecordList) items);
    }

    @Override
    protected void setEmptyMsg() {
    }

    @Override
    public void setErrTip(String errTip) {
        if (TextUtils.isEmpty(errTip)){
            super.setErrTip("获取记录失败");
        }else {

            super.setErrTip(errTip);
        }
    }

    @Override
    public String getErrTip() {
        if (TextUtils.isEmpty(super.getErrTip())){
            return "获取记录失败";
        }

        return super.getErrTip();
    }

    @Override
    public String getRequestTag() {
        return Content.REQ_getMyRecords;
    }

    @Override
    public GetMyRecordsParam getRequestParam() {
        return (GetMyRecordsParam) mRequest;
    }

}
