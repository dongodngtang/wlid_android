package net.doyouhike.app.wildbird.ui.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import net.doyouhike.app.library.ui.base.BaseFragmentActivity;
import net.doyouhike.app.library.ui.eventbus.EventCenter;
import net.doyouhike.app.library.ui.netstatus.NetUtils;
import net.doyouhike.app.library.ui.utils.CommonUtils;
import net.doyouhike.app.wildbird.app.MyApplication;

@SuppressLint({ "InlinedApi", "Registered" })
public abstract class BaseAppFragmentActivity extends BaseFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
	}

	@Override
	protected void onDestroy() {
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
	
	protected void toast(String msg){
		//消息是否为空
		if (null != msg && !CommonUtils.isEmpty(msg.trim())&&null!=mContext) {
			Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
		}
	}
}
