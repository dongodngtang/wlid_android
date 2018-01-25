package net.doyouhike.app.wildbird.ui.main.add;

import android.widget.FrameLayout;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.bean.RecordEntity;
import net.doyouhike.app.wildbird.ui.base.BaseAppFragmentActivity;

import butterknife.InjectView;

public class EditRecordActivity extends BaseAppFragmentActivity {

    private final String FRAGMENT_TAG=AddRecordFragment.class.getSimpleName();

    @InjectView(R.id.activity_content)
    FrameLayout activityContent;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_editi_record;
    }

    @Override
    protected void initViewsAndEvents() {

        boolean isNeedGetFromNet = getIntent().getBooleanExtra(AddRecordFragment.ARG_NEED_GET_FROM_NET, false);
        boolean isAdd = getIntent().getBooleanExtra(AddRecordFragment.ARG_IS_ADD, true);
        RecordEntity entity = (RecordEntity)getIntent().getSerializableExtra(AddRecordFragment.ARG_RECORD_ENTITY);


        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_content, AddRecordFragment.newInstance(isNeedGetFromNet, isAdd,entity),FRAGMENT_TAG)
                .commit();
    }

}
