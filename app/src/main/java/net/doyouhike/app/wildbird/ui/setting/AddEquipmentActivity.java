package net.doyouhike.app.wildbird.ui.setting;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.bean.ItemEquipment;
import net.doyouhike.app.wildbird.ui.adapter.SettingEquipmentAdapter;
import net.doyouhike.app.wildbird.ui.base.BaseAppActivity;
import net.doyouhike.app.wildbird.ui.view.MyGridView;
import net.doyouhike.app.wildbird.ui.view.TitleView;

import java.util.ArrayList;

import butterknife.InjectView;


/**
 * 添加装备
 */
public class AddEquipmentActivity extends BaseAppActivity implements View.OnClickListener {


    /**
     * 装备传参
     */
    public static final String I_SELECTED_EQUIPMENT = "param1";
    /**
     * 选中相机还是选中望远镜
     */
    public static final String I_SELECTED_FROM = "param2";


    /**
     * 输入装备
     */
    @InjectView(R.id.edt_setting_add_equipment)
    EditText edtSettingAddEquipment;
    /**
     * 添加按钮
     */
    @InjectView(R.id.btn_setting_add_equipment)
    Button btnSettingAddEquipment;
    /**
     * 装备列表
     */
    @InjectView(R.id.gv_setting_equipment)
    MyGridView gvSettingEquipment;

    /**
     * 标题栏
     */
    @InjectView(R.id.titleview)
    TitleView titleview;


    /**
     * 默认装备
     */
    String[] mDefaultCamera = new String[]{"CANON/佳能", "NIKON/尼康", "PENTAX/宾得", "SONY/索尼", "LEICA/莱卡", "OLYMPUS/奥林巴斯", "OTHER/其它"};

    String[] mDefaultTelescope = new String[]{"BOSMA/博冠", "SWAROVSKI/施华洛世奇", "ZEISS/蔡司", "LEICA/莱卡", "NIKON/尼康"
            , "STEINER/视得乐", "MEOPTA/米奥特", "FUJINON/富士", "MINOX/美乐时", "OLYMPUS/奥林巴斯", "CELESTRON/星特朗",
            "KOWA/兴和", "OTHER/其它"};
    String[] mDefaultEquipment;

    /**
     * 装备列表适配器
     */
    SettingEquipmentAdapter mEquipmentAdapter;
    /**
     * 已经选择的装备
     */
    String[] mSelectedEquipment;
    boolean isSelectCamera;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_add_equipment;
    }

    @Override
    protected void initViewsAndEvents() {
        mSelectedEquipment = getIntent().getStringArrayExtra(I_SELECTED_EQUIPMENT);
        mEquipmentAdapter = new SettingEquipmentAdapter(this, true, new ArrayList<ItemEquipment>());

        isSelectCamera = getIntent().getBooleanExtra(I_SELECTED_FROM,true);
        mDefaultEquipment=isSelectCamera?mDefaultCamera:mDefaultTelescope;

        titleview.setTitle(isSelectCamera?"相机":"望远镜");

        mEquipmentAdapter.setDefaultData(mDefaultEquipment);
        mEquipmentAdapter.resetData(mSelectedEquipment);
        gvSettingEquipment.setAdapter(mEquipmentAdapter);
        btnSettingAddEquipment.setOnClickListener(this);

        titleview.setListener(new TitleView.ClickListener() {
            @Override
            public void clickLeft() {
                //返回选中结果
                setEquipmentResult();
            }

            @Override
            public void clickRight() {

            }
        });
    }

    @Override
    public void onBackPressed() {
        //返回选中结果
        setEquipmentResult();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_setting_add_equipment:
                if (isInputValue()) {
                    addValue(edtSettingAddEquipment.getText().toString());
                }
                break;
        }
    }

    /**
     * 设置装备编辑结果
     */
    private void setEquipmentResult() {
        Intent intent = new Intent();
        intent.putExtra(I_SELECTED_EQUIPMENT, mEquipmentAdapter.getSelectedItem());
        setResult(RESULT_OK, intent);
        AddEquipmentActivity.this.finish();
    }

    /**
     * 添加装备
     *
     * @param s
     */
    private void addValue(String s) {
        ItemEquipment equipment = new ItemEquipment(s, true);


        if (mEquipmentAdapter.getDatas().contains(equipment)) {

            for (ItemEquipment item : mEquipmentAdapter.getDatas()) {
                if (item.equals(equipment)) {
                    item.setSelected(true);
                    break;
                }
            }
        } else {
            mEquipmentAdapter.getDatas().add(equipment);
        }

        edtSettingAddEquipment.setText(null);
        mEquipmentAdapter.notifyDataSetChanged();

    }

    /**
     * @return 输入是否有效
     */
    public boolean isInputValue() {
        return !TextUtils.isEmpty(edtSettingAddEquipment.getText());
    }
}
