package trente.asia.addresscard.commons.utils;

import android.content.Context;
import android.database.Cursor;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import org.apache.commons.collections.CollectionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import trente.asia.addresscard.services.business.model.CardModel;
import trente.asia.addresscard.services.shop.model.TagModel;

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

    public static String getSize(List<CardModel> list) {
        return String.valueOf(list.size());
    }
}
