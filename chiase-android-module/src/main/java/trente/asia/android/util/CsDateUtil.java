package trente.asia.android.util;

import java.util.Calendar;
import java.util.Date;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCStringUtil;

/**
 * <strong>CsDateUtil</strong><br>
 * <br>
 * 
 * @author HoanLV
 * @version $Id$
 */
public class CsDateUtil {

	public static Integer getAge(String birthday){

		if(CCStringUtil.isEmpty(birthday)){
			return null;
		}
		Date birthdayDate = CCDateUtil.makeDate(birthday);

		Integer age = getAge(birthdayDate);
		return age;
	}

	/**
	 * <strong>getAge</strong><br>
	 * <br>
	 * 
	 * @param birthday
	 * @return
	 */
	public static Integer getAge(Date birthday){
		// Reference:
		// http://stackoverflow.com/questions/15128851/java-how-to-calculate-age-with-day-month-year

		if(birthday == null){
			return 0;
		}else{
			Calendar calNow = Calendar.getInstance();

			Calendar calBirthday = Calendar.getInstance();
			calBirthday.setTime(birthday);

			Integer factor = 0;
			if(calNow.get(Calendar.DAY_OF_YEAR) < calBirthday.get(Calendar.DAY_OF_YEAR)){
				factor = -1;
			}

			Integer age = calNow.get(Calendar.YEAR) - calBirthday.get(Calendar.YEAR) + factor;
			return age;
		}

	}
}