package net.doyouhike.app.wildbird.ui.fragment;

import net.doyouhike.app.wildbird.ui.base.BaseFragment;
import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.ui.adapter.ColorAdapter;
import net.doyouhike.app.wildbird.biz.model.ChooseBean;
import net.doyouhike.app.wildbird.biz.model.ChooseBean.ChooseListener;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ShapeFragment extends BaseFragment implements OnItemClickListener{

	private View view;
	private GridView baseconds;
	private ColorAdapter adapter;
	private int pos = -1;
	
	private ChooseListener listener;

	public void setListener(ChooseListener listener) {
		this.listener = listener;
	}
	@Override
	protected int getContentViewLayoutID() {
		return R.layout.condition_view;
	}

	@Override
	protected void initViewsAndEvents() {
		super.initViewsAndEvents();
		view = getView();

		baseconds = (GridView) view.findViewById(R.id.baseconds);
		view.findViewById(R.id.color_linear).setVisibility(View.GONE);

		baseconds.setNumColumns(1);
		adapter = ChooseBean.getInstance(getActivity()).getColorAdapter(getActivity(), 1);
		baseconds.setAdapter(adapter);
		baseconds.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		if(pos != -1)listener.delete("shape", adapter.getSelectText(pos));
		pos = position;
		adapter.setSelect(position);
		if(adapter.isCheck(position))listener.choose("shape", adapter.getSelectText(position));
		else listener.delete("shape", adapter.getSelectText(position));
	}

	public void clear(String value) {
		// TODO Auto-generated method stub
		if(adapter == null){
			adapter = ChooseBean.getInstance(getActivity()).getColorAdapter(getActivity(), 1);
		}
		adapter.clear(1, value);
	}
}
