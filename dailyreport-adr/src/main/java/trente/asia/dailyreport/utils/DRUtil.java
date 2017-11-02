package nguyenhoangviet.vpcorp.dailyreport.utils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;

import asia.chiase.core.util.CCStringUtil;
import nguyenhoangviet.vpcorp.dailyreport.DRConst;
import nguyenhoangviet.vpcorp.dailyreport.services.report.model.ReportModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.CommentModel;

/**
 * Created by viet on 7/8/2016.
 */
public class DRUtil{

	/**
	 * get root file of application
	 *
	 * @return string
	 */
	public static String getFilesFolderPath(){
		File folder = new File(Environment.getExternalStorageDirectory(), DRConst.APP_FOLDER);
		if(!folder.exists()){
			folder.mkdirs();
		}
		return folder.getAbsolutePath();
	}

	public static int getFileNum(ReportModel reportModel){
		int fileNum = 0;
		for(CommentModel commentModel : reportModel.comments){
			if(CommentModel.COMMENT_TYPE_FILE.equals(commentModel.commentType) || CommentModel.COMMENT_TYPE_MOVIE.equals(commentModel.commentType) || CommentModel.COMMENT_TYPE_PHOTO.equals(commentModel.commentType)){
				fileNum++;
			}
		}
		return fileNum;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static boolean isNotEmpty(String str){
		return !CCStringUtil.isEmpty(str);
	}

	public static String getDateString(String dateStr, String sourceFormat, String destinateFormat){
		Date date = getDateFromString(dateStr, sourceFormat);
		return getDateString(date, destinateFormat);
	}

	public static Date getDateFromString(String dateStr, String format){
		SimpleDateFormat sourceFormatter = new SimpleDateFormat(format);
		try{
			return sourceFormatter.parse(dateStr);
		}catch(ParseException e){
			e.printStackTrace();
		}
		return null;
	}

	public static String getDateString(Date date, String destinateFormat){
		SimpleDateFormat destinateFormatter = new SimpleDateFormat(destinateFormat);
		return destinateFormatter.format(date);
	}

}
