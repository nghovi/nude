package nguyenhoangviet.vpcorp.thankscard.services.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by viet on 2/18/2016.
 */
public class PageFragment extends Fragment{

	/**
	 * Created by viet on 2/19/2016.
	 */
	public interface PageModelContainer{

		Object getPageModel();
	}

	public interface OnPageLayoutBuilder{

		View buildPageLayout(LayoutInflater inflater, PageModelContainer pageModelContainer);
	}

	PageModelContainer	pageModelContainer;
	OnPageLayoutBuilder	builder;

	public void setContent(PageModelContainer pageModelContainer, OnPageLayoutBuilder builder){
		this.pageModelContainer = pageModelContainer;
		this.builder = builder;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return builder.buildPageLayout(inflater, pageModelContainer);
	}

}
