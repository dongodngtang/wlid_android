package net.doyouhike.app.wildbird.ui.main.add;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.gson.Gson;

import net.doyouhike.app.library.ui.uistate.UiState;
import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.db.bean.DbRecord;
import net.doyouhike.app.wildbird.biz.model.Content;
import net.doyouhike.app.wildbird.biz.model.bean.ChooseImage;
import net.doyouhike.app.wildbird.biz.model.bean.CitySelectInfo;
import net.doyouhike.app.wildbird.biz.model.bean.LocationEntity;
import net.doyouhike.app.wildbird.biz.model.bean.RecordEntity;
import net.doyouhike.app.wildbird.biz.model.bean.RecordImage;
import net.doyouhike.app.wildbird.biz.model.bean.WbLocation;
import net.doyouhike.app.wildbird.biz.model.event.CancelUploadRecordEvent;
import net.doyouhike.app.wildbird.biz.service.database.DraftDbService;
import net.doyouhike.app.wildbird.ui.MultiChoosePhotoActivity;
import net.doyouhike.app.wildbird.ui.login.LoginActivity;
import net.doyouhike.app.wildbird.ui.view.MyAlertDialogUtil;
import net.doyouhike.app.wildbird.ui.view.MyPopupWindow;
import net.doyouhike.app.wildbird.util.CheckNetWork;
import net.doyouhike.app.wildbird.util.LocalSharePreferences;
import net.doyouhike.app.wildbird.util.PermissionUtil;
import net.doyouhike.app.wildbird.util.location.EnumLocation;
import net.doyouhike.app.wildbird.util.location.EventLocation;
import net.doyouhike.app.wildbird.util.location.LocationManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by zengjiang on 16/6/2.
 */
public class AddRecordPresenter implements MyPopupWindow.SetAvatarListener, MyAlertDialogUtil.DialogListener {


    AddRecordFragment fragment;
    TimePickerDialog timePickerDialog;
    DbRecord mRecord;
    String filePath;
    private List<RecordImage> imgItems;
    private AddPictureAdapter mAdapter;
    Calendar calendar;

    public AddRecordPresenter(AddRecordFragment fragment, RecordEntity entity) {
        this.fragment = fragment;
        mRecord = new DbRecord(entity);

        initListView();
    }

    private void initListView() {
        imgItems = new ArrayList<>();
        mAdapter = new AddPictureAdapter(imgItems);


        LinearLayoutManager layoutManager = new LinearLayoutManager
                (fragment.getContext(), LinearLayoutManager.HORIZONTAL, false);

        fragment.getRvAddRecordImages().setLayoutManager(layoutManager);
        fragment.getRvAddRecordImages().setAdapter(mAdapter);
    }

    public void onDestroy() {
        fragment = null;
        //取消发送纪录任务
        EventBus.getDefault().post(new CancelUploadRecordEvent());
    }

    public DbRecord getRecord() {
        return mRecord;
    }

    /**
     * 选中时间
     */
    public void selectTime() {

        final Calendar current_time = Calendar.getInstance();
        calendar = Calendar.getInstance();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        if (null == timePickerDialog)
            timePickerDialog = new TimePickerDialog(
                    fragment.getContext(),
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            calendar.set(Calendar.MINUTE, minute);
                            setTimeValue(calendar.getTimeInMillis() / 1000);
                        }
                    },
                    current_time.get(Calendar.HOUR_OF_DAY),
                    current_time.get(Calendar.MINUTE), true);


        DatePickerDialog datePickerDialog = new DatePickerDialog(
                fragment.getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        if (year > current_time.get(Calendar.YEAR)) {
                            fragment.toast("观测时间不能超过当前时间");
                            return;
                        } else if (year == current_time.get(Calendar.YEAR)) {
                            if (monthOfYear > current_time.get(Calendar.MONTH)) {
                                fragment.toast("观测时间不能超过当前时间");
                                return;
                            } else if (monthOfYear == current_time.get(Calendar.MONTH)) {
                                if (dayOfMonth > current_time.get(Calendar.DAY_OF_MONTH)) {
                                    fragment.toast("观测时间不能超过当前时间");
                                    return;
                                }
                            }
                        }
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        timePickerDialog.show();
                    }
                },
                current_time.get(Calendar.YEAR),
                current_time.get(Calendar.MONTH),
                current_time.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    public void selectPicture(View view) {

        MyPopupWindow pop = MyPopupWindow.getInstance(fragment.getContext());
        pop.setListener(this);
        pop.setAnimationStyle(R.style.GrowFromBottom);
        pop.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public void handleLocation(Intent data) {
        if (data == null) return;
        LocationManager.getInstance().stop();
        mRecord.setCityName(data.getStringExtra("city"));
        mRecord.setCityID(data.getIntExtra("cityID", 0));
        mRecord.setLocation(data.getStringExtra("location"));
        mRecord.setLatitude(data.getDoubleExtra("latitude", 0.000000));
        mRecord.setLongitude(data.getDoubleExtra("longtitude", 0.000000));
        mRecord.setAltitude(data.getDoubleExtra("altitude", 0.000000));

        if (mRecord.getLocation() != null && !mRecord.getLocation().equals("")) {
            fragment.setLocation(mRecord.getLocation());
        }
    }

    public void handleSelectPicture(int requestCode, Intent data) {
        switch (requestCode) {
            case 102:// 拍照获取图片
                File file = new File(filePath);
                Uri uri2 = Uri.fromFile(file);
                RecordImage recordImage = new RecordImage();
                recordImage.setImageUri(uri2.toString());
                imgItems.add(0, recordImage);
                mAdapter.notifyDataSetChanged();
                break;
            case 105:// 从相册选择图片返回
                ChooseImage image = (ChooseImage) data.getSerializableExtra("images");
                imgItems.clear();
                imgItems.addAll(image.getList());
                mAdapter.notifyDataSetChanged();
                break;
        }
    }


    public void handleLocation(EventLocation event) {

        EnumLocation enumLocation = event.getEnumLocation();

        switch (enumLocation) {
            case FAIL:
                fragment.setSelectCityView(true);
                break;
            case PREVENT:
                fragment.toast(event.getMsg());
                break;
            case SUCCESS:
                WbLocation location = event.getLocation();
                setLocationValue(location);
                break;
        }

    }

    /**
     * 设置位置信息
     *
     * @param location 地理信息
     */
    private void setLocationValue(WbLocation location) {
        mRecord.setLatitude(location.getLatitude());
        mRecord.setLongitude(location.getLongitude());
        mRecord.setAltitude(location.getAltitude());
        mRecord.setCityName(location.getCity());
        mRecord.setLocation(location.getAddress());

        String cityCode = location.getCityCode();
        if (!TextUtils.isEmpty(cityCode)) {
            mRecord.setCityID(Integer.parseInt(location.getCityCode()));
        }

        fragment.setLocation(location.getAddress());
    }


    private void setTimeValue(long timeValue) {
        mRecord.setTime(timeValue);
        fragment.setTime(mRecord.getTime());
    }


    /**
     * @param data 鸟种选中返回
     */
    public void handleKind(Intent data) {

        int speciesID = Integer.parseInt(data.getStringExtra("speciesID"));
        String speciesName = data.getStringExtra("name");
        mRecord.setSpeciesID(speciesID);
        mRecord.setSpeciesName(speciesName);
        fragment.setKind(speciesName);
    }

    public void onCitySelect(CitySelectInfo csInfo) {


        String content;
        content = csInfo.getProvinceName() + csInfo.getCityName();
        //剔除像北京.北京这样的情况
        if (!TextUtils.isEmpty(csInfo.getProvinceName()) && !TextUtils.isEmpty(csInfo.getCityName())) {
            if (csInfo.getProvinceName().equals(csInfo.getCityName())) {
                content = csInfo.getProvinceName();
            }
        }

        fragment.setCityText(content);


        mRecord.setLongitude(0.0);
        mRecord.setLatitude(0.0);
        mRecord.setLocation(content);

        mRecord.setCityName(csInfo.getCityName());
        mRecord.setCityID(csInfo.getCityId());
    }

    @Override
    public void startCamera() {

        if (!PermissionUtil.getInstance().checkStoragePermission(fragment.getActivity())) {
            return;
        }
        /**
         * 图片保存地址
         */
        File dir = new File(MyPopupWindow.PHOTO_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, new SimpleDateFormat("yyMMddHHmmss").format(new Date()) + ".jpg");
        filePath = file.getAbsolutePath();// 获取相片的保存路径
        Uri imageUri = Uri.fromFile(file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        fragment.startActivityForResult(intent, 102);
    }

    @Override
    public void startGallery() {

        if (!PermissionUtil.getInstance().checkStoragePermission(fragment.getActivity())) {
            return;
        }
        Intent intent = new Intent(fragment.getContext(), MultiChoosePhotoActivity.class);
        ChooseImage image = new ChooseImage();
        image.setList(imgItems);
        intent.putExtra(MultiChoosePhotoActivity.I_IMAGES, image);
        fragment.startActivityForResult(intent, 105);
    }

    /**
     * @return 输入检查 是否有效
     */
    public boolean isInputValue() {


        mRecord.setNumber(fragment.getCount());

        mRecord.setDescription(fragment.getRemark());

        if (TextUtils.isEmpty(mRecord.getLocation())) {
            fragment.toast(fragment.getString(R.string.tip_add_record_empty_city));
            return false;
        }

        if (mRecord.getSpeciesID() <= 0) {

            fragment.toast("请选择鸟种。");
            return false;
        }
        if (mRecord.getNumber() <= 0 || mRecord.getNumber() > 10000) {

            fragment.toast("请输入鸟种数量必须大于零，且数量最大不能超过5位");
            return false;
        }

        if (!TextUtils.isEmpty(mRecord.getDescription()) && mRecord.getDescription().length() >= 300) {

            fragment.toast("笔记只能输入少于300字的内容。");
            return false;
        }

        if (TextUtils.isEmpty(mRecord.getLocation())) {
            fragment.toast(fragment.getString(R.string.tip_add_record_empty_city));
            return false;
        }

        if (!CheckNetWork.isNetworkAvailable(fragment.getContext())) {
            MyAlertDialogUtil.showDialog(this, fragment.getActivity(), 0);
            return false;
        }
        if (LocalSharePreferences.getValue(fragment.getContext(), Content.SP_USER_ID).equals("")) {
            MyAlertDialogUtil.showLoginDialog(this, fragment.getActivity(), 0);
            return false;
        }


        return true;
    }

    @Override
    public void dialogClick(int pos) {
        switch (pos) {
            case 0:// 打开系统设置
                fragment.startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                break;
            case 1:// 保存至草稿箱
                saveToDraft();
                clearInputData();
                if (fragment.getActivity() instanceof EditRecordActivity) {
                    fragment.getActivity().finish();
                }
                break;
            case 2:// 立即登录
                Intent intent = new Intent(fragment.getContext(), LoginActivity.class);
                fragment.startActivity(intent);
                break;
            case 3:// 保存至草稿箱
                saveToDraft();
                clearInputData();
                break;
        }
    }

    /**
     * 清空纪录
     */
    private void clearInputData() {
        //位置信息
        WbLocation wbLocation=getWbLocation();
        //时间
        long time=mRecord.getTime();

        resetRecord(null);
        fragment.setDefaultData(null);

        setLocationValue(wbLocation);
        setTimeValue(time);
    }

    /**
     * 保存至草稿箱
     */
    public void saveToDraft() {


        if (mRecord.getRecord() > 0) {
            //属于编辑数据
            DraftDbService.getInstance().updateRecord(mRecord);
        } else {
            //属于插入数据
            DraftDbService.getInstance().insertRecord(mRecord);
        }

        fragment.toast("已保存至草稿箱");

    }


    /**
     * 上传纪录
     */
    public void save() {
        fragment.toast("正在保存记录");

        fragment.updateView(UiState.LOADING_DIALOG);

        mRecord.setList(imgItems);

        EventBus.getDefault().post(mRecord);

    }

    /**
     * @param entity 重置观鸟纪录
     */
    public void resetRecord(RecordEntity entity) {
        mRecord = new DbRecord(entity);
    }

    /**
     * 更新图片信息
     *
     * @param list 图片信息
     */
    public void updateImgList(List<RecordImage> list) {

        mRecord.getOriginImage().clear();
        imgItems.clear();

        if (null == list || list.isEmpty()) {
            mAdapter.notifyDataSetChanged();
            return;
        }

        imgItems.addAll(list);
        mRecord.setOriginImage(list);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 上传纪录成功
     */
    public void uploadSuccess() {

        fragment.toast("提交记录成功.");
        DraftDbService.getInstance().deleteRecord(mRecord.getRecord());

        clearInputData();

    }

    /**
     * 上传纪录成功
     */
    public void uploadError() {

        fragment.toast("提交记录失败。");
        //保存草稿箱
        saveToDraft();
        //重置数据
        clearInputData();

    }

    /**
     * 设置锁定值
     *
     * @param isLock 是否锁定
     */
    public void setLockState(boolean isLock) {


        //设置View状态
        fragment.setLockState(isLock);

        if (isLock) {

            //设置位置信息
            String strLocation = LocalSharePreferences.getValue(fragment.getContext(), LocalSharePreferences.KEY_LOCK_LOCATION, "");
            if (!TextUtils.isEmpty(strLocation)) {
                WbLocation location = new Gson().fromJson(strLocation, WbLocation.class);

                if (location != null) {
                    setLocationValue(location);
                }
            }

            //设置时间
            long time = LocalSharePreferences.getValue(fragment.getContext(), LocalSharePreferences.KEY_LOCK_TIME, mRecord.getTime());
            setTimeValue(time);
        }


    }

    /**
     * 保存锁定值
     *
     * @param isLock 是否锁定
     */
    public void saveLockValue(boolean isLock) {

        if (TextUtils.isEmpty(mRecord.getLocation())) {
            fragment.toast("地址为空,锁定失败");
            return;
        }

        fragment.setLockState(isLock);

        LocalSharePreferences.put(fragment.getContext(), LocalSharePreferences.KEY_LOCK_LOCATION_TIME, isLock);

        if (isLock) {
            LocalSharePreferences.put(fragment.getContext(), LocalSharePreferences.KEY_LOCK_TIME, mRecord.getTime());

            WbLocation location = getWbLocation();
            String strLocation = new Gson().toJson(location);

            LocalSharePreferences.put(fragment.getContext(), LocalSharePreferences.KEY_LOCK_LOCATION, strLocation);
        }
    }

    @NonNull
    private WbLocation getWbLocation() {
        WbLocation location = new WbLocation();
        location.setLatitude(mRecord.getLatitude());
        location.setLongitude(mRecord.getLongitude());
        location.setAltitude(mRecord.getAltitude());
        location.setCityCode(String.valueOf(mRecord.getCityID()));
        location.setCity(mRecord.getCityName());
        location.setAddress(mRecord.getLocation());
        return location;
    }

}
