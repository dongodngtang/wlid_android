package net.doyouhike.app.wildbird.ui.base;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import net.doyouhike.app.library.ui.base.BaseLazyFragment;
import net.doyouhike.app.library.ui.eventbus.EventCenter;
import net.doyouhike.app.wildbird.util.ProgressDialogUtils;


public abstract class BaseFragment extends BaseLazyFragment {


	protected ProgressDialogUtils mProgressDialog;

	@Override
	protected void onFirstUserVisible() {

	}

	@Override
	protected void onUserVisible() {

	}

	@Override
	protected void onUserInvisible() {

	}

	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@Override
	protected void initViewsAndEvents() {
		mProgressDialog=new ProgressDialogUtils(getContext(),null);
	}


	@Override
	protected void onAppEventComming(EventCenter eventCenter) {

	}

	@Override
	protected boolean isBindEventBusHere() {
		return false;
	}

	public void toast(String msg){
		showToast(msg);
	}
}
