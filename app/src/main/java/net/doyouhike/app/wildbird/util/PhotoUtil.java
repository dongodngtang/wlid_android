package net.doyouhike.app.wildbird.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.doyouhike.app.wildbird.biz.model.bean.AlbumInfo;
import net.doyouhike.app.wildbird.biz.model.bean.PhotoInfo;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;

/**
 * 相册
 * @author Administrator
 *
 */
public class PhotoUtil {

	private static PhotoUtil photoUtil = null;
	private List<AlbumInfo> list;
	private ContentResolver cr;
	private Map<Integer, String> thumbnails;
	
	public static void clear(){
		photoUtil = null;
	}
	
	public List<AlbumInfo> getList() {
		return list;
	}

	public Map<Integer, String> getThumbnails() {
		return thumbnails;
	}
	
	public String getByKey(int key, String defaultPath){
		if(thumbnails == null || !thumbnails.containsKey(key))return defaultPath;
		return thumbnails.get(key);
	}

	public static PhotoUtil getInstance(Context context){
		if(photoUtil == null){
			synchronized (PhotoUtil.class) {
				if(photoUtil == null){
					photoUtil = new PhotoUtil(context);
				}
			}
		}
		return photoUtil;
	}

	public PhotoUtil(Context context) {
		// TODO Auto-generated constructor stub
		cr = context.getContentResolver();
		refrsh(context);
	}
	
	@SuppressLint("UseSparseArrays")
	public void refrsh(Context context) {
		// TODO Auto-generated method stub
		if(list != null) list.clear();
		list = new ArrayList<AlbumInfo>();
		if(thumbnails != null)thumbnails.clear();
		thumbnails = new HashMap<Integer, String>();
		new ImageAsyncTask().execute();
	}

	private class ImageAsyncTask extends AsyncTask<Void, Void, Object>{

		@Override
		protected Object doInBackground(Void... params) {
			//获取缩略图
			String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID, Thumbnails.DATA };
			Cursor cur = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);

			if (cur!=null&&cur.moveToFirst()) {
				int image_id;
				String image_path;
				int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
				int dataColumn = cur.getColumnIndex(Thumbnails.DATA);
				do {
					image_id = cur.getInt(image_idColumn);
					image_path = cur.getString(dataColumn);
					thumbnails.put(image_id, "file://"+image_path);
				} while (cur.moveToNext());
			}
			//获取原图,在不存在缩略图时就要调用原图了
			Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, "date_modified DESC");  

			HashMap<String,AlbumInfo> myhash = new HashMap<String, AlbumInfo>();
			AlbumInfo albumInfo = null;
			PhotoInfo photoInfo = null;
			if (cursor!=null&&cursor.moveToFirst())
			{
				do{  
					int index = 0;
					int _id = cursor.getInt(cursor.getColumnIndex("_id")); 
					String path = cursor.getString(cursor.getColumnIndex("_data"));
					String album = cursor.getString(cursor.getColumnIndex("bucket_display_name"));  
					photoInfo = new PhotoInfo();
					if(myhash.containsKey(album)){
						albumInfo = myhash.remove(album);
						if(list.contains(albumInfo))
							index = list.indexOf(albumInfo);
						photoInfo.setImage_id(_id);
						photoInfo.setPath_file("file://"+path);
						photoInfo.setPath_absolute(path);
						albumInfo.getList().add(photoInfo);
						list.set(index, albumInfo);
						myhash.put(album, albumInfo);
					}else{
						List<PhotoInfo> stringList = new ArrayList<PhotoInfo>();
						albumInfo = new AlbumInfo();
						stringList.clear();
						photoInfo.setImage_id(_id);
						photoInfo.setPath_file("file://"+path);
						photoInfo.setPath_absolute(path);
						stringList.add(photoInfo);
						albumInfo.setImage_id(_id);
						albumInfo.setPath_file("file://"+path);
						albumInfo.setPath_absolute(path);
						albumInfo.setName_album(album);
						albumInfo.setList(stringList);
						list.add(albumInfo);
						myhash.put(album, albumInfo);
					}
				}while (cursor.moveToNext());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
		}
	}
	/**
	 * 
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * 
	 * @return degree
	 * 			      旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
	/**
	 * 旋转图片一定角度 rotaingImageView 
	 * 
	 * @return Bitmap 
	 * @throws
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}
	/**
	 * 将图片转化为圆形头像
	 * 
	 * @Title: toRoundBitmap @throws
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;

			left = 0;
			top = 0;
			right = width;
			bottom = width;

			height = width;

			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;

			float clip = (width - height) / 2;

			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;

			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);// 设置画笔无锯齿

		canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas

		// 以下有两种方法画圆,drawRounRect和drawCircle
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
		// canvas.drawCircle(roundPx, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
		canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

		return output;
	}
	/**
	 * saveBitmap
	 * 
	 * @param @param filename---完整的路径格式-包含目录以及文件名
	 * @param @param bitmap 
	 * @param @param isDelete --是否只留一张
	 * @return void 
	 * @throws
	 */
	public static void saveBitmap(String dirpath, String filename, Bitmap bitmap, boolean isDelete) {
		File dir = new File(dirpath);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		File file = new File(dirpath, filename);
		// 若存在即删除-默认只保留一张
		if (isDelete) {
			if (file.exists()) {
				file.delete();
			}
		}

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
				out.flush();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
