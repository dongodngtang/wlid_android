package net.doyouhike.app.wildbird.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import net.doyouhike.app.wildbird.app.MyApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {

    /**
     * 处理图片的工具类.
     */
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int TOP = 3;
    public static final int BOTTOM = 4;

    public static final int IMAGE_TYPE_JPG = 2328;
    public static final int IMAGE_TYPE_PNG = 2329;
    public static final int IMAGE_TYPE_GIF = 2330;
    public static final int IMAGE_TYPE_OTHER = 2331;


    private static final int REQ_PIC_SIZE = 2 * 1024 * 1024;
    private static final int REQ_PIC_RESOLUTION = 1000;


    public static String getSmallImagePath(String picUrl) throws IOException {
        int imageType = getImageType(picUrl);
        int issForSize = calculateInSampleSizeForSize(picUrl);
        int issForResolution = calculateInSampleSizeForResolution(picUrl);
        if (imageType != IMAGE_TYPE_JPG || !isJpegSuffix(picUrl)
                || issForSize > 1 || issForResolution > 1) {
            //保存临时图片
            Bitmap bitmap = BitmapUtil.compressImage(BitmapUtil.decodeFile(picUrl), REQ_PIC_RESOLUTION, REQ_PIC_RESOLUTION, REQ_PIC_SIZE);
            picUrl = BitmapUtil.saveBitmap(bitmap, getTampPicPath());

        }

        return picUrl;
    }


    /** */
    /**
     * 图片去色,返回灰度图片
     *
     * @param bmpOriginal 传入的图片
     * @return 去色后的图片
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
                Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    /** */
    /**
     * 去色同时加圆角
     *
     * @param bmpOriginal 原图
     * @param pixels      圆角弧度
     * @return 修改后的图片
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
        return toRoundCorner(toGrayscale(bmpOriginal), pixels);
    }

    /** */
    /**
     * 把图片变成圆角
     *
     * @param bitmap 需要修改的图片
     * @param pixels 圆角的弧度
     * @return 圆角图片
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /** */
    /**
     * 使圆角功能支持BitampDrawable
     *
     * @param bitmapDrawable
     * @param pixels
     * @return
     */
    public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable,
                                               int pixels) {
        Bitmap bitmap = bitmapDrawable.getBitmap();
        bitmapDrawable = new BitmapDrawable(toRoundCorner(bitmap, pixels));
        return bitmapDrawable;
    }

    // 计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;

        int heightRatio = 1;
        int widthRatio = 1;

        if (height > reqHeight && reqHeight > 0) {
            heightRatio = Math.round((float) height
                    / (float) reqHeight);
        }

        if (width > reqWidth && reqWidth > 0) {
            widthRatio = Math.round((float) width / (float) reqWidth);
        }

        return heightRatio < widthRatio ? heightRatio : widthRatio;
    }

    public static int calculateInSampleSize(String picPath, int reqWidth,
                                            int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picPath, options);
        return calculateInSampleSize(options, reqWidth, reqHeight);
    }

    /**
     * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处： 1.
     * 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
     * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 2.
     * 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸。
     *
     * @param imagePath 图像的路径
     * @param width     指定输出图像的宽度
     * @param height    指定输出图像的高度
     * @return 生成的缩略图
     */
    public static Bitmap getSmallBitmap(String imagePath, int width, int height) {

        if (!new File(imagePath).exists()) {
            return null;
        }

        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }


    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static BitmapInfo getSmallBitmap(String picPath, int inSampleSize) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picPath, options);

        // Calculate inSampleSize
        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeFile(picPath, options);
        } catch (OutOfMemoryError e) {
            recycleBitmap(bitmap);
            return getSmallBitmap(picPath, inSampleSize + 1);
        }
        BitmapInfo info = new BitmapInfo();
        info.bitmap = bitmap;
        info.inSampleSize = inSampleSize;
        return info;
    }

    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            // 回收并且置为null
            bitmap.recycle();
            bitmap = null;
        }
        System.gc();
    }

    /**
     * 保存图片为PNG
     *
     * @param bitmap
     * @param name
     */
    public static void savePNG_After(Bitmap bitmap, String name) {
        File file = new File(name);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(CompressFormat.PNG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存图片为JPEG
     *
     * @param bitmap
     * @param path
     * @throws IOException
     */
    public static void saveJPGE_After(Bitmap bitmap, String path, int quality)
            throws IOException {
        File file = new File(path);
        FileOutputStream out = new FileOutputStream(file);
        if (bitmap.compress(CompressFormat.JPEG, quality, out)) {
            out.flush();
            out.close();
        }
    }

    /**
     * 水印
     *
     * @param watermark
     * @return
     */
    public static Bitmap createBitmapForWatermark(Bitmap src, Bitmap watermark) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        int ww = watermark.getWidth();
        int wh = watermark.getHeight();
        // create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        // draw src into
        cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
        // draw watermark into
        cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);// 在src的右下角画入水印
        // save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        // store
        cv.restore();// 存储
        return newb;
    }

    /**
     * 图片合成
     *
     * @return
     */
    public static Bitmap potoMix(int direction, Bitmap... bitmaps) {
        if (bitmaps.length <= 0) {
            return null;
        }
        if (bitmaps.length == 1) {
            return bitmaps[0];
        }
        Bitmap newBitmap = bitmaps[0];
        // newBitmap =
        // createBitmapForFotoMix(bitmaps[0],bitmaps[1],direction);
        for (int i = 1; i < bitmaps.length; i++) {
            newBitmap = createBitmapForFotoMix(newBitmap, bitmaps[i], direction);
        }
        return newBitmap;
    }

    private static Bitmap createBitmapForFotoMix(Bitmap first, Bitmap second,
                                                 int direction) {
        if (first == null) {
            return null;
        }
        if (second == null) {
            return first;
        }
        int fw = first.getWidth();
        int fh = first.getHeight();
        int sw = second.getWidth();
        int sh = second.getHeight();
        Bitmap newBitmap = null;
        if (direction == LEFT) {
            newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh,
                    Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, sw, 0, null);
            canvas.drawBitmap(second, 0, 0, null);
        } else if (direction == RIGHT) {
            newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh,
                    Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, 0, 0, null);
            canvas.drawBitmap(second, fw, 0, null);
        } else if (direction == TOP) {
            newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh,
                    Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, 0, sh, null);
            canvas.drawBitmap(second, 0, 0, null);
        } else if (direction == BOTTOM) {
            newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh,
                    Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, 0, 0, null);
            canvas.drawBitmap(second, 0, fh, null);
        }
        return newBitmap;
    }

    /**
     * 将Bitmap转换成指定大小
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createBitmapBySize(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    /**
     * Drawable 转 Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmapByBD(Drawable drawable) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        return bitmapDrawable.getBitmap();
    }

    /**
     * Bitmap 转 Drawable
     *
     * @param bitmap
     * @return
     */
    public static Drawable bitmapToDrawableByBD(Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }

    /**
     * byte[] 转 bitmap
     *
     * @param b
     * @return
     */
    public static Bitmap bytesToBimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * bitmap 转 byte[]
     *
     * @param bm
     * @return
     */
    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        boolean isSuccess = bm.compress(CompressFormat.JPEG, 100, baos);
        if (isSuccess) {
            return baos.toByteArray();
        } else {
            return null;
        }
    }

    public static int getImageType(File file) throws IOException {
        int length = 10;
        InputStream is = new FileInputStream(file);
        byte[] data = new byte[length];
        is.read(data);
        is.close();

        // Log.w("yoline_test", "data = " + data.toString());

        // Png test:
        if (data[1] == 'P' && data[2] == 'N' && data[3] == 'G') {
            Log.w("yoline_test", "上传的图片类型是png的类型"); // TODO
            return IMAGE_TYPE_PNG;
        }
        // Gif test:
        if (data[0] == 'G' && data[1] == 'I' && data[2] == 'F') {
            Log.w("yoline_test", "上传的图片类型是gif的类型"); // TODO
            return IMAGE_TYPE_GIF;
        }
        // JPG test:
        if (data[6] == 'J' && data[7] == 'F' && data[8] == 'I'
                && data[9] == 'F') {
            Log.w("yoline_test", "上传的图片类型是jpg的类型"); // TODO
            return IMAGE_TYPE_JPG;
        }
        Log.w("yoline_test", "上传的图片类型是other的类型"); // TODO
        return IMAGE_TYPE_OTHER;
    }

    public static int getImageType(String path) throws IOException {
        return getImageType(new File(path));
    }

    public static int[] getImageSize(String path) {
        if (path != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            return new int[]{options.outWidth, options.outHeight};
        } else {
            return new int[]{0, 0};
        }
    }

    public static boolean saveImageToGallery(Context context, Bitmap bmp,
                                             String savePath) {
        // 首先保存图片
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(savePath, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

//		// 其次把文件插入到系统图库
//		try {
//			MediaStore.Images.Media.insertImage(context.getContentResolver(),
//					file.getAbsolutePath(), fileName, null);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			return false;
//		}
//		// 最后通知图库更新
//		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
//				Uri.parse("file://" + savePath + fileName)));
        return true;
    }

    /**
     * 获取当前时间生成的保存路径（不包括文件名）
     *
     * @param context
     * @return
     */
    public static String getSaveImagePath(Context context) {

        String imagePath = context.getFilesDir() + "/images/";

        String sdStatus = Environment.getExternalStorageState();
        if (sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            // 可用，则指向sd卡目录
            imagePath = Environment.getExternalStorageDirectory()
                    + "/doyouhike/jianghu/";
        }

        File file = new File(imagePath);
        if (!file.exists()) {
            file.mkdirs();// 创建文件夹
        }

        return imagePath;

    }

    public static class BitmapInfo {
        public int inSampleSize;
        public Bitmap bitmap;
    }


    private static int calculateInSampleSizeForSize(String path) {
        File picFile = new File(path);
        int inSampleSize = 1;
        if (picFile.exists()) {
            long len = picFile.length();
            if (len > REQ_PIC_SIZE) {
                inSampleSize = Math.round(len / REQ_PIC_SIZE * 1F);
            }
        }
        return inSampleSize;
    }

    private static int calculateInSampleSizeForResolution(String path) {
        int inSampleSize = ImageUtil.calculateInSampleSize(path,
                REQ_PIC_RESOLUTION, REQ_PIC_RESOLUTION);
        return inSampleSize;
    }


    private static boolean isJpegSuffix(String picUrl) {
        if (!TextUtils.isEmpty(picUrl)) {
            String tail = picUrl.substring(picUrl.lastIndexOf("."),
                    picUrl.length());
            return ".jpg".equalsIgnoreCase(tail);
        } else {
            return false;
        }
    }


    private static String getTampPicPath() {
        String fileName = System.currentTimeMillis() + "";
        String tampPath = "";

        if (null != MyApplication.getInstance().getExternalCacheDir()) {

            tampPath = MyApplication.getInstance().getExternalCacheDir().getPath();

        }


        if (TextUtils.isEmpty(tampPath)) {
            tampPath = MyApplication.getInstance().getCacheDir().getPath();
        }


        return tampPath + fileName + ".jpg";

    }
}