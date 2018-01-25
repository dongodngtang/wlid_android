package net.doyouhike.app.wildbird.ui.login;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.Content;
import net.doyouhike.app.wildbird.biz.model.base.BaseResponse;
import net.doyouhike.app.wildbird.biz.model.request.get.ResetPwEmParam;
import net.doyouhike.app.wildbird.biz.service.net.ApiReq;
import net.doyouhike.app.wildbird.ui.base.BaseFragment;
import net.doyouhike.app.wildbird.util.ProgressDialogUtils;
import net.doyouhike.app.wildbird.util.RegexValidateUtil;
import net.doyouhike.app.wildbird.ui.view.LineEditText;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPwEmailFragment extends BaseFragment implements OnClickListener {

    private LineEditText mEdtEmail;
    private TextView btnSubmit;
    private ProgressDialogUtils progressDialogUtils;
    private String strEmail;
    private String net_err_tip;

    public ResetPwEmailFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_reset_pw_email;
    }

    @Override
    protected void initViewsAndEvents() {
        super.initViewsAndEvents();
        View view = getView();
        mEdtEmail = (LineEditText) view.findViewById(R.id.et_rspw_em_fg_email);
        btnSubmit = (TextView) view.findViewById(R.id.btn_rspw_em_fg_submit);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressDialogUtils = new ProgressDialogUtils(getActivity(), null);

        net_err_tip=getActivity().getString(R.string.tip_net_error);
    }

    @Override
    public void onDestroyView() {
        progressDialogUtils.onDestroy();
        progressDialogUtils.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_rspw_em_fg_submit:
                if (isInputValue()) {
                    //隐藏键盘
                    InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getApplicationContext().
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(mEdtEmail.getWindowToken(), 0); //隐藏

                    //提交请求
                    sendEmail();
                }
                break;
        }
    }

    private boolean isInputValue() {

        strEmail = mEdtEmail.getText().toString().trim();

        //如果邮箱输入不为空，且包含@符号，则邮箱格式输入正确
        if (strEmail.isEmpty()) {
            toast("邮箱输入为空，请重新输入");
            mEdtEmail.setError("不能为空");
            return false;
        }

        if (!RegexValidateUtil.checkEmail(strEmail)){
            toast("邮箱输入格式有误，请重新输入");
            mEdtEmail.setError("邮箱输入格式有误");
            return false;
        }

        return true;
    }

    private void sendEmail() {
        progressDialogUtils.showProgressDialog("提交中……");

        ResetPwEmParam pwEmParam=new ResetPwEmParam();
        pwEmParam.setEmail(strEmail);

        ApiReq.doGet(pwEmParam, Content.REQ_RESET_PW_EMAIL,resetPwdSuc,resetPwdErr);
    }



    //重置密码失败回调
    private Response.ErrorListener resetPwdErr = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            String errMsg = error.getMessage();

            if (TextUtils.isEmpty(errMsg)||errMsg.contains(net_err_tip)) {
                errMsg=net_err_tip+"，请重试。";
            }

            progressDialogUtils.dismissDialog();
            toast(errMsg);
        }
    };

    //    重置密码成功回调
    private Response.Listener<BaseResponse> resetPwdSuc =
            new Response.Listener<BaseResponse>() {

                @Override
                public void onResponse(BaseResponse response) {
                    progressDialogUtils.dismissDialog();
                    toast("提交成功，请查收验证邮件");
                }
            };


}
