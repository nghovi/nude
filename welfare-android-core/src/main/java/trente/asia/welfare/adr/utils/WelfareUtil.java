package trente.asia.welfare.adr.utils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.util.AndroidUtil;
import trente.asia.welfare.adr.R;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.ApiObjectModel;
import trente.asia.welfare.adr.models.DeptModel;
import trente.asia.welfare.adr.models.UserModel;

/**
 * WelfareUtil
 *
 * @author TrungND
 */
public class WelfareUtil{

	/**
	 * <strong>size</strong><br>
	 * <br>
	 *
	 * @param list
	 * @return
	 */
	public static int size(Collection<?> list){

		if(CCCollectionUtil.isEmpty(list)){
			return 0;
		}else{
			return list.size();
		}
	}

	/**
	 * <strong>divideFilenameByExt</strong><br>
	 * <br>
	 *
	 * @param fileName
	 * @return
	 */
	public static String[] divideFilenameByExt(String fileName){

		String[] result = new String[2];

		result[0] = fileName.substring(0, fileName.lastIndexOf("."));
		result[1] = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		return result;

	}

	/**
	 * <strong>getFileType</strong><br>
	 * <br>
	 *
	 * @param filename
	 * @return
	 */
	public static String getItemFileType(String filename){

		String names[] = divideFilenameByExt(filename);

		if(WelfareConst.IMG_EXT_SET.contains(names[1])){
			return WelfareConst.ITEM_FILE_TYPE_PHOTO;
		}

		if(WelfareConst.MOVIE_EXT_SET.contains(names[1])){
			return WelfareConst.ITEM_FILE_TYPE_MOVIE;
		}

		return WelfareConst.ITEM_FILE_TYPE_FILE;

	}

	/**
	 * get image path from uri
	 *
	 * @param cntx
	 * @param uri uri of file
	 * @param isOverJellyBeam is android version over jelly beam version
	 * @return string
	 */
	public static String getImagePath(Context cntx, Uri uri, boolean isOverJellyBeam, String desPath){

		if(isOverJellyBeam){
			try{
				ParcelFileDescriptor parcelFileDescriptor = cntx.getContentResolver().openFileDescriptor(uri, "r");
				FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

				AndroidUtil.copyStream(new FileInputStream(fileDescriptor), new FileOutputStream(new File(desPath)));
				parcelFileDescriptor.close();
				return desPath;
			}catch(Exception e){
				e.printStackTrace();
				return "";
			}

		}else{

			String[] projection = {MediaStore.Images.Media.DATA};
			Cursor cursor = cntx.getContentResolver().query(uri, projection, null, null, null);

			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();

			String returnedString = cursor.getString(column_index);
			cursor.close();

			return returnedString;
		}
	}

	/**
	 * Return url photo map from lat,lon.
	 *
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public static String getGoogleUrl(String latitude, String longitude, int mapWidthSize, int mapHeightSize){
		String url = "http://maps.googleapis.com/maps/api/staticmap";
		String extension = "?center=" + latitude + "," + longitude + "&zoom=" + 17 + "&scale=1" + "&size=" + mapWidthSize + "x" + mapHeightSize + "&markers=color:red%7C" + latitude + "," + longitude + "&sensor=false";

		url += extension;
		return url;
	}

	public static String getDisplayNum(int num){
		return num <= 9 ? "0" + num : "" + num;
	}

	/*
	 * @return "2016/09"
	 */
	public static String getYearMonthStr(int year, int monthOfYear){
		return year + "/" + getDisplayNum(monthOfYear);
	}

	/**
	 * make date with server format: yyyy/MM/dd HH:mm:ss
	 *
	 * @param data
	 * @return
	 */
	public static Date makeDate(String data){
		Date date = CCDateUtil.makeDateCustom(data, WelfareConst.WL_DATE_TIME_1);
		return date;
	}

	public static Date makeMonthWithFirstDate(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	public static Date addMonth(Date date, int add){
		Calendar calendar = CCDateUtil.makeCalendar(date);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + add);
		return calendar.getTime();
	}

	public static Date addDate(Date date, int add){
		Calendar calendar = CCDateUtil.makeCalendar(date);
		calendar.add(Calendar.DATE, add);
		return calendar.getTime();
	}

	/**
	 * convert user model list -> user name list
	 *
	 * @param lstUser
	 */
	public static List<String> convert2UserName(List<UserModel> lstUser){
		List<String> lstUserName = new ArrayList<>();
		if(!CCCollectionUtil.isEmpty(lstUser)){
			for(UserModel userModel : lstUser){
				lstUserName.add(userModel.userName);
			}
		}
		return lstUserName;
	}

	/**
	 * convert user model list -> user id list
	 *
	 * @param lstUser
	 */
	public static List<String> convert2UserId(List<UserModel> lstUser){
		List<String> lstUserName = new ArrayList<>();
		if(!CCCollectionUtil.isEmpty(lstUser)){
			for(UserModel userModel : lstUser){
				lstUserName.add(userModel.key);
			}
		}
		return lstUserName;
	}

	/**
	 * convert user model list -> user name list
	 *
	 * @param location
	 */
	public static String getAddress4Location(Activity activity, Location location){
		List<Address> addresses;
		Geocoder geocoder = new Geocoder(activity, Locale.getDefault());

		try{
			StringBuilder addressAll = new StringBuilder("");
			addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
			String address = addresses.get(0).getAddressLine(0);
			String city = addresses.get(0).getLocality();
			String state = addresses.get(0).getAdminArea();
			String country = addresses.get(0).getCountryName();

			addToStringIfNotEmpty(addressAll, address);
			addToStringIfNotEmpty(addressAll, city);
			addToStringIfNotEmpty(addressAll, state);
			addToStringIfNotEmpty(addressAll, country);

			return addressAll.toString();
		}catch(IOException e){
			e.printStackTrace();
		}
		return "";
	}

	public static void addToStringIfNotEmpty(StringBuilder builder, String toAdd){
		if(!CCStringUtil.isEmpty(toAdd)){
			if(CCStringUtil.isEmpty(builder.toString())){
				builder.append(toAdd);
			}else{
				builder.append(", " + toAdd);
			}
		}
	}

	public static DeptModel getDept4UserId(List<DeptModel> lstDept, String userId){
		if(!CCCollectionUtil.isEmpty(lstDept)){
			for(DeptModel deptModel : lstDept){
				if(DeptModel.KEY_ALL.equals(deptModel.key)){
					continue;
				}
				for(UserModel userModel : deptModel.members){
					if(userModel.key.equals(userId)){
						return deptModel;
					}
				}
			}
		}

		return null;
	}

	/**
	 * convert dept model list -> map
	 */
	public static Map<String, String> convertDept2Map(List<DeptModel> lstDept, Context context){
		Map<String, String> map = new LinkedHashMap<>();
		map.put(CCConst.NONE, context.getString(R.string.chiase_common_all));
		for(DeptModel deptModel : lstDept){
			map.put(deptModel.key, deptModel.deptName);
		}
		return map;
	}

	// // TODO: 9/6/2016 how to turn 1000.25 into 1,000.25 ?
	// Requirement: if kpi is SALE or COUNT, use format Account
	// if kpi is time, max is 99.99
	public static String formatAmount(String data){
		return CCFormatUtil.formatAmount(data);
	}

	public static String getAmount(String formattedAmount){
		return formattedAmount.replaceAll(",", "");
	}

	public static int findUserInList(List<UserModel> lstUser, UserModel findUser){
		int index = 0;
		if(!CCCollectionUtil.isEmpty(lstUser) && findUser != null){
			int loop = 0;
			for(UserModel userModel : lstUser){
				if(userModel.key.equals(findUser.key)){
					index = loop;
					break;
				}
				loop++;
			}
		}
		return index;
	}

    public static boolean containUserInList(List<UserModel> lstUser, UserModel findUser){
        if(!CCCollectionUtil.isEmpty(lstUser) && findUser != null){
            for(UserModel userModel : lstUser){
                if(userModel.key.equals(findUser.key)){
                    return true;
                }
            }
        }
        return false;
    }

    public static void addInList(List<UserModel> lstUser, UserModel findUser){
        if(!CCCollectionUtil.isEmpty(lstUser) && findUser != null){
            if(!containUserInList(lstUser, findUser)){
                lstUser.add(findUser);
            }
        }
    }

    public static void removeInList(List<UserModel> lstUser, UserModel findUser){
        if(!CCCollectionUtil.isEmpty(lstUser) && findUser != null){
            if(containUserInList(lstUser, findUser)){
                for(UserModel userModel : lstUser){
                    if(userModel.key.equals(findUser.key)){
                        findUser = userModel;
                    }
                }
                lstUser.remove(findUser);
            }
        }
    }

	public static int findDeptInList(List<DeptModel> lstDept, DeptModel findDept){
		int index = 0;
		if(!CCCollectionUtil.isEmpty(lstDept) && findDept != null){
			for(DeptModel deptModel : lstDept){
				if(deptModel.key.equals(findDept.key)){
					break;
				}
				index++;
			}
		}
		return index;
	}

	public static void startCropActivity(Fragment fragment, Uri inputImage, Uri outputUri){
		try{
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setData(inputImage);
			intent.putExtra("outputX", WelfareConst.PROFILE_SIZE_TB);
			intent.putExtra("outputY", WelfareConst.PROFILE_SIZE_TB);
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("scale", true);
			intent.putExtra("noFaceDetection", true);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
			fragment.startActivityForResult(intent, WelfareConst.RequestCode.PHOTO_CROP);
		}catch(Exception ex){
			ex.printStackTrace();
		}
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
			Toast.makeText(context, context.getString(R.string.wf_common_invalid_permission), Toast.LENGTH_LONG).show();
		}
		return null;
	}

	//// TODO: 12/22/2016 delete this function
	public static List<String> getServiceName(Context context){
		List<String> lstService = new ArrayList<>();
		lstService.add(context.getString(R.string.wf_thanks_card_service_name));
		lstService.add(context.getString(R.string.wf_messenger_service_name));
		lstService.add(context.getString(R.string.wf_daily_report_service_name));
		lstService.add(context.getString(R.string.wf_shift_working_service_name));
		lstService.add(context.getString(R.string.wf_fukuri_service_name));
		return lstService;
	}

	public static List<String> getServiceCd(){
		List<String> lstCd = new ArrayList<>();
		lstCd.add("");
		lstCd.add(WelfareConst.SERVICE_CD_TC);
		lstCd.add(WelfareConst.SERVICE_CD_MS);
		lstCd.add(WelfareConst.SERVICE_CD_DR);
		lstCd.add(WelfareConst.SERVICE_CD_SW);
		lstCd.add(WelfareConst.SERVICE_CD_FUKURI);
		return lstCd;
	}

	public static List<String> getContactTypeCd(){
		List<String> lstCd = new ArrayList<>();
		lstCd.add("");
		lstCd.add(WelfareConst.WF_CONTACT_PROBLEM);
		lstCd.add(WelfareConst.WF_CONTACT_IMPROVE);
		return lstCd;
	}

	public static ApiObjectModel findApiObject4Id(List<ApiObjectModel> lstObject, String key){
		ApiObjectModel apiObjectModel = null;
		if(!CCCollectionUtil.isEmpty(lstObject)){
			for(ApiObjectModel model : lstObject){
				if(model.key.equals(key)){
					apiObjectModel = model;
					break;
				}
			}
		}
		return apiObjectModel;
	}

	public static int dpToPx(int dp){
		return (int)(dp * Resources.getSystem().getDisplayMetrics().density);
	}

	public static int convertSp2Px(int sp, Context context){
		return (int)(context.getResources().getDisplayMetrics().scaledDensity * sp);
	}
}
