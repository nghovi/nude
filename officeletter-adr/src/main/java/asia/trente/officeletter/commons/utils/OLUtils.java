package asia.trente.officeletter.commons.utils;

import android.os.Environment;

import java.io.File;

import asia.trente.officeletter.commons.defines.OLConst;

/**
 * Created by tien on 8/18/2017.
 */

public class OLUtils {
    public static String getFilesFolderPath(){
        File folder = new File(Environment.getExternalStorageDirectory(), OLConst.APP_FOLDER);
        if(!folder.exists()){
            folder.mkdirs();
        }
        return folder.getAbsolutePath();
    }
}
