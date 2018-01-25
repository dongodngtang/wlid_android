package net.doyouhike.app.wildbird.ui.login;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.Content;
import net.doyouhike.app.wildbird.biz.model.base.BaseResponse;
import net.doyouhike.app.wildbird.biz.model.request.get.GetRegVcodeParam;
import net.doyouhike.app.wildbird.biz.service.net.ApiReq;
import net.doyouhike.app.wildbird.ui.base.BaseAppActivity;
import net.doyouhike.app.wildbird.ui.main.MainActivity;
import net.doyouhike.app.wildbird.ui.ModifyUsernameActivity;
import net.doyouhike.app.wildbird.util.CheckNetWork;
import net.doyouhike.app.wildbird.util.RegexValidateUtil;
import net.doyouhike.app.wildbird.ui.view.LineEditText;
import net.doyouhike.app.wildbird.ui.view.LineEditText.SearchListener;

import com.android.volley.Response;
import com.android.volley.VolleyError;

@SuppressWarnings("deprecation")
public class RegistActivity extends BaseAppActivity implements OnClickListener, SearchListener {
    private static final int REQ_CODE_MODIFY_USER_NM=1;

    private LineEditText regist_phone, regist_pw;
    private EditText securityCode;
    private TextView getCode, register, agree, proxy;
    private boolean vcode = false;
    private String getCodeTip = "获取验证码";
    private String strTelNum;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            handler.removeCallbacks(run);
            switch (msg.what) {
                case 1:
                    break;
                case 2:
                    getCode.setClickable(true);
                    getCode.setText(getCodeTip);
                    break;
            }
        }
    };
    private int progress = 60;
    private Runnable run = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            progress--;
            getCode.setText(progress + "秒");
            handler.postDelayed(run, 1000);
            if (progress == 0) {
                getCode.setClickable(true);
                getCode.setText(getCodeTip);
                handler.sendEmptyMessage(1);
            }
        }
    };


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.register;
    }

    @Override
    protected void initViewsAndEvents() {
        super.initViewsAndEvents();

        regist_phone = (LineEditText) super.findViewById(R.id.regist_phone);
        regist_pw = (LineEditText) super.findViewById(R.id.regist_pw);
        securityCode = (EditText) super.findViewById(R.id.securityCode);
        getCode = (TextView) super.findViewById(R.id.getCode);
        register = (TextView) super.findViewById(R.id.register);
        agree = (TextView) super.findViewById(R.id.agree);
        proxy = (TextView) super.findViewById(R.id.proxy);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        regist_pw.setListener(this);
        regist_pw.setIsPW(true);
        agree.setSelected(true);
        agree.setEnabled(false);
        getCode.setOnClickListener(this);
        register.setOnClickListener(this);
        agree.setOnClickListener(this);
        proxy.setOnClickListener(this);

        registerReceiver(receiver, new IntentFilter(MainActivity.EXIT_MAIN));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode==RESULT_OK&&requestCode==REQ_CODE_MODIFY_USER_NM){
            //注册成功回调
            setResult(RESULT_OK);
            finish();
        }
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
        regist_pw.showPassword();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.getCode:
                if (isInputTelMunValid()) {
                    getRegVcode();
                }
                break;
            case R.id.register:
                String phone2 = regist_phone.getText().toString().trim();
                String pw = regist_pw.getText().toString().trim();
                String code = securityCode.getText().toString().trim();
                if (phone2.equals("") || phone2.length() != 11) {
                    toast("请输入正确的手机号码");
                    return;
                }
                if (pw.equals("") || !(pw.length() >= 6 && pw.length() <= 20)) {
                    toast("请输入密码且密码长度在6~20之间.");
                    return;
                }
                if (code.equals("")) {
                    toast("请输入验证码");
                    return;
                }
                if (vcode) {
                    Intent intent = new Intent(RegistActivity.this, ModifyUsernameActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("phoneNumber", phone2);
                    bundle.putString("password", pw);
                    bundle.putString("vcode", code);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, REQ_CODE_MODIFY_USER_NM);
                } else {
                    toast("请获取验证码");
                }
                break;
            case R.id.agree:
                agree.setSelected(!agree.isSelected());
                break;
            case R.id.proxy:

                break;
        }
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        handler.removeCallbacks(run);
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            finish();
        }
    };

    private boolean isInputTelMunValid() {

        strTelNum = regist_phone.getText().toString().trim();
        if (!RegexValidateUtil.checkMobileNumber(strTelNum)) {
            toast("请输入正确的手机号码");

            return false;
        }
        if (!getCode.getText().toString().equals("重新获取") && !getCode.getText().toString().equals("获取验证码")) {
            return false;
        }
        if (!CheckNetWork.isNetworkAvailable(this)) {
            toast("请设置好网络条件。");
            return false;
        }
        return true;
    }


    private void getRegVcode() {
        progress = 60;
        handler.removeCallbacks(run);
        handler.post(run);


        GetRegVcodeParam param=new GetRegVcodeParam();
        param.setPhoneNumber(strTelNum);

        ApiReq.doGet(param, Content.REQ_getRegVcode,getRegCodeSuc,getRegCodeErr);

    }


    //获取验证码失败回调
    private Response.ErrorListener getRegCodeErr = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            String errMsg=error.getMessage();

            if (TextUtils.isEmpty(errMsg)){
                errMsg="发送失败，请重试";
            }else if (errMsg.contains(getString(R.string.tip_net_error))){
                errMsg="发送失败，请重试";
            }

            getCodeTip = "重新获取";
            handler.removeCallbacks(run);
            getCode.setClickable(true);
            getCode.setText(getCodeTip);

            toast(errMsg);
        }
    };

    //      获取验证码成功回调
    private Response.Listener<BaseResponse> getRegCodeSuc =
            new Response.Listener<BaseResponse>() {

                @Override
                public void onResponse(BaseResponse response) {
                    vcode = true;
                    getCodeTip = "获取验证码";
                    toast("短信发送成功.请查收！谢谢注册.");
                }
            };

}
