package nguyenhoangviet.vpcorp.messenger.commons.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.widget.Toast;

import asia.chiase.core.util.CCCollectionUtil;
import nguyenhoangviet.vpcorp.android.util.AndroidUtil;
import nguyenhoangviet.vpcorp.messenger.commons.defines.MsConst;
import nguyenhoangviet.vpcorp.messenger.services.message.model.RealmUserModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.LikeModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;

/**
 * MsUtils
 *
 * @author TrungND
 */
public class MsUtils{

	/**
	 * get root file of application
	 * 
	 * @return string
	 */
	public static String getFilesFolderPath(){
		File folder = new File(Environment.getExternalStorageDirectory(), MsConst.APP_FOLDER);
		if(!folder.exists()){
			folder.mkdirs();
		}
		return folder.getAbsolutePath();
	}

	/**
	 * get user location
	 *
	 * @param context version to compare
	 */
	public static Location getLocation(Activity context){

		try{
			if(AndroidUtil.verifyLocationPermissions(context)){
				LocationManager mLocationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
				List<String> providers = mLocationManager.getProviders(true);
				Location bestLocation = null;
				for(String provider : providers){
					Location lastKnownLocation = mLocationManager.getLastKnownLocation(provider);
					if(lastKnownLocation == null){
						continue;
					}
					if(bestLocation == null || lastKnownLocation.getAccuracy() < bestLocation.getAccuracy()){
						// Found best last known location: %s", l);
						bestLocation = lastKnownLocation;
					}
				}
				return bestLocation;
			}
		}catch(SecurityException ex){
			Toast.makeText(context, "You don't have permission", Toast.LENGTH_LONG).show();
		}
		return null;

		// try{
		// String locationProvider = LocationManager.NETWORK_PROVIDER;
		// LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		// Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
		// return lastKnownLocation;
		// }catch(SecurityException ex){
		// Toast.makeText(context, "You don't have permission", Toast.LENGTH_LONG).show();
		// }
		// return null;
	}

	/**
	 * convert like model 2 user id
	 *
	 * @param lstLike
	 */
	public static List<String> convert2UserId(List<LikeModel> lstLike){

		if(CCCollectionUtil.isEmpty(lstLike)) return null;

		List<String> lstUserId = new ArrayList<>();
		for(LikeModel likeModel : lstLike){
			lstUserId.add(likeModel.likeUser.key);
		}
		return lstUserId;
	}

	/**
	 * <strong>replace all</strong><br>
	 * <br>
	 *
	 * @param original
	 * @return
	 */
	public static SpannableStringBuilder replaceAll(Context context, String original, String regex, int resourceId){

		SpannableStringBuilder builder = new SpannableStringBuilder();
		builder.append(original);
		ImageSpan imageSpan = new ImageSpan(context, resourceId);

		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(original);

		while(match.find()){
			int startPosition = match.start() - 1;
			builder.setSpan(imageSpan, startPosition, startPosition + regex.length(), 0);
		}

		return builder;
	}

	/**
	 * find user from user name
	 *
	 * @param lstUser
	 */
	public static UserModel findUser4UserName(List<UserModel> lstUser, String userName){

		UserModel userModel = null;
		for(UserModel user : lstUser){
			if(user.userName.equals(userName)){
				userModel = user;
				break;
			}
		}
		return userModel;
	}

	/**
	 * find user from user name
	 *
	 * @param lstUser
	 */
	public static RealmUserModel findUser4AccountName(List<RealmUserModel> lstUser, String accountName){

		RealmUserModel userModel = null;
		for(RealmUserModel user : lstUser){
			if(user.userAccount.equals(accountName)){
				userModel = user;
				break;
			}
		}
		return userModel;
	}
}
