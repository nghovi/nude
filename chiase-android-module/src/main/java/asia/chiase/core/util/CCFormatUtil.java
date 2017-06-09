package asia.chiase.core.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import asia.chiase.core.exception.CCException;

/**
 * <strong>CCFormatUtil</strong><br>
 * <br>
 * 
 * @author takano-yasuhiro
 * @version $Id$
 */
public class CCFormatUtil{

	/*
	 * Common date format
	 */
	/** Date formatter(yyyy/MM) */
	public static SimpleDateFormat	SDF_MONTH_SLS				= new SimpleDateFormat("yyyy/MM");

	/** Date formatter(yyyy/MM/dd) */
	public static SimpleDateFormat	SDF_DATE_SLS				= new SimpleDateFormat("yyyy/MM/dd");

	/** Date formatter(yyyyMMdd) */
	public static SimpleDateFormat	SDF_DATE_YMD				= new SimpleDateFormat("yyyyMMdd");

	/** Date formatter(yyyy-MM-dd) */
	public static SimpleDateFormat	SDF_DATE_HYP				= new SimpleDateFormat("yyyy-MM-dd");

	/** Date formatter(HH:mm) */
	public static SimpleDateFormat	SDF_TIME_CLN				= new SimpleDateFormat("HH:mm");

	public static final String		PRODUCT_PERCENT_STR			= "%";
	public static final String		PRODUCT_DATE_TIME_FORMAT	= "yyyy-MM-dd hh:mm:ss";
	public static final String		COMMON_DATE_TIME_FORMAT_12H	= "MMM dd, yyyy hh:mm:ss aa";
	public static final String		COMMON_DATE_TIME			= "yyyy/MM/dd";
	public static final String		PRODUCT_DECIMAL_FORMAT_STR	= "#,#00.00";
	public static final Character	PRODUCT_DECIMAL_SEPERATOR	= '.';
	public static final Character	PRODUCT_DECIMAL_GROUPING	= ',';

	/**
	 * <strong>formatQuantity</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @return
	 */
	public static String formatQuantity(Double data){

		try{
			if(data == null){
				return "";
			}

			return String.format("%.2f", data);

		}catch(Exception ex){
			new CCException(ex);
			return "";
		}
	}

	/**
	 * <strong>formatQuantity</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @return
	 */
	public static String formatQuantity(String data){

		try{
			if(CCStringUtil.isEmpty(data)){
				return "";
			}

			return String.format("%.2f", Double.valueOf(data));

		}catch(Exception ex){
			new CCException(ex);
			return "";
		}
	}

	/**
	 * @param data
	 * @return
	 */
	public static String formatAmount(BigDecimal data){

		try{
			if(data == null){
				return "";
			}

			DecimalFormat formatter = new DecimalFormat("#.###");
			return formatter.format(data);

		}catch(Exception ex){
			new CCException(ex);
			return "";
		}
	}

	public static String formatAmount(Long data){

		try{
			if(data == null){
				return "";
			}

			DecimalFormat formatter = new DecimalFormat("#.###");
			return formatter.format(data);

		}catch(Exception ex){
			new CCException(ex);
			return "";
		}
	}

	/**
	 * <strong>formatAmount</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @return
	 */
	public static String formatAmount(Double data){

		try{
			if(data == null){
				return "";
			}

			DecimalFormat formatter = new DecimalFormat("#.###");
			return formatter.format(data);

		}catch(Exception ex){
			new CCException(ex);
			return "";
		}
	}

	public static String formatAmount(Integer data){

		try{
			if(data == null){
				return "";
			}else{
				return formatAmount(CCStringUtil.toString(data));
			}
		}catch(Exception ex){
			new CCException(ex);
			return "";
		}
	}

	/**
	 * <strong>formatAmount</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @return
	 */
	public static String formatAmount(String data){

		try{
			if(CCStringUtil.isEmpty(data)){
				return "";
			}

			DecimalFormat formatter = new DecimalFormat("#,###");
			return formatter.format(Double.valueOf(data));

		}catch(Exception ex){
			new CCException(ex);
			return "";
		}
	}

	/**
	 * <strong>formatAmountToDisplay</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @return
	 */
	public static String formatAmountToDisplay(Object data){

		try{
			if(data == null){
				return "";
			}

			DecimalFormat formatter = new DecimalFormat("#,###,###,###.###");
			return formatter.format(data);
		}catch(Exception ex){
			new CCException(ex);
			return "";
		}
	}

	/**
	 * <strong>formatRate</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @return
	 */
	public static String formatRate(Double data){

		try{

			if(data == null || !CCNumberUtil.isDouble(Double.valueOf(data).toString())){
				return "--.--";
			}

			// DecimalFormatSymbols symbols = new DecimalFormatSymbols();
			// symbols.setDecimalSeparator('.');
			// symbols.setGroupingSeparator(',');
			// DecimalFormat ft = new DecimalFormat("#,#00.00",symbols);
			// return ft.format(data);

			return String.format("%.2f", Double.valueOf(data));

		}catch(Exception ex){
			new CCException(ex);
			return "";
		}
	}

	/**
	 * <strong>formatDecimal</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @return
	 */
	public static String formatDecimal(Double data){
		try{
			if(data == null){
				return null;
			}else{
				DecimalFormatSymbols symbols = new DecimalFormatSymbols();
				symbols.setDecimalSeparator(PRODUCT_DECIMAL_SEPERATOR);
				symbols.setGroupingSeparator(PRODUCT_DECIMAL_GROUPING);
				DecimalFormat ft = new DecimalFormat(PRODUCT_DECIMAL_FORMAT_STR, symbols);
				return ft.format(data);
			}
		}catch(Exception ex){
			new CCException(ex);
			return null;
		}

	}

	/**
	 * <strong>formatMail</strong><br>
	 * <br>
	 * 
	 * @param email
	 * @return
	 */
	public static boolean formatMail(String data){
		try{
			if(CCStringUtil.isEmpty(data)){
				return false;
			}else{
				String EMAIL_REGEX = "^[\\w.-_\\.+]*[\\w.-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
				return data.matches(EMAIL_REGEX);
			}
		}catch(Exception ex){
			new CCException(ex);
			return false;
		}
	}

	/**
	 * <strong>formatDate</strong><br>
	 * <br>
	 * 
	 * @param dt
	 * @return String yyyy/MM/dd
	 */
	public static String formatDate(Date dt){
		try{
			if(dt != null){
				// COMMON_DATE_FORMAT.format(date)
				// SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd");
				return CCFormatUtil.SDF_DATE_SLS.format(dt);
			}else
				return "";
		}catch(Exception ex){
			return "";
		}
	}

	/**
	 * <strong>formatDateYMD</strong><br>
	 * <br>
	 * 
	 * @param dt
	 * @return String yyyyMMdd
	 */
	public static String formatDateYMD(Date dt){
		try{
			if(dt != null){
				return CCFormatUtil.SDF_DATE_YMD.format(dt);
			}else
				return "";
		}catch(Exception ex){
			return "";
		}
	}

	/**
	 * <strong>formatDateYM</strong><br>
	 * <br>
	 *
	 * @param dt
	 * @return String YYYY/MM
	 */
	public static String formatDateYM(Date dt){
		try{
			if(dt != null){
				return CCFormatUtil.SDF_MONTH_SLS.format(dt);
			}else
				return "";
		}catch(Exception ex){
			return "";
		}
	}

	/**
	 * <strong>formatDateHYP</strong><br>
	 * <br>
	 *
	 * @param dt
	 * @return
	 */
	public static String formatDateHYP(Date dt){
		try{
			if(dt != null){
				return CCFormatUtil.SDF_DATE_HYP.format(dt);
			}else
				return "";
		}catch(Exception ex){
			return "";
		}
	}

	/**
	 * <strong>formatMonth</strong><br>
	 * <br>
	 * 
	 * @param dt
	 * @return yyyy/MM
	 */
	public static String formatMonth(Date dt){
		try{
			if(dt != null){
				SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM");
				return ft.format(dt);
			}else
				return "";
		}catch(Exception ex){
			return "";
		}
	}

	/**
	 * <strong>formatMonthHYP</strong><br>
	 * <br>
	 *
	 * @param dt
	 * @return
	 */
	public static String formatMonthHYP(Date dt){
		try{
			if(dt != null){
				SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM");
				return ft.format(dt);
			}else
				return "";
		}catch(Exception ex){
			return "";
		}
	}

	/**
	 * <strong>formatTime</strong><br>
	 * <br>
	 * 
	 * @param dt
	 * @return String HH:mm
	 */
	public static String formatTime(Date dt){
		try{
			if(dt != null){
				// SimpleDateFormat ft = new SimpleDateFormat("HH:mm");
				return SDF_TIME_CLN.format(dt);
			}else
				return "";
		}catch(Exception ex){
			return "";
		}
	}

	public static String formatTime(String dt){
		try{
			if(!CCStringUtil.isEmpty(dt)){
				// SimpleDateFormat ft = new SimpleDateFormat("HH:mm");
				return SDF_TIME_CLN.format(CCDateUtil.makeDate(dt));
			}else
				return "";
		}catch(Exception ex){
			return "";
		}

	}

	/**
	 * <strong>formatDateTime</strong><br>
	 * <br>
	 * 
	 * @param dt
	 * @return String yyyy/MM/dd HH:mm
	 */
	public static String formatDateTime(Date dt){
		try{
			SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			return ft.format(dt);
		}catch(Exception ex){
			return "";
		}
	}

	/**
	 * <strong>formatDateTimeSec</strong><br>
	 * <br>
	 * 
	 * @param dt
	 * @return String yyyy/MM/dd HH:mm:ss
	 */
	public static String formatDateTimeSec(Date dt){
		try{
			SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			return ft.format(dt);
		}catch(Exception ex){
			return "";
		}
	}

	/**
	 * <strong>formatDateTimeSecMin</strong><br>
	 * <br>
	 * 
	 * @param cal
	 * @return
	 */
	public static String formatDateTimeSecMin(Calendar cal){
		try{
			SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			return ft.format(cal.getTime());
		}catch(Exception ex){
			return "";
		}
	}

	/**
	 * <strong>formatDateTimeGmail</strong><br>
	 * <br>
	 * Format Date Time Gmail
	 * 
	 * @param date
	 * @return
	 * @author HoanLV
	 */
	public static String formatDateTimeGmail(Date date){
		if(date == null){
			return "";
		}else{
			SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
			Date today = new Date();

			String sDate = formatDate(date);
			String sToday = formatDate(today);

			if(sDate.equals(sToday)){
				// Today
				return formatTime(date);
			}else if(sdfYear.format(date).equals(sdfYear.format(today))){
				// This year
				SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd");
				return sdf2.format(date);
			}else{
				return sDate;
			}
		}
	}

	/**
	 * <strong>formatDateCustom</strong><br>
	 * <br>
	 * 
	 * @param fmt
	 * @param date
	 * @return
	 */
	public static String formatDateCustom(SimpleDateFormat fmt, Date date){
		try{
			if(date == null){
				return "";
			}else{
				return fmt.format(date);
			}
		}catch(Exception ex){
			new CCException(ex);
			return "";
		}
	}

	/**
	 * <strong>formatDateCustom</strong><br>
	 * <br>
	 * 
	 * @param pattern
	 * @param date
	 * @return
	 */
	public static String formatDateCustom(String pattern, Date date){
		try{
			if(date == null){
				return "";
			}else{
				SimpleDateFormat fmt = new SimpleDateFormat(pattern);
				return fmt.format(date);
			}
		}catch(Exception ex){
			new CCException(ex);
			return "";
		}
	}

	/**
	 * <strong>parseDateCustom</strong><br>
	 * <br>
	 * 
	 * @param fmt
	 * @param date
	 * @return
	 */
	public static Date parseDateCustom(SimpleDateFormat fmt, String date){
		try{
			if(CCStringUtil.isEmpty(date)){
				return null;
			}else{
				return fmt.parse(date);
			}
		}catch(Exception ex){
			new CCException(ex);
			return null;
		}
	}

	/**
	 * <strong>formatCurrency</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @param symbol
	 * @return String format ex: $100
	 */
	public static String formatCurrency(Object data, String symbol){

		try{
			if(data == null){
				return "";
			}

			DecimalFormat formatter = new DecimalFormat("#,###,###,###.###");
			return symbol + formatter.format(data);
		}catch(Exception ex){
			new CCException(ex);
			return "";
		}
	}

	/**
	 * <strong>formatStringMinute</strong><br>
	 * <br>
	 * for ex: 90 mins will show 01:30
	 * 
	 * @param obj
	 * @return String format HH:mm
	 */
	public static String formatStringMinute(Object obj){
		try{
			if(obj != null){
				int iTotalMin = Integer.parseInt(obj.toString());
				int iHour = iTotalMin / 60;
				int iMin = iTotalMin - 60 * iHour;
				return String.format("%02d", iHour) + ":" + String.format("%02d", iMin);
			}else{
				return "";
			}
		}catch(Exception exx){
			return "";
		}
	}

	/**
	 * <strong>isDoubleFormat</strong><br>
	 * <br>
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isDoubleFormat(String str){
		try{
			Double.valueOf(str);
			return true;
		}catch(Exception ex){
			return false;
		}
	}

	/**
	 * <strong>getCurrencySymbol</strong><br>
	 * <br>
	 * 
	 * @param comp_id
	 * @return
	 */
	public static String getCurrencySymbol(String comp_id){
		try{

			// SelectQuery<TrSystemsetting> query =
			// Database.query(TrSystemsetting.class);
			// query.setQualifier(exp1);
			// query.andQualifier(exp2);
			// TrSystemsetting tmp = query.fetchSingle();
			// Map<String,String> tmp_map =
			// TRAccessDatabaseUtil.getTrSystemConstMap(comp_id,"CURRENCY_MAP_SYM");
			// return tmp_map.get(tmp.getSettingValue());
			return "";
		}catch(Exception ex){
			new CCException(ex);
			return null;

		}
	}

	/**
	 * <strong>getPercentSymbol</strong><br>
	 * <br>
	 * 
	 * @return
	 */
	public static String getPercentSymbol(){
		return PRODUCT_PERCENT_STR;
	}

	/**
	 * <strong>formatZero</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @return
	 */
	public static String formatZero(int data){

		try{
			DecimalFormat formatter = new DecimalFormat("00");
			return formatter.format(data);
		}catch(Exception ex){
			new CCException(ex);
			return "00";
		}
	}

	/**
	 * <strong>formatZero</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @param count
	 * @return
	 */
	public static String formatZero(int data, int count){

		try{
			DecimalFormat formatter;
			switch(count){
			case 3:
				formatter = new DecimalFormat("000");
				return formatter.format(data);
			case 4:
				formatter = new DecimalFormat("0000");
				return formatter.format(data);
			case 5:
				formatter = new DecimalFormat("00000");
				return formatter.format(data);
			default:
				formatter = new DecimalFormat("00");
				return formatter.format(data);
			}

		}catch(Exception ex){
			new CCException(ex);
			return "00";
		}
	}

	/**
	 * <strong>formatSpace_front</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @param len
	 * @return
	 */
	public static String formatSpaceFront(String data, int len){
		int bytelen = getByteLength(data);
		for(int i = 0; i < (len - bytelen); i++){
			data = " " + data;
		}
		return data;
	}

	/**
	 * <strong>formatSpace_back</strong><br>
	 * 
	 * @param data
	 * @param len
	 * @return
	 */
	public static String formatSpaceBack(String data, int len){
		int bytelen = getByteLength(data);
		for(int i = 0; i < (len - bytelen); i++){
			data = data + " ";
		}
		return data;
	}

	/*
	 * 文字列の切り出し
	 */
	public static String leftB(String data, Integer len, String charset, String replaceChar){
		String strRet = data;
		try{
			int dataLen = getByteLength(data);
			if(dataLen > len){
				StringBuffer sb = new StringBuffer();
				int replaceCharLen = getByteLength(replaceChar);
				int cnt = 0;

				for(int i = 0; i < data.length(); i++){
					String tmpStr = data.substring(i, i + 1);
					byte[] b = tmpStr.getBytes(charset);
					if(cnt + b.length > len - replaceCharLen){
						strRet = sb.toString() + replaceChar;
						break;
					}else{
						sb.append(tmpStr);
						cnt += b.length;
					}
				}
			}
			return strRet;
		}catch(Exception e){
			e.printStackTrace();
			return strRet;
		}

	}

	/*
	 * 文字列のバイト長を取得
	 */
	public static int getByteLength(String value){
		int length = 0;
		for(int i = 0; i < value.length(); i++){
			char c = value.charAt(i);
			if(c >= 0x20 && c <= 0x7E){ // JISローマ字(ASCII)
				length++;
			}else if(c >= 0xFF61 && c <= 0xFF9F){ // JISカナ(半角カナ)
				length++;
			}else{ // その他(全角)
				length += 2;
			}
		}
		return length;
	}

	/**
	 * <strong>substring</strong><br>
	 * <br>
	 *
	 * @param str
	 * @param maxIndex
	 * @return
	 */
	public static String substring(String str, Integer maxIndex){
		if(CCStringUtil.isEmpty(str)){
			return "";
		}else{
			if(maxIndex.compareTo(str.length()) < 0){
				return str.substring(0, maxIndex);
			}
		}
		return str;
	}

	/**
	 * <strong>ellipsis</strong><br>
	 * <br>
	 *
	 * @param str
	 * @param maxIndex
	 * @return
	 */
	public static String ellipsis(String str, Integer maxIndex){
		if(CCStringUtil.isEmpty(str)){
			return "";
		}else{
			if(maxIndex.compareTo(str.length()) < 0){
				return str.substring(0, maxIndex) + "...";
			}
		}
		return str;
	}

}
