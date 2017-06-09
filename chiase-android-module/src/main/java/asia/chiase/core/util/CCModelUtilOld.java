package asia.chiase.core.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import asia.chiase.core.exception.CCException;

public class CCModelUtilOld<M, T>{

	public List<T> buildFieldList(List<M> list, String key){

		List<T> result = new ArrayList<T>();
		try{
			for(M obj : list){
				// String value = BeanUtils.getProperty(obj, key);

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

	// public List<String> buildFieldList(List<M> list, String key){
	//
	// List<String> result = new ArrayList<String>();
	// try{
	// for(M obj : list){
	// String value = BeanUtils.getProperty(obj, key);
	// if(!CCStringUtil.isEmpty(value)){
	// result.add(value);
	// }
	// }
	// }catch(Exception ex){
	// logger.error(ex.getClass().getName() + ": " + ex.getMessage());
	// logger.error("invalid key : " + key);
	// return result;
	// }
	// return result;
	// }
}
