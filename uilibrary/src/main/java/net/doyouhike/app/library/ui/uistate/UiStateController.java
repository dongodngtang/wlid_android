package net.doyouhike.app.library.ui.uistate;

import android.view.View;

/**
 * 主要实现功能：
 * 作者：zaitu
 * 日期：15-12-23.
 */
public class UiStateController {

    BaseUiStateHandle uiStateHandle;
    UiState state;

    public UiStateController() {

    }

    public UiStateController(BaseUiStateHandle uiStateHandle) {
        this.uiStateHandle = uiStateHandle;
    }

    public UiStateController(View view) {
        SimpleUiHandler.Builder builder = new SimpleUiHandler.Builder();
        this.uiStateHandle = builder.setHelper(view).getUiHandler();
    }

    public void updateView(UiState state, View.OnClickListener onClickListener) {
        this.state = state;
        uiStateHandle.updateView(state, onClickListener);
    }

    public BaseUiStateHandle getUiStateHandle() {
        return uiStateHandle;
    }

    public void setUiStateHandle(BaseUiStateHandle uiStateHandle) {
        this.uiStateHandle = uiStateHandle;
    }

    public UiState getState() {
        return state;
    }

    public View getParentView(){
        return uiStateHandle.getParentView();
    }
}
