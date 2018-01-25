package net.doyouhike.app.wildbird.ui.main.fragment;

import java.util.ArrayList;
import java.util.List;

import net.doyouhike.app.wildbird.biz.service.database.WildbirdDbService;
import net.doyouhike.app.wildbird.ui.base.BaseFragment;
import net.doyouhike.app.wildbird.ui.FilterActivity;
import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.ui.adapter.AutoEditAdapter;
import net.doyouhike.app.wildbird.ui.adapter.HabitAdapter;
import net.doyouhike.app.wildbird.ui.adapter.RecordAdapter;
import net.doyouhike.app.wildbird.biz.model.ChooseBean;
import net.doyouhike.app.wildbird.biz.model.bean.SpeciesInfo;
import net.doyouhike.app.wildbird.util.GotoActivityUtil;
import net.doyouhike.app.wildbird.util.LogUtil;
import net.doyouhike.app.wildbird.ui.view.LineEditText;
import net.doyouhike.app.wildbird.ui.view.LineEditText.SearchListener;
import net.doyouhike.app.wildbird.ui.view.MyGridView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

@SuppressLint({ "InflateParams", "ClickableViewAccessibility" })
public class SearchFragment extends BaseFragment implements OnClickListener,
		OnItemClickListener, SearchListener, TextWatcher{
	
	private View view;// 该Fragment的界面
	
	private LinearLayout search_top;// 野鸟名输入框的父布局
	private LineEditText bird_name;// 输入鸟种名
	private TextView edit_bird, lessusedword, chosenCount, goTop, findNone, autoNone;// 鸟名、筛选、生僻字、条件数、置顶、记录查询为空、鸟名查询为空
	private ImageView auto_back;// 鸟种输入返回
	private FrameLayout choose_frame, autolayout, main_search;// 野鸟名查询布局、野鸟记录查询布局
	
	private GridView autoedit;// 野鸟名查询列表
	private List<SpeciesInfo> autolist;// 野鸟名查询表
	private AutoEditAdapter autoadapter;// 野鸟名列表适配器
	
	private GridView gvBirds;// 野鸟列表
	private RecordAdapter adapter;// 野鸟适配器
	private List<SpeciesInfo> birdlist;// 野鸟表
	private String[] sel;
	
	private PopupWindow popup;// 生僻字弹窗
	private HabitAdapter word_adapter;// 生僻字适配器
	private String[] lessuseword = new String[] {"鹇","鴷","鸮","鸱","鹬","鸻","鹮","鹗","鹫",
			"鹞","鵟","鹲","鹈","鳽","鹕","鹳","鹱","鵙","鹟","鸲","鵖","鳾","鹩","鹪","鹛","鹨","鹀",
			"鹧鸪","鸺鹠","鸊鷉","鸬鹚","鹡鸰"};// 生僻字数组
	private int word_pos = -1;// 生僻字位置
	
//	private DBHelper helper;
//	private SQLiteDatabase db;
	
	private boolean none = false;// 判断野鸟表是否还可加载
	private boolean moreAuto = false;// 判断野鸟名匹配是否还可加载
	
	private VisibleListener listenner;// 监听是否需要隐藏底部栏
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		listenner = (VisibleListener) activity;
	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.search_view;
	}

	@Override
	protected void initViewsAndEvents() {
		super.initViewsAndEvents();
		View view = getView();
		
		search_top = (LinearLayout) view.findViewById(R.id.search_top);
		search_top.setPadding(8, 8, 8, 8);

		auto_back = (ImageView) view.findViewById(R.id.auto_back);
		edit_bird = (TextView) view.findViewById(R.id.edit_bird);
		bird_name = (LineEditText) view.findViewById(R.id.bird_name);
		choose_frame = (FrameLayout) view.findViewById(R.id.choose_frame);
		lessusedword = (TextView) view.findViewById(R.id.lessusedword);
		chosenCount = (TextView) view.findViewById(R.id.chosenCount);
		goTop = (TextView) view.findViewById(R.id.goTop);
		
		autolayout = (FrameLayout) view.findViewById(R.id.autolayout);
		autoNone = (TextView) view.findViewById(R.id.autoNone);
		autoedit = (GridView) view.findViewById(R.id.autoedit);
		
		main_search = (FrameLayout) view.findViewById(R.id.main_search);
		findNone = (TextView) view.findViewById(R.id.findNone);
		gvBirds = (GridView) view.findViewById(R.id.birds);

		sel = ChooseBean.getInstance(getActivity()).getSelect();
		birdlist =new ArrayList<>();
		adapter = new RecordAdapter(getActivity(), birdlist);
		gvBirds.setAdapter(adapter);
		changeFindView();// 判断野鸟表是否为空，然后显示相应控件View

		auto_back.setOnClickListener(this);
		edit_bird.setOnClickListener(this);
		bird_name.addTextChangedListener(this);
		bird_name.setListener(this);
		choose_frame.setOnClickListener(this);
		lessusedword.setOnClickListener(this);
		goTop.setOnClickListener(this);
		gvBirds.setOnItemClickListener(this);
		
		gvBirds.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if(totalItemCount > 0 && firstVisibleItem + visibleItemCount == totalItemCount){
					if(!none){
						List<SpeciesInfo> list = WildbirdDbService.getInstance().search(sel, birdlist.size(), 20);
//						db = helper.getWritableDatabase();
//						List<SpeciesInfo> list = ControlDB.search(db, sel, birdlist.size(), 20);
						if(list.size() < 20){
							none = true;
							toast("没有更多了");
						}
						birdlist.addAll(list);
						adapter.notifyDataSetChanged();
						changeFindView();// 判断野鸟表是否为空，然后显示相应控件View
					}
				}
			}
		});
		
		autolist = new ArrayList<SpeciesInfo>();
		autoadapter = new AutoEditAdapter(getActivity(), autolist);
		autoedit.setAdapter(autoadapter);
		autoedit.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if(!moreAuto && totalItemCount > 0 && firstVisibleItem+visibleItemCount==totalItemCount){
					moreAuto = true;
					String sel_name = "%"+bird_name.getText().toString().trim()+"%";
					List<SpeciesInfo> list2 = WildbirdDbService.getInstance().autoEdit(sel_name, autolist.size(), 20);
					autolist.addAll(list2);
					autoadapter.notifyDataSetChanged();
					moreAuto = false;
					if(list2.size() < 20){
						moreAuto = true;
					}
				}
			}
		});
		autoedit.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				GotoActivityUtil.totoBirdDetailActivity(mContext, autolist.get(position).getSpeciesID());
			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		resetBirdList();
	}

	@Override
	public void onResume() {
		super.onResume();

		LogUtil.d("MainActivity", "SearchFragment onResume ");
		int count = ChooseBean.getInstance(getActivity()).getCount();
		if(count > 0){
			chosenCount.setVisibility(View.VISIBLE);
			chosenCount.setText(""+count);
		}else{
			chosenCount.setVisibility(View.GONE);
		}


	}

	/**
	 * 重置鸟种列表,目的为了刷新
	 */
	private void resetBirdList() {
		birdlist.clear();
		birdlist.addAll(WildbirdDbService.getInstance().search( sel, 0, 20));
		adapter.notifyDataSetChanged();
		gvBirds.setSelection(0);
		changeFindView();
	}

	;
	/**
	 * 确认筛选条件查询并获得野鸟列表
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){
			none = false;
			sel = ChooseBean.getInstance(getActivity()).getSelect();
			if(ChooseBean.getInstance(getActivity()).isChosen()){
				choose_frame.setSelected(true);
			}else{
				choose_frame.setSelected(false);
			}
			List<SpeciesInfo> list = WildbirdDbService.getInstance().search(sel, 0, 20);
			if(list.size() < 20){
				none = true;
			}
			birdlist.clear();
			birdlist.addAll(list);
			adapter = new RecordAdapter(getActivity(), birdlist);
			gvBirds.setAdapter(adapter);
			changeFindView();// 判断野鸟表是否为空，然后显示相应控件View
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		if(popup != null && popup.isShowing())return;
		if (!TextUtils.isEmpty(s)) {
			autoEdit();// 自动匹配数据库信息
		}else{
			bird_name.setGone();
		}
	}
	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
	}
	/**
	 * 判断野鸟表是否为空，然后显示相应控件View
	 */
	protected void changeFindView() {
		// TODO Auto-generated method stub
		if(birdlist.size() == 0){
			findNone.setVisibility(View.VISIBLE);
			gvBirds.setVisibility(View.GONE);
		}else{
			findNone.setVisibility(View.GONE);
			gvBirds.setVisibility(View.VISIBLE);
		}
	}
	/**
	 * 判断野鸟匹配野鸟名表是否为空，然后显示相应控件View
	 */
	protected void changeAutoView() {
		// TODO Auto-generated method stub
		if(autolist.size() == 0){
			autoNone.setVisibility(View.VISIBLE);
			autoedit.setVisibility(View.GONE);
		}else{
			autoNone.setVisibility(View.GONE);
			autoedit.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void search() {
		// TODO Auto-generated method stub
	}

	@Override
	public void autoEdit() {
		// TODO Auto-generated method stub
		bird_name.setClear();
		moreAuto = false;
		String sel_name = "%"+bird_name.getText().toString().trim()+"%";
		List<SpeciesInfo> list = WildbirdDbService.getInstance().autoEdit(sel_name, 0, 20);
		if(list.size() < 20) moreAuto = true;
		autolist.clear();
		autolist.addAll(list);
		autoadapter.notifyDataSetChanged();
		changeAutoView();
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		moreAuto = false;
		word_pos = -1;
		autolist.clear();
		autoadapter.notifyDataSetChanged();
		changeAutoView();
	}

	@Override
	public void showPW() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.auto_back:
			edit_bird.setVisibility(View.VISIBLE);
			bird_name.setVisibility(View.GONE);
			listenner.setVisivle(View.VISIBLE);
			search_top.setPadding(8, 8, 8, 8);
			auto_back.setVisibility(View.GONE);
			lessusedword.setVisibility(View.GONE);
			choose_frame.setVisibility(View.VISIBLE);
			autolayout.setVisibility(View.GONE);
			main_search.setVisibility(View.VISIBLE);
			goTop.setVisibility(View.VISIBLE);
			InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);  
			imm.hideSoftInputFromWindow(bird_name.getWindowToken(), 0);
			break;
		case R.id.edit_bird:
			edit_bird.setVisibility(View.GONE);
			bird_name.setVisibility(View.VISIBLE);
			listenner.setVisivle(View.GONE);
			search_top.setPadding(0, 8, 8, 8);
			autolayout.setVisibility(View.VISIBLE);
			main_search.setVisibility(View.GONE);
			goTop.setVisibility(View.GONE);
			auto_back.setVisibility(View.VISIBLE);
			lessusedword.setVisibility(View.VISIBLE);
			choose_frame.setVisibility(View.GONE);
			InputMethodManager imm2 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm2.showSoftInput(bird_name, 0);
			break;
		case R.id.choose_frame:// 筛选
			startActivityForResult(new Intent(getActivity(), FilterActivity.class), 111);
			break;
		case R.id.lessusedword:
			lessusedword.setSelected(true);
			showPopupWindow();
			break;
		case R.id.goTop:
			gvBirds.setSelection(0);
			break;
		}
	}
	/**
	 * 弹出生僻字窗口
	 */
	private void showPopupWindow() {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.color_view, null);

		view.findViewById(R.id.view).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popup.dismiss();
			}
		});

		MyGridView grid = (MyGridView) view.findViewById(R.id.less_words);
		if (word_adapter == null)
			word_adapter = new HabitAdapter(getActivity(), lessuseword, 1);
		grid.setAdapter(word_adapter);

		grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if(word_pos != -1)word_adapter.setSelect(word_pos);
				word_adapter.setSelect(position);
				String str = bird_name.getText().toString().trim();
				if(word_pos != -1){
					str = str.substring(0, str.length()-lessuseword[word_pos].length());
				}
				bird_name.setText(str + lessuseword[position]);
				bird_name.setSelection(bird_name.getText().toString().length());
				word_pos = position;
				popup.dismiss();
				autoEdit();
			}
		});
		showPop(view);
		popup.setTouchable(true);
		popup.setFocusable(true);
		popup.setOutsideTouchable(true);
		popup.showAsDropDown(lessusedword);
		popup.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				lessusedword.setSelected(false);
			}
		});
	}

	private void showPop(View view) {
		// TODO Auto-generated method stub
		DisplayMetrics metric = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
		int mScreenWidth = metric.widthPixels;
		int height = metric.heightPixels;
		popup = new PopupWindow(view, mScreenWidth, height);
		popup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		popup.setBackgroundDrawable(new BitmapDrawable(getActivity().getResources(), ""));
		popup.setAnimationStyle(R.style.GrowFromTop);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		GotoActivityUtil.totoBirdDetailActivity(mContext,birdlist.get(position).getSpeciesID());
	}

	public boolean isHide() {
		// TODO Auto-generated method stub
		if(autolayout.getVisibility() == View.VISIBLE){
			edit_bird.setVisibility(View.VISIBLE);
			bird_name.setVisibility(View.GONE);
			listenner.setVisivle(View.VISIBLE);
			search_top.setPadding(8, 8, 8, 8);
			autolayout.setVisibility(View.GONE);
			main_search.setVisibility(View.VISIBLE);
			goTop.setVisibility(View.VISIBLE);
			auto_back.setVisibility(View.GONE);
			lessusedword.setVisibility(View.GONE);
			choose_frame.setVisibility(View.VISIBLE);
			return false;
		}
		return true;
	}

	
	public interface VisibleListener{
		public void setVisivle(int visibility);
	}
}
