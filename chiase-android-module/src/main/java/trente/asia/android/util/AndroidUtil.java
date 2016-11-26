package trente.asia.android.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.text.DecimalFormat;

import javax.microedition.khronos.opengles.GL10;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;

import trente.asia.android.define.CAConst;

/**
 * Created by takyas on 9/1/15.
 */
public class AndroidUtil{

	/**
	 * バージョンコードを取得する
	 *
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context){
		PackageManager pm = context.getPackageManager();
		int versionCode = 0;
		try{
			PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
			versionCode = packageInfo.versionCode;
		}catch(PackageManager.NameNotFoundException e){
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * バージョン名を取得する
	 *
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context){
		PackageManager pm = context.getPackageManager();
		String versionName = "";
		try{
			PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = packageInfo.versionName;
		}catch(PackageManager.NameNotFoundException e){
			e.printStackTrace();
		}
		return versionName;

	}

	public static Uri getUriFromFileInternal(Context context, File file){
		Uri uri = null;
		if(file.exists()){
			try{
				uri = Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), null, null));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return uri;
	}

	public static boolean isNetwork(Context activity){
		try{
			ConnectivityManager connectivityManager = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info1 = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo info2 = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo.State val1 = (info1 == null ? null : info1.getState());
			NetworkInfo.State val2 = (info2 == null ? null : info2.getState());
			return (info1 != null && NetworkInfo.State.CONNECTED.equals(val1)) || (info2 != null && NetworkInfo.State.CONNECTED.equals(val2));
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * invalidInternet.
	 */
	public static boolean invalidInternet(final Activity activity){
		if(!AndroidUtil.isNetwork(activity)){
			return true;
		}
		return false;
	}

	/**
	 * checked if android version is above given version
	 *
	 * @param version version to compare
	 * @return true is above, false is equals or below
	 */
	public static boolean isBuildOver(int version){
		if(android.os.Build.VERSION.SDK_INT > version) return true;
		return false;
	}

	/**
	 * get video path from url
	 *
	 * @param uri version to compare
	 * @return video path
	 */
	public static String getVideoPath(Context context, Uri uri){

		if(uri.getScheme().equals("content")){

			String[] proj = {MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.DISPLAY_NAME};
			Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);

			int column_index_path = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
			cursor.moveToFirst();

			String returnedString = cursor.getString(column_index_path);

			cursor.close();

			return returnedString;

		}else if(uri.getScheme().equals("file")){
			return new File(URI.create(uri.toString())).getAbsolutePath();
		}

		return null;
	}

	/**
	 * get thumbnail at 1 second time.
	 *
	 * @param time retrieve thumbnail
	 * @return video path
	 */
	public static Bitmap getCustomThumbnail(MediaMetadataRetriever retriever, long time){
		Bitmap thumbnail = null;
		// 秒単位で指定
		thumbnail = retriever.getFrameAtTime(1 * 1000 * time);
		// サムネイルを任意のサイズにリサイズ
		thumbnail = ThumbnailUtils.extractThumbnail(thumbnail, 100, 75);

		return thumbnail;
	}

	/**
	 * return string of given size example 1MB, 1,4GB, 300KB
	 *
	 * @param size size to convert
	 * @return string
	 */
	public static String readableFileSize(long size){
		if(size <= 0) return "0";
		final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
		int digitGroups = (int)(Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

	/**
	 * copy stream without progress listener
	 *
	 * @param is input stream
	 * @param os output stream
	 */
	public static void copyStream(InputStream is, OutputStream os){
		copyStream(is, os, -1, null);
	}

	/**
	 * copy stream with progress listener
	 *
	 * @param is input stream
	 * @param os output stream
	 * @param length length of stream
	 * @param listener progress listener
	 */
	public static void copyStream(InputStream is, OutputStream os, long length, DownloadFileManager.OnDownloadListener listener){
		final int buffer_size = 1024;
		int totalLen = 0;
		try{

			byte[] bytes = new byte[buffer_size];
			while(true){
				// Read byte from input stream

				int count = is.read(bytes, 0, buffer_size);
				if(count == -1){
					if(listener != null){
						listener.onFinishDownload();
					}
					break;
				}

				// Write byte from output stream
				if(length != -1 && listener != null){
					totalLen = totalLen + count;
					if(listener != null){
						listener.onProgress(totalLen);
					}
				}
				os.write(bytes, 0, count);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public static File resizeFile(Activity activity, File sourceFile, String desFilePath){
		try{
			// InputStream imageStream = activity.getContentResolver().openInputStream(Uri.fromFile(sourceFile));
			ExifInterface exif = new ExifInterface(sourceFile.getPath());
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			int angle = 0;

			if(orientation == ExifInterface.ORIENTATION_ROTATE_90){
				angle = 90;
			}else if(orientation == ExifInterface.ORIENTATION_ROTATE_180){
				angle = 180;
			}else if(orientation == ExifInterface.ORIENTATION_ROTATE_270){
				angle = 270;
			}

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			Bitmap bmpImage = BitmapFactory.decodeFile(sourceFile.getPath(), options);
			// BitmapFactory.Options options = new BitmapFactory.Options();
			// options.inSampleSize = 2;
			// Bitmap bmpImage = BitmapFactory.decodeStream(imageStream, null, options);
			Matrix mat = new Matrix();
			boolean isNeedResize = false;

			if(angle % 360 != 0){
				// rotate image
				mat.postRotate(angle % 360);
				isNeedResize = true;
			}
			if(bmpImage.getWidth() > GL10.GL_MAX_TEXTURE_SIZE / 2 || bmpImage.getHeight() > GL10.GL_MAX_TEXTURE_SIZE / 2){
				isNeedResize = true;
				// mat.postScale(bmpImage.getWidth(), bmpImage.getHeight());
			}

			if(isNeedResize){
				Bitmap scaleImage = Bitmap.createBitmap(bmpImage, 0, 0, bmpImage.getWidth(), bmpImage.getHeight(), mat, false);
				if(scaleImage != null){
					File desFile = new File(desFilePath);
					FileOutputStream fOut = new FileOutputStream(desFile);
					scaleImage.compress(Bitmap.CompressFormat.JPEG, 80, fOut);
					fOut.flush();
					fOut.close();

					return desFile;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return sourceFile;
	}

	/**
	 * Get size of screen width.
	 *
	 * @param activity
	 * @return
	 */
	public static int getWidthScreen(Activity activity){
		DisplayMetrics displayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics.widthPixels;
	}

	/**
	 * with android version >= 6.0, We have to check storage permission any time
	 *
	 * @param activity
	 * @return
	 */
	public static boolean verifyStoragePermissions(Activity activity){
		// Check if we have write permission
		int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

		if(writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED){
			// We don't have permission so prompt the user
			ActivityCompat.requestPermissions(activity, CAConst.PERMISSIONS_STORAGE, CAConst.REQUEST_EXTERNAL_STORAGE);
			return false;
		}
		return true;
	}

	/**
	 * with android version >= 6.0, We have to check location permission any time
	 *
	 * @param activity
	 * @return
	 */
	public static boolean verifyLocationPermissions(Activity activity){
		// Check if we have write permission
		int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);

		if(permission != PackageManager.PERMISSION_GRANTED){
			// We don't have permission so prompt the user
			ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, CAConst.REQUEST_EXTERNAL_STORAGE);
			return false;
		}
		return true;
	}

	public static Bitmap createBitmapFromResouce(Resources r, int id){
		Bitmap bmp = BitmapFactory.decodeResource(r, id);
		return bmp;
	}

	public static Uri getUriFromFile(Context context, File file){
		if(file == null){
			return null;
		}

		Uri outputFileUri = null;
		// check android version >= 7.0
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
			outputFileUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
		}else{
			outputFileUri = Uri.fromFile(file);
		}
		return outputFileUri;
	}
}
