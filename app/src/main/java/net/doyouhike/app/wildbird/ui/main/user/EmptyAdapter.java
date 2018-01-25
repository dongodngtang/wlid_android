package net.doyouhike.app.wildbird.ui.main.user;

import android.content.Intent;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.doyouhike.app.library.ui.uistate.UiState;
import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.util.UiUtils;

/**
 * 功能：列表情况的空白提醒
 *
 * @author：曾江 日期：16-4-13.
 */
public class EmptyAdapter extends BaseAdapter {

    private UiState state;
    private View.OnClickListener onClickListener;

    public void updateState(UiState state, View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.state = state;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return state;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (null == convertView) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_tip, parent, false);
        }



        if (null==state){
            return convertView;
        }


        TextView textView = ((TextView) convertView.findViewById(R.id.tv_tip));
        ProgressBar progressBar = ((ProgressBar) convertView.findViewById(R.id.pb_loading));

        UiUtils.showView(textView,state!=UiState.LOADING);
        UiUtils.showView(progressBar,state==UiState.LOADING);


        if (state==UiState.NETERR){
            textView.setText("暂无网络连接,请点击设置网络.");
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    v.getContext().
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                }
            });

            return convertView;
        }


        if (state.getMsg() != null){
            textView.setText(state.getMsg()[0]);
        }
        
        textView.setOnClickListener(onClickListener);

        return convertView;
    }
}
