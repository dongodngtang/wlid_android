package net.doyouhike.app.wildbird.ui.base;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import net.doyouhike.app.library.ui.base.BaseActivity;
import net.doyouhike.app.library.ui.eventbus.EventCenter;
import net.doyouhike.app.library.ui.netstatus.NetUtils;
import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.app.MyApplication;
import net.doyouhike.app.wildbird.util.ProgressDialogUtils;
import net.doyouhike.app.wildbird.ui.view.TitleView;

public abstract class BaseAppActivity extends BaseActivity implements IBaseView {
    protected ProgressDialogUtils mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // 统计时长
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        if (null != mProgressDialog)
            mProgressDialog.onDestroy();
        MyApplication.getInstance().removeActivity(this);
        super.onDestroy();
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }


    @Override
    protected void onAppEventComming(EventCenter eventCenter) {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        mProgressDialog = new ProgressDialogUtils(this, null);
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return false;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }

    public void toast(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void setHeaderTitle(String title) {
        ((TitleView) findViewById(R.id.titleview)).setTitle(title);
    }
}
