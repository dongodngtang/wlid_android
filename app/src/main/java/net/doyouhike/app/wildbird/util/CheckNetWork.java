package net.doyouhike.app.wildbird.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;

import net.doyouhike.app.library.ui.uistate.UiState;

public class CheckNetWork {
	public static final int NETWORN_NONE = 0;
	public static final int NETWORN_WIFI = 1;
	public static final int NETWORN_MOBILE = 2;

	public static int getNetworkState(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context.getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		// Wifi
		NetworkInfo.State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
			return NETWORN_WIFI;
		}

		// 3G
		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
			return NETWORN_MOBILE;
		}
		return NETWORN_NONE;
	}
	public static boolean isNetworkAvailable(Context context) {
		if (null==context)
			return false;
		return getNetworkState(context)!=NETWORN_NONE;
	}

	public static String getErrorMsg(VolleyError error){

		String msg="";

		if (error instanceof NoConnectionError){
			msg="暂无网络连接,请点击设置网络.";
		}else {
			msg="网络不佳，点击刷新。";
		}



		return msg;
	}
}
