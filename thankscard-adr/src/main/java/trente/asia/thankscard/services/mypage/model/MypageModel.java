package trente.asia.thankscard.services.mypage.model;

import java.util.List;

import trente.asia.thankscard.services.common.model.Template;
import trente.asia.welfare.adr.models.DeptModel;

/**
 * Created by viet on 2/18/2016.
 */
public class MypageModel{

	public Integer				pointPost;
	public Integer				pointReceive;
	public Integer				pointTotal;

	public Integer				seqPost;
	public Integer				seqRecieve;
	public Integer				stageNo;
	public Integer				archivePost;
	public Integer				archiveReceive;

	public List<DeptModel>		depts;
	public List<Template>		templates;
	public List<NoticeModel>	notices;

}
