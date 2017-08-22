package asia.trente.officeletter.services.salary;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.logansquare.LoganSquare;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import asia.trente.officeletter.R;
import asia.trente.officeletter.commons.defines.OLConst;
import asia.trente.officeletter.commons.fragment.AbstractListFragment;
import asia.trente.officeletter.databinding.FragmentSalaryListBinding;
import asia.trente.officeletter.services.salary.adapter.SalaryAdapter;
import asia.trente.officeletter.services.salary.model.MonthModel;
import asia.trente.officeletter.services.salary.model.YearModel;

/**
 * Created by tien on 8/18/2017.
 */

public class SalaryListFragment extends AbstractListFragment implements SalaryAdapter.OnSalaryAdapterListener{

	private FragmentSalaryListBinding	binding;
	private SalaryAdapter				adapter	= new SalaryAdapter();

	@Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_salary_list, container, false);
			binding = DataBindingUtil.bind(mRootView);
		}
		return mRootView;
	}

	@Override
	protected int getFooterItemId(){
		return R.id.lnr_view_common_footer_salary;
	}

	@Override
	protected void initView(){
		super.initView();
		initHeader(null, myself.userName, null);
		adapter.setCallback(this);
		binding.rlvSalary.setAdapter(adapter);
		binding.rlvSalary.setLayoutManager(new LinearLayoutManager(getContext()));
	}

	@Override
	protected void initData(){
		super.initData();
		loadSalaryList();
	}

	private void loadSalaryList(){
		JSONObject jsonObject = new JSONObject();
		requestLoad(OLConst.API_OL_SALARY_MONTH, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(OLConst.API_OL_SALARY_MONTH.equals(url)){
			try{
				List<YearModel> years = LoganSquare.parseList(response.optString("years"), YearModel.class);
				List<MonthModel> months = new ArrayList<>();
				for(YearModel year : years){
					months.add(new MonthModel(year.year));
					for(MonthModel month : year.months){
						months.add(month);
					}
				}
				adapter.setMonths(months);
			}catch(IOException e){
				e.printStackTrace();
			}
		}else{
			super.successLoad(response, url);
		}
	}

	private void log(String msg){
		Log.e("SalaryListFragment", msg);
	}

	@Override
	public void onSalaryClick(int salaryId){
		SalaryDetailFragment salaryDetailFragment = new SalaryDetailFragment();
		salaryDetailFragment.setSalaryId(salaryId);
		gotoFragment(salaryDetailFragment);
	}
}
