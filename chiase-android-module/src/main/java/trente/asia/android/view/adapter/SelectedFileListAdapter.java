package trente.asia.android.view.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import trente.asia.android.R;

/**
 * Created by TrungND on 10/23/2014.
 */
public class SelectedFileListAdapter extends ArrayAdapter<SelectedFileItem>{

	private Activity					context;
	private ArrayList<SelectedFileItem>	list;
	private Integer						imageId;

	public SelectedFileListAdapter(Activity context, ArrayList<SelectedFileItem> list, Integer imageId){
		super(context, R.layout.selected_file_list, list);
		this.context = context;
		this.list = list;
		this.imageId = imageId;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent){
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.selected_file_list, null, true);
		TextView txtTitle = (TextView)rowView.findViewById(R.id.txt);
		ImageView imageView = (ImageView)rowView.findViewById(R.id.img);
		ImageView deleteView = (ImageView)rowView.findViewById(R.id.delete);
		txtTitle.setText(list.get(position).getName());
		imageView.setImageResource(imageId);
		deleteView.setImageResource(R.drawable.ic_action_remove);
		return rowView;
	}
}
