package net.doyouhike.app.wildbird.ui.fragment;

import java.util.List;

import net.doyouhike.app.wildbird.ui.base.BaseFragment;
import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.ui.adapter.FolderAdapter;
import net.doyouhike.app.wildbird.biz.model.bean.AlbumInfo;
import net.doyouhike.app.wildbird.util.PhotoUtil;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FolderFragment extends BaseFragment{

	private View view;
	private ListView folders;
	private List<AlbumInfo> list;
	private FolderAdapter folderAdapter;
	private itemClickListener listener;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		listener = (itemClickListener) activity;
	}
	public interface itemClickListener{
		public void click(int position);
	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.folder_view;
	}


	@Override
	protected void initViewsAndEvents() {
		super.initViewsAndEvents();
		view = getView();
		folders = (ListView) view.findViewById(R.id.folders);
		
		list = PhotoUtil.getInstance(getActivity()).getList();
		folderAdapter = new FolderAdapter(getActivity(), list);
		folders.setAdapter(folderAdapter);
		folders.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				listener.click(position);
			}
		});

	}


}
