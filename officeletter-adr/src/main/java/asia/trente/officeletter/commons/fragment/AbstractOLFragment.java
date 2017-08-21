package asia.trente.officeletter.commons.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.trente.officeletter.BuildConfig;
import asia.trente.officeletter.R;
import asia.trente.officeletter.services.document.DocumentListFragment;
import asia.trente.officeletter.services.history.HistoryListFragment;
import asia.trente.officeletter.services.salary.SalaryListFragment;
import asia.trente.officeletter.services.setting.SettingFragment;
import asia.trente.officeletter.services.wiki.WikiListFragment;
import trente.asia.welfare.adr.activity.WelfareFragment;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * Created by tien on 8/18/2017.
 */

public abstract class AbstractOLFragment extends WelfareFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        host = BuildConfig.HOST;
    }

    @Override
    protected String getServiceCd() {
        return WelfareConst.SERVICE_CD_OL;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getFragmentLayoutId(), container, false);
        }
        return mRootView;
    }

    protected int getFragmentLayoutId() {
        return 0;
    }

    @Override
    protected void initView() {
        super.initView();
        buildFooter();
    }

    protected void buildFooter(){
        int footerItemId = getFooterItemId();
        if(footerItemId != 0){
            View.OnClickListener listener = new View.OnClickListener() {

                @Override
                public void onClick(View view){
                    emptyBackStack();
                    switch(view.getId()){
                        case R.id.lnr_view_common_footer_document:
                            gotoFragment(new DocumentListFragment());
                            break;
                        case R.id.lnr_view_common_footer_salary:
                            gotoFragment(new SalaryListFragment());
                            break;
                        case R.id.lnr_view_common_footer_wiki:
                            gotoFragment(new WikiListFragment());
                            break;
                        case R.id.lnr_view_common_footer_history:
                            gotoFragment(new HistoryListFragment());
                            break;
                        case R.id.lnr_view_common_footer_setting:
                            gotoFragment(new SettingFragment());
                            break;
                        default:
                            break;
                    }
                }
            };
            getView().findViewById(R.id.lnr_view_common_footer_document).setOnClickListener(listener);
            getView().findViewById(R.id.lnr_view_common_footer_salary).setOnClickListener(listener);
            getView().findViewById(R.id.lnr_view_common_footer_wiki).setOnClickListener(listener);
            getView().findViewById(R.id.lnr_view_common_footer_history).setOnClickListener(listener);
            getView().findViewById(R.id.lnr_view_common_footer_setting).setOnClickListener(listener);
            setSelectedFooterItem(footerItemId);
        }
    }

    private void setSelectedFooterItem(int footerItemId){
        ImageView imgFooterItem = (ImageView)((LinearLayout)getView().findViewById(footerItemId)).getChildAt(0);
        switch(footerItemId){
            case R.id.lnr_view_common_footer_document:
                imgFooterItem.setImageResource(R.drawable.ol_footer_letter);
                break;
            case R.id.lnr_view_common_footer_salary:
                imgFooterItem.setImageResource(R.drawable.ol_footer_salary);
                break;
            case R.id.lnr_view_common_footer_wiki:
                imgFooterItem.setImageResource(R.drawable.ol_footer_wiki);
                break;
            case R.id.lnr_view_common_footer_history:
                imgFooterItem.setImageResource(R.drawable.ol_footer_history);
                break;
            case R.id.lnr_view_common_footer_setting:
                imgFooterItem.setImageResource(R.drawable.ol_footer_setting);
                break;
            default:
                break;
        }

        TextView txtFooterItem = (TextView)((LinearLayout)getView().findViewById(footerItemId)).getChildAt(1);
        txtFooterItem.setTextColor(getResources().getColor(R.color.chiase_white));
    }

    protected abstract int getFooterItemId();
}
