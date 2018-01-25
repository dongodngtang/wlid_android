package net.doyouhike.app.library.ui.uistate;

import android.view.View;
import android.view.ViewGroup;

import net.doyouhike.app.library.ui.widgets.AsyncAlertDialog;

/**
 * 主要实现功能： 更具ui状态调用接口
 * 作者：zaitu
 * 日期：15-12-23.
 */
public abstract class BaseUiStateHandle {


    protected ViewGroup parentView;
    protected View contentView;
    protected View netErrView;
    protected View errorView;
    protected View tipView;
    protected View loadingView;
    protected AsyncAlertDialog dialog;


    OnViewListener onViewListener;

    public void updateView(UiState state, View.OnClickListener onClickListener) {

        String[] msgs = state.getMsg();

        switch (state) {
            case NORMAL:
                if (null != onViewListener) {
                    onViewListener.onNormalView();
                }
                break;
            case ERROR:
                if (null != onViewListener) {
                    onViewListener.onErrorView(onClickListener, msgs);
                }
                break;
            case TIP:
                if (null != onViewListener) {
                    onViewListener.onTipView(onClickListener, msgs);
                }
                break;
            case LOADING:
                if (null != onViewListener) {
                    onViewListener.onLoadingView();
                }
                break;
            case EMPTY:
                if (null != onViewListener) {
                    onViewListener.onEmptyView(onClickListener, msgs);
                }
                break;
            case NETERR:
                if (null != onViewListener) {
                    onViewListener.onNetErrView(onClickListener);
                }
                break;
            case LOADING_DIALOG:
                if (null != onViewListener) {
                    onViewListener.showLoadingDialog();
                }
                break;
            case DISMISS_DIALOG:
                if (null != onViewListener) {
                    onViewListener.dismissLoadingDialog();
                }
                break;
        }
    }

    public void setOnViewListener(OnViewListener onViewListener) {
        this.onViewListener = onViewListener;
    }

    /**
     * ######监听回调start#########
     */

    public interface OnViewListener {
        void onNormalView();

        void onErrorView(View.OnClickListener onClickListener, String... msgs);

        void onTipView(View.OnClickListener onClickListener, String... msgs);

        void onLoadingView();

        void showLoadingDialog();

        void dismissLoadingDialog();

        void onEmptyView(View.OnClickListener onClickListener, String... msgs);

        void onNetErrView(View.OnClickListener onClickListener);
    }

    /**
     * ######监听回调end#########
     */

    public ViewGroup getParentView() {
        return parentView;
    }

    public void setParentView(ViewGroup parentView) {
        this.parentView = parentView;
    }

    public View getContentView() {
        return contentView;
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    public View getNetErrView() {
        return netErrView;
    }

    public void setNetErrView(View netErrView) {
        this.netErrView = netErrView;
    }

    public View getErrorView() {
        return errorView;
    }

    public void setErrorView(View errorView) {
        this.errorView = errorView;
    }

    public View getTipView() {
        return tipView;
    }

    public void setTipView(View tipView) {
        this.tipView = tipView;
    }

    public View getLoadingView() {
        return loadingView;
    }

    public void setLoadingView(View loadingView) {
        this.loadingView = loadingView;
    }
}
