package nguyenhoangviet.vpcorp.android.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import nguyenhoangviet.vpcorp.android.R;
import nguyenhoangviet.vpcorp.android.util.AndroidUtil;

public class ChiaseDownloadFileDialog extends Dialog{

	private TextView	txtProgress;
	private TextView	txtTitle;
	private ProgressBar	pgrDownload;

	private int			max;
	private String		maxString;

	public static ChiaseDownloadFileDialog startDialog(Context context){
		ChiaseDownloadFileDialog dialog = new ChiaseDownloadFileDialog(context);
		return dialog;
	}

	public ChiaseDownloadFileDialog(Context context){
		// super(context, R.style.Theme_Dialog);
		super(context);

		setCancelable(false);
		setCanceledOnTouchOutside(false);

		setOwnerActivity((Activity)context);

		show();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_download_file);

		txtTitle = (TextView)findViewById(R.id.txt_id_title);
		txtProgress = (TextView)findViewById(R.id.txt_id_progress);
		pgrDownload = (ProgressBar)findViewById(R.id.pgr_id_download);
	}

	public void setMax(int max){
		pgrDownload.setMax(max);
		this.max = max;
		this.maxString = AndroidUtil.readableFileSize(max);
		String text = "0/" + maxString;
		txtProgress.setText(text);
	}

	public void setCurrent(int current){
		pgrDownload.setProgress(current);
		String text = AndroidUtil.readableFileSize(current) + "/" + maxString;
		txtProgress.setText(text);
	}

	// public void fileDownloaded(){
	// progressPb.setVisibility(View.INVISIBLE);
	// loading.setVisibility(View.VISIBLE);
	// progressTv.setText(getOwnerActivity().getString(R.string.finishing));
	// }

}