package asia.chiase.core.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import asia.chiase.core.exception.CCException;
import asia.chiase.core.util.comparator.ChiaseComparatorCalenderDesc;

/**
 * <strong>CCModelUtil</strong><br>
 * <br>
 * 
 * @author takano-yasuhiro
 * @version $Id$
 */
public class CCModelUtil{

	/**
	 * <strong>buildFieldList</strong><br>
	 * <br>
	 * 
	 * @param maps
	 * @param key
	 * @return
	 */
	public static <T>List<T> buildFieldList(List<?> maps, String key){

		List<T> result = new ArrayList<T>();
		try{
			for(Object obj : maps){

				@SuppressWarnings("unchecked")
				T value2 = (T)PropertyUtils.getProperty(obj, key);

				if(!CCStringUtil.isEmpty(value2)){
					result.add(value2);
				}
			}
		}catch(Exception ex){
			new CCException(ex);
			// logger.error("invalid key : " + key);
			return result;
		}
		return result;
	}

	/**
	 * <strong>getLatest</strong><br>
	 * <br>
	 * 
	 * @param list
	 * @return
	 */
	public static Calendar getLatest(List<Calendar> list){

		try{

			Collections.sort(list, new ChiaseComparatorCalenderDesc());
			return list.get(0);

		}catch(Exception ex){
			new CCException(ex);
			return null;
		}

	}
}
