package net.doyouhike.app.wildbird.ui;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.ui.adapter.MyArrayAdapter;
import net.doyouhike.app.wildbird.ui.fragment.ColorFragment;
import net.doyouhike.app.wildbird.ui.fragment.FeatureFragment;
import net.doyouhike.app.wildbird.ui.fragment.HabitFragment;
import net.doyouhike.app.wildbird.ui.fragment.ShapeFragment;
import net.doyouhike.app.wildbird.biz.model.ChooseBean;
import net.doyouhike.app.wildbird.biz.model.ChooseBean.ChooseListener;
import net.doyouhike.app.wildbird.biz.model.bean.ChoosedItem;
import net.doyouhike.app.wildbird.ui.base.BaseAppFragmentActivity;
import net.doyouhike.app.wildbird.util.ChooseBeanHelper;
import net.doyouhike.app.wildbird.ui.view.MyListView;
import net.doyouhike.app.wildbird.ui.view.TitleView;
import net.doyouhike.app.wildbird.ui.view.TitleView.ClickListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FilterActivity extends BaseAppFragmentActivity implements ClickListener,
        OnClickListener, OnItemClickListener, ChooseListener {

    private TitleView titleview;
    private LinearLayout choosedLind1, choosedlind2;
    private Button resetCond;
    private MyListView lvConditions, lvFeatureConds;

    private HashMap<String, ArrayList<String>> mFirstlindChoosedItems, mSecondChoosedItems;
    private ArrayList<ChoosedItem> mFirstlindChoosedItems_vi, mSecondChoosedItems_vi;
    private int length1, length2;

    private MyArrayAdapter adapter1, adapter2;
    private String[] conds = new String[]{"颜色", "体型", "习性", "身体\n特征"};
    private String[] features = new String[]{"头部", "颈背", "胸腹胁", "翅膀", "腰", "脚爪趾", "尾部"};

    private FragmentManager fm = null;
    private FragmentTransaction ft = null;

    private int pos = 0, index = 0;
    private ColorFragment color;
    private ShapeFragment shape;
    private HabitFragment habit;
    private FeatureFragment feature;
    private boolean isActivityCreated = false;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_filter;
    }

    @Override
    protected void initViewsAndEvents() {


        titleview = (TitleView) super.findViewById(R.id.titleview);
        choosedLind1 = (LinearLayout) super.findViewById(R.id.choose1);
        choosedlind2 = (LinearLayout) super.findViewById(R.id.choose2);
        resetCond = (Button) super.findViewById(R.id.resetCond);
        lvConditions = (MyListView) super.findViewById(R.id.conditions);
        lvFeatureConds = (MyListView) super.findViewById(R.id.featureConds);

        titleview.setListener(this);
        resetCond.setOnClickListener(this);
        lvConditions.setOnItemClickListener(this);
        lvFeatureConds.setOnItemClickListener(this);


        initChooseData();

        initChosenList(0);
        initChosenList(1);

        adapter1 = new MyArrayAdapter(this, lvConditions, conds, 0);
        lvConditions.setAdapter(adapter1);
        lvConditions.setSelection(0);
        adapter2 = new MyArrayAdapter(this, lvFeatureConds, features, 1);
        lvFeatureConds.setAdapter(adapter2);

        color = new ColorFragment();
        color.setListener(this);
        shape = new ShapeFragment();
        shape.setListener(this);
        habit = new HabitFragment();
        habit.setListener(this);
        feature = new FeatureFragment();
        feature.setListener(this);

        //首次加载身体特征fragment，因为没有加载的话，若已有选择记录，而执行水平滑动视图里的删除操作时，
        // 由于它的view视图没有加载，会导致报错,删除其他fragment里的内容暂时没发现问题
        //所以先加载身体特征fragment，在onResume  里再切换回color fragment
        try {
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();
            ft.add(R.id.condContainer, feature);
            ft.add(R.id.condContainer, habit);
            ft.add(R.id.condContainer, shape);
            ft.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        isActivityCreated = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isActivityCreated) {
            isActivityCreated = false;
            try {
                ft = fm.beginTransaction();
                ft.replace(R.id.condContainer, color);
                ft.commit();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        mFirstlindChoosedItems = null;
        mSecondChoosedItems = null;
        super.onDestroy();
    }


    /**
     * 删除条件时的操作
     *
     * @param key
     * @param value
     */
    protected void changeFragment(String key, String value) {
        // TODO Auto-generated method stub
        switch (key) {
            case "color":
                color.clear(value);
                break;
            case "shape":
                shape.clear(value);
                break;
            case "habit":
                habit.clear(value);
                break;
            case "头部":
                feature.clear(0, key, value);
                break;
            case "颈背":
                feature.clear(1, key, value);
                break;
            case "胸腹胁":
                feature.clear(2, key, value);
                break;
            case "翅膀":
                feature.clear(3, key, value);
                break;
            case "腰":
                feature.clear(4, key, value);
                break;
            case "脚爪趾":
                feature.clear(5, key, value);
                break;
            case "尾部":
                feature.clear(6, key, value);
                break;
        }
    }


    /**
     * 条件筛选栏布局
     *
     * @param key
     * @param text
     */
    @Override
    public void choose(String key, String text) {
        // TODO Auto-generated method stub
        text = text.replace("\n", "");

        ChoosedItem item = new ChoosedItem(key, text);

        addItems(item, mFirstlindChoosedItems);

        averageList();
    }

    /**
     * 重新适配已选择的item视图，若第一行大于第二行，则显示在第二行，若第二行大于第一行则添加显示在第一行
     */
    private void averageList() {
        // TODO Auto-generated method stub
        int fistLindSize, secondLindSize;

        fistLindSize = ChooseBeanHelper.getTotalItemSize(mFirstlindChoosedItems);
        secondLindSize = ChooseBeanHelper.getTotalItemSize(mSecondChoosedItems);

        if (length1 > length2) {
            while (fistLindSize - secondLindSize > 1) {


                ChoosedItem item = ChooseBeanHelper.getLastItem(mFirstlindChoosedItems);


                if (null != item) {
                    addItems(item, mSecondChoosedItems);
                    delItem(item, mFirstlindChoosedItems);
                }


                fistLindSize = ChooseBeanHelper.getTotalItemSize(mFirstlindChoosedItems);
                secondLindSize = ChooseBeanHelper.getTotalItemSize(mSecondChoosedItems);


            }
        } else if (length2 > length1) {

            while (secondLindSize - fistLindSize >= 1) {

                ChoosedItem item = ChooseBeanHelper.getLastItem(mSecondChoosedItems);


                if (null != item) {

                    addItems(item, mFirstlindChoosedItems);
                    delItem(item, mSecondChoosedItems);
                }

                fistLindSize = ChooseBeanHelper.getTotalItemSize(mFirstlindChoosedItems);
                secondLindSize = ChooseBeanHelper.getTotalItemSize(mSecondChoosedItems);
            }
        }
        choosedLind1.removeAllViews();
        choosedlind2.removeAllViews();
        initChosenList(0);
        initChosenList(1);
    }

    @Override
    public void delete(String key, String text) {
        // TODO Auto-generated method stub
        text = text.replace("\n", "");

        ChoosedItem item = new ChoosedItem(key, text);

        boolean isFistDelSuc = delItem(item, mFirstlindChoosedItems);

        //如果已经在第一行删除不成功，则在第二行删除
        if (!isFistDelSuc) {
            delItem(item, mSecondChoosedItems);

        }


        averageList();
    }


    /**
     * 重置
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        choosedLind1.removeAllViews();
        choosedlind2.removeAllViews();
        length1 = 0;
        length2 = 0;

        mFirstlindChoosedItems_vi.clear();
        mSecondChoosedItems_vi.clear();

        ChooseBean.getInstance(this).refresh();
        if (feature != null) feature.refresh();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
        switch (parent.getId()) {
            case R.id.conditions:
                adapter1.setCheck(pos, false);
                pos = position;
                adapter1.setCheck(pos, true);
                if (position == 3) {
                    lvFeatureConds.setVisibility(View.VISIBLE);
                } else {
                    lvFeatureConds.setVisibility(View.GONE);
                }
                switch (position) {
                    case 0:// color
                        ft = fm.beginTransaction();
                        ft.replace(R.id.condContainer, color);
                        ft.commit();
                        break;
                    case 1:// shape
                        ft = fm.beginTransaction();
                        ft.replace(R.id.condContainer, shape);
                        ft.commit();
                        break;
                    case 2:// habit
                        ft = fm.beginTransaction();
                        ft.replace(R.id.condContainer, habit);
                        ft.commit();
                        break;
                    case 3:// feature
                        ft = fm.beginTransaction();
                        ft.replace(R.id.condContainer, feature);
                        ft.commit();
                        adapter1.setCheck(pos, false);
                        break;
                }
                break;
            case R.id.featureConds:// 身体特征二级选项
                adapter2.setCheck(index, false);
                index = position;
                adapter2.setCheck(index, true);
                feature.click(position);
                break;
        }
    }

    @Override
    public void clickLeft() {
        // TODO Auto-generated method stub
        onBackPressed();
    }

    @Override
    public void clickRight() {
        // TODO Auto-generated method stub
        toast("筛选成功");
        ChooseBean.getInstance(this).makeSure();
        if (feature != null) feature.makeSure();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (feature != null) feature.init();
        ChooseBean.getInstance(getApplicationContext()).init();
        super.onBackPressed();
    }

    /**
     * 初始化选择框的视图
     *
     * @param index 0为第一行，1为第二行
     */
    private void initChosenList(final int index) {

        ArrayList<ChoosedItem> items;
        if (index == 0) {
            length1 = 0;
            items = mFirstlindChoosedItems_vi;
        } else {
            length2 = 0;
            items = mSecondChoosedItems_vi;
        }


        for (ChoosedItem item : items) {

            View view = View.inflate(this, R.layout.chosen_item, null);
            TextView chosen_text = (TextView) view.findViewById(R.id.chosen_text);
            chosen_text.setText(item.getContent());
            final ImageView image = (ImageView) view.findViewById(R.id.delete);
            image.setTag(item);
            image.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    ChoosedItem item = (ChoosedItem) v.getTag();
                    if (null == item)
                        return;

                    String tempKey = item.getKey();
                    String tempContent = item.getContent();

                    changeFragment(tempKey, tempContent);

                    if (index == 0) {
                        //属于第一行
                        delItem(item, mFirstlindChoosedItems);
                    } else {
                        delItem(item, mSecondChoosedItems);
                    }

                    averageList();
                }
            });

            if (index == 0) {
                //属于第一行
                choosedLind1.addView(view);
            } else {
                choosedlind2.addView(view);
            }
        }

    }

    private void initChooseData() {

        mFirstlindChoosedItems = ChooseBean.getInstance(this).getChoosedItems(0);
        mSecondChoosedItems = ChooseBean.getInstance(this).getChoosedItems(1);

        mFirstlindChoosedItems_vi = new ArrayList<>();
        mSecondChoosedItems_vi = new ArrayList<>();

        ChooseBeanHelper.setValues(mFirstlindChoosedItems, mFirstlindChoosedItems_vi);
        ChooseBeanHelper.setValues(mSecondChoosedItems, mSecondChoosedItems_vi);

    }


    private void addItems(ChoosedItem item, HashMap<String, ArrayList<String>> choosedItems) {

        ChooseBeanHelper.addItem(item, choosedItems);
        if (choosedItems.equals(mFirstlindChoosedItems)) {
            mFirstlindChoosedItems_vi.add(0, item);
            length1 += item.getContent().length();
        } else {
            mSecondChoosedItems_vi.add(0, item);
            length2 += item.getContent().length();
        }
    }

    private boolean delItem(ChoosedItem item, HashMap<String, ArrayList<String>> choosedItems) {

        boolean isDelSuc = ChooseBeanHelper.delItem(item, choosedItems);

        if (isDelSuc) {
            //
            if (choosedItems.equals(mFirstlindChoosedItems)) {

                if (mFirstlindChoosedItems_vi.contains(item)) {
                    mFirstlindChoosedItems_vi.remove(item);
                    length1 -= item.getContent().length();
                }
            } else {
                if (mSecondChoosedItems_vi.contains(item)) {
                    mSecondChoosedItems_vi.remove(item);
                    length2 -= item.getContent().length();
                }
            }


        }

        return isDelSuc;
    }

}
