package nguyenhoangviet.vpcorp.shiftworking.services.worktime;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import nguyenhoangviet.vpcorp.android.view.ChiaseListDialog;
import nguyenhoangviet.vpcorp.android.view.ChiaseTextView;
import nguyenhoangviet.vpcorp.shiftworking.R;
import nguyenhoangviet.vpcorp.shiftworking.common.defines.SwConst;
import nguyenhoangviet.vpcorp.shiftworking.common.fragments.AbstractSwFragment;
import nguyenhoangviet.vpcorp.shiftworking.services.shiftworking.view.CommonMonthView;
import nguyenhoangviet.vpcorp.shiftworking.services.worktime.model.NoticeModel;
import nguyenhoangviet.vpcorp.shiftworking.services.worktime.view.NoticeAdapter;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.models.DeptModel;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WelfareUtil;

public class WorknoticeListFragment extends AbstractSwFragment {

    private ListView lsvNotice;
    private CommonMonthView monthView;
    private NoticeAdapter mAdapter;

    private ChiaseListDialog dlgOfferDept;
    private ChiaseTextView txtOfferDept;
    private LinearLayout lnrOfferDept;
    private boolean isMakeDeptFilter = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_work_notice_list, container, false);
        }
        return mRootView;
    }

    @Override
    public int getFooterItemId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();

        txtOfferDept = (ChiaseTextView) getView().findViewById(R.id.txt_id_offer_dept);
        lnrOfferDept = (LinearLayout) getView().findViewById(R.id.lnr_id_offer_dept);
        lnrOfferDept.setOnClickListener(this);
        txtOfferDept.setValue(CCConst.NONE);
        txtOfferDept.setText(getString(R.string.chiase_common_all));

        lsvNotice = (ListView) getView().findViewById(R.id.lsv_id_notice);
        lsvNotice.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoticeModel noticeModel = (NoticeModel) parent.getItemAtPosition(position);
                WorknoticeFormFragment fragment = new WorknoticeFormFragment();
                fragment.setNoticeModel(noticeModel);
                gotoFragment(fragment);
            }
        });

        mAdapter = new NoticeAdapter(activity, new ArrayList<NoticeModel>());
        lsvNotice.setAdapter(mAdapter);

        swipeRfresh = (SwipeRefreshLayout) getView().findViewById(R.id.swp_id_notice);
        swipeRfresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                loadNoticeList(false);
            }
        });
    }

    @Override
    public void initData() {
    }

    public void initMonthView(CommonMonthView monthView) {
        this.monthView = monthView;
        monthView.imgBack.setOnClickListener(this);
        monthView.imgNext.setOnClickListener(this);
        monthView.btnThisMonth.setOnClickListener(this);
        loadNoticeList(true);
    }

    private void loadNoticeList(boolean isLoading) {
        monthView.txtMonth.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE_WEEKDAY, monthView.workMonth));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", prefAccUtil.getUserPref().key);
            jsonObject.put("searchDateString", CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, monthView.workMonth));
            String deptId = txtOfferDept.getValue();
            if (!CCStringUtil.isEmpty(deptId) && !CCConst.NONE.equals(deptId)) {
                jsonObject.put("targetDeptId", deptId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestLoad(SwConst.API_NOTICE_LIST, jsonObject, isLoading);
    }

    @Override
    protected void successLoad(JSONObject response, String url) {
        if (SwConst.API_NOTICE_LIST.equals(url)) {
            List<NoticeModel> lstNotice = CCJsonUtil.convertToModelList(response.optString("notifications"), NoticeModel.class);
            List<DeptModel> lstDept = CCJsonUtil.convertToModelList(response.optString("depts"), DeptModel.class);
            if (!CCCollectionUtil.isEmpty(lstDept) && isMakeDeptFilter) {
                makeDeptFilter(lstDept);
            }
            mAdapter.clearAll();
            if (!CCCollectionUtil.isEmpty(lstNotice)) {
                mAdapter.addAll(lstNotice);
            }
        } else {
            super.successLoad(response, url);
        }
    }

    private void makeDeptFilter(List<DeptModel> lstDept) {
        isMakeDeptFilter = false;
        Map<String, String> deptMap = WelfareUtil.convertDept2Map(lstDept, activity);
        dlgOfferDept = new ChiaseListDialog(activity, getString(R.string.sw_work_notice_item_offer_dept), deptMap, txtOfferDept, new ChiaseListDialog.OnItemClicked() {

            @Override
            public void onClicked(String selectedKey, boolean isSelected) {
                loadNoticeList(true);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_id_back:
                monthView.workMonth = WelfareUtil.addDate(monthView.workMonth, -1);
                loadNoticeList(true);
                break;
            case R.id.btn_id_next:
                monthView.workMonth = WelfareUtil.addDate(monthView.workMonth, 1);
                loadNoticeList(true);
                break;
            case R.id.img_id_this_month:
                monthView.workMonth = new Date();
                loadNoticeList(true);
                break;
            case R.id.lnr_id_offer_dept:
                dlgOfferDept.show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        monthView = null;
        lsvNotice = null;
    }
}
