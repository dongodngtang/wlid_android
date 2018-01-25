package net.doyouhike.app.wildbird.ui.main.user;

import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

import net.doyouhike.app.library.ui.uistate.BaseUiStateHandle;
import net.doyouhike.app.library.ui.uistate.UiState;
import net.doyouhike.app.library.ui.widgets.LoadMoreListView;
import net.doyouhike.app.wildbird.util.UiUtils;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-13.
 */
public class UserUiHandler extends BaseUiStateHandle {

    LoadMoreListView listView;
    EmptyAdapter emptyAdapter;
    BaseAdapter originalAdapter;

    public UserUiHandler(LoadMoreListView listView, BaseAdapter originalAdapter) {

        this.listView = listView;
        this.originalAdapter = originalAdapter;

        emptyAdapter = new EmptyAdapter();
    }

    @Override
    public void updateView(UiState state, View.OnClickListener onClickListener) {
        BaseAdapter adapter;

        if (state == UiState.NORMAL) {
            adapter = originalAdapter;
            listView.addFooterView();
        } else {
            emptyAdapter.updateState(state, onClickListener);
            adapter = emptyAdapter;
            listView.removeFooterView();
        }

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


}
