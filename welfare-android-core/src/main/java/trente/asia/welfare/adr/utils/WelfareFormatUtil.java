package trente.asia.welfare.adr.utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.welfare.adr.models.ApiObjectModel;

/**
 * WelfareFormatUtil
 *
 * @author TrungND
 */
public class WelfareFormatUtil{

	/**
	 * <strong>connect 2 string</strong><br>
	 * example: string1 - string2<br>
	 *
	 * @return
	 */
	public static String connect2String(String value1, String value2, String character){

		StringBuilder builder = new StringBuilder();
		if(!CCStringUtil.isEmpty(value1)){
			builder.append(value1);
			builder.append(" ");
		}
		if(!CCStringUtil.isEmpty(value1) || !CCStringUtil.isEmpty(value2)){
			builder.append(CCStringUtil.toString(character));
		}

		if(!CCStringUtil.isEmpty(value2)){
			builder.append(" ");
			builder.append(value2);
		}
		return builder.toString();
	}

	/**
	 * <strong>convert api object list to Map</strong><br>
	 *
	 * @return
	 */
	public static Map<String, String> convertList2Map(List<ApiObjectModel> list){

		if(CCCollectionUtil.isEmpty(list)){
			return null;
		}

		Map<String, String> map = new LinkedHashMap<>();
		for(ApiObjectModel model : list){
			map.put(model.key, model.value);
		}
		return map;
	}
}
