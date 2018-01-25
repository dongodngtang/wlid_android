package net.doyouhike.app.wildbird.ui.main.user.mine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import net.doyouhike.app.library.ui.uistate.UiState;
import net.doyouhike.app.library.ui.uistate.UiStateController;
import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.dao.sharepref.UserInfoSpUtil;
import net.doyouhike.app.wildbird.biz.model.Content;
import net.doyouhike.app.wildbird.biz.model.bean.BirdRecordDetailItem;
import net.doyouhike.app.wildbird.biz.model.bean.BirdRecordTotalItem;
import net.doyouhike.app.wildbird.biz.model.bean.RecStatsEntity;
import net.doyouhike.app.wildbird.biz.model.bean.UserInfo;
import net.doyouhike.app.wildbird.biz.model.event.DelRecordEvent;
import net.doyouhike.app.wildbird.biz.model.event.EventUploadRecord;
import net.doyouhike.app.wildbird.biz.service.database.DraftDbService;
import net.doyouhike.app.wildbird.ui.MultiChoosePhotoActivity;
import net.doyouhike.app.wildbird.ui.login.LoginActivity;
import net.doyouhike.app.wildbird.ui.main.discovery.ranking.leaderboar.LeaderboardActivity;
import net.doyouhike.app.wildbird.ui.main.discovery.ranking.page.PageBase;
import net.doyouhike.app.wildbird.ui.main.user.UserFragment;
import net.doyouhike.app.wildbird.ui.main.user.UserUiHandler;
import net.doyouhike.app.wildbird.ui.record.DraftActivity;
import net.doyouhike.app.wildbird.ui.setting.SettingActivity;
import net.doyouhike.app.wildbird.ui.view.MyPopupWindow;
import net.doyouhike.app.wildbird.ui.view.TitleView;
import net.doyouhike.app.wildbird.util.GotoActivityUtil;
import net.doyouhike.app.wildbird.util.ImageLoaderUtil;
import net.doyouhike.app.wildbird.util.LogUtil;
import net.doyouhike.app.wildbird.util.PermissionUtil;
import net.doyouhike.app.wildbird.util.PhotoUtil;
import net.doyouhike.app.wildbird.util.UiUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.OnClick;

/**
 * 功能：个人主页
 * {@link net.doyouhike.app.wildbird.ui.main.MainActivity}
 *
 * @author：曾江 日期：16-4-13.
 */
public class MeFragment extends UserFragment implements TitleView.ClickListener
        , MyPopupWindow.SetAvatarListener{


    public static String AVATAR_PATH = Content.FILE_PATH_PARENT_AVATAR;

    public static final String UPLOAD_SUCCESS = "upload_success";
    public static final String EDIT_SUCCESS = "edit_success";

    private String filePath = "";// 头像路径
    private boolean isFromCamera = false;// 是否是从相机拍照取得头像图片
    private int degree = 0;// 相机旋转角度


    IMeFragPresenter mPresenter;

    private TextView tvDraft;
    private boolean isUpdateRrcSuc = false;



    public void updateAvater(String localAvatar) {
        ImageLoaderUtil.getInstance().showAvatar(ivAvatar, localAvatar);
    }

    public void removedMyRec(BirdRecordDetailItem item) {

        updateView(UiState.DISMISS_DIALOG);

        updateRecord();
        toast("删除成功");

        mPresenter.updateDelItem(getItems(),item);
        notifyDataChange();

        if (getItems().isEmpty()) {
            updateView(UiState.EMPTY);
        } else {
            updateView(UiState.NORMAL);
        }
    }

    public void updateAvaterFail() {
        filePath="";
    }


    @Override
    protected void onUserVisible() {
        setDraftView();
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void initViewsAndEvents() {
        mPresenter = new MeFragPresenterImp(this);

        lvLoadMore.setBackgroundResource(R.color.white);
        mListHelper = getListHelper(lvLoadMore, viRefresh, this, getPage(getContext()));
        uiStateController = new UiStateController(new UserUiHandler(lvLoadMore, mListHelper.getAdapter()));

        //设置标题栏的设置按钮图标
        mTitleView.setLefImageSrc(R.drawable.ic_setting);
        tvRanking.setVisibility(View.VISIBLE);
        View titleRightView = mTitleView.setRightView(R.layout.layout_me_draft);
        tvDraft = (TextView) titleRightView.findViewById(R.id.draftCount);
        mTitleView.setListener(this);

        setDraftView();

        if (UserInfoSpUtil.getInstance().isLogin()) {
            mListHelper.getData(true);
            mPresenter.updRecordCount();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        setLoginView();

    }



    @Override
    public void updateUserInfo(UserInfo info) {
        UiUtils.showView(notlogin, info == null);
        UiUtils.showView(mineInfo, info != null);
        super.updateUserInfo(info);

    }


    @Override
    protected PageBase<BirdRecordTotalItem> getPage(Context context) {
        return new MePage(context);
    }

    Uri uri = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 101:// gallery
                    if (data == null) {
                        return;
                    }
                    isFromCamera = false;
                    filePath = data.getStringExtra("image").replace("file://", "");
                    uri = Uri.fromFile(new File(filePath));
                    GotoActivityUtil.startCropIntent(this, uri, 103);
                    break;
                case 102:// camera
                    isFromCamera = true;
                    File file = new File(filePath);
                    degree = PhotoUtil.readPictureDegree(file.getAbsolutePath());
                    uri = Uri.fromFile(file);
                    GotoActivityUtil.startCropIntent(this,uri,103);
                    break;
                case 103:// photo_cut


                    if (uri == null) {
                        uri=data.getData();
                    }

                    if (uri == null) {
                        break;
                    }


                    saveCropAvator(uri);
                    // 上传图片
                    mPresenter.modifyAvatar(filePath);
//                    updateAvater(filePath);
                    LogUtil.d("filePath", filePath);
                    break;
                case Content.REQ_CODE_LOGIN:
                    break;
            }
        }
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void clickLeft() {

        startActivity(new Intent(getActivity(), SettingActivity.class));
    }


    @OnClick(R.id.notlogin)
    public void login() {
        readyGo(LoginActivity.class);
    }

    @OnClick(R.id.avatar)
    public void avatar() {
        if (UserInfoSpUtil.getInstance().isLogin()) {
            MyPopupWindow pop = MyPopupWindow.getInstance(getActivity());
            pop.setListener(this);
            pop.setAnimationStyle(R.style.GrowFromBottom);
            pop.showAtLocation(mTitleView, Gravity.CENTER, 0, 0);
            return;
        }


        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @OnClick(R.id.tv_me_ranking)
    public void ranking() {
        //排行榜
        readyGo(LeaderboardActivity.class);
    }


    @Override
    public void clickRight() {
        startActivity(new Intent(getActivity(), DraftActivity.class));
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isUpdateRrcSuc) {
            mPresenter.updRecordCount();
        }
    }

    @Override
    public void updateRecordCount(RecStatsEntity recStatsEntity) {
        isUpdateRrcSuc = true;
        super.updateRecordCount(recStatsEntity);
    }

    @Override
    public void updateRecordCountErr() {
        isUpdateRrcSuc = false;
        super.updateRecordCountErr();
    }

    protected void updateRecord() {
        isUpdateRrcSuc = false;
        resetRecordCount();
        mPresenter.updRecordCount();
    }




    /**
     * 保存裁剪的头像
     *
     * @param uri
     */
    @SuppressLint("SimpleDateFormat")
    private void saveCropAvator(Uri uri) {


        if (uri != null) {
            Bitmap bitmap = null;


            try {
                // 读取uri所在的图片
                bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);

            } catch (Exception e) {
                e.printStackTrace();
            }


            if (bitmap != null) {
//                bitmap = PhotoUtil.toRoundBitmap(bitmap);
                if (isFromCamera && degree != 0) {
                    bitmap = PhotoUtil.rotaingImageView(degree, bitmap);
                }
//				avatar.setImageBitmap(bitmap);
                // 保存图片
                if (isFromCamera) {
                    File file = new File(filePath);
                    file.delete();
                }
                String filename = new SimpleDateFormat("yyMMddHHmmss").format(new Date()) + ".jpg";
                filePath = AVATAR_PATH + filename;
                PhotoUtil.saveBitmap(AVATAR_PATH, filename, bitmap, true);
                // 上传头像
                if (bitmap != null && bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
        }
    }

    @Override
    public void startCamera() {

        if (!PermissionUtil.getInstance().checkStoragePermission(getActivity())) {
            return;
        }

        /**
         * 图片保存地址
         */
        File dir = new File(AVATAR_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, new SimpleDateFormat("yyMMddHHmmss").format(new Date()) + ".jpg");
        filePath = file.getAbsolutePath();// 获取相片的保存路径
        Uri imageUri = Uri.fromFile(file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 102);
    }

    @Override
    public void startGallery() {

        if (!PermissionUtil.getInstance().checkStoragePermission(getActivity())) {
            return;
        }

        Intent intent = new Intent(getActivity(), MultiChoosePhotoActivity.class);
        intent.putExtra("avatar", true);
        startActivityForResult(intent, 101);
    }


    /**
     * 删除记录
     * @param item 删除条目
     */
    private void delMyRecord(BirdRecordDetailItem item) {

        toast("删除中…");
        updateView(UiState.LOADING_DIALOG);
        mPresenter.deleteRecord(item);
    }

    /**
     * 设置登陆界面
     */
    private void setLoginView() {

        UserInfo userInfo = UserInfoSpUtil.getInstance().getUserInfo();


        if (null != userInfo) {
            updateUserInfo(userInfo);

            if (!TextUtils.isEmpty(filePath)){
                updateAvater("file:/"+filePath);
            }

            if (getItems().isEmpty()) {
                mListHelper.getData(true);
            }


            return;
        }

        mTitleView.setTitle(null);

        updateUserInfo(null);
        updateView(UiState.EMPTY.setMsg("未登录，部分功能无法使用。"), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyGo(LoginActivity.class);
            }
        });
        mListHelper.getItems().clear();
    }


    /**
     * 设置草稿箱的数目
     */
    private void setDraftView() {
        // 设置草稿箱的数目
        //##start
        int count = DraftDbService.getInstance().getRecordCount();
        if (count > 0) {
            tvDraft.setText("" + count);
        }
        UiUtils.showView(tvDraft, count > 0);
        //##end
        // 设置草稿箱的数目
    }


    /**
     * 编辑记录  上传观鸟记录
     *
     * @param result
     */
    public void onEventMainThread(EventUploadRecord result) {

        updateRecord();//更新统计记录

        mListHelper.getData(true);
    }

    public void onEventMainThread(DelRecordEvent event){
        delMyRecord(event.getItem());
    }
}
