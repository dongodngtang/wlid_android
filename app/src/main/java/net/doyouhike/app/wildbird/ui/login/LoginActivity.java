package net.doyouhike.app.wildbird.ui.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.dao.sharepref.UserInfoSpUtil;
import net.doyouhike.app.wildbird.biz.model.Content;
import net.doyouhike.app.wildbird.biz.model.base.CommonResponse;
import net.doyouhike.app.wildbird.biz.model.request.post.LoginParam;
import net.doyouhike.app.wildbird.biz.model.response.LoginResp;
import net.doyouhike.app.wildbird.biz.service.net.ApiReq;
import net.doyouhike.app.wildbird.ui.base.BaseAppActivity;
import net.doyouhike.app.wildbird.ui.main.MainActivity;
import net.doyouhike.app.wildbird.ui.webview.WebActivity;
import net.doyouhike.app.wildbird.util.CheckNetWork;
import net.doyouhike.app.wildbird.util.MD5Util;
import net.doyouhike.app.wildbird.util.UmengEventUtil;
import net.doyouhike.app.wildbird.ui.view.Clickable;
import net.doyouhike.app.wildbird.ui.view.LineEditText;
import net.doyouhike.app.wildbird.ui.view.LineEditText.SearchListener;

@SuppressWarnings("deprecation")
public class LoginActivity extends BaseAppActivity implements OnClickListener, SearchListener {

    private static final String TAG = "LoginActivity";

    private static final int REQ_CODE_GOTO_WEBSITE = 1;
    private static final int REQ_CODE_REGIST = 1002;

    private LineEditText account, password;
    private TextView login, regist, resetpw, tvLink;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.login;
    }

    @Override
    protected void initViewsAndEvents() {
        super.initViewsAndEvents();

        account = (LineEditText) super.findViewById(R.id.login_account);
        password = (LineEditText) super.findViewById(R.id.login_pwd);
        login = (TextView) super.findViewById(R.id.login);
        regist = (TextView) super.findViewById(R.id.login_regist);
        resetpw = (TextView) super.findViewById(R.id.login_resetpw);
        tvLink = (TextView) findViewById(R.id.tv_login_tips_weblink);

        initData();

        password.setListener(this);
        password.setIsPW(true);
        login.setOnClickListener(this);
        regist.setOnClickListener(this);
        resetpw.setOnClickListener(this);

        registerReceiver(receiver, new IntentFilter(MainActivity.EXIT_MAIN));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQ_CODE_GOTO_WEBSITE) {
            String userNm = data.getStringExtra(WebActivity.I_USER_NM);
            account.setText(userNm);
        }else if (requestCode==REQ_CODE_REGIST){
            //注册成功
            finish();
        }
    }

    private void initData() {

        tvLink.setText(getClickableSpan(getString(R.string.login_tip_two), 9));
        tvLink.setMovementMethod(LinkMovementMethod.getInstance());

    }


    private SpannableString getClickableSpan(String content, int length) {


        SpannableString spanableInfo = new SpannableString(content);
        int end = content.length() - 1;   //超链接结束位置
        int start = end - length;  //超链接起始位置

        //可以为多部分设置超链接
        spanableInfo.setSpan(new Clickable(this), start, end, Spanned.SPAN_MARK_MARK);
        spanableInfo.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance),
                start, end, Spanned.SPAN_MARK_MARK);

        return spanableInfo;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.login:
                if (isInputValue()) {
                    login();
                }
                break;
            case R.id.login_regist:
                startActivityForResult(new Intent(LoginActivity.this, RegistActivity.class), REQ_CODE_REGIST);
                break;
            case R.id.login_resetpw:
                startActivity(new Intent(LoginActivity.this, ResetPWActivity.class));
                break;
            case R.id.tv_login_tips_weblink:
                gotoWebsite("/mobile/user/bird");
                break;
        }
    }

    private void gotoWebsite(String subUrl) {

        String url = Content.WEB_MOBILE_URL+subUrl;

        Intent intent = new Intent(LoginActivity.this, WebActivity.class);
        intent.putExtra(WebActivity.I_URL, url);

        startActivityForResult(intent, REQ_CODE_GOTO_WEBSITE);
    }

    @Override
    public void search() {
        // TODO Auto-generated method stub
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
    }

    @Override
    public void autoEdit() {
        // TODO Auto-generated method stub

    }

    @Override
    public void showPW() {
        // TODO Auto-generated method stub
        password.showPassword();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            finish();
        }
    };

    private boolean isInputValue() {
        String ac = account.getText().toString().trim();
        String pw = password.getText().toString().trim();
        if (ac.equals("")) {
            toast("请输入帐号");
            return false;
        }
        if (pw.equals("")) {
            toast("请输入密码");
            return false;
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
        if (!CheckNetWork.isNetworkAvailable(this)) {
            toast("请设置好网络。");
            return false;
        }
        return true;
    }

    private void login() {

        mProgressDialog.showProgressDialog("正在登录，请稍后...");

        String ac = account.getText().toString().trim();
        String pw = password.getText().toString().trim();
        String md5Pw = MD5Util.Encode(pw);


        LoginParam param = new LoginParam();
        param.setPhoneNumber(ac);
        param.setPassword(md5Pw);

        ApiReq.doPost(param, Content.REQ_login, loginSuc, updRecordCountErr);

    }


    //      登陆失败
    private Response.ErrorListener updRecordCountErr = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {


            String errMsg = error.getMessage();


            if (TextUtils.isEmpty(errMsg)) {
                errMsg = "登录不成功";
            }

            toast(errMsg);
            mProgressDialog.dismissDialog();

        }
    };


    //      登陆成功回调
    private Response.Listener<CommonResponse<LoginResp>> loginSuc =
            new Response.Listener<CommonResponse<LoginResp>>() {

                @Override
                public void onResponse(CommonResponse<LoginResp> response) {
                    mProgressDialog.dismissDialog();

                    UserInfoSpUtil.getInstance().saveUserInfo(response.getT());

                    toast("登录成功");
                    UmengEventUtil.onLoginEvent();
                    setResult(RESULT_OK);
                    finish();
                }
            };
}
