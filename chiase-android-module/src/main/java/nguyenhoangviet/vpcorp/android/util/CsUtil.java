package nguyenhoangviet.vpcorp.android.util;

import java.io.File;
import java.util.List;

import android.os.Environment;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCStringUtil;
import nguyenhoangviet.vpcorp.android.view.model.ChiaseSpinnerModel;

/**
 * <strong>CsUtil</strong><br>
 * <br>
 * 
 * @author TrungND
 * @version $Id$
 */
public class CsUtil{

	/**
	 * <strong>findPosition4Spinner</strong><br>
	 * <br>
	 * find position in spinner for key
	 *
	 * @return
	 */
	public static int findPosition4Spinner(List<ChiaseSpinnerModel> lstModel, String key){

		if(CCCollectionUtil.isEmpty(lstModel) || CCStringUtil.isEmpty(key)){
			return 0;
		}

		int position = 0;
		for(ChiaseSpinnerModel model : lstModel){
			if(key.equals(model.key)){
				return position;
			}else{
				position++;
			}
		}
		return 0;
	}

	/**
	 * get root file of application
	 *
	 * @return string
	 */
	public static String getFilesFolderPath(String appFolder){
		File folder = new File(Environment.getExternalStorageDirectory(), appFolder);
		if(!folder.exists()){
			folder.mkdirs();
		}
		return folder.getAbsolutePath();
	}

	public static String makeAppFile(String appFolder, String fileName){
		String filePath = getFilesFolderPath(appFolder) + "/" + fileName;
		return filePath;
	}

}
