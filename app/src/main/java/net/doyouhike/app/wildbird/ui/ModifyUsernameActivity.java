package net.doyouhike.app.wildbird.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.dao.sharepref.UserInfoSpUtil;
import net.doyouhike.app.wildbird.biz.model.Content;
import net.doyouhike.app.wildbird.biz.model.base.CommonResponse;
import net.doyouhike.app.wildbird.biz.model.request.post.RegParam;
import net.doyouhike.app.wildbird.biz.model.response.LoginResp;
import net.doyouhike.app.wildbird.biz.service.net.ApiReq;
import net.doyouhike.app.wildbird.biz.service.net.RequestUtil;
import net.doyouhike.app.wildbird.ui.base.BaseAppActivity;
import net.doyouhike.app.wildbird.util.MD5Util;
import net.doyouhike.app.wildbird.ui.view.LineEditText;

import java.util.HashMap;

@SuppressWarnings("deprecation")
public class ModifyUsernameActivity extends BaseAppActivity implements OnClickListener {

    private LineEditText username;
    private TextView modify_username;
    private Intent intent;
    private String mStrTel = "";
    private String mStrUserNm = "";

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.modify_username;
    }

    @Override
    protected void initViewsAndEvents() {
        super.initViewsAndEvents();

        intent = getIntent();
        username = (LineEditText) super.findViewById(R.id.username);
        modify_username = (TextView) super.findViewById(R.id.modify_username);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        modify_username.setOnClickListener(this);
        mProgressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        String name = username.getText().toString().trim();
        if (name.equals("")) {
            toast("请输入用户名");
        } else {

            register();

//
//			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//			imm.hideSoftInputFromWindow(username.getWindowToken(), 0);
//
//			mProgressDialog.showProgressDialog("正在注册，请稍后...");
//
//			JSONObject jsonObject = new JSONObject();
//			try {
//				mStrTel=intent.getStringExtra("phoneNumber");
//				mStrUserNm=username.getText().toString();
//
//				jsonObject.put("phoneNumber", mStrTel);
//				String MD5_password = MD5Util.Encode(intent.getStringExtra("password"));
//				jsonObject.put("password", MD5_password);
//				jsonObject.put("vcode", intent.getStringExtra("vcode"));
//				jsonObject.put("userName", mStrUserNm);
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			httpRequest(jsonObject, Content.WB_URL + "user/reg");
        }
    }

    @Override
    protected void onStop() {
        RequestUtil.getInstance().cancelAllRequests(Content.REQ_reg);
        mProgressDialog.dismissDialog();
        super.onStop();

    }

    //	private void httpRequest(final JSONObject jsonObject, final String url) {
//		try {
//			StringEntity stringEntity = new StringEntity(jsonObject.toString(), "utf-8");
//			stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//			.post(ModifyUsernameActivity.this, url, stringEntity, "application/json",
//					new JsonHttpResponseHandler() {
//						@Override
//						public void onFailure(int statusCode, Header[] headers, Throwable throwable,
//											  JSONObject errorResponse) {
//							// TODO Auto-generated method stub
//							super.onFailure(statusCode, headers, throwable, errorResponse);
//							if (progressDialog != null) progressDialog.dismiss();
//						}
//
//						@Override
//						public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//							// TODO Auto-generated method stub
//							super.onSuccess(statusCode, headers, response);
//							Log.i("info", response.toString());
//							try {
//								if (response.getInt("ret") == 0) {
//									toast("恭喜您,注册成功!");
//									Intent intent = new Intent(MainActivity.EXIT_MAIN);
//									intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
//									sendBroadcast(intent);
//									JSONObject jsonObject = response.getJSONObject("data");
//									LocalSharePreferences.commit(ModifyUsernameActivity.this, "token", jsonObject.getString("token"));
//									JSONObject jsonObject2 = jsonObject.getJSONObject("userInfo");
//									LocalSharePreferences.commit(ModifyUsernameActivity.this, "userId", jsonObject2.getString("userId"));
//									LocalSharePreferences.commit(ModifyUsernameActivity.this, "userName", jsonObject2.getString("userName"));
//									LocalSharePreferences.commit(ModifyUsernameActivity.this, "sex", "" + jsonObject2.getInt("sex"));
//									LocalSharePreferences.commit(ModifyUsernameActivity.this, "avatar", jsonObject2.getString("avatar"));
//									LocalSharePreferences.commit(ModifyUsernameActivity.this, "recordNum", "" + jsonObject2.getInt("recordNum"));
//									LocalSharePreferences.commit(ModifyUsernameActivity.this, "speciesNum", "" + jsonObject2.getInt("speciesNum"));
//									LocalSharePreferences.commit(ModifyUsernameActivity.this, "thisYearRecordNum", "" + jsonObject2.getInt("thisYearRecordNum"));
//									LocalSharePreferences.commit(ModifyUsernameActivity.this, "thisYearSpeciesNum", "" + jsonObject2.getInt("thisYearSpeciesNum"));
//									HashMap<String,String> map=new HashMap<String, String>();
//									map.put("tel",mStrTel);
//									map.put("userNm",mStrUserNm);
//
//									MobclickAgent.onEvent(ModifyUsernameActivity.this, "Regist",map);
//									finish();
//								} else {
//									switch (response.getInt("code")) {
//										case 1101012:
//											toast("该号码已经注册，您可以直接登录.");
//											break;
//										case 1101009:
//											toast("该用户名已被使用，换个用户名吧~");
//											break;
//										case 1101014:// 该号码没有请求过验证码
//											toast("该号码没有请求过验证码");
//											break;
//										case 100004:
//											toast("验证码错误,请返回重新输入!");
//											break;
//									}
//								}
//								if (progressDialog != null) progressDialog.dismiss();
//							} catch (JSONException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						}
//					});
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

    private void register() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(username.getWindowToken(), 0);


        String MD5_password = MD5Util.Encode(intent.getStringExtra("password"));

        mStrTel = intent.getStringExtra("phoneNumber");
        mStrUserNm = username.getText().toString();

        RegParam param = new RegParam();
        param.setPhoneNumber(mStrTel);
        param.setPassword(MD5_password);
        param.setVcode(intent.getStringExtra("vcode"));
        param.setUserName(mStrUserNm);

        mProgressDialog.showProgressDialog("正在注册，请稍后...");
        ApiReq.doPost(param, Content.REQ_reg, loginSuc, updRecordCountErr);
    }


    //      注册失败
    private Response.ErrorListener updRecordCountErr = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {


            String errMsg = error.getMessage();


            if (TextUtils.isEmpty(errMsg)) {
                errMsg = "注册失败";
            }

            toast(errMsg);
            if (null != mProgressDialog)
                mProgressDialog.dismissDialog();

        }
    };


    //      注册成功回调
    private Response.Listener<CommonResponse<LoginResp>> loginSuc =
            new Response.Listener<CommonResponse<LoginResp>>() {

                @Override
                public void onResponse(CommonResponse<LoginResp> response) {

                    if (null != mProgressDialog)
                        mProgressDialog.dismissDialog();

                    UserInfoSpUtil.getInstance().saveUserInfo(response.getT());

                    HashMap<String, String> map = new HashMap<>();
                    map.put("tel", mStrTel);
                    map.put("userNm", mStrUserNm);
                    MobclickAgent.onEvent(ModifyUsernameActivity.this, "Regist", map);

                    toast("恭喜您,注册成功!");
                    setResult(RESULT_OK);
                    finish();
                }
            };
}
