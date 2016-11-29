package trente.asia.android.view.util;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.define.CAConst;
import trente.asia.android.view.ChiaseButton;
import trente.asia.android.view.ChiaseDateView;
import trente.asia.android.view.ChiaseEditText;
import trente.asia.android.view.ChiaseNumericEditText;
import trente.asia.android.view.ChiaseRadioGroup;
import trente.asia.android.view.ChiaseSeekBar;
import trente.asia.android.view.ChiaseSpinner;
import trente.asia.android.view.ChiaseSwitch;
import trente.asia.android.view.ChiaseTextView;
import trente.asia.android.view.model.ChiaseSpinnerModel;

/**
 * <strong>CCSerializeObjectUtil</strong><br>
 * <br>
 *
 * @author TrungND
 * @version $Id$
 */
public class CAObjectSerializeUtil{

	/**
	 * <strong>serializeObject</strong><br>
	 * <br>
	 *
	 * @return
	 */
	public static JSONObject serializeObject(ViewGroup layout, JSONObject jsonObject){
		if(layout == null){
			return null;
		}

		if(jsonObject == null){
			jsonObject = new JSONObject();
		}
		try{
			Resources resources = layout.getContext().getResources();
			for(int i = 0; i < layout.getChildCount(); i++){
				String name = "", value = "";
				View view = layout.getChildAt(i);
				if(view instanceof ChiaseEditText){
					name = ((ChiaseEditText)view).getmName();
                    if(view instanceof ChiaseNumericEditText){
                        value = ((ChiaseNumericEditText)view).getNumericString();
                    }else{
                        value = CCStringUtil.toString(((EditText)view).getText());
                    }
				}else if(view instanceof ChiaseSpinner){
					name = ((ChiaseSpinner)view).getmName();
					ChiaseSpinnerModel item = (ChiaseSpinnerModel)((Spinner)view).getSelectedItem();
					if(item != null){
						value = item.getKey();
					}
				}else if(view instanceof ChiaseSeekBar){
					ChiaseSeekBar bar = (ChiaseSeekBar)view;
					name = bar.getmName();
					value = CCStringUtil.toString((int)bar.getValue());
				}else if(view instanceof ChiaseSwitch){
					name = ((ChiaseSwitch)view).getmName();
					value = CCStringUtil.toString(((Switch)view).isChecked());
				}else if(view instanceof ChiaseTextView){
					name = ((ChiaseTextView)view).getmName();
					value = ((ChiaseTextView)view).getValue();
				}else if(view instanceof ChiaseRadioGroup){
					name = ((ChiaseRadioGroup)view).mName;
					value = ((ChiaseRadioGroup)view).getValue();
				}else if(view instanceof ViewGroup){
					serializeObject((ViewGroup)view, jsonObject);
				}else if(view instanceof ChiaseDateView){
					name = ((ChiaseDateView)view).getmName();
					value = CCStringUtil.toString(((ChiaseDateView)view).getText());
				}

				if(!CCStringUtil.isEmpty(name) && !CCStringUtil.isEmpty(value)){
					jsonObject.put(name, value);
				}
			}
		}catch(JSONException e){
			e.printStackTrace();
			Log.e(CAConst.ROOT, e.getMessage());
		}
		return jsonObject;
	}

	/**
	 * <strong>deserializeObject</strong><br>
	 * <br>
	 *
	 * @return
	 */
	public static void deserializeObject(ViewGroup layout, JSONObject jsonObject){
		if(layout == null || jsonObject == null){
			return;
		}

		for(int i = 0; i < layout.getChildCount(); i++){
			String name = "";
			View view = layout.getChildAt(i);
			if(view instanceof ChiaseEditText){
				name = ((ChiaseEditText)view).getmName();
				if(jsonObject.has(name)){
					((EditText)view).setText(jsonObject.optString(name));
				}
				// }else if(view instanceof ChiaseSpinner){
				// name = ((ChiaseSpinner)view).getmName();
				// String defaultValue = "";
				// Map<String, String> map = CCCollectionUtil.convertString2Map(jsonObject.getString(((ChiaseSpinner)view).getNameMap()));
				// if(!CCCollectionUtil.isEmpty(map)){
				// defaultValue = jsonObject.getString(name);
				// CAViewUtil.initSpinner((ChiaseSpinner)view, map, defaultValue);
				// }
			}else if(view instanceof ChiaseTextView){
				name = ((ChiaseTextView)view).getmName();
				if(jsonObject.has(name)){
					((ChiaseTextView)view).setValue(jsonObject.optString(name));
				}
			}else if(view instanceof ViewGroup){
				deserializeObject((ViewGroup)view, jsonObject);
			}
			// else if(view instanceof ChiaseImageView){
			// name = ((ChiaseImageView)view).getmName();
			// if(jsonObject.has(name) && !CCStringUtil.isEmpty(jsonObject.getString(name))){
			// byte[] decodedString = Base64.decode(jsonObject.getString(name), Base64.DEFAULT);
			// Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
			// ((ChiaseImageView)view).setImageBitmap(decodedByte);
			// }
			// }
		}
	}

	/**
	 * <strong>disabledButton</strong><br>
	 * <br>
	 *
	 * @return
	 */
	public static void disabledButton(ViewGroup layout, boolean disabled){
		if(layout == null){
			return;
		}

		for(int i = 0; i < layout.getChildCount(); i++){
			View view = layout.getChildAt(i);
			if(view instanceof ChiaseButton){
				view.setEnabled(disabled);
			}else if(view instanceof ViewGroup){
				disabledButton((ViewGroup)view, disabled);
			}
		}
	}
}
