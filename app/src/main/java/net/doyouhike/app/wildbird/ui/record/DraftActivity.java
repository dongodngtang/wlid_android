package net.doyouhike.app.wildbird.ui.record;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.event.EventUploadRecord;
import net.doyouhike.app.wildbird.biz.model.bean.RecordEntity;
import net.doyouhike.app.wildbird.biz.service.database.DraftDbService;
import net.doyouhike.app.wildbird.biz.service.net.UploadRecordService;
import net.doyouhike.app.wildbird.ui.adapter.MineAdapter;
import net.doyouhike.app.wildbird.ui.base.BaseAppActivity;
import net.doyouhike.app.wildbird.ui.login.LoginActivity;
import net.doyouhike.app.wildbird.ui.main.user.mine.MeFragment;
import net.doyouhike.app.wildbird.util.CheckNetWork;
import net.doyouhike.app.wildbird.util.GotoActivityUtil;
import net.doyouhike.app.wildbird.util.LocalSharePreferences;
import net.doyouhike.app.wildbird.util.LogUtil;
import net.doyouhike.app.wildbird.ui.view.MyAlertDialogUtil;
import net.doyouhike.app.wildbird.ui.view.MyAlertDialogUtil.DialogListener;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

@SuppressWarnings("deprecation")
public class DraftActivity extends BaseAppActivity implements OnClickListener,
        OnItemLongClickListener, OnItemClickListener, DialogListener {

    public static final String EDIT_BACK = "edit_back";
    private ListView swipelist;
    private MineAdapter adapter;
    private List<RecordEntity> list;
    private TextView nothing;
    private Dialog dialog = null;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.draft_view;
    }

    @Override
    protected void initViewsAndEvents() {
        super.initViewsAndEvents();

        EventBus.getDefault().register(this);

        swipelist = (ListView) super.findViewById(R.id.swipelist);
        nothing = (TextView) super.findViewById(R.id.nothing);

        list = new ArrayList<>();

        adapter = new MineAdapter(this, list, 1);
        swipelist.setAdapter(adapter);
        swipelist.setOnItemClickListener(this);
        swipelist.setOnItemLongClickListener(this);
        swipelist.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                if (list.size() % 20 == 0 && totalItemCount > 0 && firstVisibleItem + visibleItemCount == totalItemCount) {
                    //加载更多
                    List<RecordEntity> list2 = DraftDbService.getInstance().getRecord(list.size());
                    list.addAll(list2);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetListData();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, LookRecordActivity.class);
        intent.putExtra("Record", list.get(position));
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
        showItemDialog(position);
        return true;
    }

    /**
     * 操作对话框
     * @param position
     */
    private void showItemDialog(int position) {
        // TODO Auto-generated method stub
        if (dialog == null) {
            dialog = MyAlertDialogUtil.showDialog(this);
        }
        dialog.show();
        dialog.findViewById(R.id.mine_relate).setOnTouchListener(new OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (v.getId() != R.id.control) {
                    dialog.dismiss();
                }
                return false;
            }
        });
        dialog.findViewById(R.id.upload_line).setVisibility(View.VISIBLE);
        TextView edit = (TextView) dialog.findViewById(R.id.edit);
        TextView delete = (TextView) dialog.findViewById(R.id.delete);
        TextView upload = (TextView) dialog.findViewById(R.id.upload);
        upload.setVisibility(View.VISIBLE);
        //设置位置标签
        edit.setTag(position);
        delete.setTag(position);
        upload.setTag(position);
        edit.setOnClickListener(this);
        delete.setOnClickListener(this);
        upload.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        // TODO Auto-generated method stub
        if (dialog != null) dialog.dismiss();
        switch (v.getId()) {
            case R.id.edit:
                //编辑
                entity = null;
                GotoActivityUtil.gotoEditRecord(this, false, list.get((Integer) v.getTag()));
                break;
            case R.id.delete:
                //删除
                long id_record = list.get((Integer) v.getTag()).getRecord();
                DraftDbService.getInstance().deleteRecord(id_record);
                adapter.remove((Integer) v.getTag());
                break;
            case R.id.upload:
                //上传
                uploadRecord((int) v.getTag());
                break;
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Override
    public void dialogClick(int pos) {
        // TODO Auto-generated method stub
        switch (pos) {
            case 0:
                //无网络设置
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                break;
            case 1:
                // 取消
                break;
            case 2:
                //登陆
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case 3:
                // 取消
                break;
        }
    }

    /**
     * 上传纪录
     *
     * @param position
     */
    protected void uploadRecord(int position) {
        // TODO Auto-generated method stub
        if (!CheckNetWork.isNetworkAvailable(this)) {
            MyAlertDialogUtil.showDialog(this, this, 1);
            return;
        }
        if (LocalSharePreferences.getValue(this, "userId").equals("")) {
            MyAlertDialogUtil.showLoginDialog(this, this, 1);
            return;
        }
        upload(position);
    }


    private void resetListData() {
        list.clear();
        list.addAll(DraftDbService.getInstance().getRecord(0));
        adapter.notifyDataSetChanged();

        if (list.size() <= 0) {
            nothing.setVisibility(View.VISIBLE);
            swipelist.setVisibility(View.GONE);
        }
    }

    /**
     * 上传观鸟记录成功回调
     *
     * @param result
     */
    public void onEventMainThread(EventUploadRecord result) {
        if (null == entity) {
            return;
        }
        mProgressDialog.dismissDialog();
        if (result.isSuc()) {


            toast("提交记录成功.");
            String nodeId = result.getNode_id();
            if (!TextUtils.isEmpty(nodeId))
                entity.setRecordID(Integer.parseInt(nodeId));


            if (entity.getList().size() > 0 && !entity.getList().get(0).getImageUri().equals("")) {
                entity.setHasImage(1);
            } else {
                entity.setHasImage(0);
            }

            Intent intent = new Intent(MeFragment.UPLOAD_SUCCESS);
            intent.putExtra("record", entity);
            intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            sendBroadcast(intent);

            DraftDbService.getInstance().deleteRecord(entity.getRecord());
            resetListData();

        } else {
            toast(result.getStrErrMsg());
        }
    }

    RecordEntity entity;

    private void upload(final int position) {

        mProgressDialog.showProgressDialog("正在保存记录，请稍后...");
        entity = list.get(position);

        EventBus.getDefault().post(entity);
    }
}
