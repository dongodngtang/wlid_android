package net.doyouhike.app.wildbird.util;

import android.content.Context;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.biz.model.Content;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by zaitu on 15-12-15.
 */
public class CopyDatabaseUtil {

    /**
     * @param context
     */
    public static void copyDataBase(Context context) {

        boolean isSuc;

        try {
            CopyDatabaseUtil.copySourceDirToTargetDir(context, Content.DB_NAME_WILDBIRD);
            isSuc = true;
        } catch (IOException e) {
            e.printStackTrace();
            isSuc = false;
        }

        if (isSuc) {
            //标志已复制了数据库
            LocalSharePreferences.put(context, Content.SP_COPY_DB_SYMBOL, true);
            //已经更新特征离线数据到数据库一起打包，离线特征数据版本为5.7
            LocalSharePreferences.commit(context, Content.SP_DATABASE_VERSION, Content.DATABASE_VERSION_CODE);
        }

    }

    public static void copySourceDirToTargetDir(Context context, String name) throws IOException {

        File targetLocation = context.getDatabasePath(name);
        LogUtil.d("databasePath", targetLocation.getPath());
        LogUtil.d(targetLocation.getParent());

        if (!targetLocation.exists()) {
            //数据库文件不存在
            if (targetLocation.getParentFile().mkdirs()) {
                //创建路径成功
                File file = new File(targetLocation.getPath());
                file.createNewFile();
            }

        } else {
            //删除数据库文件,再创建新的文件,执行这个会触发attempt to write a readonly database错误
            context.deleteDatabase(name);
            File file = new File(targetLocation.getPath());
            file.createNewFile();
        }

        InputStream in = context.getResources().openRawResource(R.raw.wildbird);

        OutputStream out = new FileOutputStream(targetLocation);

        // Copy the bits from instream to outstream
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();

    }


}
