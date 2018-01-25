package net.doyouhike.app.library.ui.uistate;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.doyouhike.app.library.ui.R;
import net.doyouhike.app.library.ui.proccess.CircularProgressBar;
import net.doyouhike.app.library.ui.widgets.AsyncAlertDialog;

/**
 * 主要实现功能： 简单的ui控制器 视图不同加载状态在此实现
 * 作者：zaitu
 * 日期：15-12-23.
 */
public class SimpleUiHandler extends BaseUiStateHandle implements BaseUiStateHandle.OnViewListener {

    private IVaryViewHelper helper;

    public SimpleUiHandler() {

        setOnViewListener(this);
    }


    @Override
    public void onEmptyView(View.OnClickListener onClickListener, String... msgs) {
        if (null == tipView) {
            tipView = initTipView();
        }
        showView(tipView, onClickListener, msgs);
    }

    @Override
    public void onErrorView(View.OnClickListener onClickListener, String... msgs) {
        if (null == errorView) {
            errorView = initTipView();
        }
        showView(errorView, onClickListener, msgs);
    }

    @Override
    public void onTipView(View.OnClickListener onClickListener, String... msgs) {
        if (null == tipView) {
            tipView = initTipView();
        }
        showView(tipView, onClickListener, msgs);
    }

    @Override
    public void onLoadingView() {

        if (null == loadingView) {
            loadingView = helper.inflate(R.layout.layout_loading);
        }

        CircularProgressBar progressBar = (CircularProgressBar) loadingView.findViewById(R.id.pb_loading);
        progressBar.setVisibility(View.VISIBLE);
        helper.showLayout(loadingView);
    }

    @Override
    public void showLoadingDialog() {


        if (dialog == null) {
            dialog = new AsyncAlertDialog(getParentView().getContext());
        }

        dialog.show();
    }

    @Override
    public void dismissLoadingDialog() {

        if (null!=dialog&&dialog.isShowing()){
            dialog.dismiss();
        }
    }


    @Override
    public void onNetErrView(View.OnClickListener onClickListener) {

        if (null == netErrView) {
            netErrView = initTipView();
        }

        showView(netErrView, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.getContext().
                        startActivity(new Intent(Settings.ACTION_SETTINGS));

            }
        }, "网络好像有点问题");
    }

    @Override
    public void onNormalView() {

        if (null!=dialog&&dialog.isShowing()){
            dialog.dismiss();
        }
        helper.restoreView();
    }

    private View initTipView() {
        return helper.inflate(R.layout.layout_tip);
    }

    private void setTipViewContent(View tipView, View.OnClickListener onClickListener, String... msgs) {

        //设置提示图标，是错误提示还是普通提示


        TextView tvFirstTip = (TextView) tipView.findViewById(R.id.tv_tip_first_word);


        tvFirstTip.setVisibility(View.GONE);

        tvFirstTip.setOnClickListener(onClickListener);

        if (null != msgs) {
            //如果传入提示信息不为空

            if (msgs.length >= 1) {
                tvFirstTip.setVisibility(View.VISIBLE);
                tvFirstTip.setText(msgs[0]);
            }

        }


    }

    private void showView(View tipView, View.OnClickListener onClickListener, String... msgs) {
        setTipViewContent(tipView, onClickListener, msgs);

        if (null!=dialog&&dialog.isShowing()){
            dialog.dismiss();
        }

        helper.showLayout(tipView);
    }


    protected void setGoneView() {
        if (null != parentView)
            parentView.setVisibility(View.GONE);
        if (null != contentView)
            contentView.setVisibility(View.GONE);
        if (null != tipView)
            tipView.setVisibility(View.GONE);
        if (null != errorView)
            errorView.setVisibility(View.GONE);
        if (null != loadingView)
            loadingView.setVisibility(View.GONE);
    }

    @Override
    public ViewGroup getParentView() {
        if (null == parentView) {
            setParentView(helper.getParentView());
        }
        return parentView;
    }

    public IVaryViewHelper getHelper() {
        return helper;
    }

    public void setHelper(IVaryViewHelper helper) {
        this.helper = helper;
    }


    public static class Builder {
        SimpleUiHandler uiHandler = new SimpleUiHandler();


        public Builder setContentView(View contentView) {
            uiHandler.contentView = contentView;
            return this;
        }

        public Builder setRootView(ViewGroup rootView) {
            uiHandler.parentView = rootView;
            return this;
        }

        public Builder setErrorView(View errorView) {
            uiHandler.errorView = errorView;
            return this;
        }

        public Builder setTipView(View tipView) {
            uiHandler.tipView = tipView;
            return this;
        }

        public Builder setLoadingView(View loadingView) {
            uiHandler.loadingView = loadingView;
            return this;
        }

        public Builder setHelper(View view) {
            uiHandler.helper = new VaryViewHelper(view);
            return this;
        }

        public SimpleUiHandler getUiHandler() {
            return uiHandler;
        }
    }


}
