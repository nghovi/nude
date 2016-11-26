package trente.asia.welfare.adr.pref;

import com.google.gson.Gson;

import android.content.Context;

import trente.asia.android.util.CAPreferences;
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by TuVD on 8/6/2015.
 */
public class PreferencesAccountUtil{

	public static String	KEY_PREF_USER	= "pref_user";

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
