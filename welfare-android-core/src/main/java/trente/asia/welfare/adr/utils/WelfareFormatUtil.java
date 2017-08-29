package trente.asia.welfare.adr.utils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.view.ChiaseTextView;
import trente.asia.welfare.adr.define.WelfareConst;
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

    /**
     * make date with server format: yyyy/MM/dd
     *
     * @param data
     * @return
     */
    public static Date makeDate(String data){
        Date date = CCDateUtil.makeDateCustom(data, WelfareConst.WF_DATE_TIME_DATE);
        return date;
    }

    /**
     * format date with server format: yyyy/MM/dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String 	formatDate(Date date){
        String data = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, date);
        return data;
    }

    /**
     * add # character at prefix
     *
     * @param color
     * @return
     */
    public static String formatColor(String color){
        String formatColor = "#" + color;
        return formatColor;
    }

    /**
     * set data, value for chiase textview
     *
     * @return
     */
    public static void setChiaseTextView(ChiaseTextView textView, String data){
        textView.setText(data);
        textView.setValue(data);
    }

    public static String removeTime4Date(String date){
        if(CCStringUtil.isEmpty(date)){
            return "";
        }
        return date.substring(0, 10);
    }
}
