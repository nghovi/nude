package trente.asia.welfare.adr.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.os.AsyncTask;

/**
 * BuildTempFileAsync
 * reference Spika open source
 */
public class BuildTempFileAsync extends AsyncTask<InputStream, Void, String>{

	private String						mFilePath;
	private String						mFileName;
	private OnTempFileCreatedListener	mListener;
	private Context						ctx;

	public BuildTempFileAsync(Context ctx, String filePath, String fileName, OnTempFileCreatedListener listener){
		this.mFilePath = filePath;
		this.mFileName = fileName;
		this.mListener = listener;
		this.ctx = ctx;
	}

	@Override
	protected String doInBackground(InputStream...params){
		try{
			InputStream in = params[0];

			File tempFile = new File(mFilePath);
			OutputStream out = new FileOutputStream(tempFile);

			// Transfer bytes from in to out
			byte[] buf = new byte[1024];
			int len;
			while((len = in.read(buf)) > 0){
				out.write(buf, 0, len);
			}
			in.close();
			out.close();

			return tempFile.getAbsolutePath();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}

		return "";
	}

	@Override
	protected void onPostExecute(String filePath){
		super.onPostExecute(filePath);
		if(mListener != null){
			mListener.onTempFileCreated(filePath, mFileName);
		}
	}

	public interface OnTempFileCreatedListener{

		void onTempFileCreated(String path, String name);
	}

}
