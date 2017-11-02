package nguyenhoangviet.vpcorp.thankscard.services.common.model;

import java.util.List;

import nguyenhoangviet.vpcorp.thankscard.services.posted.model.ApiStickerModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.AttachmentModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.LikeModel;

/**
 * Created by viet on 2/17/2016.
 */
public class HistoryModel{

	public Template					template;
	public String					message;
	public String					loginUserId;
	public String					posterId;
	public String					posterName;
	public String					receiverName;
	public String					receiverId;
	public String					postDate;
	public String					categoryId;
	public String					deleteFlag;
	public List<LikeModel>			likeInfo;
	public String					cateogryId;
	public String					key;
	public String					posterAvatarPath;
	public boolean					isSecret;
	public List<ApiStickerModel>	stickers;
	public String					templateType;
	public String					photoLocationX;
	public String					photoLocationY;
	public String					photoScale;
	public AttachmentModel			attachment;
	public int						posterTotalPoint;

	public void setTemplate(Template template){
		this.template = template;
	}

	public HistoryModel(){
	}

	public HistoryModel(String key){
		this.key = key;
	}

	public HistoryModel(String message, String posterId, String receiverId, String postDate){
		this.message = message;
		this.posterId = posterId;
		this.receiverId = receiverId;
		this.postDate = postDate;
	}
}
