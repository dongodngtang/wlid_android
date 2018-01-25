package net.doyouhike.app.wildbird.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.doyouhike.app.library.ui.adapterutil.CommonAdapter;
import net.doyouhike.app.library.ui.adapterutil.ViewHolder;
import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.bean.ItemEquipment;

import java.util.ArrayList;
import java.util.List;

/**
 * 装备适配器
 * Created by zengjiang on 16/6/12.
 */
public class SettingEquipmentAdapter extends CommonAdapter<ItemEquipment> {

    private final boolean isCanSelect;

    public SettingEquipmentAdapter(Context context, boolean isCanSelect, List<ItemEquipment> datas) {
        super(context, datas, R.layout.item_setting_equipment);
        this.isCanSelect = isCanSelect;
    }

    public List<ItemEquipment> getDatas() {
        return mDatas;
    }

    @Override
    public void convert(ViewHolder holder, final ItemEquipment itemEquipment) {


        final TextView content = holder.getView(R.id.tv_item_setting_equipment);
        content.setText(itemEquipment.getName());
        content.setSelected(itemEquipment.isSelected());

        if (isCanSelect) {

            content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    boolean isSelected = v.isSelected();


                    //最多选择两个
                    if (!isSelected
                            && isSelectedCountTransfinite()) {
                        Toast.makeText(v.getContext(), "最多选择两个", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    itemEquipment.setSelected(!isSelected);
                    v.setSelected(!isSelected);

                }
            });
        }
    }

    public String[] getSelectedItem() {
        List<String> selectedItem = new ArrayList<>();

        for (ItemEquipment item : getDatas()) {

            if (item.isSelected()) {
                selectedItem.add(item.getName());
            }

        }

        return selectedItem.toArray(new String[selectedItem.size()]);


    }


    public void setDefaultData(String[] defaultItems) {

        getDatas().clear();

        if (defaultItems == null || defaultItems.length == 0) {
            return;
        }

        for (String defaultItem : defaultItems) {
            //添加默认数据
            ItemEquipment equipment = new ItemEquipment(defaultItem);
            getDatas().add(equipment);
        }

    }


    public void resetData(String[] selectItems) {


        if (selectItems == null || selectItems.length == 0) {
            return;
        }

        //添加选中数据,设置状态为true 若选中数据有item, 而默认数据中没有此item , 追增item
        for (String selectItem : selectItems) {
            ItemEquipment equipment = new ItemEquipment(selectItem, true);

            if (getDatas().contains(equipment)) {

//                getDatas().remove(equipment);

                for (ItemEquipment item:getDatas()){

                    if (item.equals(equipment)){
                        item.setSelected(true);
                    }

                }



            }else {
                getDatas().add(equipment);
            }

        }
    }


    /**
     * @return 选中数量超限
     */
    public boolean isSelectedCountTransfinite() {

        int selectedCount = 0;
        for (ItemEquipment itemEquipment : mDatas) {
            if (itemEquipment.isSelected()) {
                selectedCount++;
            }

            if (selectedCount >= 2) {
                return true;
            }
        }


        return false;
    }
}
