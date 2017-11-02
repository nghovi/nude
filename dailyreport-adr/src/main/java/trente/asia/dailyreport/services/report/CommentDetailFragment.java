package nguyenhoangviet.vpcorp.dailyreport.services.report;

import android.widget.ImageView;
import android.widget.TextView;

import nguyenhoangviet.vpcorp.dailyreport.R;
import nguyenhoangviet.vpcorp.dailyreport.fragments.AbstractDRFragment;
import nguyenhoangviet.vpcorp.welfare.adr.models.CommentModel;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by viet on 2/15/2016.
 */
public class CommentDetailFragment extends AbstractDRFragment{

	private static final int FILE_SELECT_CODE = 1234;

	@Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_comment_detail;
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	ImageView	photo;
	TextView	comment;

	public void setCommentModel(CommentModel commentModel){
		this.commentModel = commentModel;
	}

	CommentModel commentModel;

	@Override
	public void buildBodyLayout(){
		super.initHeader(R.drawable.wf_back_white, getString(R.string.fragment_title_empty), null);

		photo = (ImageView)getView().findViewById(R.id.fragment_comment_detail_photo);
		comment = (TextView)getView().findViewById(R.id.fragment_comment_detail_content);
		WfPicassoHelper.loadImage2(activity, host, photo, commentModel.attachment.fileUrl);
		comment.setText(commentModel.commentContent);
		updateHeader(commentModel.commentUser.userName + "\n" + commentModel.commentDate);
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		photo = null;
		comment = null;
	}

}
