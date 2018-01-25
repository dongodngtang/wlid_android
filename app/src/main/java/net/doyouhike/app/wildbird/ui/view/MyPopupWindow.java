package net.doyouhike.app.wildbird.ui.view;

import net.doyouhike.app.wildbird.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MyPopupWindow extends PopupWindow implements OnClickListener{
	
	public static String PHOTO_PATH = Environment.getExternalStorageDirectory()+"/wildbird/recordPhoto/";
	
	private TextView camera, gallery;
	private View view, view2;
	private SetAvatarListener listener;

	@SuppressLint("InflateParams")
	public static MyPopupWindow getInstance(Context context){
		View view = LayoutInflater.from(context).inflate(R.layout.popview, null);
		
		return new MyPopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}

	public MyPopupWindow(View contentView, int width, int height) {
		super(contentView, width, height);
		// TODO Auto-generated constructor stub
		camera = (TextView) contentView.findViewById(R.id.camera);
		gallery = (TextView) contentView.findViewById(R.id.gallery);
		view = contentView.findViewById(R.id.view);
		view2 = contentView.findViewById(R.id.view2);
		
		camera.setOnClickListener(this);
		gallery.setOnClickListener(this);
		view.setOnClickListener(this);
		view2.setOnClickListener(this);
	}
	
	public void setFirst(String text){
		camera.setText(text);
	}
	
	public void setSecond(String text){
		gallery.setText(text);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.camera:
			listener.startCamera();
			dismiss();
			break;
		case R.id.gallery:
			listener.startGallery();
			dismiss();
			break;
		case R.id.view:
			dismiss();
			break;
		case R.id.view2:
			dismiss();
			break;
		}
	}
	
	public void setListener(SetAvatarListener listen){
		listener = listen;
	}
	
	public interface SetAvatarListener{
		public void startCamera();
		public void startGallery();
	}

}
