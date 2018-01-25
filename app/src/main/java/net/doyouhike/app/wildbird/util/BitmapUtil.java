package net.doyouhike.app.wildbird.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import net.doyouhike.app.wildbird.R;
import net.doyouhike.app.wildbird.app.MyApplication;

public class BitmapUtil {

    private static final String WATER_MARK_TITLE = "中国野鸟速查";

    // 计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) throws FileNotFoundException{
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    // 把bitmap转换成String
    public static String bitmapToString(String filePath) {

        Bitmap bm = null;
        try {
            bm = getSmallBitmap(filePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);//图片压缩50%
            byte[] b = baos.toByteArray();
            bm.recycle();
            return Base64.encodeToString(b, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return "";
    }


    // 把bitmap转换成byte[]
    public static byte[] bitmapTobytes(String filePath) {

        Bitmap bm = null;
        try {
            bm = getSmallBitmap(filePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);//图片压缩50%
            byte[] b = baos.toByteArray();
            bm.recycle();

            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return b;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }
    public static Bitmap small(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(0.5f, 0.5f); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return resizeBmp;
    }
    public static void saveWaterMark(Bitmap m_srcBitmap,String title, Drawable drawable) {


        int m_bmpWidth = m_srcBitmap.getWidth();
        int m_bmpHeight = m_srcBitmap.getHeight();
        Bitmap.Config m_bmpConfig = m_srcBitmap.getConfig();

        // 绘制新的bitmap
        Bitmap m_newBitmap = Bitmap.createBitmap(m_bmpWidth, m_bmpHeight, m_bmpConfig);
        Canvas m_newCanvas = new Canvas(m_newBitmap);
        m_newCanvas.drawBitmap(m_srcBitmap, 0, 0, null);// 在 0，0坐标开始画入src

        Paint paint = new Paint();
        //加入图片
        if (drawable != null) {
            Bitmap watermark = drawable2Bitmap(drawable);

            if (watermark != null) {

                watermark=small(watermark);

                int ww = watermark.getWidth();
                int wh = watermark.getHeight();
                m_newCanvas.drawBitmap(watermark, m_bmpWidth - ww + 5, m_bmpHeight - wh + 5, paint);// 在src的右下角画入水印
            }
        }


        //加入文字

        String familyName = "宋体";
        Typeface font = Typeface.create(familyName, Typeface.BOLD);
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTypeface(font);
        textPaint.setTextSize(18);
        //这里是自动换行的
        m_newCanvas.drawText(WATER_MARK_TITLE, 1, m_bmpHeight-36, textPaint);

        //文字就加左上角算了
        if (!TextUtils.isEmpty(title)){
            textPaint.setTextSize(16);
            m_newCanvas.drawText("作者: "+title, 1, m_bmpHeight-18, textPaint);
        }

        m_newCanvas.save(Canvas.ALL_SAVE_FLAG);// 保存
        m_newCanvas.restore();// 存储

        MediaStore.Images.Media.insertImage(
                MyApplication.getInstance().getContentResolver(), m_newBitmap, "title", "来自中国野鸟速查APP");

        if (!m_newBitmap.isRecycled()){
            m_newBitmap.recycle();
            System.gc();
        }
    }


    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }




    /**
     *
     * TODO 利用 InputStream 加载图片节省java 层 空间. <br/>
     * 日期: 2015-9-21 下午4:48:38 <br/>
     *
     * @author zhulin
     * @param image
     * @param size
     * @return
     * @since JDK 1.6
     */
    public static Bitmap decodeFile(String path) {
        Bitmap bitmap = null;
        try {
            InputStream is = new FileInputStream(path);
            // 2.为位图设置100K的缓存
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inTempStorage = new byte[16 * 1024];
            // 3.设置位图颜色显示优化方式
            // ALPHA_8：每个像素占用1byte内存（8位）
            // ARGB_4444:每个像素占用2byte内存（16位）
            // ARGB_8888:每个像素占用4byte内存（32位）
            // RGB_565:每个像素占用2byte内存（16位）
            // Android默认的颜色模式为ARGB_8888，这个颜色模式色彩最细腻，显示质量最高。但同样的，占用的内存//也最大。也就意味着一个像素点占用4个字节的内存。我们来做一个简单的计算题：3200*2400*4
            // bytes //=30M。如此惊人的数字！哪怕生命周期超不过10s，Android也不会答应的。
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            // 4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
            opts.inPurgeable = true;

            // 5.设置位图缩放比例
            // width，hight设为原来的四分一（该参数请使用2的整数倍）,这也减小了位图占用的内存大小；例如，一张//分辨率为2048*1536px的图像使用inSampleSize值为4的设置来解码，产生的Bitmap大小约为//512*384px。相较于完整图片占用12M的内存，这种方式只需0.75M内存(假设Bitmap配置为//ARGB_8888)。
            // opts.inSampleSize = 4;
            // 6.设置解码位图的尺寸信息
            opts.inInputShareable = true;
            // 7.解码位图
            bitmap = BitmapFactory.decodeStream(is, null, opts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }




    /**
     * 保存 mBitmap 到SD卡
     * @param mBitmap
     * @param filePath
     * @param fileName
     */
    public static String saveBitmap(Bitmap mBitmap,String fileName)  {
        File f = new File(fileName);
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f.getAbsolutePath();
    }

    public static Bitmap compressImage(Bitmap image,int reqWidth,int reqHeight,int reqSize) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > reqWidth) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / reqWidth);
        } else if (w < h && h > reqHeight) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / reqHeight);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap,reqSize);//压缩好比例大小后再进行质量压缩
    }

    public static Bitmap compressImage(Bitmap image,int size){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > size) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

}
