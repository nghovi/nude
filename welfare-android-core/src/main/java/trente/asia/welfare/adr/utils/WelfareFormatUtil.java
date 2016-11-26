package trente.asia.welfare.adr.utils;

import asia.chiase.core.util.CCStringUtil;

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
}
