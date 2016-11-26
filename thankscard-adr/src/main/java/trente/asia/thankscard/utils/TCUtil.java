package trente.asia.thankscard.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.util.TypedValue;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import trente.asia.thankscard.commons.defines.TcConst;
import trente.asia.thankscard.services.common.model.HistoryModel;

/**
 * Created by viet on 2/15/2016.
 */
public class TCUtil{

	/**
	 * get root file of application
	 *
	 * @return string
	 */
	public static String getFilesFolderPath(){
		File folder = new File(Environment.getExternalStorageDirectory(), TcConst.APP_FOLDER);
		if(!folder.exists()){
			folder.mkdirs();
		}
		return folder.getAbsolutePath();
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static int dpToPx(int dp){
		return (int)(dp * Resources.getSystem().getDisplayMetrics().density);
	}

	public static int changeDpToPx(Context contect, int dp){
		Resources res = contect.getResources();
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
	}

	public static String getDateString(String dateStr, String sourceFormat, String destinateFormat){
		Date date = CCDateUtil.makeDateCustom(dateStr, sourceFormat);
		return getDateString(date, destinateFormat);
	}

	public static String getDateString(Date date, String destinateFormat){
		SimpleDateFormat destinateFormatter = new SimpleDateFormat(destinateFormat);
		return destinateFormatter.format(date);
	}

	/**
	 * find history in list for key
	 */
	public static int findHistory4Key(List<HistoryModel> lstHistory, String key){
		int position = 0;
		if(!CCCollectionUtil.isEmpty(lstHistory)){
			int index = 0;
			for(HistoryModel historyModel : lstHistory){
				if(historyModel.key.equals(key)){
					position = index;
					break;
				}
				index++;
			}
		}
		return position;
	}
}
