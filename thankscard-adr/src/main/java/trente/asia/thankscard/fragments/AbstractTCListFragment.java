package nguyenhoangviet.vpcorp.thankscard.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import nguyenhoangviet.vpcorp.thankscard.R;
import nguyenhoangviet.vpcorp.thankscard.activities.MainActivity;
import nguyenhoangviet.vpcorp.thankscard.commons.defines.TcConst;
import nguyenhoangviet.vpcorp.thankscard.services.common.TCDetailFragment;
import nguyenhoangviet.vpcorp.thankscard.services.common.model.Category;
import nguyenhoangviet.vpcorp.thankscard.services.common.model.HistoryModel;
import nguyenhoangviet.vpcorp.thankscard.services.common.model.Template;
import nguyenhoangviet.vpcorp.thankscard.services.posted.PostTCFragment;
import nguyenhoangviet.vpcorp.thankscard.services.rank.view.TCTabLinearLayout;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.models.DeptModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WelfareUtil;
import nguyenhoangviet.vpcorp.welfare.adr.view.WfSpinner;

/**
 * Created by viet on 2/15/2016.
 */
public abstract class AbstractTCListFragment extends AbstractTCFragment implements DatePickerDialog.OnDateSetListener,TCTabLinearLayout.OnTabSelectedListener{

	protected int				year		= 0;
	protected int				month		= 0;
	private int					minYear		= -1;
	private int					minMonth	= -1;
	private int					maxYear		= -1;
	private int					maxMonth	= -1;

	private WfSpinner			spnDeparment;
	private WfSpinner			spnUserModel;
	protected DeptModel			selectedDept;
	protected UserModel			selectedUser;
	private List<Template>		templates;
	private List<HistoryModel>	filteredThanksCards;
	private LayoutInflater		inflater;

	List<DeptModel>				departments;
	private List<HistoryModel>	mLstHistory	= new ArrayList<>();
	private List<Category>		categories;
	protected Category			selectedCategory;
	private TCTabLinearLayout	mTabContainer;
	private Button				btnPost;
	private Button				btnBack;
	private Button				btnNext;

	public boolean hasBackBtn(){
		return false;
	};

	@Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_tc_list;
	}

	@Override
	public boolean hasSettingBtn(){
		return true;
	}

	public void buildBodyLayout(){
		inflater = LayoutInflater.from(activity);
		spnDeparment = (WfSpinner)getView().findViewById(R.id.spn_fragment_tc_list_departure);
		spnUserModel = (WfSpinner)getView().findViewById(R.id.spn_fragment_tc_list_user);

//		buildCategoriesTab();
		buildSelectMonthBtn();
		buildPostBtn();

		month = month == 0 ? Calendar.getInstance().get(Calendar.MONTH) + 1 : month;
		year = year == 0 ? Calendar.getInstance().get(Calendar.YEAR) : year;

		// Don't show future post/receive
		maxMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
		maxYear = Calendar.getInstance().get(Calendar.YEAR);

		selectedDept = myself.dept;
		selectedUser = myself;

		buildDirectButton();

	}

	@Override
	protected void initData(){
		requestCardHistoryInfo();
	}

	protected UserModel createByUserId(String userId, String userName){
		UserModel userModel = new UserModel();
		userModel.loginUserId = userId;
		userModel.userName = userName;
		return userModel;
	}

	public void buildCategoriesTab(){
		categories = Category.getCategoryList();
		mTabContainer = (TCTabLinearLayout)getView().findViewById(R.id.tc_tab_linear_layout);
		List<String> categoriesName = new ArrayList<>();
		for(Category category : categories){
			categoriesName.add(category.categoryName);
		}
		mTabContainer.initTabs(categoriesName, this, selectedCategory != null ? selectedCategory.categoryName : null);
	}

	public void onTabSelected(String tabName){
		selectedCategory = getSelectedCategory(categories, tabName);
		requestCardHistoryInfo();
	}

	public static Category getSelectedCategory(List<Category> categories, String name){
		Category selectedCategory = null;
		for(Category category : categories){
			if(name.equals(category.categoryName)){
				selectedCategory = category;
			}
		}
		return selectedCategory;
	}

	protected void buildDirectButton(){
		btnBack = (Button)getView().findViewById(R.id.btn_view_common_pager_back);
		btnNext = (Button)getView().findViewById(R.id.btn_view_common_pager_next);
		btnBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				if(AbstractTCListFragment.this.month == 1){
					AbstractTCListFragment.this.year -= 1;
					AbstractTCListFragment.this.month = 12;
				}else{
					AbstractTCListFragment.this.month -= 1;
				}
				updateDirectingButtonStatusByDate();
				if(monthYearPickerDialog != null) monthYearPickerDialog.setmSelectedTime(AbstractTCListFragment.this.year, AbstractTCListFragment.this.month);
				requestCardHistoryInfo();
			}
		});
		btnNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				if(AbstractTCListFragment.this.month == 12){
					AbstractTCListFragment.this.year += 1;
					AbstractTCListFragment.this.month = 1;
				}else{
					AbstractTCListFragment.this.month += 1;
				}
				updateDirectingButtonStatusByDate();
				if(monthYearPickerDialog != null) monthYearPickerDialog.setmSelectedTime(AbstractTCListFragment.this.year, AbstractTCListFragment.this.month);
				requestCardHistoryInfo();
			}
		});
		updateDirectingButtonStatusByDate();
	}

	private void updateDirectingButtonStatusByDate(){
		if(AbstractTCListFragment.this.year == maxYear && AbstractTCListFragment.this.month == maxMonth){
			btnNext.setEnabled(false);
		}else{
			btnNext.setEnabled(true);
		}
		if(AbstractTCListFragment.this.year == minYear && AbstractTCListFragment.this.month == minMonth){
			btnBack.setEnabled(false);
		}else{
			btnBack.setEnabled(true);
		}
	}

	private void buildSelectMonthBtn(){
		View.OnClickListener listener = new View.OnClickListener() {

			@Override
			public void onClick(View v){
				// design change, now don't show future post/receive
				showLimitedMonthPickerDialog(AbstractTCListFragment.this, AbstractTCListFragment.this.year, AbstractTCListFragment.this.month, maxYear, maxMonth);
			}
		};
		getView().findViewById(R.id.txt_fragment_tc_list_month).setOnClickListener(listener);
		getView().findViewById(R.id.txt_fragment_tc_list_month_title).setOnClickListener(listener);
	}

	/**
	 * Only show this button at Post List
	 */
	protected void buildPostBtn(){
		btnPost = (Button)getView().findViewById(R.id.btn_fragment_month_post);
		btnPost.setText(getString(R.string.fragment_month_post_button));
		btnPost.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				gotoPostEditFragment();
			}
		});
	}

	private void gotoPostEditFragment(){
		gotoFragment(new PostTCFragment());
	}

	abstract protected JSONObject getHistoryParam();

	private void requestCardHistoryInfo(){
		JSONObject param = getHistoryParam();
		requestLoad(getConstApi(), param, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(getView() != null){
			if(getConstApi().equals(url)){
				requestCardHistoryInfoSuccess(response);
			}else{
				super.successLoad(response, url);
			}
		}
	}

	protected abstract String getConstApi();

	private void requestCardHistoryInfoSuccess(JSONObject response){
		((TextView)getView().findViewById(R.id.txt_fragment_tc_list_month)).setText(this.year + "-" + CCFormatUtil.formatZero(this.month));
		mLstHistory = CCJsonUtil.convertToModelList(response.optString("histories"), HistoryModel.class);
		departments = CCJsonUtil.convertToModelList(response.optString("depts"), DeptModel.class);
		if(WelfareUtil.size(departments) == 1 && WelfareUtil.size(departments.get(0).members) == 1 && btnPost != null){
			btnPost.setEnabled(false);
		}
		templates = CCJsonUtil.convertToModelList(response.optString("templates"), Template.class);

		// appendUserNames();
		appendDepartmentDeptAll();
		buildSpinners();
	}

	private void log(String msg) {
		Log.e("AbstractTC", msg);
	}

	// private void appendUserNames(){
	// for(HistoryModel historyModel : mLstHistory){
	// historyModel.receiverName = DeptModel.getUserByUserId(departments, historyModel.receiverId).userName;
	// historyModel.posterName = DeptModel.getUserByUserId(departments, historyModel.posterId).userName;
	// }
	// }

	private void appendDepartmentDeptAll(){
		DeptModel deptAll = new DeptModel(DeptModel.KEY_ALL, getString(R.string.spinner_default_department));
		deptAll.members = new ArrayList<>();
		for(DeptModel deptModel : departments){
			for(UserModel userModel : deptModel.members){
				addUserToListIfNotExist(deptAll.members, userModel);
			}
		}
		departments.add(0, deptAll);
		appendUserUserAll();
	}

	private void appendUserUserAll(){
		UserModel userAll = new UserModel(UserModel.KEY_ALL, getString(R.string.spinner_default_user));
		for(DeptModel dept : departments){
			dept.members.add(0, userAll);
		}
	}

	private void addUserToListIfNotExist(List<UserModel> users, UserModel userModel){
		for(UserModel user : users){
			if(user.key.equals(userModel.key)){
				return;
			}
		}
		users.add(userModel);
	}

	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
		this.year = year;
		this.month = monthOfYear;
		updateDirectingButtonStatusByDate();
		requestCardHistoryInfo();
	}

	private void search(){
		filteredThanksCards = filterThanksCards(mLstHistory);
		loadPagerLayout();
	}

	/**
	 * @param thanksCards
	 * @return
	 */
	private List<HistoryModel> filterThanksCards(List<HistoryModel> thanksCards){
		List<HistoryModel> result = new ArrayList<>();
		if(selectedDept != null && selectedUser != null){
			for(HistoryModel thanksCard : thanksCards){
				if((DeptModel.KEY_ALL.equals(selectedDept.key) && UserModel.KEY_ALL.equals(selectedUser.key)) || (DeptModel.KEY_ALL.equals(selectedDept.key) && !UserModel.KEY_ALL.equals(selectedUser.key) && getFilteredField(thanksCard).equals(selectedUser.key)) || (!DeptModel.KEY_ALL.equals(selectedDept.key) && !UserModel.KEY_ALL.equals(selectedUser.key) && getFilteredField(thanksCard).equals(selectedUser.key)) || (!DeptModel.KEY_ALL.equals(selectedDept.key) && UserModel.KEY_ALL.equals(selectedUser.key) && belongToDept(getFilteredField(thanksCard), selectedDept))){
					result.add(thanksCard);
				}
			}
		}

		return result;
	}

	private boolean belongToDept(String userId, DeptModel selectedDept){
		for(UserModel userModel : selectedDept.members){
			if(userModel.key.equals(userId)){
				return true;
			}
		}
		return false;
	}

	abstract protected String getFilteredField(HistoryModel historyModel);

	private boolean hasDeptModel(DeptModel department, HistoryModel historyModel){
		List<DeptModel> departments = getDeptModelFilterValue(historyModel);
		for(DeptModel department1 : departments){
			if(department1.key.equals(department.key)){
				return true;
			}
		}
		return false;
	}

	protected abstract List<DeptModel> getDeptModelFilterValue(HistoryModel historyModel);

	protected abstract UserModel getUserModelFilterValue(HistoryModel thanksCard);

	public void loadPagerLayout(){
		TableLayout lnrTable = (TableLayout)getView().findViewById(R.id.tbl_page_with_table);
		lnrTable.removeAllViews();
		TableRow tableRow = new TableRow(getActivity());
		for(int i = 0; i < filteredThanksCards.size(); i++){
			if(i % 2 == 0){
				tableRow = new TableRow(getActivity());
				TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f);
				params.setMargins(0, 0, 0, WelfareUtil.dpToPx(16));
				tableRow.setLayoutParams(params);
				tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
				tableRow.setGravity(Gravity.CENTER_VERTICAL);
				lnrTable.addView(tableRow);
			}

			View lnrCardFrame = buildThankCardLayout(inflater, filteredThanksCards.get(i));
			final int finalI = i;
			lnrCardFrame.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v){
					gotoTCDetail((MainActivity)getActivity(), filteredThanksCards, departments, getDetailTCTitle(), getFooterItemId(), filteredThanksCards.get(finalI));
				}
			});
			tableRow.addView(lnrCardFrame);
		}
	}

	private void gotoTCDetail(MainActivity activity, List<HistoryModel> lstHistory, List<DeptModel> deptModels, int title, int footerItemId, HistoryModel selectedHistory){
		Bundle args = new Bundle();
		args.putInt(TcConst.ACTIVE_FOOTER_ITEM_ID, footerItemId);
		args.putInt(TCDetailFragment.DETAIL_TC_TITLE, title);
		args.putInt(TCDetailFragment.DETAIL_TC_DEFAULT_POS, lstHistory.indexOf(selectedHistory));

		TCDetailFragment tcDetailFragment = new TCDetailFragment();
		tcDetailFragment.setLstHistory(lstHistory);
		tcDetailFragment.setDepts(deptModels);
		tcDetailFragment.setCurrentHistory(selectedHistory);
		tcDetailFragment.setArguments(args);
		gotoFragment(tcDetailFragment);
	}

	protected abstract int getDetailTCTitle();

	//
	private List<String> getDeptModelDisplayedValue(List<DeptModel> deptModels){
		List<String> values = new ArrayList<>();
		for(DeptModel deptModel : deptModels){
			values.add(deptModel.deptName);
		}
		return values;
	}

	private void onDeptSelected(int selectedPosition){
		selectedDept = departments.get(selectedPosition);
		updateUserSpinner();
	}

	private void updateUserSpinner(){
		selectedUser = getSelectedUser(selectedDept.members, selectedUser);
		int selectedIndex = WelfareUtil.findUserInList(selectedDept.members, selectedUser);
		spnUserModel.setupLayout(getString(R.string.fragment_post_edit_user), getUserDisplayedValue(selectedDept.members), selectedIndex, new WfSpinner.OnDRSpinnerItemSelectedListener() {

			@Override
			public void onItemSelected(int selectedPosition){
				selectedUser = selectedDept.members.get(selectedPosition);
				search();
			}
		}, true);
		if(selectedDept.members == null || selectedDept.members.size() == 0){
			selectedUser = null;
		}
	}

	private UserModel getSelectedUser(List<UserModel> users, UserModel previousSelectedUser){

		if(CCCollectionUtil.isEmpty(users)){
			return null;
		}

		if(previousSelectedUser == null){
			selectedUser = users.get(0);
		}else{
			boolean isMatch = false;
			for(UserModel userModel : users){
				if(userModel.key.equals(previousSelectedUser.key)){
					isMatch = true;
					selectedUser = userModel;
				}
			}
			if(!isMatch){
				selectedUser = users.get(0);
			}
		}
		return selectedUser;
	}

	private List<String> getUserDisplayedValue(List<UserModel> userModels){
		List<String> values = new ArrayList<>();
		if(userModels != null){
			for(UserModel userModel : userModels){
				values.add(userModel.userName);
			}
		}
		return values;
	}

	private void buildSpinners(){
		int selectedIndex = WelfareUtil.findDeptInList(departments, selectedDept);
		spnDeparment.setupLayout(getString(R.string.fragment_post_edit_dept), getDeptModelDisplayedValue(departments), selectedIndex,

		new WfSpinner.OnDRSpinnerItemSelectedListener() {

			@Override
			public void onItemSelected(int selectedPosition){
				onDeptSelected(selectedPosition);
			}
		}, true);

	}

	protected View buildThankCardLayout(LayoutInflater inflater, HistoryModel historyModel){
		View cardLayout = inflater.inflate(R.layout.item_thanks_card_frame, null);
		TCDetailFragment.buildTCFrame(getActivity(), cardLayout, historyModel, true);
		return cardLayout;
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		spnDeparment = null;
		spnUserModel = null;
		btnBack = null;
		btnNext = null;
		btnPost = null;
	}

	@Override
	protected String getServiceCd() {
		return WelfareConst.SERVICE_CD_TC;
	}
}
