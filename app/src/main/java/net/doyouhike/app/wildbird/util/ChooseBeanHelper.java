package net.doyouhike.app.wildbird.util;

import net.doyouhike.app.wildbird.biz.model.bean.ChoosedItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zaitu on 15-11-12.
 */
public class ChooseBeanHelper {
    /**
     * 获取被选择条码的数量
     *
     * @param choosedItems 已选的
     * @return
     */
    public static int getTotalItemSize(HashMap<String, ArrayList<String>> choosedItems) {

        int count = 0;

        Iterator iterator = choosedItems.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ArrayList<String>> entry = (Map.Entry<String, ArrayList<String>>) iterator.next();

            ArrayList<String> items = entry.getValue();
            if (null != items) {
                count = count + items.size();
            }
        }
        return count;
    }


    public static void addItem(ChoosedItem item, HashMap<String, ArrayList<String>> choosedItems) {


        String key=item.getKey();
        String content=item.getContent();


        ArrayList<String> subItems;

        //如果当前记录已经记录了该类别,类别：color/shape/habit/head...
        if (choosedItems.containsKey(key)) {

            subItems = choosedItems.get(key);

            if (null == subItems) {
                subItems = new ArrayList<>();
                subItems.add(0, content);
                choosedItems.put(key, subItems);
            } else {
                choosedItems.get(key).add(0, content);
            }

        } else {

            //  没有记录该类别则添加一个新的类别
            subItems = new ArrayList<>();
            subItems.add(0, content);
            choosedItems.put(key, subItems);

        }
    }


    public static boolean delItem(ChoosedItem item,HashMap<String, ArrayList<String>> choosedItems) {

        String key=item.getKey();
        String content=item.getContent();

        //如果包含类别
        if (choosedItems.containsKey(key)) {

            ArrayList<String> items = choosedItems.get(key);
            //如果已选的子条目不为空，且子条目包含要删除的字符值
            if (null != items && items.contains(content)) {
                items.remove(content);
                return true;
            }
        }


        return false;
    }


    public static ChoosedItem getLastItem(HashMap<String, ArrayList<String>> choosedItems) {
        //如果包含类别

        Iterator iterator = choosedItems.entrySet().iterator();

        String key=null;

        while (iterator.hasNext()){

            Map.Entry<String, ArrayList<String>> entry = (Map.Entry<String, ArrayList<String>>) iterator.next();

            ArrayList<String> items = entry.getValue();
            if (null != items&&!items.isEmpty()) {
                key=entry.getKey();
            }
        }

        if (null!=key){
            ArrayList<String> items = choosedItems.get(key);
            String content=items.get(items.size()-1);
            return new ChoosedItem(key,content);
        }


        return null;
    }

    public static void setValues(HashMap<String, ArrayList<String>> choosedItems,ArrayList<ChoosedItem> viewItems){

        if (null==viewItems){
            return;
        }

        Iterator iterator = choosedItems.entrySet().iterator();

        while (iterator.hasNext()){

            Map.Entry<String, ArrayList<String>> entry = (Map.Entry<String, ArrayList<String>>) iterator.next();

            String key=entry.getKey();
            ArrayList<String> items = entry.getValue();
            if (null != items&&!items.isEmpty()) {

                for (String content:items)
                viewItems.add(new ChoosedItem(key,content));
            }
        }
    }
}
