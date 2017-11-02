package nguyenhoangviet.vpcorp.dailyreport.services.report;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import nguyenhoangviet.vpcorp.android.util.AndroidUtil;
import nguyenhoangviet.vpcorp.android.util.DownloadFileManager;
import nguyenhoangviet.vpcorp.android.util.OpenDownloadedFile;
import nguyenhoangviet.vpcorp.android.view.ChiaseDownloadFileDialog;
import nguyenhoangviet.vpcorp.android.view.ChiaseEditText;
import nguyenhoangviet.vpcorp.android.view.ChiaseListViewNoScroll;
import nguyenhoangviet.vpcorp.dailyreport.BuildConfig;
import nguyenhoangviet.vpcorp.dailyreport.DRConst;
import nguyenhoangviet.vpcorp.dailyreport.R;
import nguyenhoangviet.vpcorp.dailyreport.activities.CameraPhotoPreviewActivity;
import nguyenhoangviet.vpcorp.dailyreport.activities.FilePreviewActivity;
import nguyenhoangviet.vpcorp.dailyreport.activities.MainDLActivity;
import nguyenhoangviet.vpcorp.dailyreport.dialogs.DRDialog;
import nguyenhoangviet.vpcorp.dailyreport.fragments.AbstractDRFragment;
import nguyenhoangviet.vpcorp.dailyreport.services.comment.menu.CommentMenuManager;
import nguyenhoangviet.vpcorp.dailyreport.services.report.model.DRMessageContentModel;
import nguyenhoangviet.vpcorp.dailyreport.services.report.model.ReportModel;
import nguyenhoangviet.vpcorp.dailyreport.services.report.view.CommentListAdapter;
import nguyenhoangviet.vpcorp.dailyreport.utils.DRUtil;
import nguyenhoangviet.vpcorp.welfare.adr.activity.WelfareActivity;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.dialog.WfDialog;
import nguyenhoangviet.vpcorp.welfare.adr.menu.OnMenuButtonsListener;
import nguyenhoangviet.vpcorp.welfare.adr.menu.OnMenuManageListener;
import nguyenhoangviet.vpcorp.welfare.adr.models.CommentModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.LikeModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WelfareUtil;

/**
 * Created by viet on 2/15/2016.
 */
public class ReportDetailFragment extends AbstractDRFragment implements View.OnClickListener{

	private static final int	FILE_SELECT_CODE	= 1234;
	private boolean				scrollToLastComment	= false;
	private boolean				willSendText		= false;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		((WelfareActivity)activity).setOnDeviceBackButtonClickListener(this);
	}

	@Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_report_detail;
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public void buildBodyLayout(){
		super.initHeader(R.drawable.wf_back_white, getString(R.string.fragment_title_empty), null);

		inflater = LayoutInflater.from(activity);
		scrollView = (ScrollView)getView().findViewById(R.id.fragment_report_detail_scroll_view);
		txtDate = (TextView)getView().findViewById(R.id.fragment_report_detail_date);

		txtCheckedNum = (TextView)getView().findViewById(R.id.share_lnr_check_txt_checked_num);
		txtLikedNum = (TextView)getView().findViewById(R.id.share_lnr_like_txt_num);
		txtCommentNum = (TextView)getView().findViewById(R.id.share_lnr_comment_txt_num);
		txtFileNum = (TextView)getView().findViewById(R.id.share_lnr_file_txt_num);

		lnrCheckView = (LinearLayout)getView().findViewById(R.id.share_lnr_check);
		lnrLikeView = (LinearLayout)getView().findViewById(R.id.share_lnr_like);
		lnrCommentView = (LinearLayout)getView().findViewById(R.id.share_lnr_comment);
		lnrFileView = (LinearLayout)getView().findViewById(R.id.share_lnr_file);

		lnrBtnLikeComment = (LinearLayout)getView().findViewById(R.id.lnr_btn_like_comment);
		lnrEnterComment = (LinearLayout)getView().findViewById(R.id.lnr_enter_comment);

		lstComment = (ChiaseListViewNoScroll)getView().findViewById(R.id.fragment_report_detail_lst_comment);
		btnShowCommentArea = (LinearLayout)getView().findViewById(R.id.fragment_report_detail_btn_comment);
		btnLike = (LinearLayout)getView().findViewById(R.id.fragment_report_detail_btn_like);
		imgLikeBottom = (ImageView)getView().findViewById(R.id.img_like);
		txtLikeBottom = (TextView)getView().findViewById(R.id.fragment_report_detail_txt_like);

		imgCheck = (ImageView)getView().findViewById(R.id.fragment_report_detail_img_check);
		imgLike = (ImageView)getView().findViewById(R.id.fragment_report_detail_img_like);
		imgFile = (ImageView)getView().findViewById(R.id.fragment_report_detail_img_file);
		imgComment = (ImageView)getView().findViewById(R.id.fragment_report_detail_img_comment);
		btnShowCommentArea.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				showCommentArea();
			}
		});

		lstComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
				CommentModel commentModel = reportModel.comments.get(i);
				onCommentModelSelected(commentModel);
			}
		});

		// Old ReportCommentFragment
		mBtnSend = (ImageView)getView().findViewById(R.id.btn_id_send_cmt);
		mEdtComment = (ChiaseEditText)getView().findViewById(R.id.edt_id_comment);
		mViewForMenuBehind = getView().findViewById(R.id.viewForMenuBehind);
		mBtnSend.setOnClickListener(this);
		mViewForMenuBehind.setOnClickListener(this);

		menuManager = new CommentMenuManager();
		menuManager.setMenuLayout(activity, R.id.menuMain, onMenuManagerListener, onMenuButtonsListener);

		mEdtComment.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){

			}

			@Override
			public void afterTextChanged(Editable editable){
				if(DRUtil.isNotEmpty(editable.toString())){
					willSendText = true;
					mBtnSend.setImageResource(R.drawable.wf_send);
				}else{
					willSendText = false;
					mBtnSend.setImageResource(R.drawable.wf_attachment);
				}
			}
		});

		mEdtComment.setOnKeyBoardClosedListener(new ChiaseEditText.OnKeyBoardClosedListener() {

			@Override
			public void onKeyBoardClose(){
				lnrEnterComment.setVisibility(View.GONE);
				lnrBtnLikeComment.setVisibility(View.VISIBLE);
			}
		});
	}

	private void onCommentModelSelected(CommentModel commentModel){
		if(CommentModel.COMMENT_TYPE_PHOTO.equals(commentModel.commentType)){
			WfDialog mDlgPhotoDetail = new WfDialog(activity);
			mDlgPhotoDetail.setDialogPhotoDetail(BuildConfig.HOST + commentModel.attachment.fileUrl);
			mDlgPhotoDetail.show();

			// CommentDetailFragment fragment = new CommentDetailFragment();
			// fragment.setCommentModel(commentModel);
			// ((WelfareActivity)activity).addFragment(fragment);
		}else if(CommentModel.COMMENT_TYPE_MOVIE.equals(commentModel.commentType) || CommentModel.COMMENT_TYPE_FILE.equals(commentModel.commentType)){
			downloadFile(commentModel);
		}
	}

	@Override
	public void initData(){
		callReportDetailApi();
	}

	public void callReportDetailApi(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetReportId", reportModel.key);
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		requestLoad(DRConst.API_REPORT_DETAIL, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(DRConst.API_REPORT_DETAIL.equals(url)){
			oldReportModel = reportModel;
			reportModel = CCJsonUtil.convertToModel(response.optString("detail"), ReportModel.class);
			if(WelfareUtil.size(oldReportModel.checks) != WelfareUtil.size(reportModel.checks)){
				((WelfareActivity)activity).isInitData = true;
			}
			buildReportDetail();
		}
	}

	public void showLikers(int titleId, boolean isLikers){
		dialogLikers = new DRDialog(activity);
		dialogLikers.setLikersDialog(activity, reportModel, getString(titleId), isLikers);
		dialogLikers.show();
	}

	private void buildReportDetail(){
		updateHeader(reportModel.reportUser.userName);
		txtDate.setText(DRUtil.getDateString(reportModel.reportDate, DRConst.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS, DRConst.DATE_FORMAT_YYYY_MM_DD));
		int checkNum = reportModel.checks.size();
		int likeNum = reportModel.likes.size();
		int commentNum = reportModel.comments.size();
		int fileNum = DRUtil.getFileNum(reportModel);

		txtCheckedNum.setText(String.valueOf(checkNum));
		txtLikedNum.setText(String.valueOf(likeNum));
		txtCommentNum.setText(String.valueOf(commentNum));
		txtFileNum.setText(String.valueOf(fileNum));

		int colorDisable = ContextCompat.getColor(activity, R.color.file_count_zero);
		int colorEnable = ContextCompat.getColor(activity, R.color.file_count);

		// Check
		if(checkNum <= 0){
			// imgCheck.setColorFilter(colorDisable);
			lnrCheckView.setOnClickListener(null);
			txtCheckedNum.setTextColor(colorDisable);
		}else{
			imgCheck.setColorFilter(null);
			lnrCheckView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view){
					showLikers(R.string.fragment_follower_title_checker, false);
				}
			});
			txtCheckedNum.setTextColor(colorEnable);
		}

		// Like
		if(likeNum <= 0){
			// imgLike.setColorFilter(colorDisable);
			lnrLikeView.setOnClickListener(null);
			txtLikedNum.setTextColor(colorDisable);
		}else{
			imgLike.setColorFilter(null);
			lnrLikeView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view){
					showLikers(R.string.fragment_follower_title_liker, true);
				}
			});
			txtLikedNum.setTextColor(colorEnable);
		}

		// CommentCheck
		if(commentNum <= 0){
			// imgComment.setColorFilter(colorDisable);
			lnrCommentView.setOnClickListener(null);
			txtCommentNum.setTextColor(colorDisable);
		}else{
			imgComment.setColorFilter(null);
			txtCommentNum.setTextColor(colorEnable);
		}

		// FileCheck
		if(fileNum <= 0){
			// imgFile.setColorFilter(colorDisable);
			imgFile.setOnClickListener(null);
			txtFileNum.setTextColor(colorDisable);
		}else{
			imgFile.setColorFilter(null);
			txtFileNum.setTextColor(colorEnable);
		}

		if(userAlreadyLikedReport(prefAccUtil.getUserPref(), reportModel)){
			btnLike.setOnClickListener(null);
			imgLikeBottom.setEnabled(false);
			// imgLikeBottom.setColorFilter(colorDisable);
			txtLikeBottom.setTextColor(colorDisable);
		}else{
			btnLike.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view){
					onClickLike();
				}
			});
		}
		buildReportDetailWithTemplate();
		buildReportCustom();
		// buildKPI();
		buildCommentList();
	}

	private void buildReportCustom(){
		LinearLayout lnrReportCustom = (LinearLayout)getView().findViewById(R.id.fragment_report_detail_lnr_custom);
		if(!CCStringUtil.isEmpty(reportModel.reportCustomTitle)){
			lnrReportCustom.setVisibility(View.VISIBLE);
			TextView txtCustomTitle = (TextView)getView().findViewById(R.id.fragment_report_detail_txt_custom_title);
			TextView txtCustomContent = (TextView)getView().findViewById(R.id.fragment_report_detail_txt_custom_content);
			txtCustomTitle.setText(reportModel.reportCustomTitle);
			txtCustomContent.setText(reportModel.reportCustom);
		}else{
			lnrReportCustom.setVisibility(View.GONE);
		}
	}

	// private void buildKPI(){
	// buildMonthlyGoals();
	// buildActionPlans();
	// }
	//
	// private void buildMonthlyGoals(){
	// if(reportModel.goalEntries.size() > 0){
	// LinearLayout lnrMonthlyGoalList = (LinearLayout)getView().findViewById(R.id.view_kpi_lnr_monthly);
	// buildGoalEntries(lnrMonthlyGoalList);
	// }else{
	// LinearLayout lnrMonthlyGoal = (LinearLayout)getView().findViewById(R.id.view_kpi_lnr_monthly_goal);
	// lnrMonthlyGoal.setVisibility(View.GONE);
	// }
	// }
	//
	// private void buildActionPlans(){
	// if(reportModel.actionEntries.size() > 0){
	// final LinearLayout kpiActualPlanHeader = (LinearLayout)getView().findViewById(R.id.view_kpi_lnr_actual_plan_header);
	// final LinearLayout kpiActualPlanContainer = (LinearLayout)getView().findViewById(R.id.view_kpi_lnr_actual_plan);
	// buildOldActualPlanKpis(kpiActualPlanContainer, kpiActualPlanHeader);
	// }else{
	// LinearLayout lnrActionPlan = (LinearLayout)getView().findViewById(R.id.view_kpi_lnr_action_plan);
	// lnrActionPlan.setVisibility(View.GONE);
	// }
	// }
	//
	// private void buildGoalEntries(LinearLayout container){
	// container.removeAllViews();
	// for(GoalEntry goalEntry : reportModel.goalEntries){
	// addGoalEntry(goalEntry, container);
	// }
	// }
	//
	// private void buildOldActualPlanKpis(LinearLayout container, LinearLayout header){
	// container.removeAllViews();
	// for(ActionEntry actionEntry : reportModel.actionEntries){
	// View itemView = inflater.inflate(R.layout.item_action_entry, null);
	// ReportEditFragment.buildActionPlanItemLayout(itemView, actionEntry);
	// container.addView(itemView);
	// header.setVisibility(View.VISIBLE);
	// }
	// }
	//
	// private void addGoalEntry(GoalEntry goalEntry, LinearLayout lnrConainer){
	// View itemView = inflater.inflate(R.layout.item_goal_entry_detail, null);
	// TextView txtItem = (TextView)itemView.findViewById(R.id.item_kpi_name);
	// TextView txtGoal = (TextView)itemView.findViewById(R.id.item_kpi_goal);
	// TextView txtSum = (TextView)itemView.findViewById(R.id.item_kpi_sum);
	// TextView txtPlan = (TextView)itemView.findViewById(R.id.item_kpi_month_txt_plan);
	// TextView txtActual = (TextView)itemView.findViewById(R.id.item_kpi_month_txt_actual);
	// TextView txtAchievement = (TextView)itemView.findViewById(R.id.item_kpi_achievement);
	//
	// txtItem.setText(goalEntry.goalName);
	// if(Kpi.KPI_UNIT_TIME.equals(goalEntry.goalUnit)){
	// txtGoal.setText(goalEntry.goalValue);
	// txtPlan.setText(goalEntry.goalPlan);
	// txtActual.setText(goalEntry.goalActual);
	// txtSum.setText(goalEntry.actualSum);
	// }else{
	// txtGoal.setText(WelfareUtil.formatAmount(goalEntry.goalValue));
	// txtPlan.setText(WelfareUtil.formatAmount(goalEntry.goalPlan));
	// txtActual.setText(WelfareUtil.formatAmount(goalEntry.goalActual));
	// txtSum.setText(WelfareUtil.formatAmount(goalEntry.actualSum));
	// }
	//
	// txtAchievement.setText(GoalEntry.getAchievementString(goalEntry));
	// lnrConainer.addView(itemView);
	// }

	@Override
	protected void onClickBackBtn(){
		if(isClickNotification){
			emptyBackStack();
			gotoFragment(new MyReportFragment());
		}else{
			if(oldReportModel.comments.size() != reportModel.comments.size()){
				((WelfareActivity)activity).isInitData = true;
			}
			((WelfareActivity)activity).backOneFragment();
		}
	}

	private boolean userAlreadyLikedReport(UserModel userPref, ReportModel reportModel){
		for(LikeModel likeModel : reportModel.likes){
			if(likeModel.likeUser.key.equals(userPref.key)){
				return true;
			}
		}
		return false;
	}

	private void buildReportDetailWithTemplate(){
		TextView txtContent = (TextView)getView().findViewById(R.id.fragment_report_edit_edt_content);
		txtContent.setText(reportModel.reportContent);
	}

	private void buildCommentList(){
		lstComment.setFocusable(false);
		CommentListAdapter commentListAdapter = new CommentListAdapter(activity, R.layout.item_report_comment, reportModel.comments);
		lstComment.setAdapter(commentListAdapter);
		if(scrollToLastComment == true){
			scrollToLastComment = false;
			lstComment.setFocusable(true);
			scrollView.post(new Runnable() {

				@Override
				public void run(){
					scrollView.fullScroll(View.FOCUS_DOWN);
				}
			});
		}else{
			lstComment.setFocusable(false);
		}
	}

	private void checkWritePermission(){

	}

	private void downloadFile(final CommentModel commentModel){
		if(!CCStringUtil.isEmpty(commentModel.attachment.fileUrl)){
			if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
				final File folder = new File(Environment.getExternalStorageDirectory(), DRConst.APP_FOLDER);
				if(!folder.canWrite()){
					((MainDLActivity)activity).grantWritePermission(new MainDLActivity.OnWriteExternalStoragePermissionGranted() {

						@Override
						public void onGranted(){
							continueDownloadFile(commentModel, DRUtil.getFilesFolderPath());
						}
					});
				}else{
					continueDownloadFile(commentModel, folder.getAbsolutePath());
				}
			}else{
				continueDownloadFile(commentModel, DRUtil.getFilesFolderPath());
			}
		}
	}

	private void continueDownloadFile(CommentModel commentModel, String absolutePath){
		File file = new File(absolutePath + "/" + commentModel.attachment.fileName);
		if(!file.exists()){
			Toast.makeText(activity, "NO EXSIT", Toast.LENGTH_LONG).show();
		}
		if(!file.canWrite()){
			Toast.makeText(activity, "NO EXSIT", Toast.LENGTH_LONG).show();
		}
		final ChiaseDownloadFileDialog dialog = ChiaseDownloadFileDialog.startDialog(activity);
		DownloadFileManager.downloadFile(activity, host + commentModel.attachment.fileUrl, file, new DownloadFileManager.OnDownloadListener() {

			@Override
			public void onStart(){
				Log.d("LOG", "START DOWNLOADING");
			}

			@Override
			public void onSetMax(final int max){
				activity.runOnUiThread(new Runnable() {

					@Override
					public void run(){
						dialog.setMax(max);
					}
				});
			}

			@Override
			public void onProgress(final int current){
				activity.runOnUiThread(new Runnable() {

					@Override
					public void run(){
						dialog.setCurrent(current);
					}
				});
			}

			@Override
			public void onFinishDownload(){
				activity.runOnUiThread(new Runnable() {

					@Override
					public void run(){
						// dialog.fileDownloaded();
					}
				});
			}

			@Override
			public void onResponse(boolean isSuccess, final String path){
				activity.runOnUiThread(new Runnable() {

					@Override
					public void run(){
						dialog.dismiss();
						OpenDownloadedFile.downloadedFileDialog(new File(path), getActivity());
					}
				});
			}
		});
	}

	private void onClickLike(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetReportId", reportModel.key);
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		requestUpdate(DRConst.API_REPORT_LIKE, jsonObject, true);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		if(DRConst.API_REPORT_LIKE.equals(url)){
			// Toast.makeText(activity, "You liked the report!", Toast.LENGTH_LONG).show();
			((WelfareActivity)activity).isInitData = true;
			callReportDetailApi();
		}else if(DRConst.API_REPORT_COMMENT.equals(url)){
			((WelfareActivity)activity).isInitData = true;
			onCommentSuccess();
		}else{
			super.successUpdate(response, url);
		}
	}

	private void onCommentSuccess(){
		// Toast toast = Toast.makeText(activity, "You've commented on this " + "report", Toast.LENGTH_LONG);
		// toast.setGravity(Gravity.CENTER, 0, 0);
		// toast.show();
		lnrBtnLikeComment.setVisibility(View.VISIBLE);
		lnrEnterComment.setVisibility(View.GONE);
		scrollToLastComment = true;
		mEdtComment.setText("");
		callReportDetailApi();
	}

	private void showCommentArea(){
		lnrEnterComment.setVisibility(View.VISIBLE);
		lnrBtnLikeComment.setVisibility(View.GONE);
		mEdtComment.requestFocus();
		InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mEdtComment, InputMethodManager.SHOW_IMPLICIT);
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}

	// http://stackoverflow.com/questions/5568874/how-to-extract-the-file
	// -name-from-uri-returned-from-intent-action-get-content
	public String getFileName(Uri uri){
		String result = null;
		if(uri.getScheme().equals("content")){
			Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
			try{
				if(cursor != null && cursor.moveToFirst()){
					result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
				}
			}finally{
				cursor.close();
			}
		}
		if(result == null){
			result = uri.getPath();
			int cut = result.lastIndexOf('/');
			if(cut != -1){
				result = result.substring(cut + 1);
			}
		}
		return result;
	}

	private ReportModel				oldReportModel;
	private TextView				txtCheckedNum;
	private TextView				txtLikedNum;
	private TextView				txtCommentNum;
	private TextView				txtFileNum;
	private ChiaseListViewNoScroll	lstComment;
	private TextView				txtDate;
	private LinearLayout			btnShowCommentArea;
	private LinearLayout			btnLike;
	private LinearLayout			lnrCheckView;
	private LinearLayout			lnrLikeView;
	private LinearLayout			lnrCommentView;
	private LinearLayout			lnrFileView;
	private TextView				txtFileName;
	private ImageView				imgCheck;
	private ImageView				imgLike;
	private ImageView				imgFile;
	private ImageView				imgComment;

	private LinearLayout			lnrEnterComment;
	private LinearLayout			lnrBtnLikeComment;
	private DRDialog				dialogLikers;
	private LayoutInflater			inflater;
	private ImageView				imgLikeBottom;
	private TextView				txtLikeBottom;
	private View					mViewForMenuBehind;
	private CommentMenuManager		menuManager;
	private ImageView				mBtnSend;
	private ChiaseEditText			mEdtComment;
	private ScrollView				scrollView;

	@Override
	public void onDestroy(){
		super.onDestroy();
		txtCheckedNum = null;
		txtLikedNum = null;
		txtCommentNum = null;
		txtFileNum = null;
		txtDate = null;
		txtFileName = null;
		txtLikeBottom = null;
		lstComment = null;
		btnShowCommentArea = null;
		btnLike = null;
		lnrCheckView = null;
		lnrLikeView = null;
		lnrCommentView = null;
		lnrFileView = null;
		imgCheck = null;
		imgLike = null;
		lnrEnterComment = null;
		lnrBtnLikeComment = null;
		dialogLikers = null;
		inflater = null;
		imgLikeBottom = null;
		scrollView = null;
	}

	private OnMenuManageListener	onMenuManagerListener	= new OnMenuManageListener() {

																@Override
																public void onMenuOpened(){
																}

																@Override
																public void onMenuClosed(){
																	mEdtComment.setEnabled(true);
																	mViewForMenuBehind.setVisibility(View.GONE);
																}
															};

	protected OnMenuButtonsListener	onMenuButtonsListener	= new OnMenuButtonsListener() {

																@Override
																public void onCameraClicked(){
																	CameraPhotoPreviewActivity.starCameraPhotoPreviewActivity(ReportDetailFragment.this, reportModel.key, true);
																	onButtonMenuOpenedClicked();
																}

																@Override
																public void onAudioClicked(){
																}

																@Override
																public void onFileClicked(){
																	FilePreviewActivity.startFilePreviewActivity(ReportDetailFragment.this, reportModel.key, mEdtComment.getText().toString());
																	onButtonMenuOpenedClicked();
																}

																@Override
																public void onVideoClicked(){
																	alertDialog.setMessage(getString(R.string.chiase_common_disabled_function));
																	alertDialog.show();
																	// RecorderVideoActivity.starVideoPreviewActivity(ReportDetailFragment.this,
																	// reportModel.key);
																	// onButtonMenuOpenedClicked();
																}

																@Override
																public void onLocationClicked(){
																}

																@Override
																public void onGalleryClicked(){
																	CameraPhotoPreviewActivity.starCameraFromGalleryPhotoPreviewActivity(ReportDetailFragment.this, reportModel.key, true);
																	onButtonMenuOpenedClicked();
																}

																@Override
																public void onContactClicked(){
																}
															};
	private ReportModel				reportModel;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent returnedIntent){
		super.onActivityResult(requestCode, resultCode, returnedIntent);

		if(resultCode != Activity.RESULT_OK) return;
		switch(requestCode){
		case FILE_SELECT_CODE:
			if(resultCode == Activity.RESULT_OK){
				// Get the Uri of the selected file
				Uri uri = returnedIntent.getData();
				String fileName = getFileName(uri);
				if(DRUtil.isNotEmpty(fileName)) txtFileName.setText(fileName);
			}
			break;
		case WelfareConst.RequestCode.PHOTO_CHOOSE:
			appendFile(returnedIntent);
			break;
		case WelfareConst.RequestCode.VIDEO_CHOOSE:
			appendFile(returnedIntent);
			break;
		case WelfareConst.RequestCode.PICK_FILE:
			appendFile(returnedIntent);
			break;
		default:
			break;
		}
	}

	private void appendFile(Intent returnedIntent){
		String detailMessage = returnedIntent.getExtras().getString("detail");
		if(WelfareConst.WF_FILE_SIZE_NG.equals(detailMessage)){
			alertDialog.setMessage(getString(R.string.wf_invalid_photo_over));
			alertDialog.show();
		}else{
			DRMessageContentModel photoModel = CCJsonUtil.convertToModel(CCStringUtil.toString(detailMessage), DRMessageContentModel.class);
			onCommentSuccess();
		}
	}

	private void sendComment(String commentMessage){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("parentKey", reportModel.key);
			jsonObject.put("commentContent", commentMessage);
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		requestUpdate(DRConst.API_REPORT_COMMENT, jsonObject, true);
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.viewForMenuBehind:
			onButtonMenuOpenedClicked();
			break;
		case R.id.btn_id_send_cmt:
			if(willSendText){
				String commentMessage = mEdtComment.getText().toString();
				// if(!CCStringUtil.isEmpty(commentMessage)){
				sendComment(commentMessage);
				// }else{
				// alertDialog.setMessage(getString(R.string.msg_msg_invalid_empty));
				// alertDialog.show();
				// mEdtComment.setText("");
				// }
			}else{
				if(AndroidUtil.verifyStoragePermissions(activity)){
					onButtonMenuClicked();
				}
			}
		default:
			break;
		}
	}

	private void onButtonMenuClicked(){
		mEdtComment.setEnabled(false);
		menuManager.openMenu(mBtnSend);
		mViewForMenuBehind.setVisibility(View.VISIBLE);
	}

	private void onButtonMenuOpenedClicked(){
		if(mViewForMenuBehind.getVisibility() == View.GONE){
			return;
		}
		menuManager.closeMenu();
	}

	public void setReportModel(ReportModel reportModel){
		this.reportModel = reportModel;
	}
}
