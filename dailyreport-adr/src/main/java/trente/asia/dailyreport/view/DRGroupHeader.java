package nguyenhoangviet.vpcorp.dailyreport.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import asia.chiase.core.util.CCCollectionUtil;
import nguyenhoangviet.vpcorp.dailyreport.R;
import nguyenhoangviet.vpcorp.dailyreport.services.kpi.model.GroupKpi;

/**
 * Created by viet on 7/8/2016.
 */
public class DRGroupHeader extends LinearLayout{

	private GroupKpi						selectedGroup;
	private int								currentIdx	= 0;
	private List<GroupKpi>					groups		= new ArrayList<>();
	private OnGroupHeaderActionsListener	onGroupHeaderActionsListener;

	public DRGroupHeader(Context context){
		super(context);
	}

	public DRGroupHeader(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	public DRGroupHeader(Context context, AttributeSet attrs, int defStyleAttr){
		super(context, attrs, defStyleAttr);
	}

	public DRGroupHeader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public GroupKpi getSelectedGroup(){
		return selectedGroup;
	}

	private ImageView	btnNext;
	private ImageView	btnBack;
	private Button		btnNowGroup;

	public void setOnGroupHeaderActionListener(OnGroupHeaderActionsListener listener){
		this.onGroupHeaderActionsListener = listener;
		btnNext = (ImageView)findViewById(R.id.btn_group_header_next);
		btnBack = (ImageView)findViewById(R.id.btn_group_header_back);

		btnNowGroup = (Button)findViewById(R.id.img_calendar_header_this_group);
		btnNowGroup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view){
				onGroupHeaderActionsListener.onNowGroupClicked(selectedGroup);
			}
		});

		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v){
				selectOtherGroup(true);
			}
		});
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v){
				selectOtherGroup(false);
			}
		});
		btnNext.setEnabled(false);
		btnBack.setEnabled(false);
	}

	public interface OnGroupHeaderActionsListener{

		public void onGroupChange(GroupKpi newGroup);

		public void onNowGroupClicked(GroupKpi selectedGroup);
	}

	public void buildLayout(List<GroupKpi> groups, int selectedIdx, boolean hasNowButton){
		if(!hasNowButton){
			btnNowGroup.setOnClickListener(null);
			btnNowGroup.setBackgroundColor(Color.TRANSPARENT);
			btnNowGroup.setTextColor(Color.BLACK);
		}
		if(CCCollectionUtil.isEmpty(groups)){
			return;
		}
		this.groups = groups;
		this.currentIdx = selectedIdx;
		this.selectedGroup = this.groups.get(this.currentIdx);
		btnNowGroup.setText(this.selectedGroup.name);
		updateDirectingButtonStatus();

	}

	private void selectOtherGroup(boolean isNext){
		if(isNext){
			currentIdx += 1;
		}else{
			currentIdx -= 1;
		}

		selectedGroup = groups.get(currentIdx);
		btnNowGroup.setText(selectedGroup.name);
		updateDirectingButtonStatus();
		onGroupHeaderActionsListener.onGroupChange(selectedGroup);
	}

	private void updateDirectingButtonStatus(){
		if(currentIdx == groups.size() - 1){
			btnNext.setEnabled(false);
		}else{
			btnNext.setEnabled(true);
		}
		if(currentIdx == 0){
			btnBack.setEnabled(false);
		}else{
			btnBack.setEnabled(true);
		}
	}

}
