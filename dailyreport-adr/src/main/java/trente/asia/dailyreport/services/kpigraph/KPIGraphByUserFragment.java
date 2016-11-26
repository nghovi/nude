package trente.asia.dailyreport.services.kpigraph;

import java.util.ArrayList;
import java.util.List;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.dailyreport.R;
import trente.asia.dailyreport.fragments.AbstractDRFragment;
import trente.asia.dailyreport.services.kpigraph.model.ReportMonthyGraphHolderModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class KPIGraphByUserFragment extends AbstractDRFragment{

	private List<ReportMonthyGraphHolderModel>	modelList;
	private BarChart							chart;
	private BarDataSet							barDataSet;
	private ArrayList<BarDataSet>				dataSets	= new ArrayList<>();
	private ArrayList<BarEntry>					valueSet	= new ArrayList<BarEntry>();
	private ArrayList<String>					xAxis		= new ArrayList<>();
	private float								valueX		= 0.0f;

	public void setModelList(List<ReportMonthyGraphHolderModel> modelList){
		this.modelList = modelList;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_kpigraph_byuser, container, false);
	}

	@Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_kpigraph_byuser;
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public void buildBodyLayout(){
		super.initHeader(null, getString(R.string.kpi_graph_byuser), null);

		ImageView imgClose = (ImageView)getView().findViewById(R.id.img_id_header_right_icon);
		imgClose.setVisibility(View.VISIBLE);
		imgClose.setImageResource(R.drawable.wf_close_white);
		imgClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				onClickBackBtn();
			}
		});

		chart = (BarChart)getView().findViewById(R.id.chart);
		chart.setDescription("");

		BarData data = new BarData(getXAxisValues(), getDataSet());
		data.setValueFormatter(new PercentFormatter());
		chart.setData(data);
		chart.animateXY(2000, 2000);
		chart.isPinchZoomEnabled();

		XAxis xAxis = chart.getXAxis();
		xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
		xAxis.setDrawLabels(true);
		xAxis.setSpaceBetweenLabels(3);

		chart.invalidate();
	}

	private ArrayList<BarDataSet> getDataSet(){
		for(ReportMonthyGraphHolderModel model : modelList){
			if(!CCStringUtil.isEmpty(model.goal.actualRate)){
				valueX = Float.parseFloat(model.goal.actualRate) * 100;
				valueSet.add(new BarEntry(valueX, modelList.indexOf(model)));
			}
		}
		barDataSet = new BarDataSet(valueSet, "");
		barDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
		dataSets.add(barDataSet);
		return dataSets;
	}

	private ArrayList<String> getXAxisValues(){
		for(ReportMonthyGraphHolderModel model : modelList){
			if(!CCStringUtil.isEmpty(model.userName)){
				xAxis.add(model.userName);
			}
		}
		return xAxis;
	}

	@Override
	public void onDestroy(){

		modelList = null;
		chart = null;
		barDataSet = null;
		dataSets = null;
		valueSet = null;
		xAxis = null;
		super.onDestroy();

	}
}
