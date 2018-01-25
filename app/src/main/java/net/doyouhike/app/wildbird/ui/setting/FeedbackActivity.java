package net.doyouhike.app.wildbird.ui.setting;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.ui.base.BaseAppActivity;
import net.doyouhike.app.wildbird.util.CheckNetWork;
import net.doyouhike.app.wildbird.util.LocalSharePreferences;
import net.doyouhike.app.wildbird.ui.view.TitleView;
import net.doyouhike.app.wildbird.ui.view.TitleView.ClickListener;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.SyncListener;
import com.umeng.fb.model.Conversation;
import com.umeng.fb.model.Reply;

import java.util.List;

public class FeedbackActivity extends BaseAppActivity implements ClickListener {

	private TitleView titleview;
	private EditText edit_feedback;
	
	private FeedbackAgent agent;
	private Conversation conversation;

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.feedback_view;
	}

	@Override
	protected void initViewsAndEvents() {
		super.initViewsAndEvents();

		agent = new FeedbackAgent(this);

		titleview = (TitleView) super.findViewById(R.id.titleview);
		edit_feedback = (EditText) super.findViewById(R.id.edit_feedback);
		titleview.setListener(this);
		
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			edit_feedback.setText("版本 "+info.versionName+" Android\n");
			edit_feedback.setSelection(edit_feedback.length());
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(LocalSharePreferences.getValue(getApplicationContext(), "userId").equals("")){
			conversation = agent.getDefaultConversation();
		}else{
			conversation = agent.getConversationById(LocalSharePreferences.getValue(getApplicationContext(), "userId"));
			if(conversation == null){
				conversation = Conversation.newInstance(getApplicationContext(), 
						LocalSharePreferences.getValue(getApplicationContext(), "userId"));
			}
		}
	}

	@Override
	public void clickLeft() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public void clickRight() {
		// TODO Auto-generated method stub
		String FeedBackInfo = edit_feedback.getText().toString().trim();
		if (FeedBackInfo.equals("")) {
			toast("请输入您的意见");
			return;
		}
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit_feedback.getWindowToken(), 0);
		if(CheckNetWork.isNetworkAvailable(getApplicationContext())){
			toast("感谢您的宝贵意见，我们会尽快进行处理。");
		}else{
			toast("当前没有网络。");
		}
		if(LocalSharePreferences.getValue(getApplicationContext(), "userId").equals("")){
			conversation.addUserReply(FeedBackInfo);
			conversation.sync(new SyncListener() {
				@Override
				public void onSendUserReply(List<Reply> arg0) {
					// TODO Auto-generated method stub
				}
				@Override
				public void onReceiveDevReply(List<Reply> arg0) {
					// TODO Auto-generated method stub
				}
			});
		}else{
			Reply reply = new Reply(FeedBackInfo, LocalSharePreferences.getValue(getApplicationContext(), "userId"), 
					Reply.CONTENT_TYPE_TEXT_REPLY, System.currentTimeMillis());
			conversation.addReply(reply);
			conversation.sendReplyOnlyOne(LocalSharePreferences.getValue(getApplicationContext(), "userName"), reply);
		}
		finish();
//		final JSONObject jsonObject = new JSONObject();
//		try {
//			jsonObject.put("content", FeedBackInfo);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//			httpPost(jsonObject);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
	}

}
