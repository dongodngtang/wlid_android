package net.doyouhike.app.wildbird.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.ui.adapter.AddNewPicAdapter;
import net.doyouhike.app.wildbird.ui.fragment.FolderFragment;
import net.doyouhike.app.wildbird.ui.fragment.FolderFragment.itemClickListener;
import net.doyouhike.app.wildbird.ui.fragment.PhotoFragment;
import net.doyouhike.app.wildbird.ui.fragment.PhotoFragment.clickPhotoListener;
import net.doyouhike.app.wildbird.biz.model.bean.AlbumInfo;
import net.doyouhike.app.wildbird.biz.model.bean.ChooseImage;
import net.doyouhike.app.wildbird.biz.model.bean.RecordImage;
import net.doyouhike.app.wildbird.ui.base.BaseAppFragmentActivity;
import net.doyouhike.app.wildbird.util.PhotoUtil;
import net.doyouhike.app.wildbird.ui.view.TitleView;
import net.doyouhike.app.wildbird.ui.view.TitleView.ClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 浏览图片
 */
public class MultiChoosePhotoActivity extends BaseAppFragmentActivity implements ClickListener,
        itemClickListener, clickPhotoListener, AddNewPicAdapter.deletePhoto {

    public final static String DELETE_PHOTO = "delete_photo";
    public final static String I_IMAGES = "images";
    public final static String I_DEL_IMG = "del_img";
    private ArrayList<Integer> delImages = new ArrayList<>();

    private TitleView titleview;
    private LinearLayout linear;
    private RecyclerView choose_photo;

    /**
     * 图片列表
     */
    private List<AlbumInfo> list;
    private AddNewPicAdapter adapter;
    /**
     * 已经选中的图片
     */
    private List<RecordImage> mChooseImage;
    private FragmentManager manager;
    private FolderFragment folder;
    private PhotoFragment fragment;
    private boolean isFolder = true;
    private boolean avatar = false;// 判断是否是选择头像
    private String image = "";// 头像uri

    public int getCount() {
        if (avatar) return 0;
        return mChooseImage.size();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.multi_photo;
    }

    @Override
    protected void initViewsAndEvents() {


        titleview = (TitleView) super.findViewById(R.id.titleview);
        linear = (LinearLayout) super.findViewById(R.id.linear);
        choose_photo = (RecyclerView) super.findViewById(R.id.rv_choose_photo);


        LinearLayoutManager layoutManager = new LinearLayoutManager
                (this, LinearLayoutManager.HORIZONTAL, false);

        choose_photo.setLayoutManager(layoutManager);

        titleview.setListener(this);
        avatar = getIntent().getBooleanExtra("avatar", false);
        if (avatar) {
            linear.setVisibility(View.GONE);
        } else {
            ChooseImage image = (ChooseImage) getIntent().getSerializableExtra(I_IMAGES);
            mChooseImage = image.getList();
            for (int i = 0; i < mChooseImage.size(); i++) {
                //移除空的图片
                if (mChooseImage.get(i).getImageUri().equals("")) mChooseImage.remove(i);
            }
            adapter = new AddNewPicAdapter(this, mChooseImage, 1);
            adapter.setListener(this);
            choose_photo.setAdapter(adapter);
        }
        manager = getSupportFragmentManager();
        list = PhotoUtil.getInstance(getApplicationContext()).getList();
        FragmentTransaction transaction = manager.beginTransaction();
        folder = new FolderFragment();
        transaction.add(R.id.container, folder);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void click(int position) {
        // TODO Auto-generated method stub
        isFolder = false;
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(folder).commit();

        fragment = new PhotoFragment();
        Bundle bundle = new Bundle();
        ChooseImage image = new ChooseImage();
        image.setPhotolist(list.get(position).getList());
        image.setList(mChooseImage);
        bundle.putSerializable("photo", image);
        bundle.putBoolean("avatar", avatar);
        fragment.setArguments(bundle);
        transaction = manager.beginTransaction();
        transaction.add(R.id.container, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (!isFolder) {
            isFolder = true;
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.show(folder).commit();
            manager.popBackStack(0, 0);
        } else {
            finish();
        }
    }

    @Override
    public void clickPhoto(String uri) {
        // TODO Auto-generated method stub
        if (avatar) {
            image = uri;
        } else {


            RecordImage image = new RecordImage();
            image.setImageUri(uri);

            if (mChooseImage.contains(image)) {
                mChooseImage.remove(image);
            } else {
                mChooseImage.add(image);
            }


            adapter.notifyDataSetChanged();

        }
    }

    @Override
    public void clickLeft() {
        // TODO Auto-generated method stub
        finish();
    }

    @Override
    public void clickRight() {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        if (avatar) {
            intent.putExtra("image", image);
        } else {
            ChooseImage image = new ChooseImage();
            image.setList(mChooseImage);
            intent.putExtra(I_IMAGES, image);
            intent.putIntegerArrayListExtra(I_DEL_IMG, delImages);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    ;

    @Override
    public void delete(int imageID) {
        delImages.add(imageID);
    }
}
