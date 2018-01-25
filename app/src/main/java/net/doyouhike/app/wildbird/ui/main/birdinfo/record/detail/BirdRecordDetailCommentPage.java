package net.doyouhike.app.wildbird.ui.main.birdinfo.record.detail;

import android.content.Context;

import net.doyouhike.app.wildbird.biz.model.bean.BirdRecordDetailCommentItem;
import net.doyouhike.app.wildbird.biz.model.request.get.GetRecordCommentRequestParam;
import net.doyouhike.app.wildbird.ui.main.discovery.ranking.page.PageBase;

/**
 * 功能：观鸟记录评论列表标签
 *
 * @author：曾江 日期：16-4-12.
 */
public class BirdRecordDetailCommentPage extends PageBase<BirdRecordDetailCommentItem> {
    public BirdRecordDetailCommentPage(Context context) {
        super();
        mRequest=new GetRecordCommentRequestParam();
        adapter=new RecordCommentAdapter(context,items);
    }

    @Override
    protected void setEmptyMsg() {
        setEmptyTip("暂无评论");
    }

    @Override
    public GetRecordCommentRequestParam getRequestParam() {
        return (GetRecordCommentRequestParam)mRequest;
    }
}
