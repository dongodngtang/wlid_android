package net.doyouhike.app.wildbird.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-14.
 */
public class SoundUtil {

    private static MediaPlayer player;

    public SoundUtil(Context context, String url) {
        player = MediaPlayer.create(context, Uri
                .parse(url));//实例化对象，通过播放本机服务器上的一首音乐
        player.setLooping(true);//设置循环播放
    }

    public  void start() {

        if (player.isPlaying()){
            stopPlay();
        }else {
            player.start();
        }
    }
    public  void stopPlay() {
        player.stop();
    }

}
