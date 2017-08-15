package trente.asia.thankscard.services.mypage.model;

import java.util.List;

import trente.asia.thankscard.services.common.model.Template;
import trente.asia.welfare.adr.models.DeptModel;

/**
 * Created by viet on 2/18/2016.
 */
public class MypageModel{

	public int					pointPost;
	public int					pointReceive;
	public int					pointTotal;

	public int					seqPost;
	public int					seqRecieve;
	public int					stageNo;
	public int					archivePost;
	public int					archiveReceive;

	public List<DeptModel>		depts;
	public List<Template>		templates;
	public List<NoticeModel>	notices;

}
