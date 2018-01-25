package net.doyouhike.app.wildbird.ui.fragment;

import net.doyouhike.app.wildbird.ui.base.BaseFragment;
import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.ui.adapter.HabitAdapter;
import net.doyouhike.app.wildbird.biz.model.ChooseBean;
import net.doyouhike.app.wildbird.biz.model.ChooseBean.ChooseListener;
import net.doyouhike.app.wildbird.ui.view.MyImageView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class FeatureFragment extends BaseFragment implements OnItemClickListener {


	private static final int ADAPTER_LIST_INDEX_BODY=1;

	private View view;
	private MyImageView show_feature;
	private GridView gridfeature;
	private HabitAdapter adapter;
	
	private String[] features = new String[]{"头部", "颈背", "胸腹胁", "翅膀", "腰", "脚爪趾", "尾部"};
	private int pos = 0;// 二级身体特征的位置
	private boolean[] isShow;// 野鸟图片是否已显示相应位置的特征

	private ChooseListener listener;

	public void setListener(ChooseListener listener) {
		this.listener = listener;
	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.fragment_feature;
	}


	@Override
	protected void initViewsAndEvents() {
		super.initViewsAndEvents();
		view = getView();

		show_feature = (MyImageView) view.findViewById(R.id.show_feature);
		gridfeature = (GridView) view.findViewById(R.id.gridfeature);

		isShow = show_feature.getShow();
		show_feature.setImageDrawable(show_feature.refresh());
		adapter = ChooseBean.getInstance(getActivity()).getBodyAdapter(getActivity(), 5, ADAPTER_LIST_INDEX_BODY);
		gridfeature.setAdapter(adapter);
		gridfeature.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		adapter.setSelect(position);
		if(adapter.isCheck(position)){
			listener.choose(features[pos], features[pos]+"-"+adapter.getSelectText(position));
		}else{
			listener.delete(features[pos], features[pos]+"-"+adapter.getSelectText(position));
		}
		if(!isShow[pos]){
			isShow[pos] = true;
			show_feature.setImageDrawable(show_feature.setPosition(pos+1, true));
		}else{
			if(adapter.isEmpty()){
				isShow[pos] = false;
				show_feature.setImageDrawable(show_feature.setPosition(pos+1, false));
			}
		}
	}

	public void click(int position) {
		// TODO Auto-generated method stub
		pos = position;
		adapter = ChooseBean.getInstance(getActivity()).getBodyAdapter(getActivity(), 5, position+ADAPTER_LIST_INDEX_BODY);
		gridfeature.setAdapter(adapter);
	}
	/**
	 * 还原野鸟图片选项
	 */
	public void init(){
		if(show_feature != null)show_feature.init();
	}
	/**
	 * 确认野鸟图片选项
	 */
	public void makeSure(){
		if(show_feature != null)show_feature.makeSure();
	}
	/**
	 * 初始化野鸟图片选项
	 */
	public void refresh(){
		if(show_feature != null){
			show_feature.refreshImage();
			show_feature.setImageDrawable(show_feature.refresh());
		}
	}

	public void clear(int index, String key, String value) {
		// TODO Auto-generated method stub
		adapter = ChooseBean.getInstance(getActivity()).getBodyAdapter(getActivity(), 5, index + ADAPTER_LIST_INDEX_BODY);
		adapter.clear(value.replace(key+"-", ""));
		if(adapter.isEmpty()){
			if (null==isShow){
				isShow=show_feature.getShow();
			}

			isShow[index] = false;
			show_feature.setImageDrawable(show_feature.setPosition(index+1, false));
		}
	}
}
