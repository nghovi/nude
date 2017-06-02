package trente.asia.android.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import trente.asia.android.R;
import trente.asia.android.item.FileDataItem;
import trente.asia.android.view.adapter.FileListAdapter;

/**
 * Created by TrungND on 10/24/2014.
 */
public class ChiaseFileChooser{

	private Dialog					dialog;

	private Activity				context;

	private File					mCurrentLocation;

	/** The list of files and folders which you can choose from */
	private ListView				mFileListView;

	private Button					btnCancel;
	private List<File>				mLstSelectedFile	= new ArrayList<>();

	private Button					btnOk;
	private boolean					mIsMultiChoice;
	private OnFileChooseListener	mFileChooseListener;

	public ChiaseFileChooser(final Activity context, final String[] fileFilters, boolean isMultiChoice){
		this.context = context;
		// this.selectedFileList = selectedFileList;
		this.mIsMultiChoice = isMultiChoice;

		final File sdCard = Environment.getExternalStorageDirectory();
		if(sdCard.canRead()){
			mCurrentLocation = sdCard;
		}else{
			mCurrentLocation = Environment.getRootDirectory();
		}
		dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Light);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.dialog_file_chooser);
		dialog.setTitle(mCurrentLocation.getAbsolutePath());

		setCancelButton();
		setOkButton();
		prepareFilesList();
	}

	/**
	 * This method prepares the mFileListView
	 */
	private void prepareFilesList(){
		mFileListView = (ListView)dialog.findViewById(R.id.lsv_id_file);
		if(mIsMultiChoice){
			mFileListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
		}else{
			mFileListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		}

		mFileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id){
				// Check if "../" item should be added.
				FileDataItem fileData = (FileDataItem)parent.getItemAtPosition(position);
				if(FileDataItem.UP_FOLDER == fileData.getFileType()){
					// choose up folder
					final String parentLocation = mCurrentLocation.getParent();
					if(parentLocation != null){ // text == "../"
						// String fileFilter = ((TextView) mFilterSpinner.getSelectedView()).getText().toString();
						mCurrentLocation = new File(parentLocation);
						dialog.setTitle(parentLocation);
						makeList(mCurrentLocation);
					}else{
						onItemSelect(parent, position);
					}
				}else{
					onItemSelect(parent, position);
				}
			}
		});
		// String filtr = mFilterSpinner.getSelectedItem().toString();
		makeList(mCurrentLocation);
	}

	/**
	 * The method that fills the list with a directories contents.
	 *
	 * @param location
	 * Indicates the directory whose contents should be displayed in
	 * the dialog_file_chooser.
	 */
	private void makeList(final File location){
		dialog.setTitle(location.getPath());
		ArrayList<FileDataItem> lstFile = new ArrayList<FileDataItem>();
		// ArrayList<FileDataItem> folderNames = new ArrayList<FileDataItem>();

		// final ArrayList<FileData> fileList = new ArrayList<FileData>();
		final String parentLocation = location.getParent();
		if(parentLocation != null){
			// First item on the list.
			lstFile.add(new FileDataItem("../", FileDataItem.UP_FOLDER));
		}
		File listFiles[] = location.listFiles();
		if(listFiles != null){
			ArrayList<FileDataItem> fileDataList = new ArrayList<FileDataItem>();
			for(int index = 0; index < listFiles.length; index++){
				File tempFile = listFiles[index];
				// if (FileUtils.accept(tempFile, fitlesFilter)) {
				int type = tempFile.isDirectory() ? FileDataItem.DIRECTORY : FileDataItem.FILE;
				fileDataList.add(new FileDataItem(listFiles[index].getName(), type));
				// }
				if(tempFile.isDirectory()){
					FileDataItem folder = new FileDataItem(tempFile.getName(), FileDataItem.DIRECTORY);
					lstFile.add(folder);
				}else{
					FileDataItem file = new FileDataItem(tempFile.getName(), FileDataItem.FILE);
					lstFile.add(file);
				}
			}
			// fileList.addAll(fileDataList);
			// Collections.sort(fileList);
		}
		// Fill the list with the contents of fileList.
		if(mFileListView != null){
			FileListAdapter adapter = new FileListAdapter(context, lstFile);
			mFileListView.setAdapter(adapter);
		}
	}

	/**
	 * Handle the file list item selection.
	 * Change the directory on the list or change the name of the saved file if
	 * the user selected a file.
	 *
	 * @param parent
	 * First parameter of the onItemClick() method of
	 * OnItemClickListener. It's a value of text property of the
	 * item.
	 * @param position
	 * Third parameter of the onItemClick() method of
	 * OnItemClickListener. It's the index on the list of the
	 * selected item.
	 */
	private void onItemSelect(final AdapterView<?> parent, final int position){
		final String itemText = ((FileDataItem)parent.getItemAtPosition(position)).getFileName();
		final String itemPath = mCurrentLocation.getAbsolutePath() + File.separator + itemText;
		final File itemLocation = new File(itemPath);

		if(!itemLocation.canRead()){
			Toast.makeText(context, "Access denied!!!", Toast.LENGTH_SHORT).show();
		}else if(itemLocation.isDirectory()){
			mCurrentLocation = itemLocation;
			// String fileFilter = ((TextView) mFilterSpinner.getSelectedView()).getText().toString();
			makeList(mCurrentLocation);
		}else if(itemLocation.isFile()){
			// final EditText fileName = (EditText) mDialog.findViewById(R.id.fileName);
			// fileName.setText(itemText);
		}
	}

	/** Simple wrapper around the Dialog.show() method. */
	public void show(){
		mLstSelectedFile.clear();
		dialog.show();
	}

	/** Set onClick() event handler for the cancel button. */
	private void setCancelButton(){
		btnCancel = (Button)dialog.findViewById(R.id.fileCancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(final View view){
				dialog.cancel();
			}
		});
	}

	private void setOkButton(){
		btnOk = (Button)dialog.findViewById(R.id.fileOk);
		btnOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(final View view){
				ok();
			}
		});
	}

	private void ok(){
		mLstSelectedFile.clear();
		for(int i = 0; i < mFileListView.getCount(); i++){
			if(mFileListView.isItemChecked(i)){
				FileDataItem fileData = (FileDataItem)mFileListView.getItemAtPosition(i);
				File file = new File(mCurrentLocation.getAbsolutePath() + File.separator + fileData.getFileName());

				mLstSelectedFile.add(file);
			}
		}

		if(mFileChooseListener != null){
			mFileChooseListener.onChoose(mLstSelectedFile);
		}

		dialog.cancel();
	}

	public void setmFileChooseListener(OnFileChooseListener mFileChooseListener){
		this.mFileChooseListener = mFileChooseListener;
	}

	public interface OnFileChooseListener{

		public void onChoose(List<File> lstFile);
	}
}
