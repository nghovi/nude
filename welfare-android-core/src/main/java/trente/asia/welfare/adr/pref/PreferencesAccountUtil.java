package trente.asia.welfare.adr.pref;

import com.google.gson.Gson;

import android.content.Context;

import trente.asia.android.util.CAPreferences;
import trente.asia.welfare.adr.models.SettingModel;
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by TuVD on 8/6/2015.
 */
public class PreferencesAccountUtil{

	public static String	KEY_PREF_USER		= "pref_user";
	public static String	KEY_PREF_SETTING	= "pref_setting";

	public CAPreferences	pref;

	public PreferencesAccountUtil(Context cntx){
		pref = new CAPreferences(cntx);
	}

	public void clear(){
		pref.clear();
	}

	public void removeKey(String key){
		pref.removeKey(key);
	}

	public void set(String key, String val){
		pref.set(key, val);
	}

	public String get(String key){
		return pref.get(key);
	}

	/**
	 * Save Object User to Prefers.
	 *
	 * @param user
	 */
	public void saveUserPref(UserModel user){
		Gson gson = new Gson();
		String json = gson.toJson(user);
		pref.set(KEY_PREF_USER, json);
	}

	/**
	 * Get Object User from Prefers.
	 *
	 * @return
	 */
	public UserModel getUserPref(){
		String json = pref.get(KEY_PREF_USER);
		UserModel userCS = null;
		Gson gson = new Gson();
		userCS = gson.fromJson(json, UserModel.class);
		if(userCS == null) userCS = new UserModel();
		return userCS;
	}

	/**
	 * Save Object setting to Prefers.
	 *
	 * @param setting
	 */
	public void saveSetting(SettingModel setting){
		if(setting != null){
			SettingModel data = getSetting();
			if(isSettingChanged(setting, data)){
				Gson gson = new Gson();
				String json = gson.toJson(setting);
				pref.set(KEY_PREF_SETTING, json);
			}
		}
	}

	private boolean isSettingChanged(SettingModel data1, SettingModel data2){
		boolean isPublicLevel = data1.DR_PUBLIC_LEVEL != null && !data1.DR_PUBLIC_LEVEL.equals(data2.DR_PUBLIC_LEVEL);
		boolean isPushSetting = data1.WF_PUSH_SETTING != null && !data1.WF_PUSH_SETTING.equals(data2.WF_PUSH_SETTING);
		boolean isMaxFile = data1.WF_MAX_FILE_SIZE != null && !data1.WF_MAX_FILE_SIZE.equals(data2.WF_MAX_FILE_SIZE);
		boolean isAppointment = data1.SW_APPOINTMENT_ENABLED != null && !data1.SW_APPOINTMENT_ENABLED.equals(data2.SW_APPOINTMENT_ENABLED);
		return isPublicLevel || isPushSetting || isMaxFile || isAppointment;
	}

	/**
	 * Get Object setting from Prefers.
	 *
	 * @return
	 */
	public SettingModel getSetting(){
		String json = pref.get(KEY_PREF_SETTING);
		Gson gson = new Gson();
		SettingModel setting = gson.fromJson(json, SettingModel.class);
		if(setting == null) setting = new SettingModel();
		return setting;
	}

	/**
	 * Save Object FilterRecruitment to Prefers.
	 *
	 * @param object
	 */
	public void setModel(String key, Object object){
		Gson gson = new Gson();
		String json = gson.toJson(object);
		pref.set(key, json);
	}
}
