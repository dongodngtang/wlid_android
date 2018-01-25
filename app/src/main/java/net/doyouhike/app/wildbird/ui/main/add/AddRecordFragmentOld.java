package net.doyouhike.app.wildbird.ui.main.add;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.TimePicker;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.Content;
import net.doyouhike.app.wildbird.biz.model.bean.ChooseImage;
import net.doyouhike.app.wildbird.biz.model.bean.CitySelectInfo;
import net.doyouhike.app.wildbird.biz.model.bean.RecordEntity;
import net.doyouhike.app.wildbird.biz.model.bean.RecordImage;
import net.doyouhike.app.wildbird.biz.model.bean.WbLocation;
import net.doyouhike.app.wildbird.biz.model.event.CheckoutPageEvent;
import net.doyouhike.app.wildbird.biz.model.event.EventUploadRecord;
import net.doyouhike.app.wildbird.biz.service.database.DraftDbService;
import net.doyouhike.app.wildbird.biz.service.net.UploadRecordService;
import net.doyouhike.app.wildbird.ui.ChooseBirdNameActivity;
import net.doyouhike.app.wildbird.ui.MultiChoosePhotoActivity;
import net.doyouhike.app.wildbird.ui.adapter.AddNewPicAdapter;
import net.doyouhike.app.wildbird.ui.base.BaseFragment;
import net.doyouhike.app.wildbird.ui.login.LoginActivity;
import net.doyouhike.app.wildbird.ui.main.user.mine.MeFragment;
import net.doyouhike.app.wildbird.ui.record.DraftActivity;
import net.doyouhike.app.wildbird.ui.record.LocationActivity;
import net.doyouhike.app.wildbird.ui.view.CitySelectDialog;
import net.doyouhike.app.wildbird.ui.view.MyAlertDialogUtil;
import net.doyouhike.app.wildbird.ui.view.MyPopupWindow;
import net.doyouhike.app.wildbird.ui.view.TitleView;
import net.doyouhike.app.wildbird.util.CheckNetWork;
import net.doyouhike.app.wildbird.util.LocalSharePreferences;
import net.doyouhike.app.wildbird.util.LogUtil;
import net.doyouhike.app.wildbird.util.PermissionUtil;
import net.doyouhike.app.wildbird.util.UiUtils;
import net.doyouhike.app.wildbird.util.location.EnumLocation;
import net.doyouhike.app.wildbird.util.location.EventLocation;
import net.doyouhike.app.wildbird.util.location.LocationManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * 功能：添加新的观鸟记录
 *
 * @author：曾江 日期：16-4-7.
 */
public class AddRecordFragmentOld extends BaseFragment
        implements TitleView.ClickListener, View.OnClickListener, AdapterView.OnItemClickListener, MyPopupWindow.SetAvatarListener
        , MyAlertDialogUtil.DialogListener, AddNewPicAdapter.deletePhoto {

    private TitleView titleview;
    private GridView new_pic;
    private TextView new_name, tvAddress, new_time;
    /**
     * 手动添加城市
     */
    private boolean isGetLocationFromSelected=false;
    private TextView tvManualCity;
    private EditText new_count, new_record;
    private AddNewPicAdapter adapter;
    private List<RecordImage> list;
    private List<Integer> delImages = new ArrayList<Integer>();
    private String url = "";
    private MyPopupWindow pop;
    private int pos = 0;
    private String filePath = "";
    private int speciesID = 0;
    private RecordEntity entity;

    private boolean isSaved = false;// 防止重复保存记录到草稿箱


    private DatePickerDialog datePickerDialog = null;
    private TimePickerDialog timePickerDialog = null;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    list.clear();
                    list.addAll(entity.getList());
                    if (list.size() < 5)
                        list.add(new RecordImage());
                    adapter.notifyDataSetChanged();
                    if (entity.getDescription() != null && !entity.getDescription().equals("NULL")) {
                        new_record.setText(entity.getDescription());
                    }
                    break;
            }
        }

        ;
    };

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.addnewrecord;
    }
    @Override
    public void onStart() {
        super.onStart();
        initLocClient();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Override
    protected void initViewsAndEvents() {
        super.initViewsAndEvents();


        titleview = (TitleView) getView().findViewById(R.id.titleview);
        new_pic = (GridView) getView().findViewById(R.id.new_pic);
        tvAddress = (TextView) getView().findViewById(R.id.new_addr);
        tvManualCity = (TextView) getView().findViewById(R.id.tv_acti_add_record_address);
        new_time = (TextView) getView().findViewById(R.id.new_time);
        new_name = (TextView) getView().findViewById(R.id.new_name);
        new_count = (EditText) getView().findViewById(R.id.new_count);
        new_record = (EditText) getView().findViewById(R.id.new_record);

        titleview.setListener(this);
        new_pic.setOnItemClickListener(this);
        tvAddress.setOnClickListener(this);
        new_name.setOnClickListener(this);
        new_time.setOnClickListener(this);
        tvManualCity.setOnClickListener(this);

        list = new ArrayList<RecordImage>();
        adapter = new AddNewPicAdapter(getActivity(), list, 0);
        adapter.setListener(this);
//        new_pic.setAdapter(adapter);


        // 新添加记录
        entity = new RecordEntity();

        list.add(new RecordImage());
        adapter.notifyDataSetChanged();

        entity.setTime(System.currentTimeMillis() / 1000);
        Date date = new Date(entity.getTime() * 1000);
        String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
        new_time.setText(datetime);


        entity.setList(list);
    }

    /**
     * 定位初始化
     */
    private void initLocClient() {
        // 定位初始化
        LocationManager.getInstance().start(mContext);
    }


    /**
     * 选择城市对话框的响应
     *
     * @param csInfo
     */
    public void onEventMainThread(CitySelectInfo csInfo) {

        isGetLocationFromSelected=true;
        setCityTv(csInfo.getProvinceName(), csInfo.getCityName());


        entity.setCityName(csInfo.getCityName());

        entity.setCityID(csInfo.getCityId());

    }

    private void setCityTv(String provinceName, String cityName) {

        String content;
        content = provinceName + cityName;
        //剔除像北京.北京这样的情况
        if (!TextUtils.isEmpty(provinceName) && !TextUtils.isEmpty(cityName)) {
            if (provinceName.equals(cityName)) {
                content = provinceName;
            }
        }
        entity.setLocation(content);
        tvManualCity.setText(content);
    }

    /**
     * 选择图片
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
        pop = MyPopupWindow.getInstance(mContext);
        pop.setListener(this);
        pop.setAnimationStyle(R.style.GrowFromBottom);
        pop.showAtLocation(titleview, Gravity.CENTER, 0, 0);
        pos = position;
    }

    /**
     * 打开相机
     */
    @Override
    public void startCamera() {
        // TODO Auto-generated method stub
        if (pop != null) pop.dismiss();


        if (!PermissionUtil.getInstance().checkStoragePermission(getActivity())) {
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
        startActivityForResult(intent, 102);
    }

    /**
     * 打开相册
     */
    @Override
    public void startGallery() {
        // TODO Auto-generated method stub
        if (pop != null) pop.dismiss();


        if (!PermissionUtil.getInstance().checkStoragePermission(getActivity())) {
            return;
        }
        Intent intent = new Intent(mContext, MultiChoosePhotoActivity.class);
        ChooseImage image = new ChooseImage();
        image.setList(list);
        intent.putExtra("images", image);
        startActivityForResult(intent, 105);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.new_addr:
                startActivityForResult(new Intent(mContext, LocationActivity.class), 103);
                break;
            case R.id.new_time:
                if (datePickerDialog != null && datePickerDialog.isShowing() ||
                        timePickerDialog != null && timePickerDialog.isShowing()) return;
                final Calendar current_time = Calendar.getInstance();
                final Calendar calendar = Calendar.getInstance();
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                timePickerDialog = new TimePickerDialog(
                        mContext,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

//                                if (calendar.get(Calendar.YEAR)==current_time.get(Calendar.YEAR)&&
//                                        calendar.get(Calendar.DAY_OF_YEAR)==current_time.get(Calendar.DAY_OF_YEAR)){
//
//                                }

                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                entity.setTime(calendar.getTimeInMillis() / 1000);
                                new_time.setText(simpleDateFormat.format(calendar.getTime()));
                            }
                        },
                        current_time.get(Calendar.HOUR_OF_DAY),
                        current_time.get(Calendar.MINUTE), true);

                datePickerDialog = new DatePickerDialog(
                        mContext,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                if (year > current_time.get(Calendar.YEAR)) {
                                    toast("观测时间不能超过当前时间");
                                    return;
                                } else if (year == current_time.get(Calendar.YEAR)) {
                                    if (monthOfYear > current_time.get(Calendar.MONTH)) {
                                        toast("观测时间不能超过当前时间");
                                        return;
                                    } else if (monthOfYear == current_time.get(Calendar.MONTH)) {
                                        if (dayOfMonth > current_time.get(Calendar.DAY_OF_MONTH)) {
                                            toast("观测时间不能超过当前时间");
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
                break;
            case R.id.new_name:
                startActivityForResult(new Intent(mContext, ChooseBirdNameActivity.class), 104);
                break;
            case R.id.tv_acti_add_record_address:
                CitySelectDialog citySelelctDialog = new CitySelectDialog(mContext);
                citySelelctDialog.show();
                break;
        }
    }
//
//    @Override
//    public void onBackPressed() {
//        // TODO Auto-generated method stub
//        if (pop != null && pop.isShowing()) pop.dismiss();
//        else if (mProgressDialog.isDialogShowing()) mProgressDialog.dismissDialog();
//
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 102:// 拍照获取图片
                    File file = new File(filePath);
                    Uri uri2 = Uri.fromFile(file);
                    list.get(pos).setImageUri(uri2.toString());
                    if (list.size() < 5 && pos == list.size() - 1)
                        list.add(new RecordImage());
                    adapter.notifyDataSetChanged();
                    break;
                case 103:// 编辑地图获取地址信息
                    if (data == null) return;
                    isGetLocationFromSelected=true;
                    LocationManager.getInstance().stop();
                    entity.setCityName(data.getStringExtra("city"));
                    entity.setCityID(data.getIntExtra("cityID", 0));
                    entity.setLocation(data.getStringExtra("location"));
                    entity.setLatitude(data.getDoubleExtra("latitude", 0.000000));
                    entity.setLongitude(data.getDoubleExtra("longtitude", 0.000000));
                    entity.setAltitude(data.getDoubleExtra("altitude", 0.000000));

                    if (entity.getLocation() != null && !entity.getLocation().equals("")) {
                        tvAddress.setText(entity.getLocation());
                    }
                    break;
                case 104:// 选择鸟种名和鸟种ID
                    new_name.setText(data.getStringExtra("name"));
                    new_name.setTextColor(Color.BLACK);
                    speciesID = Integer.parseInt(data.getStringExtra("speciesID"));
                    break;
                case 105:// 从相册选择图片返回
                    ChooseImage image = (ChooseImage) data.getSerializableExtra("images");
                    list.clear();
                    list.addAll(image.getList());
                    if (list.size() < 5)
                        list.add(new RecordImage());
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    /**
     * 返回
     */
    @Override
    public void clickLeft() {
        // TODO Auto-generated method stub
        finish();
    }

    /**
     * 弹窗选项
     */
    @Override
    public void dialogClick(int pos) {
        // TODO Auto-generated method stub
        switch (pos) {
            case 0:// 打开系统设置
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                break;
            case 1:// 保存至草稿箱
                saveToDraft();
                finish();
                break;
            case 2:// 立即登录
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                break;
            case 3:// 保存至草稿箱
                saveToDraft();
                finish();
                break;
        }
    }

    /**
     * 清理工作,发送失败或成功的清理工作
     */
    private void finish() {
       tvManualCity.setText(null);
         new_count.setText(null);
        new_name.setText(null);
        new_record.setText(null);
        list.clear();

        delImages = new ArrayList<Integer>();

         url = "";
        int pos = 0;
         filePath = "";
         int speciesID = 0;
        isSaved = false;// 防止重复保存记录到草稿箱
        isGetLocationFromSelected=false;

        list = new ArrayList<RecordImage>();
        adapter = new AddNewPicAdapter(getActivity(), list, 0);
        adapter.setListener(this);
//        new_pic.setAdapter(adapter);


        // 新添加记录
        entity = new RecordEntity();

        list.add(new RecordImage());
        adapter.notifyDataSetChanged();

        entity.setTime(System.currentTimeMillis() / 1000);
        Date date = new Date(entity.getTime() * 1000);
        String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
        new_time.setText(datetime);

        initLocClient();

        entity.setList(list);

        //切换到我的标签页
        EventBus.getDefault().post(new CheckoutPageEvent());
    }

    /**
     * 保存到草稿箱
     */
    private void saveToDraft() {
        //已经保存，或者已经activity正在退出
        if (isSaved) return;
        else isSaved = true;

        insertToDraft(0);
        toast("新记录已保存到草稿箱.");

    }

    /**
     * 编辑记录时记录需要删除的图片ID
     */
    @Override
    public void delete(int imageID) {

    }

    /**
     * 提交记录到服务器
     */
    @Override
    public void clickRight() {
        // TODO Auto-generated method stub
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(new_count.getWindowToken(), 0);
        if (new_name.getText().toString().equals("")) {

            toast("请输入鸟种名称。");
            return;
        }
        if (new_count.length() <= 0 || new_count.length() > 5) {

            toast("请输入鸟种数量必须大于零，且数量最大不能超过5位");
            return;
        } else {
            if (Integer.parseInt(new_count.getText().toString().trim()) <= 0) {

                toast("请输入鸟种数量必须大于零，且数量最大不能超过5位");
                return;
            }
        }
        if (new_record.length() >= 300) {

            toast("笔记只能输入少于300字的内容。");
            return;
        }

        if (TextUtils.isEmpty(entity.getLocation())) {
            toast(getString(R.string.tip_add_record_empty_city));
            return;
        }

        if (!CheckNetWork.isNetworkAvailable(mContext)) {
            MyAlertDialogUtil.showDialog(this,getActivity(), 0);
            return;
        }
        if (LocalSharePreferences.getValue(mContext, Content.SP_USER_ID).equals("")) {
            MyAlertDialogUtil.showLoginDialog(this, getActivity(), 0);
            return;
        }
        save();
    }

    /**
     * 提交保存记录
     */
    private void save() {

        mProgressDialog.showProgressDialog("正在保存记录，请稍后...");


        System.gc();

        entity.setList(list);
        entity.setDelImages(delImages);
        entity.setSpeciesID(speciesID);
        entity.setSpeciesName(new_name.getText().toString().trim());
        entity.setNumber(Integer.valueOf(new_count.getText().toString().trim()));
        entity.setDescription(new_record.getText().toString().trim());


        int recordId = entity.getRecordID();

//        if (recordId > 0) {
//            UploadRecordService.getInstance().modifyRecord(entity);
//        } else {
//            UploadRecordService.getInstance().publishBirdRecord(entity);
//        }

    }


    /**
     * 上传观鸟记录
     *
     * @param result
     */
    public void onEventMainThread(EventUploadRecord result) {
        mProgressDialog.dismissDialog();
        if (result.isSuc()) {


            toast("提交记录成功.");
            String nodeId = result.getNode_id();
            if (!TextUtils.isEmpty(nodeId))
                entity.setRecordID(Integer.parseInt(nodeId));


            if (list.size() > 0 && !list.get(0).getImageUri().equals("")) {
                entity.setHasImage(1);
            } else {
                entity.setHasImage(0);
            }

            LogUtil.d("info", "upload");
            Intent intent = new Intent(MeFragment.UPLOAD_SUCCESS);
            intent.putExtra("record", entity);
            intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            mContext.sendBroadcast(intent);

        } else {
            toast(result.getStrErrMsg());
            saveToDraft();
        }
        finish();
    }

    /**
     * 保存到草稿箱
     *
     * @param isUpdate
     */
    private void insertToDraft(int isUpdate) {
        // TODO Auto-generated method stub
        RecordEntity entity2 = new RecordEntity();
        entity2.setSpeciesID(speciesID);
        entity2.setRecord(entity.getRecord());
        entity2.setRecordID(entity.getRecordID());
        entity2.setLatitude(entity.getLatitude());
        entity2.setAltitude(entity.getAltitude());
        entity2.setLongitude(entity.getLongitude());
        entity2.setCityID(entity.getCityID());
        entity2.setCityName(entity.getCityName());
        entity2.setLocation(entity.getLocation());
        entity2.setTime(entity.getTime());
        entity2.setSpeciesName(new_name.getText().toString().trim());
        String count=new_count.getText().toString().trim();
        if (!TextUtils.isEmpty(count)){
            entity2.setNumber(Integer.parseInt(count));
        }
        entity2.setDescription(new_record.getText().toString().trim());

        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).getImageUri().equals("")) {
                entity2.setHasImage(1);
                RecordImage image = new RecordImage();
                image.setImageUri(list.get(i).getImageUri());
                image.setImageID(list.get(i).getImageID());
                entity2.addUri(image);
            }
        }
        if (delImages.size() > 0) {
            for (int i = 0; i < delImages.size(); i++) {
                entity2.addDelImage(delImages.get(i));
            }
        }

        if (isUpdate == 0) {
            //属于插入数据
            DraftDbService.getInstance().insertRecord(entity2);
        } else {
            //属于编辑数据
            DraftDbService.getInstance().updateRecord(entity2);
        }

        if (isUpdate == 1) {
            Intent intent = new Intent(DraftActivity.EDIT_BACK);
            intent.putExtra("entity", entity2);
            intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            mContext.sendBroadcast(intent);
        }
    }

    /**
     * 定位返回处理
     */
    public void onEventMainThread(EventLocation event) {

        if (isGetLocationFromSelected){
            return;
        }


        setAddressView(!event.getLocation().getCity().equals(LocationManager.DEFAULT_CITY));

        EnumLocation enumLocation = event.getEnumLocation();


        switch (enumLocation) {
            case FAIL:
//                toast(event.getMsg());
                break;
            case PREVENT:
                toast(event.getMsg());
                break;
            case SUCCESS:
                WbLocation location = event.getLocation();
                entity.setLatitude(location.getLatitude());
                entity.setLongitude(location.getLongitude());
                entity.setAltitude(location.getAltitude());
                entity.setCityName(location.getCity());
                entity.setLocation(location.getAddress());

                String cityCode = location.getCityCode();
                if (!TextUtils.isEmpty(cityCode)) {
                    entity.setCityID(Integer.parseInt(location.getCityCode()));
                }

                tvAddress.setText(location.getAddress());
                break;
        }

    }

    private void setAddressView(boolean isGet) {
        UiUtils.showView(tvManualCity, !isGet);
        UiUtils.showView(tvAddress, isGet);
    }

    @Override
    public void onDestroy() {
        LocationManager.getInstance().stop();
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
