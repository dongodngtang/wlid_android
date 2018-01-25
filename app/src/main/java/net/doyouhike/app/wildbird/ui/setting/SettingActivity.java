package net.doyouhike.app.wildbird.ui.setting;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.doyouhike.app.library.ui.uistate.UiState;
import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.dao.sharepref.UserInfoSpUtil;
import net.doyouhike.app.wildbird.biz.model.Content;
import net.doyouhike.app.wildbird.biz.model.base.CommonResponse;
import net.doyouhike.app.wildbird.biz.model.request.get.CheckVersionReq;
import net.doyouhike.app.wildbird.biz.model.response.CheckVersionResponse;
import net.doyouhike.app.wildbird.biz.service.net.ApiReq;
import net.doyouhike.app.wildbird.ui.base.BaseAppActivity;
import net.doyouhike.app.wildbird.ui.main.MainActivity;
import net.doyouhike.app.wildbird.util.CheckVersionUtil;
import net.doyouhike.app.wildbird.util.LocalSharePreferences;
import net.doyouhike.app.wildbird.util.LocalSpManager;
import net.doyouhike.app.wildbird.util.UiUtils;

import butterknife.InjectView;

@SuppressWarnings("deprecation")
public class SettingActivity extends BaseAppActivity implements OnClickListener {


    private TextView feedback, about, version, logout, tvPsersonal;
    private RelativeLayout updateSys;

    @InjectView(R.id.activity_setting_open_source)
    View mViDownloadDocument;

    @InjectView(R.id.ll_setting_content)
    View mContentView;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_setting;
    }

    @Override
    protected View getLoadingTargetView() {
        return mContentView;
    }

    @Override
    protected void initViewsAndEvents() {
        super.initViewsAndEvents();

        feedback = (TextView) super.findViewById(R.id.feedback);
        about = (TextView) super.findViewById(R.id.about);
        updateSys = (RelativeLayout) super.findViewById(R.id.updateSys);
        version = (TextView) super.findViewById(R.id.version);
        logout = (TextView) super.findViewById(R.id.logout);
        tvPsersonal = (TextView) super.findViewById(R.id.tv_setting_personal);
        tvPsersonal.setOnClickListener(this);

        feedback.setOnClickListener(this);
        about.setOnClickListener(this);
        updateSys.setOnClickListener(this);
        logout.setOnClickListener(this);
        mViDownloadDocument.setOnClickListener(this);


        setVersionInfo();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        UiUtils.showView(tvPsersonal, UserInfoSpUtil.getInstance().isLogin());
        UiUtils.showView(logout, UserInfoSpUtil.getInstance().isLogin());
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.feedback:
                //反馈
                startActivity(new Intent(this, FeedbackActivity.class));
                break;
            case R.id.about:
                //关于
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.updateSys:
                //版本更新
                checkUpdate();
                break;
            case R.id.logout:
                //登出
                UserInfoSpUtil.getInstance().saveUserInfo(null);
                Intent intent = new Intent(MainActivity.EXIT_MINE);
                intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                sendBroadcast(intent);
                finish();
                break;
            case R.id.activity_setting_open_source:
                readyGo(AboutOpenSourceActivity.class);
                break;
            case R.id.tv_setting_personal:
                readyGo(SettingPersonalMassageActivity.class);
                break;
        }
    }

    private void checkUpdate() {
        updateView(UiState.LOADING_DIALOG);
        ApiReq.doCancel(Content.REQ_VERSION_CHECK);
        ApiReq.doGet(new CheckVersionReq(),
                new Response.Listener<CommonResponse<CheckVersionResponse>>() {
                    @Override
                    public void onResponse(CommonResponse<CheckVersionResponse> response) {
                        LocalSpManager.saveBanner(response.getT().getVersion());
                        updateView(UiState.DISMISS_DIALOG);
                        setVersionInfo();
                        new CheckVersionUtil(SettingActivity.this).showUpdateDialog();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        updateView(UiState.DISMISS_DIALOG);
                        toast("检查更新失败,请检查网络后重试");
                    }
                });
    }


    private void setVersionInfo() {

        CheckVersionUtil checkVersionUtil = new CheckVersionUtil(this);
        boolean isNewest = checkVersionUtil.isNewest();

        String strVersion = TextUtils.concat(isNewest ? "已是最新" : "发现新版本", " V", checkVersionUtil.getVersionCode()).toString();
        version.setText(strVersion);
    }

}
