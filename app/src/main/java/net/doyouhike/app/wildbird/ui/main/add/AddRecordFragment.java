package net.doyouhike.app.wildbird.ui.main.add;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import net.doyouhike.app.library.ui.uistate.UiState;
import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.bean.CitySelectInfo;
import net.doyouhike.app.wildbird.biz.model.bean.RecordEntity;
import net.doyouhike.app.wildbird.biz.model.event.EventGetRecordDetail;
import net.doyouhike.app.wildbird.biz.model.event.EventUploadRecord;
import net.doyouhike.app.wildbird.biz.service.net.GetRecordDetailService;
import net.doyouhike.app.wildbird.ui.ChooseBirdNameActivity;
import net.doyouhike.app.wildbird.ui.base.BaseFragment;
import net.doyouhike.app.wildbird.ui.record.LocationActivity;
import net.doyouhike.app.wildbird.ui.view.CitySelectDialog;
import net.doyouhike.app.wildbird.ui.view.TitleView;
import net.doyouhike.app.wildbird.util.LocalSharePreferences;
import net.doyouhike.app.wildbird.util.UiUtils;
import net.doyouhike.app.wildbird.util.location.EventLocation;
import net.doyouhike.app.wildbird.util.location.LocationManager;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by zengjiang on 16/6/2.
 */
public class AddRecordFragment extends BaseFragment implements View.OnClickListener {


    /**
     * 上级目录传过来的纪录信息
     */
    public static final String ARG_RECORD_ENTITY = "param1";
    /**
     * 是否需要从网络加载数据
     */
    public static final String ARG_NEED_GET_FROM_NET = "param2";
    /**
     * 是否编辑
     */
    public static final String ARG_IS_ADD = "param3";

    private final int REQ_CODE_LOCATION = 103;
    private final int REQ_CODE_KIND = 104;


    /**
     * 锁定按钮
     */
    @InjectView(R.id.ll_add_record_lock)
    LinearLayout llAddRecordLock;
    /**
     * 位置
     */
    @InjectView(R.id.tv_add_record_location)
    TextView tvAddRecordLocation;
    /**
     * 城市
     */
    @InjectView(R.id.tv_add_record_select_city)
    TextView tvSelectCity;
    /**
     * 定位按钮
     */
    @InjectView(R.id.ll_add_record_location)
    LinearLayout llAddRecordLocation;
    /**
     * 城市选择
     */
    @InjectView(R.id.ll_add_record_select_city)
    LinearLayout llSelectCity;
    /**
     * 时间
     */
    @InjectView(R.id.tv_add_record_time)
    TextView tvAddRecordTime;
    /**
     * 选中时间
     */
    @InjectView(R.id.ll_add_record_time)
    LinearLayout llAddRecordTime;
    /**
     * 鸟种
     */
    @InjectView(R.id.tv_add_record_kind)
    TextView tvAddRecordKind;
    /**
     * 选中鸟种
     */
    @InjectView(R.id.ll_add_record_kind)
    LinearLayout llAddRecordKind;
    /**
     * 数量
     */
    @InjectView(R.id.edt_add_record_count)
    EditText edtAddRecordCount;
    /**
     * 备注
     */
    @InjectView(R.id.edt_add_record_remark)
    EditText edtAddRecordRemark;
    /**
     * 图片
     */
    @InjectView(R.id.rv_add_record_images)
    RecyclerView rvAddRecordImages;
    /**
     * 选中图片
     */
    @InjectView(R.id.btn_add_record_add_picture)
    ImageButton btnAddRecordAddPicture;
    /**
     * 选中图片
     */
    @InjectView(R.id.vi_add_record_lock)
    View mViLock;
    /**
     * 标题栏
     */
    @InjectView(R.id.title_add_record)
    TitleView mTitleView;
    /**
     * 内容
     */
    @InjectView(R.id.sv_add_record_content)
    ScrollView mContentView;
    /**
     * 保存并新增
     */
    @InjectView(R.id.btn_add_record_save_add)
    Button mBtnSaveAndAdd;


    /**
     * 添加编辑纪录的
     */
    AddRecordPresenter mPresenter;
    /**
     * 上级页面传过来的纪录信息
     */
    private RecordEntity entity;
    /**
     * 是否需要从网络获取
     */
    private boolean needGetFromNet;
    /**
     * 是否添加纪录
     */
    private boolean isAdd;
    /**
     * 是否从地图选取地址
     */
    private boolean isGetLocationFromSelected = false;
    /**
     * 是否锁屏
     */
    private boolean isLockLocationTm = false;
    /**
     * 是否添加并保存
     */
    private boolean isAddAndSave;


    /**
     * 第一次进入activity
     */
    private boolean isFirst;

    public AddRecordFragment() {
    }

    public static AddRecordFragment newInstance(boolean needGetFromNet, boolean isAdd, RecordEntity entity) {
        AddRecordFragment fragment = new AddRecordFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_NEED_GET_FROM_NET, needGetFromNet);
        args.putBoolean(ARG_IS_ADD, isAdd);
        args.putSerializable(ARG_RECORD_ENTITY, entity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            needGetFromNet = getArguments().getBoolean(ARG_NEED_GET_FROM_NET);
            isAdd = getArguments().getBoolean(ARG_IS_ADD);
            entity = (RecordEntity) getArguments().getSerializable(ARG_RECORD_ENTITY);
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_add_record;
    }

    @Override
    protected View getLoadingTargetView() {
        return mContentView;
    }


    @Override
    protected void initViewsAndEvents() {
        initListener();

        initData();

        initView();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        setDefaultData(entity);


        if (needGetFromNet) {

            if (needGetFromNet) {
                updateView(UiState.LOADING_DIALOG);
                GetRecordDetailService.getInstance().getRecordDetail(entity.getRecordID());
            }

        }
        isFirst=true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode != Activity.RESULT_OK) {
            return;
        }


        switch (requestCode) {
            case REQ_CODE_LOCATION:
                //地图选择回调
                isGetLocationFromSelected = true;
                mPresenter.handleLocation(data);
                break;
            case REQ_CODE_KIND:
                //鸟种选择回调
                mPresenter.handleKind(data);
                break;
            default:
                //默认 拍照或选图回调
                mPresenter.handleSelectPicture(requestCode, data);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);

        // 定位初始化
        if (!isLockLocationTm&&isFirst) {
            LocationManager.getInstance().start(getContext());
            isFirst=false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_add_record_lock:
                //位置地址锁定
                mPresenter.saveLockValue(!llAddRecordLock.isSelected());
                break;
            case R.id.ll_add_record_location:
                //选择位置
                startActivityForResult(new Intent(getContext(), LocationActivity.class), REQ_CODE_LOCATION);
                break;
            case R.id.ll_add_record_time:
                //选择时间
                mPresenter.selectTime();
                break;
            case R.id.ll_add_record_kind:
                //选中鸟种
                startActivityForResult(new Intent(mContext, ChooseBirdNameActivity.class), REQ_CODE_KIND);
                break;
            case R.id.btn_add_record_add_picture:
                //添加图片
                mPresenter.selectPicture(v);
                break;
            case R.id.btn_add_record_save_add:
                //保存
                isAddAndSave = true;
                save();
                break;
            case R.id.ll_add_record_select_city:
                //城市选择
                CitySelectDialog citySelelctDialog = new CitySelectDialog(mContext);
                citySelelctDialog.show();
                break;
        }
    }

    /**
     * @param time 时间
     */
    public void setTime(long time) {

        Date date = new Date(time * 1000);
        String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
        tvAddRecordTime.setText(datetime);
    }


    /**
     * @return 备注输入
     */
    public String getRemark() {
        return edtAddRecordRemark.getText().toString();
    }

    /**
     * @return 数量
     */
    public int getCount() {

        String count = edtAddRecordCount.getText().toString();

        if (TextUtils.isEmpty(count)) {
            return 0;
        }

        return Integer.valueOf(count);
    }

    /**
     * @param location 位置
     */
    public void setLocation(String location) {
        UiUtils.showView(tvAddRecordLocation, !TextUtils.isEmpty(location));
        tvAddRecordLocation.setText(location);
    }

    /**
     * @param city 城市信息
     */
    public void setCityText(String city) {
        tvSelectCity.setText(city);
    }


    /**
     * @param showSelectCity 是否显示城市选择
     */
    public void setSelectCityView(boolean showSelectCity) {
        UiUtils.showView(llAddRecordLocation, !showSelectCity);
        UiUtils.showView(llSelectCity, showSelectCity);
    }

    /**
     * @param kind 鸟种
     */
    public void setKind(String kind) {
        if (TextUtils.isEmpty(kind)) {
            tvAddRecordKind.setText("选中鸟种");
        } else {
            tvAddRecordKind.setText(kind);
        }
    }

    public void resetIsAdd(){
        isAdd=true;
    }
    /**
     * 绑定数据
     *
     * @param entity 纪录信息
     */
    public void setDefaultData(RecordEntity entity) {
        isAddAndSave = false;


        if (null == entity) {


            edtAddRecordCount.setText(String.valueOf(1));
            mPresenter.getRecord().setNumber(1);

            edtAddRecordRemark.setText("");
            if (isAdd){
                //属于新增
                mPresenter.setLockState(isLockLocationTm);
            }

            if (!isLockLocationTm) {
                isGetLocationFromSelected = false;
                mPresenter.getRecord().setTime(System.currentTimeMillis() / 1000);
                setTime(System.currentTimeMillis() / 1000);
            }

            setKind(null);

            mPresenter.updateImgList(null);

        } else {

            edtAddRecordCount.setText(String.valueOf(entity.getNumber()));
            edtAddRecordRemark.setText(entity.getDescription());
            setKind(entity.getSpeciesName());

            setTime(entity.getTime());

            if (isAdd) {
                mPresenter.setLockState(isLockLocationTm);
            }else {
                //位置信息是否为空
                isGetLocationFromSelected = !TextUtils.isEmpty(entity.getLocation());
                setLocation(entity.getLocation());
            }


            mPresenter.updateImgList(entity.getList());


        }


    }

    public RecyclerView getRvAddRecordImages() {
        return rvAddRecordImages;
    }

    private void initListener() {
        llAddRecordLocation.setOnClickListener(this);
        llAddRecordTime.setOnClickListener(this);
        llAddRecordKind.setOnClickListener(this);
        llAddRecordLock.setOnClickListener(this);
        btnAddRecordAddPicture.setOnClickListener(this);
        mBtnSaveAndAdd.setOnClickListener(this);
        llSelectCity.setOnClickListener(this);

        mTitleView.setListener(new TitleView.ClickListener() {
            @Override
            public void clickLeft() {
                getActivity().finish();
            }

            @Override
            public void clickRight() {
                save();
            }
        });


    }


    private void initData() {
        mPresenter = new AddRecordPresenter(this, entity);
        isLockLocationTm = LocalSharePreferences.getValue(getContext(), LocalSharePreferences.KEY_LOCK_LOCATION_TIME, false);
    }

    private void initView() {
        UiUtils.showView(mBtnSaveAndAdd, getActivity() instanceof EditRecordActivity);
        mTitleView.showLeftView(getActivity() instanceof EditRecordActivity);
    }

    /**
     * 设置锁定状态
     *
     * @param state 是否锁定
     */
    public void setLockState(boolean state) {
        isLockLocationTm = state;
        llAddRecordLock.setSelected(isLockLocationTm);
        mViLock.setSelected(isLockLocationTm);

        if (isLockLocationTm) {
            isGetLocationFromSelected = true;
        }

        llAddRecordTime.setEnabled(!isLockLocationTm);
        llAddRecordLocation.setEnabled(!isLockLocationTm);
    }

    /**
     * 上传
     */
    private void save() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtAddRecordCount.getWindowToken(), 0);
        if (mPresenter.isInputValue()) {
            mPresenter.save();
        }
    }

    /**
     * 定位返回处理
     */
    public void onEventMainThread(EventLocation event) {

        if (isGetLocationFromSelected) {
            return;
        }

        mPresenter.handleLocation(event);

    }

    /**
     * 选择城市对话框的响应
     *
     * @param csInfo
     */
    public void onEventMainThread(CitySelectInfo csInfo) {

        isGetLocationFromSelected = true;
        mPresenter.onCitySelect(csInfo);

    }

    /**
     * 获取鸟种详情
     *
     * @param result
     */
    public void onEventMainThread(EventGetRecordDetail result) {
        updateView(UiState.DISMISS_DIALOG);
        if (result.isSuc()) {
            entity = new RecordEntity(result.getResult());
            mPresenter.resetRecord(entity);
            setDefaultData(mPresenter.getRecord());
        } else {
            toast(result.getStrErrMsg());
        }
    }

    /**
     * 上传观鸟记录
     *
     * @param result
     */
    public void onEventMainThread(EventUploadRecord result) {


        boolean isNeedFinish = !isAddAndSave;
        updateView(UiState.DISMISS_DIALOG);
        if (result.isSuc()) {
            mPresenter.uploadSuccess();
        } else {
            toast(result.getStrErrMsg());
            mPresenter.uploadError();
        }


        if (isNeedFinish) {
            if (mContext instanceof EditRecordActivity) {
                getActivity().finish();
            }
        }
    }

}
