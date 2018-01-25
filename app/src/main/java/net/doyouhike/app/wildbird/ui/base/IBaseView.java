package net.doyouhike.app.wildbird.ui.base;

import android.view.View;

import net.doyouhike.app.library.ui.uistate.UiState;

/**
 * 功能：基础的mvp中的v
 *
 * @author：曾江 日期：16-4-12.
 */
public interface IBaseView  {
    /**
     * @param state    ui状态
     */
    void updateView(UiState state);

    /**
     * @param state ui状态
     * @param onClickListener 点击事件监听
     */
    void updateView(UiState state, View.OnClickListener onClickListener);
}
