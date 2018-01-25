package net.doyouhike.app.wildbird.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.BaseShareContent;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;


import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.bean.ShareContent;

import java.util.List;

@SuppressWarnings("deprecation")
public class ShareUtil {

    private static final String DESCRIPTOR = "com.umeng.share";
    private static UMSocialService mController = UMServiceFactory
            .getUMSocialService(DESCRIPTOR);

    //QQ互联申请的APP ID
    private static String qqAppID = "1104889511";
    //在QQ互联申请的APP kEY
    private static String qqAppSecret = "iDzZeLmszwVXIeNu";

    private static String wxAppID = "wx4522bc70898045a9";
    private static String wxAppSecret = "2db4a45f72e32fe6c5eeddc6553fb194";
    static boolean hasInited = false;


    public static void init(Context context) {
        if (hasInited) {
            return;
        }

        mController.getConfig().closeToast();
        addWeiboPlatform();
        addWXPlatform(context);
        addQQPlatform(context);
        hasInited = true;
    }


    /**
     * @return
     * @功能描述 : 添加微信平台分享
     */
    private static void addWXPlatform(Context context) {
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(context, wxAppID, wxAppSecret);
        wxHandler.addToSocialSDK();
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(context, wxAppID,
                wxAppSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    /**
     * @return
     * @功能描述 : 添加微博平台分享
     */
    private static void addWeiboPlatform() {
        // 添加新浪SSO授权
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
    }

    /**
     * @return
     * @功能描述 : 添加QQ分享平台
     */
    private static void addQQPlatform(Context context) {

        //参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) context,
                qqAppID, qqAppSecret);
        qqSsoHandler.addToSocialSDK();
    }

    /**
     * @param context 上下文
     * @param content 分享内容
     */
    public static void shareToWeixin(final Context context, ShareContent content) {
        UMImage urlImage = getUMImage(context, content);
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(content.getContent());
        weixinContent.setTitle(content.getTitle());
        weixinContent.setTargetUrl(content.getUrl());
        weixinContent.setShareMedia(urlImage);
        mController.setShareMedia(weixinContent);

        performShare(context, SHARE_MEDIA.WEIXIN);
    }

    /**
     * @param context 上下文
     * @param content 分享内容
     */
    public static void shareToPyq(final Context context, ShareContent content) {
        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(content.getContent());
        circleMedia.setTitle(content.getTitle());
        UMImage urlImage = getUMImage(context, content);
        circleMedia.setShareMedia(urlImage);
        circleMedia.setTargetUrl(content.getUrl());
        mController.setShareMedia(circleMedia);

        performShare(context, SHARE_MEDIA.WEIXIN_CIRCLE);
    }


    /**
     * 分享到QQ好友
     *
     * @param context      上下文
     * @param content      分享内容
     */
    public static void shareQQ(final Context context, ShareContent content) {
        mController.getConfig().setSsoHandler(new SinaSsoHandler());

        QQShareContent qqShareContent = new QQShareContent();

        setShareContent(context, qqShareContent, content);

        mController.setShareMedia(qqShareContent);

        performShare(context, SHARE_MEDIA.QQ);
    }

    /**
     * @param context 上下文
     * @param content 分享内容
     */
    public static void shareToWeibo(final Context context, ShareContent content) {
        mController.getConfig().setSsoHandler(new SinaSsoHandler());

        SinaShareContent sinaContent = new SinaShareContent();


        setShareContent(context, sinaContent, content);

        String strContent=content.getContent();
        if (null==strContent){
            strContent="";
        }
        if (!TextUtils.isEmpty(content.getUrl())){
            strContent=strContent+content.getUrl();
            sinaContent.setShareContent(strContent);
        }
        mController.setShareMedia(sinaContent);

        performShare(context, SHARE_MEDIA.SINA);
    }


    // 执行分享
    private static void performShare(final Context context, SHARE_MEDIA platform) {
        mController.postShare(context, platform, new SnsPostListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode,
                                   SocializeEntity entity) {
                if (eCode == StatusCode.ST_CODE_SUCCESSED) {
//					showText += "平台分享成功";
                } else if (eCode != StatusCode.ST_CODE_ERROR_CANCEL) {
                    String showText = platform.toString();
                    showText += "平台分享失败";
                    showText += eCode;
                    Toast.makeText(context, showText,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void copyLink(Context context, String content) {
        ClipboardManager clip = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        clip.setText(content); // 复制
        Toast.makeText(context, R.string.copy_success_can_to_share, Toast.LENGTH_SHORT)
                .show();
    }

    /**
     * 判断微信是否可用
     *
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data) {

        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = null;

        if (null != mController) {
            ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        }

        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }


    private static void setShareContent(Context context, BaseShareContent shareContent, ShareContent content) {

        shareContent.setShareContent(content.getContent());
        shareContent.setTitle(content.getTitle());
        UMImage urlImage = getUMImage(context, content);
        shareContent.setShareMedia(urlImage);
        shareContent.setTargetUrl(content.getUrl());
    }

    private static UMImage getUMImage(Context context, ShareContent content) {

        LogUtil.d("ShareUtil", "ShareContent:" + content);

        if (TextUtils.isEmpty(content.getImgUrl())) {
            return new UMImage(context, content.getDefaultImg());
        }

        return new UMImage(context, content.getImgUrl());
    }
}
