package net.doyouhike.app.wildbird.ui.view.dialog;

import android.content.Context;

import net.doyouhike.app.library.ui.adapterutil.CommonAdapter;
import net.doyouhike.app.library.ui.adapterutil.ViewHolder;
import net.doyouhike.app.wildbird.R;

import java.util.List;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-16.
 */
public class SimpleBottomDialogAdapter extends CommonAdapter<String> {

    public SimpleBottomDialogAdapter(Context context, List<String> datas) {
        super(context, datas, R.layout.dialog_bottom_item);
    }

    @Override
    public void convert(ViewHolder holder, String s) {
        holder.setText(R.id.tv_dialog_bottom_item,s);
    }
}
