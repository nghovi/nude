package trente.asia.addresscard.commons.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import trente.asia.addresscard.services.business.model.AddressCardModel;

/**
 * Created by tien on 5/18/2017.
 */

public class Utils {
    public static String getPathFromMediaUri(@NonNull Context context, @NonNull Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String copyImageToStorage(String folder, String fileName, Bitmap bitmap, int scale) {
        Bitmap scaleBitmap = scale == 1 ? bitmap : Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / scale,
                bitmap.getHeight() / scale, false);
        File pic = new File(folder, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(pic);
            scaleBitmap.compress(Bitmap.CompressFormat.PNG, 75, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        scaleBitmap.recycle();
        return pic.getAbsolutePath();
    }

    public static String createFolder(String folderName) {
        String storage = Environment.getExternalStorageDirectory().toString();
        new File(storage + "/" + folderName).mkdir();
        return storage + "/" +folderName;
    }

    public static String getSize(List<AddressCardModel> list) {
        return String.valueOf(list.size());
    }

    public static void startPhoneCall(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }

    public static void sendEmail(Context context, String mail) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mail});
        context.startActivity(intent);
    }

    public static void openMap(Context context, String address) {
        Intent searchAddress = new  Intent(Intent.ACTION_VIEW,Uri.parse("geo:0,0?q="+address));
        context.startActivity(searchAddress);
    }

    public static void openBrowser(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }
}
