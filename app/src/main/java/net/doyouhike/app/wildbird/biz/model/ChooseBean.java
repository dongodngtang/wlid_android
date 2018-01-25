package net.doyouhike.app.wildbird.biz.model;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import net.doyouhike.app.wildbird.ui.adapter.ColorAdapter;
import net.doyouhike.app.wildbird.ui.adapter.HabitAdapter;
import net.doyouhike.app.wildbird.util.ChooseBeanHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChooseBean {

    private static ChooseBean entity = null;

    /**
     * 单例
     *
     * @return
     */
    public static ChooseBean getInstance(Context context) {
        if (entity == null) {
            synchronized (ChooseBean.class) {
                if (entity == null) {
                    entity = new ChooseBean();
                }
            }
        }
        return entity;
    }

    public ChooseBean() {
        // TODO Auto-generated constructor stub
//        list.add(scene);
        list.add(newlocate);
//        list.add(done);
        list.add(head);
        list.add(neck);
        list.add(belly);
        list.add(wing);
        list.add(waist);
        list.add(leg);
        list.add(tail);
    }

    public interface ChooseListener {
        public void choose(String from, String text);

        public void delete(String from, String text);
    }
    //已选择的记录，总共有两组，第0组和第1组
    //第1组是内容时所属分类
    //第1组是已经选择的内容
//    private List<List<String>> list1_c = new ArrayList<List<String>>();
//    private List<List<String>> list2_c = new ArrayList<List<String>>();
    private HashMap<String,ArrayList<String>> mFirstlindChoosedItems =new HashMap<>();
    private HashMap<String,ArrayList<String>> mSecondlindChoosedItems =new HashMap<>();



    public HashMap<String,ArrayList<String>> getChoosedItems(int pos) {

        if (pos == 0) {
            return mFirstlindChoosedItems;
        }

        return mSecondlindChoosedItems;
    }

    private boolean isChosen = false;
    private ColorAdapter color_adapter, shape_adapter;
    //习性和身体特征的筛选条件,共8个
    private HabitAdapter[] adapter = new HabitAdapter[8];
    private List<String[]> list = new ArrayList<String[]>();
//    private String[] scene = new String[]{"公园", "草地", "电线上", "水里", "泥滩", "高空",
//            "农田", "高原", "戈壁沙漠", "树上", "住宅区", "湿地", "山区", "灌丛"};
    private String[] newlocate = new String[]{"苔原", "高山区", "针叶林", "阔叶林", "热带雨林", "草原",
            "戈壁/荒漠", "湖泊/池塘/河流/溪流/沼泽", "海滨/滩涂", "海洋", "草地/农田", "林冠层", "林中层", "林下/灌丛", "树干"};
//    private String[] done = new String[]{"摇尾巴", "盘旋", "啄木头", "悬停", "吸食花蜜", "游泳",
//            "夜间出现", "吃种子", "吃鱼", "吃虫子"};
    private String[] head = new String[]{"有冠羽", "有眉纹", "有贯眼纹", "耳羽有斑",
            "喉部黑色", "钩形嘴", "有眼圈", "嘴下弯", "嘴上翘", "细短嘴",
            "锥形嘴", "嘴红色", "嘴黄色", "头顶红色", "喉中线", "贯顶纹"};
    private String[] neck = new String[]{"白色领环", "黑色领环", "颈侧有斑",
            "颈背颜色\n对比明显"};
    private String[] belly = new String[]{"胸有黑斑", "胸有红斑", "胸腹部\n有纵纹",
            "胁部\n有横斑", "胁部橙色", "胁部\n有纵纹"};
    private String[] wing = new String[]{"有腕斑", "有翅斑", "翅尖黑色"};
    private String[] waist = new String[]{"白腰", "红腰", "黄腰"};
    private String[] leg = new String[]{"腿红色", "爪黄色", "腿黄绿色", "后趾特长", "有蹼"};
    private String[] tail = new String[]{"臀羽红色", "臀羽黄色", "外侧尾\n羽白色", "外侧尾羽\n有白斑",
            "尾部\n有横斑", "扇形尾", "菱形尾", "楔形尾", "燕型尾", "凹型尾"};

    public ColorAdapter getColorAdapter(FragmentActivity activity, int from) {
        // TODO Auto-generated method stub
        switch (from) {
            case 0:
                if (color_adapter == null) color_adapter = new ColorAdapter(activity, from);
                return color_adapter;
            case 1:
                if (shape_adapter == null) shape_adapter = new ColorAdapter(activity, from);
                return shape_adapter;
        }
        return null;
    }

    /**
     * @param activity
     * @param from
     * @param id
     * @return 身体特征适配器
     */
    public HabitAdapter getBodyAdapter(FragmentActivity activity, int from, int id) {
        // TODO Auto-generated method stub
        if (adapter[id] == null) adapter[id] = new HabitAdapter(activity, list.get(id), from);
        return adapter[id];
    }

    /**
     * @param activity
     * @param from
     * @param id
     * @return 习性适配器
     */
    public HabitAdapter getHabitAdapter(FragmentActivity activity, int from, int id) {
        // TODO Auto-generated method stub
        if (adapter[id] == null) adapter[id] = new HabitAdapter(activity, list.get(id), from);
        return adapter[id];
    }

    /**
     * 是否有筛选条件
     */
    public boolean isChosen() {
        return isChosen;
    }

    /**
     * 获取选择的条件数
     */
//    public int getCount() {
//        if (list1_c.size() == 0 || list2_c.size() == 0) return 0;
//        return list1_c.get(1).size() + list2_c.get(1).size();
//    }
   /**
     * 获取选择的条件数
     */
    public int getCount() {

        int fistLindChoosedCount= ChooseBeanHelper.getTotalItemSize(mFirstlindChoosedItems);
        int secondLindChoosedCount= ChooseBeanHelper.getTotalItemSize(mSecondlindChoosedItems);

        return fistLindChoosedCount+secondLindChoosedCount;

    }

    /**
     * 获取选择的条件
     */
    public String[] getSelect() {
        isChosen = false;
        String[] sel = new String[11];
        sel[0] = "%";
        if (color_adapter != null) {
            //颜色筛选条件
            sel[1] = color_adapter.getSelect();
            if (!sel[1].equals("%")) isChosen = true;
        } else sel[1] = "%";
        if (shape_adapter != null) {
            //体型筛选条件
            sel[2] = shape_adapter.getSelect();
            if (!sel[2].equals("%")) isChosen = true;
        } else sel[2] = "%";
        for (int i = 0, j = 3; i < adapter.length; i++, j++) {
            if (adapter[i] != null) {
                sel[j] = adapter[i].getSelect();
                if (!sel[j].equals("%")) isChosen = true;
            } else sel[j] = "%";
        }
        return sel;
    }

    /**
     * 确认该次的选项
     *
     */
    public void makeSure() {
        // TODO Auto-generated method stub

        if (color_adapter != null) color_adapter.makeSure();

        if (shape_adapter != null) shape_adapter.makeSure();
        for (int i = 0; i < adapter.length; i++) {
            if (adapter[i] != null) adapter[i].makeSure();
        }
    }

    /**
     * 还原上一次的选项
     *
     */
    public void init() {
        // TODO Auto-generated method stub
        if (color_adapter != null) color_adapter.init();
        if (shape_adapter != null) shape_adapter.init();
        for (int i = 0; i < adapter.length; i++) {
            if (adapter[i] != null) adapter[i].init();
        }
    }

    /**
     * 初始化选项
     */
    public void refresh() {
        // TODO Auto-generated method stub

        mFirstlindChoosedItems.clear();
        mSecondlindChoosedItems.clear();

        if (color_adapter != null) {
            color_adapter.refresh(1);
            color_adapter.notifyDataSetChanged();
        }
        if (shape_adapter != null) {
            shape_adapter.refresh(1);
            shape_adapter.notifyDataSetChanged();
        }
        for (int i = 0; i < adapter.length; i++) {
            if (adapter[i] != null) {
                adapter[i].refresh(1);
                adapter[i].notifyDataSetChanged();
            }
        }
    }




}
