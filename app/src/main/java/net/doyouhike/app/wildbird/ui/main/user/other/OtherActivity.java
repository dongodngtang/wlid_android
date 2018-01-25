package net.doyouhike.app.wildbird.ui.main.user.other;

import android.widget.FrameLayout;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.ui.base.BaseAppFragmentActivity;

import butterknife.InjectView;

/**
 * 他人主页
 */
public class OtherActivity extends BaseAppFragmentActivity {

    public static final String I_USER_ID = "i_user_id";
    public static final String I_USER_NAME= "i_user_name";

    @InjectView(R.id.fl_acti_other_content)
    FrameLayout flActiOtherContent;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_other;
    }

    @Override
    protected void initViewsAndEvents() {

        String userId = getIntent().getStringExtra(I_USER_ID);
        String userName = getIntent().getStringExtra(I_USER_NAME);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_acti_other_content, OtherFragment.getInstance(userId,userName))
                .commit();
    }

}
