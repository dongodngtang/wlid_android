package net.doyouhike.app.wildbird.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.app.MyApplication;
import net.doyouhike.app.wildbird.biz.model.Content;

import java.io.File;

/**
 * 功能：
 *
 * @author：曾江 日期：16-3-12.
 */
public class ImageLoaderUtil {

    private static ImageLoaderUtil instance;
    private DisplayImageOptions options;
    private DisplayImageOptions localOptions;
    private DisplayImageOptions avatarOptions;

    public static synchronized ImageLoaderUtil getInstance() {
        if (null == instance) {
            initInstance();
        }
        return instance;
    }

    private static synchronized void initInstance() {
        if (null == instance) {
            instance = new ImageLoaderUtil();
        }
    }


    public void initConfig(Context context) {

        String path = Content.FILE_PATH_PARENT_CACHE;
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration
                .Builder(context)
                .diskCache(new UnlimitedDiskCache(dir))
                .build();
        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);
    }

    public void showAvatar(ImageView imageView, String uri) {
        if (null==imageView){
            return;
        }

        if (!PermissionUtil.getInstance().checkStoragePermission(imageView.getContext())) {
            return;
        }

        if (!ImageLoader.getInstance().isInited()){
            initConfig(MyApplication.getInstance().getApplicationContext());
        }

        ImageLoader.getInstance().displayImage(uri, imageView, getAvatarOptions());
    }


    public void showImg(ImageView imageView, String uri) {

        if (null==imageView){
            return;
        }

        if (!PermissionUtil.getInstance().checkStoragePermission(imageView.getContext())) {
            return;
        }
        initOptions();
        ImageLoader.getInstance().displayImage(uri, imageView, options);
    }
    public void showLocalImg(ImageView imageView, String uri) {

        if (null==imageView){
            return;
        }

        if (!PermissionUtil.getInstance().checkStoragePermission(imageView.getContext())) {
            return;
        }
        initLocalOptions();
        ImageLoader.getInstance().displayImage(uri, imageView, localOptions);
    }

    private void initOptions() {
        if (null == options) {
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.u114)
                    .showImageOnFail(R.drawable.u114)
                    .showImageForEmptyUri(R.drawable.u114)
                    .cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }
    }

    private void initLocalOptions() {
        if (null == localOptions) {

            localOptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.u114)
                    .showImageOnFail(R.drawable.u114)
                    .cacheInMemory(false)
                    .cacheOnDisk(false)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }
    }


    public DisplayImageOptions getAvatarOptions() {
        if (null == avatarOptions) {
            avatarOptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.avatar)
                    .showImageOnFail(R.drawable.avatar)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .displayer(new CircleBitmapDisplayer())
                    .build();
        }
        return avatarOptions;
    }


    public void savePicture(String url,final String title) {


//        int screenWidth=UiUtils.getValidWidth(MyApplication.getInstance().getApplicationContext());
//        ImageSize imageSize=new ImageSize(screenWidth,screenWidth*2/3);
//        ImageLoader.getInstance().loadImage(url, imageSize,new SimpleImageLoadingListener(){
        ImageLoader.getInstance().loadImage(url, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {



                if (!PermissionUtil.getInstance().checkStoragePermission(MyApplication.getInstance().getApplicationContext())) {
                    return;
                }


                BitmapUtil.saveWaterMark(loadedImage, title,MyApplication.getInstance().getResources().getDrawable(R.drawable.ic_launcher));
                Toast.makeText(MyApplication.getInstance().getApplicationContext(), "保存成功,可在相册中查看", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Toast.makeText(MyApplication.getInstance().getApplicationContext(), "保存图片失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showBanner(ImageView imageView, String image_url) {

        if (null==imageView){
            return;
        }


        if (!PermissionUtil.getInstance().checkStoragePermission(imageView.getContext())) {
            return;
        }

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.slash)
                .showImageOnFail(R.drawable.slash)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader.getInstance().displayImage(image_url, imageView, options);
    }
}
