package net.doyouhike.app.wildbird.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.Browser;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import net.doyouhike.app.wildbird.biz.model.bean.BirdPictureUserInfo;
import net.doyouhike.app.wildbird.biz.model.bean.Image;
import net.doyouhike.app.wildbird.biz.model.bean.RecordEntity;
import net.doyouhike.app.wildbird.ui.BirdPictureActivity;
import net.doyouhike.app.wildbird.ui.main.add.AddRecordFragment;
import net.doyouhike.app.wildbird.ui.main.add.EditRecordActivity;
import net.doyouhike.app.wildbird.ui.main.birdinfo.detail.BirdDetailActivity;
import net.doyouhike.app.wildbird.ui.main.user.other.OtherActivity;

import java.util.List;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-16.
 */
public class GotoActivityUtil {

    /**
     * 查看他人主页
     *
     * @param context
     * @param userId
     * @param userName
     */
    public static void gotoOtherActivity(Context context, String userId, String userName) {


        Intent intent = new Intent(context, OtherActivity.class);

        intent.putExtra(OtherActivity.I_USER_NAME, userName);
        intent.putExtra(OtherActivity.I_USER_ID, userId);

        context.startActivity(intent);
    }

    public static void totoBirdDetailActivity(Context context, String speciesID) {

        Intent intent = new Intent(context, BirdDetailActivity.class);
        intent.putExtra(BirdDetailActivity.I_SPECIES_ID, speciesID);
        context.startActivity(intent);
    }

    /**
     * 查看图片
     *
     * @param position 跳转到查看图片页面
     */
    public static void gotoViewPictureActivity(Context context, BirdPictureUserInfo userInfo, int position, List<Image> images) {

        Intent intent = new Intent(context, BirdPictureActivity.class);
        intent.putExtra(BirdPictureActivity.I_HAS_IMG, images.size() > 0);
        intent.putExtra(BirdPictureActivity.I_TITLE, "");

        intent.putExtra(BirdPictureActivity.I_INDEX, position);
        intent.putExtra(BirdPictureActivity.I_USERINFO, userInfo);


        if (images.size() > 0) {
            intent.putExtra(BirdPictureActivity.I_COUNT, images.size());

            for (int i = 0; i < images.size(); i++) {
                intent.putExtra(BirdPictureActivity.I_IMAGE + (i + 1), images.get(i).getUrl());
                if (!TextUtils.isEmpty(images.get(i).getAuthor())) {
                    intent.putExtra(BirdPictureActivity.I_AUTHOR + (i + 1), images.get(i).getAuthor());
                }
            }
            context.startActivity(intent);
        }
    }


    public static void startCropIntent(Fragment fragment, Uri uri, int requestCode) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        // 设置为true直接返回bitmap
        intent.putExtra("return-data", false);
        // 上面设为false的时候将MediaStore.EXTRA_OUTPUT关联一个Uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        fragment.startActivityForResult(intent, requestCode);

    }

    public static void gotoEditRecord(Context context, boolean isNeedGetFromNet, RecordEntity entity) {
        gotoEditRecord(context, isNeedGetFromNet, false, entity);
    }

    public static void gotoEditRecord(Context context, boolean isNeedGetFromNet, boolean isAdd, RecordEntity entity) {
        Intent intent = new Intent(context, EditRecordActivity.class);
        intent.putExtra(AddRecordFragment.ARG_NEED_GET_FROM_NET, isNeedGetFromNet);
        intent.putExtra(AddRecordFragment.ARG_IS_ADD, isAdd);
        intent.putExtra(AddRecordFragment.ARG_RECORD_ENTITY, entity);
        context.startActivity(intent);
    }

    /**
     * 跳转外部浏览器
     *
     * @param context context
     * @param url     链接
     */
    public static void gotoBrowser(Context context, String url) {


        if (TextUtils.isEmpty(url)) {
            return;
        }

        Uri uri = Uri.parse(url);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.w("URLSpan", "Actvity was not found for intent, " + intent.toString());
        }
    }
}
