package net.doyouhike.app.wildbird.ui.setting;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.app.MyApplication;
import net.doyouhike.app.wildbird.biz.service.DownloadFileManager;
import net.doyouhike.app.wildbird.ui.base.BaseAppActivity;
import net.doyouhike.app.wildbird.util.GotoActivityUtil;

import butterknife.OnClick;

/**
 * 开源数据库
 */
public class AboutOpenSourceActivity extends BaseAppActivity {


    private final String DOWNLOAD_FILE_NAME="观鸟APPI接口文档 - 磨房wiki.pdf";
    private final String DOWNLOAD_TITLE="观鸟APPI接口文档";
    private final String DOWNLOAD_URL="http://c1.zdb.io/files/2016/04/26/e/ee703574bc2a2d2a43eaa9fde3bab532.pdf";

    private final String WEB_URL="http://www.doyouhike.net/group/24096/1/2367100,0,0,0.html";

    @OnClick(R.id.tv_acti_about_open_source)
    public void downloadDocument(){
        GotoActivityUtil.gotoBrowser(this,WEB_URL);
    }

    private DownloadFileManager mDownloadFileManager;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_about_open_source;
    }

    @Override
    protected void initViewsAndEvents() {
    }
}
