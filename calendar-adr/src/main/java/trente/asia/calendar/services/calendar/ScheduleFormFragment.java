package trente.asia.calendar.services.calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import asia.chiase.core.util.CCJsonUtil;
import trente.asia.android.view.util.CAObjectSerializeUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.services.calendar.model.CalendarDay;
import trente.asia.welfare.adr.define.WfUrlConst;

/**
 * ScheduleDetailFragment
 *
 * @author VietNH
 */
public class ScheduleFormFragment extends AbstractClFragment {

    private CalendarDay.Schedule schedule;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_schedule_form,
                    container, false);
        }
        return mRootView;
    }

    @Override
    protected void initView() {
        super.initView();
        initHeader(R.drawable.wf_back_white, "Weekly", null);

        try {
            Gson gson = new Gson();
            CAObjectSerializeUtil.deserializeObject((ViewGroup) getView()
                    .findViewById(R.id.lnr_id_content), new JSONObject(gson
                    .toJson(schedule)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ImageView imgRightIcon = (ImageView) getView().findViewById(R.id
                .img_id_header_right_icon);
        imgRightIcon.setImageResource(R.drawable.abc_btn_radio_material);
        imgRightIcon.setVisibility(View.VISIBLE);
        imgRightIcon.setOnClickListener(this);

    }

    @Override
    protected void initData() {
    }

    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_footer_weekly;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_id_header_right_icon:
                sendUpdatedRequest();
            default:
                break;
        }
    }

    private void sendUpdatedRequest() {
        JSONObject jsonObject = CAObjectSerializeUtil.serializeObject(
                (ViewGroup) getView().findViewById(R.id.lnr_id_content), null);
        try {
            jsonObject.put("key", schedule.key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestUpdate(WfUrlConst.WF_CL_SCHEDULE_UPD, jsonObject, true);
    }

    @Override
    protected void successUpdate(JSONObject response, String url) {
        if (WfUrlConst.WF_CL_SCHEDULE_UPD.equals(url)) {
            schedule = CCJsonUtil.convertToModel(response.optString
                    ("schedule"), CalendarDay.Schedule.class);
            onScheduleUpdateSuccess();
        } else {
            super.successUpdate(response, url);
        }
    }

    private void onScheduleUpdateSuccess() {
        Toast.makeText(activity, "Updated successfully", Toast.LENGTH_LONG)
                .show();
        //// TODO: 2/9/2017 back to detail screen ? 
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void setSchedule(CalendarDay.Schedule schedule) {
        this.schedule = schedule;
    }
}
