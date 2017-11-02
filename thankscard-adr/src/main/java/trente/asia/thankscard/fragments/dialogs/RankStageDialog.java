package nguyenhoangviet.vpcorp.thankscard.fragments.dialogs;

import java.util.List;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import nguyenhoangviet.vpcorp.thankscard.R;
import nguyenhoangviet.vpcorp.thankscard.services.rank.model.RankStage;
import nguyenhoangviet.vpcorp.thankscard.services.rank.view.RankStageListAdapter;

/**
 * Created by viet on 3/16/2016.
 */
public class RankStageDialog extends TCDialog{

	List<RankStage>	rankStages;
	private int		totalPoint;

	public void setRankStagesInfo(List<RankStage> rankStages, int totalPost){
		this.rankStages = rankStages;
		this.totalPoint = totalPost;
		for(RankStage rankStage : rankStages){
			rankStage.status = totalPoint >= rankStage.require ? RankStage.RANK_STAGE_ACHIEVED : RankStage.RANK_STAGE_UNACHIEVED;
		}
	}

	@Override
	public int getDialogLayoutId(){
		return R.layout.dialog_rank_stage;
	}

	@Override
    public void buildDialogLayout(View rootView){
		((TextView)rootView.findViewById(R.id.txt_rank_stage_dialog_title)).setText(getString(R.string.dialog_rank_stage_title, this.totalPoint));
		Button btnCancel = (Button)rootView.findViewById(R.id.btn_dialog_rank_stage_cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				RankStageDialog.this.dismiss();
			}
		});
		ListView lstRankStage = (ListView)rootView.findViewById(R.id.lst_rank_stage);
		RankStageListAdapter adapter = new RankStageListAdapter(getContext(), R.layout.item_dialog_rank_stage, rankStages);
		// adapter.setTotalPost(totalPost);
		lstRankStage.setAdapter(adapter);
	}
}
