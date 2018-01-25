package net.doyouhike.app.wildbird.ui.main.user.mine;

import net.doyouhike.app.wildbird.biz.model.bean.MyRecord;
import net.doyouhike.app.wildbird.biz.model.bean.RecordEntity;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 功能：
 *
 * @author：曾江 日期：16-3-11.
 */
public class MyRecordList extends ArrayList<MyRecord> {

    private int size;

    public synchronized void addItem(RecordEntity entity) {

        MyRecord record = new MyRecord(entity);
        record.setIsTitle(true);
        if (!contains(record)) {
            add(record);
        }
        add(record.copy());

        Collections.sort(this, Collections.reverseOrder());
    }

    public boolean containItem(RecordEntity entity) {
        MyRecord myRecord = new MyRecord(entity);
        return contains(myRecord);
    }

    /**
     * 删除一条数据,如果只有一条,那么连头部也一起删除
     * @param entity
     */
    public synchronized void removeItem(RecordEntity entity) {

        if (null == entity) {
            return;
        }

        Collections.sort(this, Collections.reverseOrder());

        if (containItem(entity)) {

            MyRecord tiTleRecord = null;

            MyRecord currentRecord = null;
            MyRecord followRecord = null;

            for (int i = 0; i < size(); i++) {

                if (get(i).isTitle()) {
                    continue;
                }

                if (get(i).getRecord().equals(entity)) {

                    currentRecord = get(i);

                    if (i - 1 >= 0 && get(i - 1).isTitle()) {
                        tiTleRecord = get(i - 1);
                    }

                    if (i + 1 < size() && get(i + 1).getDateTime().equals(currentRecord.getDateTime())) {
                        followRecord = get(i + 1);
                    }

                }
            }

            remove(currentRecord);

            if (null!=tiTleRecord&&null == followRecord) {
                remove(tiTleRecord);
            }


        }
    }

    public synchronized void addAllItem(MyRecordList list){

        for (MyRecord record:list){
            if (!contains(record)){
                add(record);
            }
        }
        Collections.sort(this, Collections.reverseOrder());
    }

    public synchronized  void setItem(RecordEntity item) {

        if (containItem(item)) {
            MyRecord myRecord = new MyRecord(item);
            remove(myRecord);
        }

        addItem(item);
    }

    public int getSize() {
        int size=0;

        for (MyRecord record:this){
            if (!record.isTitle()){
                size++;
            }
        }



        return size;
    }
}
