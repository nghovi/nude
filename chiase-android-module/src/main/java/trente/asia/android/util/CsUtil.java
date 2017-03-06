package trente.asia.android.util;

import java.util.List;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.view.model.ChiaseSpinnerModel;

/**
 * <strong>CsUtil</strong><br>
 * <br>
 * 
 * @author TrungND
 * @version $Id$
 */
public class CsUtil{

	/**
	 * <strong>findPosition4Spinner</strong><br>
	 * <br>
	 * find position in spinner for key
	 *
	 * @return
	 */
	public static int findPosition4Spinner(List<ChiaseSpinnerModel> lstModel, String key){

		if(CCCollectionUtil.isEmpty(lstModel) || CCStringUtil.isEmpty(key)){
			return 0;
		}

		int position = 0;
		for(ChiaseSpinnerModel model : lstModel){
			if(key.equals(model.key)){
				return position;
			}else{
				position++;
			}
		}
		return 0;
	}

}
