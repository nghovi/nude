package asia.chiase.core.util;

import java.util.List;

/**
 * <strong>CCStringEditUtil</strong><br>
 * <br>
 * 
 * @author takano-yasuhiro
 * @version $Id$
 */
public class CCStringEditUtil{

	public static String makeDivideString(List<String> list){
		String ret = "";
		StringBuilder builder = new StringBuilder();

		for(String str : list){
			builder.append(str).append(",");
		}

		if(builder.length() > 0){
			ret = builder.substring(0, builder.length() - 1);
		}

		return ret;
	}
}
