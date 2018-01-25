package net.doyouhike.app.wildbird.ui.main.birdinfo.common;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.bean.BirdPictureUserInfo;
import net.doyouhike.app.wildbird.biz.model.bean.Image;
import net.doyouhike.app.wildbird.biz.model.response.GetRecordDetailResp;
import net.doyouhike.app.wildbird.ui.main.birdinfo.detail.BirdPictureAdapter;
import net.doyouhike.app.wildbird.util.GotoActivityUtil;
import net.doyouhike.app.wildbird.util.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 * 功能：野鸟图片帮助类,野鸟种详情,观鸟记录详情里面用到
 *
 * @author：曾江 日期：16-4-13.
 */
public class BirdPictureHelper {

    private ViewPager vpBirdDetailPictures;
    CircleIndicator indicatorBirdDetailPictures;
    TextView tvBirdDetailPicturesSize;


    /**
     * @param vpBirdDetailPictures viewpager 鸟的图片装载器
     * @param indicatorBirdDetailPictures 圆点指示
     * @param tvBirdDetailPicturesSize 图片数量
     */
    public BirdPictureHelper(ViewPager vpBirdDetailPictures,
                             CircleIndicator indicatorBirdDetailPictures,
                             TextView tvBirdDetailPicturesSize) {
        this.vpBirdDetailPictures = vpBirdDetailPictures;
        this.indicatorBirdDetailPictures = indicatorBirdDetailPictures;
        this.tvBirdDetailPicturesSize = tvBirdDetailPicturesSize;
    }


    /**
     * @param imagesEntities 观鸟记录的图片对象
     * @return  适配的鸟图片对象
     */
    public List<Image> toImags(List<GetRecordDetailResp.ImagesEntity> imagesEntities) {
        List<Image> images = new ArrayList<>();

        if (null != imagesEntities) {
            for (GetRecordDetailResp.ImagesEntity entity : imagesEntities) {
                Image image = new Image();
                image.setUrl(entity.getImageUrl());
                images.add(image);
            }
        }

        return images;
    }

    /**
     * 设置图片,为viewpager添加图片
     * @param context
     * @param images 图片列表
     * @param userInfo 用户信息
     */
    public void initBirdPictures(Context context, final List<Image> images,final BirdPictureUserInfo userInfo) {

        if (null==images){
            return;
        }


        if (images.isEmpty()) {

            Image image = new Image();

            image.setUrl("drawable://" + R.drawable.u114);
            images.add(image);
        }

        List<View> views = new ArrayList<>();
        //添加图片
        for (Image image : images) {
            ImageView view = new ImageView(context);
            view.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageLoaderUtil.getInstance().showImg(view, image.getUrl());
            views.add(view);
        }

        //为图片设置点击事件
        for (int i = 0; i < views.size(); i++) {
            views.get(i).setTag(i);
            views.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GotoActivityUtil.gotoViewPictureActivity(v.getContext(), userInfo,(int) v.getTag(), images);
                }
            });
        }

        BirdPictureAdapter adapter = new BirdPictureAdapter(views);
        //设置图片张数
        tvBirdDetailPicturesSize.setText(String.valueOf(views.size()));
        vpBirdDetailPictures.setAdapter(adapter);
        indicatorBirdDetailPictures.setViewPager(vpBirdDetailPictures);

    }
}
