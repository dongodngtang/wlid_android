package net.doyouhike.app.wildbird.ui.record;

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
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import net.doyouhike.app.wildbird.biz.model.bean.Species;
import net.doyouhike.app.wildbird.biz.model.bean.SpeciesInfo;
import net.doyouhike.app.wildbird.biz.model.bean.WbLocation;
import net.doyouhike.app.wildbird.biz.model.event.EventGetRecordDetail;
import net.doyouhike.app.wildbird.biz.model.event.EventUploadRecord;
import net.doyouhike.app.wildbird.biz.service.database.DraftDbService;
import net.doyouhike.app.wildbird.biz.service.net.GetRecordDetailService;
import net.doyouhike.app.wildbird.biz.service.net.UploadRecordService;
import net.doyouhike.app.wildbird.ui.ChooseBirdNameActivity;
import net.doyouhike.app.wildbird.ui.MultiChoosePhotoActivity;
import net.doyouhike.app.wildbird.ui.adapter.AddNewPicAdapter;
import net.doyouhike.app.wildbird.ui.adapter.AddNewPicAdapter.deletePhoto;
import net.doyouhike.app.wildbird.ui.base.BaseAppActivity;
import net.doyouhike.app.wildbird.ui.login.LoginActivity;
import net.doyouhike.app.wildbird.ui.main.user.mine.MeFragment;
import net.doyouhike.app.wildbird.ui.view.CitySelectDialog;
import net.doyouhike.app.wildbird.ui.view.MyAlertDialogUtil;
import net.doyouhike.app.wildbird.ui.view.MyAlertDialogUtil.DialogListener;
import net.doyouhike.app.wildbird.ui.view.MyPopupWindow;
import net.doyouhike.app.wildbird.ui.view.MyPopupWindow.SetAvatarListener;
import net.doyouhike.app.wildbird.ui.view.TitleView;
import net.doyouhike.app.wildbird.ui.view.TitleView.ClickListener;
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
 * 编辑或添加新的观鸟记录
 */
@SuppressWarnings("deprecation")
@SuppressLint("SimpleDateFormat")
public class AddNewRecordActivity extends BaseAppActivity
        implements ClickListener, OnClickListener, OnItemClickListener, SetAvatarListener
        , DialogListener, deletePhoto {

    private TitleView titleview;
    private GridView new_pic;
    private TextView new_name, tvAddress, new_time;
    /**
     * 手动添加城市
     */
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

    private boolean isEdit = false;// 是否属于编辑界面
    private boolean isSaved = false;// 防止重复保存记录到草稿箱
    private boolean isGetLocationFromSelected=false;
    private boolean isFromDraft = false;


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
    protected void initViewsAndEvents() {
        super.initViewsAndEvents();

        EventBus.getDefault().register(this);

        titleview = (TitleView) super.findViewById(R.id.titleview);
        new_pic = (GridView) super.findViewById(R.id.new_pic);
        tvAddress = (TextView) super.findViewById(R.id.new_addr);
        tvManualCity = (TextView) super.findViewById(R.id.tv_acti_add_record_address);
        new_time = (TextView) super.findViewById(R.id.new_time);
        new_name = (TextView) super.findViewById(R.id.new_name);
        new_count = (EditText) super.findViewById(R.id.new_count);
        new_record = (EditText) super.findViewById(R.id.new_record);

        titleview.setListener(this);
        new_pic.setOnItemClickListener(this);
        tvAddress.setOnClickListener(this);
        new_name.setOnClickListener(this);
        new_time.setOnClickListener(this);
        tvManualCity.setOnClickListener(this);

        list = new ArrayList<RecordImage>();
        adapter = new AddNewPicAdapter(this, list, 0);
        adapter.setListener(this);
//        new_pic.setAdapter(adapter);

        isEdit = getIntent().getBooleanExtra("isEdit", false);
        isFromDraft = getIntent().getBooleanExtra("draft", false);

        if (isEdit) {// 编辑记录
            entity = (RecordEntity) getIntent().getSerializableExtra("RecordEntity");
            speciesID = entity.getSpeciesID();
            new_name.setText(entity.getSpeciesName().replace("'", ""));
            new_count.setText(entity.getNumber() + "");
            new_record.setText(entity.getDescription());
            if (entity.getLocation() != null && !entity.getLocation().equals("") &&
                    !entity.getLocation().equals("NULL")) {
                tvAddress.setText(entity.getLocation());
            }
            /**
             * 草稿箱的记录编辑更新时间
             */
            if (isFromDraft) {
//                entity.setTime(System.currentTimeMillis() / 1000);
            } else {
                new_name.setEnabled(false);
            }
            Date date = new Date(entity.getTime() * 1000);
            String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
            new_time.setText(datetime);
            /**
             * 更新图片显示列表
             */
            list.addAll(entity.getList());
            if (list.size() < 5) list.add(new RecordImage());
            adapter.notifyDataSetChanged();
            /**
             * 如果是草稿箱记录则不用联网获取详情，否则获取。
             */
            if (!isFromDraft) {
                if (CheckNetWork.isNetworkAvailable(this)) {
                    mProgressDialog.showProgressDialog("正在获取鸟种记录，请稍候...");
                    GetRecordDetailService.getInstance().getRecordDetail(entity.getRecordID());
                } else {
                    toast("请设置好网络条件。");
                }
            }
        } else {// 新添加记录
            entity = new RecordEntity();

            list.add(new RecordImage());
            adapter.notifyDataSetChanged();

            entity.setTime(System.currentTimeMillis() / 1000);
            Date date = new Date(entity.getTime() * 1000);
            String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
            new_time.setText(datetime);
            /**
             * 如果为确定的鸟种添加记录，则不能再选择更换鸟种
             */
            if (getIntent().getBooleanExtra("birdInfo", false)) {
                if (getIntent().getBooleanExtra("fromMine", false)) {
                    SpeciesInfo info = (SpeciesInfo) getIntent().getSerializableExtra("speciesInfo");
                    new_name.setText(info.getLocalName().replace("'", ""));
                    speciesID = Integer.parseInt(info.getSpeciesID());
                } else {
                    Species species = (Species) getIntent().getSerializableExtra("species");
                    new_name.setText(species.getSpeciesName().replace("'", ""));
                    speciesID = species.getSpeciesID();
                }
                new_name.setEnabled(false);
            }
            initLocClient();
        }

        entity.setList(list);

    }

    /**
     * 定位初始化
     */
    private void initLocClient() {
        // 定位初始化
        LocationManager.getInstance().start(this);
    }


    /**
     * 获取鸟种详情
     *
     * @param result
     */
    public void onEventMainThread(EventGetRecordDetail result) {
        mProgressDialog.dismissDialog();
        if (result.isSuc()) {
            entity = new RecordEntity(result.getResult());
            handler.sendEmptyMessage(1);
        } else {
            toast(result.getStrErrMsg());
        }
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
        pop = MyPopupWindow.getInstance(this);
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


        if (!PermissionUtil.getInstance().checkStoragePermission(this)){
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


        if (!PermissionUtil.getInstance().checkStoragePermission(this)){
            return;
        }
        Intent intent = new Intent(this, MultiChoosePhotoActivity.class);
        ChooseImage image = new ChooseImage();
        image.setList(list);
        intent.putExtra(MultiChoosePhotoActivity.I_IMAGES, image);
        startActivityForResult(intent, 105);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.new_addr:
                startActivityForResult(new Intent(this, LocationActivity.class), 103);
                break;
            case R.id.new_time:
                if (datePickerDialog != null && datePickerDialog.isShowing() ||
                        timePickerDialog != null && timePickerDialog.isShowing()) return;
                final Calendar current_time = Calendar.getInstance();
                final Calendar calendar = Calendar.getInstance();
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                timePickerDialog = new TimePickerDialog(
                        AddNewRecordActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                // TODO Auto-generated method stub
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                entity.setTime(calendar.getTimeInMillis() / 1000);
                                new_time.setText(simpleDateFormat.format(calendar.getTime()));
                            }
                        },
                        current_time.get(Calendar.HOUR_OF_DAY),
                        current_time.get(Calendar.MINUTE), true);

                datePickerDialog = new DatePickerDialog(
                        AddNewRecordActivity.this,
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
                startActivityForResult(new Intent(this, ChooseBirdNameActivity.class), 104);
                break;
            case R.id.tv_acti_add_record_address:
                CitySelectDialog citySelelctDialog = new CitySelectDialog(this);
                citySelelctDialog.show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (pop != null && pop.isShowing()) pop.dismiss();
        else if (mProgressDialog.isDialogShowing()) mProgressDialog.dismissDialog();
        else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    ChooseImage image = (ChooseImage) data.getSerializableExtra(MultiChoosePhotoActivity.I_IMAGES);
                    ArrayList<Integer> delImgs=data.getIntegerArrayListExtra(MultiChoosePhotoActivity.I_DEL_IMG);


                    delImages.clear();
                    delImages.addAll(delImgs);

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
                Intent intent = new Intent(AddNewRecordActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case 3:// 保存至草稿箱
                saveToDraft();
                finish();
                break;
        }
    }

    /**
     * 保存到草稿箱
     */
    private void saveToDraft() {
        //已经保存，或者已经activity正在退出
        if (isSaved || isFinishing()) return;
        else isSaved = true;

        if (isFromDraft) {
            insertToDraft(1);// update
            toast("新记录已更新到草稿箱.");
        } else {
            insertToDraft(0);
            toast("新记录已保存到草稿箱.");
        }
    }

    /**
     * 编辑记录时记录需要删除的图片ID
     */
    @Override
    public void delete(int imageID) {
        // TODO Auto-generated method stub
        if (isEdit && !isFromDraft) {
            delImages.add(imageID);
        }
    }

    /**
     * 提交记录到服务器
     */
    @Override
    public void clickRight() {
        // TODO Auto-generated method stub
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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

        if (TextUtils.isEmpty(entity.getLocation())){
            toast(getString(R.string.tip_add_record_empty_city));
            return;
        }

        if (!CheckNetWork.isNetworkAvailable(getApplicationContext())) {
            MyAlertDialogUtil.showDialog(this,this, 0);
            return;
        }
        if (LocalSharePreferences.getValue(getApplicationContext(), Content.SP_USER_ID).equals("")) {
            MyAlertDialogUtil.showLoginDialog(this,this, 0);
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

        EventBus.getDefault().post(entity);

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

            if (isEdit && !isFromDraft) {
                LogUtil.d("info", "edit");
                Intent intent = new Intent(MeFragment.EDIT_SUCCESS);
                intent.putExtra("entity", entity);
                intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                sendBroadcast(intent);
            } else {
                LogUtil.d("info", "upload");
                Intent intent = new Intent(MeFragment.UPLOAD_SUCCESS);
                intent.putExtra("record", entity);
                intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                sendBroadcast(intent);
            }

        } else {
            toast(result.getStrErrMsg());
            saveToDraft();
        }
        this.finish();
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
        entity2.setNumber(Integer.parseInt(new_count.getText().toString().trim()));
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
            sendBroadcast(intent);
        }
    }

    /**
     * 定位返回处理
     */
    public void onEventMainThread(EventLocation event) {

        if (isEdit||isGetLocationFromSelected){
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

    private void setAddressView(boolean isGet){
        UiUtils.showView(tvManualCity, !isGet);
        UiUtils.showView(tvAddress,isGet);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        LocationManager.getInstance().stop();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
