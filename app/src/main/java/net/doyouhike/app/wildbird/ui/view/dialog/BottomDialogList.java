package net.doyouhike.app.wildbird.ui.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.doyouhike.app.wildbird.R;

import java.util.Arrays;
import java.util.List;


/**
 * 功能：
 *
 * @author：曾江 日期：16-3-28.
 */
public class BottomDialogList extends Dialog {

    BottomDialogListener listener;
    BaseAdapter adapter;
    /**
     * 对话框内容列表
     */
    ListView lvContent;

    public BottomDialogList(Context context, BottomDialogListener listener, BaseAdapter adapter) {
        super(context, R.style.MyDialogStyleBottom);
        this.listener=listener;
        this.adapter=adapter;
    }

    public BottomDialogList(Context context, BottomDialogListener listener, List<String> items) {
        super(context, R.style.MyDialogStyleBottom);
        this.listener=listener;
        this.adapter=new SimpleBottomDialogAdapter(context,items);
    }
    public BottomDialogList(Context context, BottomDialogListener listener, String ...items) {
        super(context, R.style.MyDialogStyleBottom);
        this.listener=listener;
        this.adapter=new SimpleBottomDialogAdapter(context, Arrays.asList(items));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_bottom_list);
        initView();
        initData();
    }
    private void initView() {

        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        this.getWindow().setGravity(Gravity.BOTTOM);

        lvContent =(ListView)this.findViewById(R.id.lv_dialog_bottom_content);

        // 类型选择项的点击事件
        lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (listener != null) {
                    //listview的item选择事件
                    listener.onItemSelected(position);
                }
                dismiss();
            }
        });
        TextView textView=(TextView)this.findViewById(R.id.tv_dialog_bottom_cancel);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消
                dismiss();
            }
        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //消失监听
                listener.onDismiss();
            }
        });

    }


    private void initData() {
        lvContent.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public interface BottomDialogListener {
        /**
         * @param position 列表的索引号
         */
        void onItemSelected(int position);

        /**
         * 消失调用
         */
        void onDismiss();
    }

}
