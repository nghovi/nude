package nguyenhoangviet.vpcorp.team360.services.member;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import io.realm.Realm;
import io.realm.RealmQuery;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import nguyenhoangviet.vpcorp.android.define.CsConst;
import nguyenhoangviet.vpcorp.android.exception.CAException;
import nguyenhoangviet.vpcorp.android.util.AndroidUtil;
import nguyenhoangviet.vpcorp.android.util.CARequestUtil;
import nguyenhoangviet.vpcorp.team360.BuildConfig;
import nguyenhoangviet.vpcorp.team360.services.entity.UserEntity;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.define.WfUrlConst;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;
import nguyenhoangviet.vpcorp.welfare.adr.pref.PreferencesAccountUtil;

/*
 * 型パラメータは
 * 1.非同期処理の実行時にメインスレッド側から与えられる情報
 * 2.進捗状況を管理するための情報
 * 3.非同期処理の結果
 */
public class TmMemberApiAsyncTask extends AsyncTask<Context, Integer, String> {

    private Context ctx;

    private  List<UserEntity> mBindingUsers = new ArrayList<>();

    private  Map<String,Integer> mBindingIndexes = new LinkedHashMap<>();

    public TmMemberApiAsyncTask(Context ctx, List<UserEntity> bindingUsers) {
        super();
        this.ctx = ctx;
        this.mBindingUsers = bindingUsers;

        for(int i = 0; i < bindingUsers.size(); i++){
            mBindingIndexes.put(bindingUsers.get(i).getUserAccount(), i);
        }
    }

    // doInBackgroundの事前準備処理（UIスレッド）
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Context... params) {

        //Toast.makeText(ctx, "[async] start call api", Toast.LENGTH_LONG).show();

        JSONObject jsonObject = new JSONObject();
        String result = null;
        try {
            Date date = new Date();
            jsonObject.put("targetDate", CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, date));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        initParams(jsonObject);

        try {
            final OkHttpClient client = new OkHttpClient();

            String fullUrl = CARequestUtil.getGetUrl(BuildConfig.HOST + WfUrlConst.WF_ACC_INFO_MEMBER, jsonObject);

            final Request request = new Request.Builder().url(fullUrl).build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return null;
            }

            result = response.body().string();

            if (!CCStringUtil.isEmpty(result)) {
                JSONObject resJson = new JSONObject(result);
            }


        } catch (Exception ex) {

        }

        //Toast.makeText(ctx, "[async] end call api", Toast.LENGTH_LONG).show();
        return result;
    }

    private void initParams(JSONObject jsonObject) {

        PreferencesAccountUtil prefAccUtil = new PreferencesAccountUtil(ctx);
        if (prefAccUtil != null) {
            UserModel userModel = prefAccUtil.getUserPref();

            try {
                jsonObject.put("loginUserId", CCStringUtil.toString(userModel.key));
                jsonObject.put("companyId", CCStringUtil.toString(userModel.companyId));
                jsonObject.put(CsConst.ARG_TOKEN, userModel.token);
                jsonObject.put("language", Resources.getSystem().getConfiguration().locale.getLanguage());
                TimeZone timeZone = TimeZone.getDefault();
                jsonObject.put(CsConst.ARG_TIMEZONE, timeZone.getID());
                jsonObject.put(CsConst.ARG_DEVICE, "A");
                jsonObject.put(CsConst.ARG_VERSION, AndroidUtil.getVersionName(ctx));
                jsonObject.put("serviceCd", WelfareConst.SERVICE_CD_SW);
            } catch (JSONException ex) {
                new CAException(ex);
            }
        }
    }

    // doInBackgroundの事後処理(UIスレッド)
    @Override
    protected void onPostExecute(String r) {
        try {

            //Toast.makeText(ctx, "[async] start update db", Toast.LENGTH_LONG).show();

            if (!CCStringUtil.isEmpty(r)) {

                Realm realm = Realm.getDefaultInstance();
                JSONObject resJson = new JSONObject(r);


                List<UserModel> results = new ArrayList<>();
                RealmQuery<UserEntity> query = realm.where(UserEntity.class);
                Map<String, UserEntity> exists = new LinkedHashMap<>();
                for (UserEntity entity : query.findAll()) {
                    exists.put(entity.getUserAccount(), entity);
                }

                final List<UserModel> userModels = CCJsonUtil.convertToModelList(resJson.optString("members"), UserModel.class);

                realm.beginTransaction();
                for (UserModel user : userModels) {

                    if(mBindingIndexes.containsKey(user.userAccount)){
                        int idx = mBindingIndexes.get(user.userAccount);
                        mBindingUsers.get(idx).userName = user.userName;
                    }

                    UserEntity result = exists.get(user.userAccount);

                    if (result == null) { // insert
                        result = realm.createObject(UserEntity.class);
                    }
                    result.setUserAccount(user.userAccount);
                    result.setUserName(user.userName);
                    result.setAvatarPath(user.avatarPath);

                }
                realm.commitTransaction();

                //Toast.makeText(ctx, "[async] end update db", Toast.LENGTH_LONG).show();

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ;
        }
    }

    // 進捗状況をUIに反映するための処理(UIスレッド)
    @Override
    protected void onProgressUpdate(Integer... values) {
        // progressDialogなどで進捗表示したりする
    }

    // 非同期処理がキャンセルされた場合の処理
    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
    }
}
