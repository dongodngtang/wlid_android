/*
 * -----------------------------------------------------------------
 * Copyright (C) 2013-2015, by Gemo Info , All rights reserved.
 * -----------------------------------------------------------------
 *
 * File: SettingPersonalMassageActivity.java
 * Author: ChenWeiZhen
 * Version: 1.0
 * Create: 2015-10-5
 *
 * Changes (from 2015-10-5)
 * -----------------------------------------------------------------
 * 2015-10-5 创建SettingPersonalMassageActivity.java (ChenWeiZhen);
 * -----------------------------------------------------------------
 * 2015-11-3 有关于“个人说明”的部分全部注释。另外：“个人说明”之后就是“签名” (ChenWeiZhen);
 * -----------------------------------------------------------------
 */
package net.doyouhike.app.wildbird.ui.setting;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.doyouhike.app.library.ui.uistate.UiState;
import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.dao.sharepref.UserInfoSpUtil;
import net.doyouhike.app.wildbird.biz.model.Content;
import net.doyouhike.app.wildbird.biz.model.bean.CitySelectInfo;
import net.doyouhike.app.wildbird.biz.model.bean.ItemEquipment;
import net.doyouhike.app.wildbird.biz.model.response.ModifyInfoReq;
import net.doyouhike.app.wildbird.biz.service.net.ApiReq;
import net.doyouhike.app.wildbird.ui.adapter.SettingEquipmentAdapter;
import net.doyouhike.app.wildbird.ui.base.BaseAppActivity;
import net.doyouhike.app.wildbird.ui.view.CitySelectDialog;
import net.doyouhike.app.wildbird.ui.view.MyGridView;
import net.doyouhike.app.wildbird.ui.view.TitleView;
import net.doyouhike.app.wildbird.util.GetCityIDUtils;

import java.util.ArrayList;

import butterknife.InjectView;


public class SettingPersonalMassageActivity extends BaseAppActivity implements View.OnClickListener {


    private final int REQ_CODE_ADD_CAMERA = 0x01;
    private final int REQ_CODE_ADD_TELESCOPE = 0x02;


    @InjectView(R.id.sv_setting_content)
    ScrollView mScrollView;
    @InjectView(R.id.titleview)
    TitleView mTitleView;
    @InjectView(R.id.edt_setting_insitution)
    EditText edtInsitution;
    @InjectView(R.id.tv_setting_nickname)
    TextView tvSettingNickname;
    /**
     * 添加按钮
     */
    @InjectView(R.id.iv_setting_add_camera)
    ImageView btnSettingAddCamera;
    /**
     * 城市
     */
    @InjectView(R.id.ll_setting_personal_city)
    LinearLayout llSettingCity;
    /**
     * 城市
     */
    @InjectView(R.id.tv_setting_city)
    TextView tvSettingCity;
    /**
     * 相机列表
     */
    @InjectView(R.id.gv_setting_camera)
    MyGridView gvSettingEquipment;
    /**
     * 添加望远镜
     */
    @InjectView(R.id.iv_setting_add_telescope)
    ImageView btnSettingAddTelescope;
    /**
     * 望远镜列表
     */
    @InjectView(R.id.gv_setting_telescope)
    MyGridView gvSettingTelescope;


    /**
     * 已选的望远镜数据
     */
    SettingEquipmentAdapter mTelescopeAdapter;

    /**
     * 已选的相机数据
     */
    SettingEquipmentAdapter mCameraAdapter;

    private ModifyInfoReq mModifyInfoReq;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_setting_personal_message;
    }

    @Override
    protected void initViewsAndEvents() {
        initData();
        initListener();
    }

    @Override
    protected View getLoadingTargetView() {
        return mScrollView;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode != RESULT_OK) {
            return;
        }

        String[] strings = data.getStringArrayExtra(AddEquipmentActivity.I_SELECTED_EQUIPMENT);
        switch (requestCode) {
            case REQ_CODE_ADD_CAMERA:
                resetCameraData(strings);
                break;
            case REQ_CODE_ADD_TELESCOPE:
                resetTelescopeData(strings);
                break;
        }

    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SettingPersonalMassageActivity.this, AddEquipmentActivity.class);
        switch (v.getId()) {
            case R.id.iv_setting_add_camera:
                //添加相机
                intent.putExtra(AddEquipmentActivity.I_SELECTED_EQUIPMENT, mModifyInfoReq.getArrayCamera());
                intent.putExtra(AddEquipmentActivity.I_SELECTED_FROM, true);
                startActivityForResult(intent, REQ_CODE_ADD_CAMERA);
                break;
            case R.id.iv_setting_add_telescope:
                //添加望远镜
                intent.putExtra(AddEquipmentActivity.I_SELECTED_EQUIPMENT, mModifyInfoReq.getArrayTelescope());
                intent.putExtra(AddEquipmentActivity.I_SELECTED_FROM, false);
                startActivityForResult(intent, REQ_CODE_ADD_TELESCOPE);
                break;
            case R.id.ll_setting_personal_city:
                //城市选择
                CitySelectDialog citySelelctDialog = new CitySelectDialog(mContext);
                citySelelctDialog.show();
                break;
        }
    }


    private void initData() {


        mModifyInfoReq = new ModifyInfoReq();

        mModifyInfoReq.setCity_id(UserInfoSpUtil.getInstance().getCityId());
        mModifyInfoReq.setInsitution(UserInfoSpUtil.getInstance().getInsitution());
        mModifyInfoReq.setCamera(UserInfoSpUtil.getInstance().getCamera());
        mModifyInfoReq.setTelescope(UserInfoSpUtil.getInstance().getTelescope());


        tvSettingNickname.setText(UserInfoSpUtil.getInstance().getUserNm());
        edtInsitution.setText(mModifyInfoReq.getInsitution());

        String strCity = GetCityIDUtils.getCity(mModifyInfoReq.getCity_id());
        if (!TextUtils.isEmpty(strCity)) {
            tvSettingCity.setText(strCity);
        }


        initCameraData();
        initTelescopeData();
    }

    private void initCameraData() {
        mCameraAdapter = new SettingEquipmentAdapter(this, false, new ArrayList<ItemEquipment>());
        gvSettingEquipment.setAdapter(mCameraAdapter);
        resetCameraData(mModifyInfoReq.getArrayCamera());
    }

    private void initTelescopeData() {
        mTelescopeAdapter = new SettingEquipmentAdapter(this, false, new ArrayList<ItemEquipment>());
        gvSettingTelescope.setAdapter(mTelescopeAdapter);
        resetTelescopeData(mModifyInfoReq.getArrayTelescope());
    }


    private void initListener() {
        btnSettingAddCamera.setOnClickListener(this);
        btnSettingAddTelescope.setOnClickListener(this);
        llSettingCity.setOnClickListener(this);


        mTitleView.setListener(new TitleView.ClickListener() {
            @Override
            public void clickLeft() {
                //返回
                SettingPersonalMassageActivity.this.finish();
            }

            @Override
            public void clickRight() {
                //保存
                saveUserInformation();
            }
        });
    }

    /**
     * 保存用户信息
     */
    private void saveUserInformation() {


        mModifyInfoReq.setInsitution(edtInsitution.getText().toString());
        updateView(UiState.LOADING_DIALOG);

        ApiReq.doPost(mModifyInfoReq, Content.REQ_RECORD_MODIFY_INFO, new Response.Listener() {
            @Override
            public void onResponse(Object response) {

                updateView(UiState.DISMISS_DIALOG);
                SettingPersonalMassageActivity.this.toast("保存成功");
                UserInfoSpUtil.getInstance().updateUserInfo(mModifyInfoReq);
                SettingPersonalMassageActivity.this.finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                updateView(UiState.DISMISS_DIALOG);
                SettingPersonalMassageActivity.this.toast("保存失败");
            }
        });
    }

    /**
     * 重置相机数据
     *
     * @param strings
     */
    private void resetCameraData(String[] strings) {
        mModifyInfoReq.setCamera(strings);
        mCameraAdapter.setDefaultData(mModifyInfoReq.getArrayCamera());
        mCameraAdapter.notifyDataSetChanged();
    }

    /**
     * 重置望远镜数据
     *
     * @param strings
     */
    private void resetTelescopeData(String[] strings) {

        mModifyInfoReq.setTelescope(strings);
        mTelescopeAdapter.setDefaultData(mModifyInfoReq.getArrayTelescope());
        mTelescopeAdapter.notifyDataSetChanged();
    }


    /**
     * 选择城市对话框的响应
     *
     * @param csInfo 城市信息
     */
    public void onEventMainThread(CitySelectInfo csInfo) {
        tvSettingCity.setText(csInfo.getCityName());
        mModifyInfoReq.setCity_id(csInfo.getCityId());

    }


}