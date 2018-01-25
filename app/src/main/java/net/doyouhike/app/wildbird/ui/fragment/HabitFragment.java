package net.doyouhike.app.wildbird.ui.fragment;

import net.doyouhike.app.wildbird.ui.base.BaseFragment;
import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.ui.adapter.HabitAdapter;
import net.doyouhike.app.wildbird.biz.model.ChooseBean;
import net.doyouhike.app.wildbird.biz.model.ChooseBean.ChooseListener;
import net.doyouhike.app.wildbird.ui.view.MyGridView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class HabitFragment extends BaseFragment implements OnItemClickListener {

    private View view;
    private MyGridView showtime, doing;
    private HabitAdapter adapter1;

    private ChooseListener listener;

    public void setListener(ChooseListener listener) {
        this.listener = listener;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_habit;
    }

    @Override
    protected void initViewsAndEvents() {
        super.initViewsAndEvents();
        view = getView();

        showtime = (MyGridView) view.findViewById(R.id.choose_colors);
        doing = (MyGridView) view.findViewById(R.id.show_doing);

        adapter1 = ChooseBean.getInstance(getActivity()).getHabitAdapter(getActivity(), 0, 0);
        showtime.setNumColumns(3);
        showtime.setAdapter(adapter1);

        showtime.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
        switch (parent.getId()) {
            case R.id.choose_colors:
                adapter1.setSelect(position);
                if (adapter1.isCheck(position))
                    listener.choose("habit", "出现-" + adapter1.getSelectText(position));
                else listener.delete("habit", "出现-" + adapter1.getSelectText(position));
                break;
        }
    }

    public void clear(String value) {
        // TODO Auto-generated method stub
        int index=value.indexOf("出现");
        if (index==0) {
            if (adapter1 == null) {
                adapter1 = ChooseBean.getInstance(getActivity()).getHabitAdapter(getActivity(), 0, 0);
            }
            adapter1.clear(value.replace("出现-", ""));
        }
    }
}
