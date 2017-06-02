package trente.asia.android.view.adapter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import trente.asia.android.R;
import trente.asia.android.item.FileDataItem;

/**
 * Created by TrungND on 10/23/2014.
 */
public class FileListAdapter extends ArrayAdapter<FileDataItem>{

	private Activity			context;
	private List<FileDataItem>	mLstFile;
	// private Integer imageId;

	public FileListAdapter(Activity context, List<FileDataItem> lstFile){
		super(context, R.layout.file_list, lstFile);
		this.context = context;
		this.mLstFile = lstFile;
		// this.imageId = imageId;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent){
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.file_list, null, true);
		FileDataItem item = getItem(position);

		TextView txtname = (TextView)rowView.findViewById(R.id.txt_id_name);
		// ImageView imgIcon = (ImageView)rowView.findViewById(R.id.img_id_icon);
		CheckBox cbxChoose = (CheckBox)rowView.findViewById(R.id.cbx_id_choose);

		txtname.setText(item.getFileName());
		// imgIcon.setImageResource(imageId);
		if(FileDataItem.FILE == item.getFileType()){
			cbxChoose.setVisibility(View.VISIBLE);
		}
		return rowView;
	}
}
