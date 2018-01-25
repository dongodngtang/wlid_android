package net.doyouhike.app.wildbird.ui.login;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.Fragment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.Content;
import net.doyouhike.app.wildbird.biz.model.base.BaseResponse;
import net.doyouhike.app.wildbird.biz.model.request.post.ForgetPwdParam;
import net.doyouhike.app.wildbird.biz.model.request.post.GetPwdVcodeParam;
import net.doyouhike.app.wildbird.biz.service.net.ApiReq;
import net.doyouhike.app.wildbird.ui.base.BaseFragment;
import net.doyouhike.app.wildbird.util.CheckNetWork;
import net.doyouhike.app.wildbird.util.MD5Util;
import net.doyouhike.app.wildbird.util.RegexValidateUtil;
import net.doyouhike.app.wildbird.ui.view.LineEditText;
import net.doyouhike.app.wildbird.ui.view.MyAlertDialogUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPwMobileFragment extends BaseFragment implements View.OnClickListener, LineEditText.SearchListener {


    private String strTelNum;

    public ResetPwMobileFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_reset_pw_mobile;
    }

    @Override
    protected void initViewsAndEvents() {
        super.initViewsAndEvents();
        View view = getView();
        initView(view);
    }

    private void initView(View view) {

        reset_phone = (LineEditText) view.findViewById(R.id.reset_phone);
        reset_pw = (LineEditText) view.findViewById(R.id.reset_pw);
        reset_code = (EditText) view.findViewById(R.id.reset_code);
        getcode = (TextView) view.findViewById(R.id.getcode);
        reset = (TextView) view.findViewById(R.id.reset);

        reset_pw.setListener(this);
        reset_pw.setIsPW(true);
        getcode.setOnClickListener(this);
        reset.setOnClickListener(this);
    }

    private LineEditText reset_phone, reset_pw;
    private EditText reset_code;
    private TextView getcode, reset;
    private ProgressDialog progressDialog = null;
    private String getCodeTip = "获取验证码";

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            handler.removeCallbacks(run);
            switch (msg.what) {
                case 1:
                    break;
                case 2:
                    getcode.setText(getCodeTip);
                    getcode.setClickable(true);
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
            getcode.setText(progress + "秒");
            handler.postDelayed(run, 1000);
            if (progress == 0) {
                getcode.setClickable(true);
                getcode.setText(getCodeTip);
                handler.sendEmptyMessage(1);
            }
        }
    };


    @Override
    public void search() {
        // TODO Auto-generated method stub
    }

    @Override
    public void showPW() {
        // TODO Auto-generated method stub
        reset_pw.showPassword();
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
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.getcode:
                if (isInputTelMunValid()) {
                    getRegVcode();
                }
                break;
            case R.id.reset:
                if (isInputResetValid()) {
                    resetPwd();
                }
                break;
        }
    }

    private boolean isInputResetValid() {

        String pw = reset_pw.getText().toString().trim();
        String code = reset_code.getText().toString().trim();

        strTelNum = reset_phone.getText().toString().trim();
        if (!RegexValidateUtil.checkMobileNumber(strTelNum)) {
            toast("请输入正确的手机号码");

            return false;
        }

        if (pw.equals("") || !(pw.length() >= 6 && pw.length() <= 20)) {
            toast("新密码为空或密码长度小于6或大于20.");
            return false;
        }
        if (code.equals("")) {
            toast("请输入手机验证码");
            return false;
        }
        if (CheckNetWork.isNetworkAvailable(getActivity())) {

        } else {
            toast("请设置好网络条件。");
        }

        return true;
    }

    private void resetPwd() {
        progressDialog = MyAlertDialogUtil.showDialog(getActivity(), "正在重置密码，请稍后...");

        String code = reset_code.getText().toString().trim();
        String pw = reset_pw.getText().toString().trim();
        String md5Pw = MD5Util.Encode(pw);


        ForgetPwdParam param = new ForgetPwdParam();

        param.setPhoneNumber(strTelNum);
        param.setVcode(code);
        param.setTheNewPwd(md5Pw);

        ApiReq.doPost(param, Content.REQ_forgetPwd, resetPwdSuc, resetPwdErr);
    }

    private boolean isInputTelMunValid() {

        strTelNum = reset_phone.getText().toString().trim();
        if (!RegexValidateUtil.checkMobileNumber(strTelNum)) {
            toast("请输入正确的手机号码");

            return false;
        }


        if (!getcode.getText().toString().equals("获取验证码") && !getcode.getText().toString().equals("重新获取")) {
            return false;
        }

        if (!CheckNetWork.isNetworkAvailable(getActivity())) {
            toast("请设置好网络条件。");
            return false;
        }
        return true;
    }

    private void getRegVcode() {

        getcode.setClickable(false);
        progress = 60;
        handler.removeCallbacks(run);
        handler.post(run);

        GetPwdVcodeParam param = new GetPwdVcodeParam();
        param.setPhoneNumber(strTelNum);

        ApiReq.doPost(param, Content.REQ_getPwdVcode, getPwdCodeSuc, getPwdCodeErr);

    }


    //重置密码失败回调
    private Response.ErrorListener resetPwdErr = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            String errMsg = error.getMessage();

            if (TextUtils.isEmpty(errMsg)) {
                errMsg = "网络不畅";
            }

            if (progressDialog != null) progressDialog.dismiss();
            toast(errMsg);
        }
    };

    //    重置密码成功回调
    private Response.Listener<BaseResponse> resetPwdSuc =
            new Response.Listener<BaseResponse>() {

                @Override
                public void onResponse(BaseResponse response) {
                    toast("重置密码成功");
                    getActivity().finish();
                }
            };


    //获取验证码失败回调
    private Response.ErrorListener getPwdCodeErr = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            String errMsg = error.getMessage();

            if (TextUtils.isEmpty(errMsg)) {
                errMsg = "发送失败，请重试";
            } else if (errMsg.contains(getString(R.string.tip_net_error))) {
                errMsg = "发送失败，请重试";
            }

            getCodeTip = "重新获取";
            handler.sendEmptyMessage(2);

            toast(errMsg);
        }
    };

    //      获取验证码成功回调
    private Response.Listener<BaseResponse> getPwdCodeSuc =
            new Response.Listener<BaseResponse>() {

                @Override
                public void onResponse(BaseResponse response) {
                    getCodeTip = "获取验证码";
                    toast("短信验证码发送成功,请查收！谢谢使用.");
                }
            };
}
