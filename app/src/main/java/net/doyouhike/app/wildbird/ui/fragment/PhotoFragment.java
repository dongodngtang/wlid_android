package net.doyouhike.app.wildbird.ui.fragment;

import java.util.List;

import net.doyouhike.app.wildbird.biz.model.bean.RecordImage;
import net.doyouhike.app.wildbird.ui.base.BaseFragment;
import net.doyouhike.app.wildbird.ui.MultiChoosePhotoActivity;
import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.ui.adapter.PhotoAdapter;
import net.doyouhike.app.wildbird.biz.model.bean.ChooseImage;
import net.doyouhike.app.wildbird.biz.model.bean.PhotoInfo;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class PhotoFragment extends BaseFragment {

    private View view;
    private GridView photos;
    private List<PhotoInfo> photolist;
    private PhotoAdapter photoAdapter;
    private clickPhotoListener listener;
    private MultiChoosePhotoActivity activity;
    private boolean avatar = false;// 判断是否是选择头像
    private int count = 0;// 头像只能选择一张
    private int pos = -1;// 选择头像时选中的图片ID

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        this.activity = (MultiChoosePhotoActivity) activity;
        listener = (clickPhotoListener) activity;
    }

    public interface clickPhotoListener {
        /**
         * @param uri 相册图片点击
         */
        void clickPhoto(String uri);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.photo_view;
    }

    @Override
    protected void initViewsAndEvents() {

        initView();

        initData();

        initListener();
    }

    private void initView() {
        view = getView();
        photos = (GridView) view.findViewById(R.id.photos);
    }

    private void initListener() {

        photos.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                if (avatar) {
                    if (photoAdapter.getCheck(position) || count < 1) {
                        if (photoAdapter.getCheck(position)) {
                            pos = -1;
                            count--;
                        } else if (count < 1) {
                            pos = position;
                            count++;
                        }
                    } else {
                        if (pos != -1) photoAdapter.check(pos);
                        pos = position;
                    }
                    listener.clickPhoto(photolist.get(position).getPath_file());
                    photoAdapter.check(position);
                } else {

                    //最多能选10张图片
                    if (!photoAdapter.getCheck(position) && ((MultiChoosePhotoActivity) getActivity()).getCount() >= 10) {
                        toast("最多能选10张图片");
                        return;
                    }

                    listener.clickPhoto(photolist.get(position).getPath_file());
                    photoAdapter.check(position);

                }
            }
        });
        photos.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                if (scrollState == 0) {
                    ImageLoader.getInstance().resume();
                } else {
                    ImageLoader.getInstance().pause();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().registerReceiver(receiver, new IntentFilter(MultiChoosePhotoActivity.DELETE_PHOTO));
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        getActivity().unregisterReceiver(receiver);
        super.onPause();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Log.i("info", "delete");
            String path = intent.getStringExtra("photo");
            for (int i = 0; i < photolist.size(); i++) {
                if (photolist.get(i).getPath_file().equals(path)) {
                    photoAdapter.check(i);
                    break;
                }
            }
        }
    };


    private void initData() {

        avatar = getArguments().getBoolean("avatar", false);
        ChooseImage image = (ChooseImage) getArguments().getSerializable("photo");
        photolist = image.getPhotolist();

        List<RecordImage> recordImages = image.getList();
        setPhotoState(photolist, recordImages);

        photoAdapter = new PhotoAdapter(getActivity(), photolist, photos);
        photos.setAdapter(photoAdapter);
    }

    private void setPhotoState(List<PhotoInfo> list, List<RecordImage> recordImages) {


        for (PhotoInfo info : list) {

            info.setChoose(false);

            if (null == recordImages || recordImages.isEmpty()) {
                continue;
            }


            if (TextUtils.isEmpty(info.getPath_file())) {
                continue;
            }

            for (RecordImage image : recordImages) {

                if (TextUtils.isEmpty(image.getImageUri())) {
                    continue;
                }

                if (info.getPath_file().equals(image.getImageUri())) {
                    //设置为选中状态
                    info.setChoose(true);
                }

            }
        }

    }

}
