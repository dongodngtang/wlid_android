package net.doyouhike.app.wildbird.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.bean.ShareContent;
import net.doyouhike.app.wildbird.util.ShareUtil;

/**
 */
public class ShareDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private ShareContent mShareContent;

    /**
     * 标记点击的是哪个分享按钮
     */
    private int toShare = -1;

    /**
     * 分享dialog
     *
     * @param context
     */
    public ShareDialog(Context context) {
        super(context, R.style.MyDialogStyleBottom);
        this.context = context;
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_share_weixin:
                if (ShareUtil.isWeixinAvilible(context)) {
                    ShareUtil.shareToWeixin(context, mShareContent);
                } else {
                    Toast.makeText(v.getContext(),"未安装微信,分享失败",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_share_pyq:

                if (ShareUtil.isWeixinAvilible(context)) {
                    ShareUtil.shareToPyq(context, mShareContent);
                } else {
                    Toast.makeText(v.getContext(),"未安装微信,分享失败",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_share_weibo:
                ShareUtil.shareToWeibo(context, mShareContent);
                break;
            case R.id.ll_share_qq:
                ShareUtil.shareQQ(context, mShareContent);
                break;
        }
        dismiss();
    }

    public void showShareDialog(ShareContent content) {
        this.mShareContent = content;
        show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_action_share);

        ShareUtil.init(context);

        initDialog(); // 初始化dialog

        findView();

        initView(); // 初始化组件

    }

    private void findView() {
    }

    /**
     * 初始化组件
     */
    private void initView() {
        initShareBtn(); // 初始化分享
        initCancelBtn(); // 初始化取消按钮
    }

    private void initShareBtn() {
        LinearLayout llytWeiXin = (LinearLayout) this
                .findViewById(R.id.ll_share_weixin);
        LinearLayout llytWeiBo = (LinearLayout) this
                .findViewById(R.id.ll_share_weibo);
        LinearLayout llytPyq = (LinearLayout) this
                .findViewById(R.id.ll_share_pyq);
        LinearLayout llytQq = (LinearLayout) this
                .findViewById(R.id.ll_share_qq);

        llytWeiXin.setOnClickListener(this);
        llytWeiBo.setOnClickListener(this);
        llytPyq.setOnClickListener(this);
        llytQq.setOnClickListener(this);

    }

    private void initCancelBtn() {
        TextView tvCancel = (TextView) this.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    private void clickFavoriteBtn(final TextView tvFavorite) {
        tvFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initDialog() {
        this.getWindow().setLayout(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        this.getWindow().setGravity(Gravity.BOTTOM);
    }

}
