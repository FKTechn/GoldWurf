package com.gold.wurf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BarRenderer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

import com.gold.wurf.R;
import android.graphics.Shader;
import android.support.v4.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentTop10 extends Fragment {
	// ������ ������
	private XYPlot Top10WurfsPlot = null;
	private SimpleXYSeries WurfsBarSeries, Wurf_1618_Harmony_Serie;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// ���������� ���������� ��������� fragment_top10
		View view =  inflater.inflate(R.layout.fragment_top10, null);
		setRetainInstance(true);
		return view;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// ���������� ����� ����������� ����� �� ����� � ������� �� ������
		LoadAndDisplayWurfs();
	}

	/** ����� ���������� � ���������� �����, ���������� ��������� � ���� WurfsTop10.DAT
	 * 
	 */
	public void LoadAndDisplayWurfs()
	  {
        // ������ ������
        Top10WurfsPlot = (XYPlot) getView().findViewById(R.id.Top10Plot);
        Top10WurfsPlot.clear();

		// ��������� ����� ��������� �� ����� WurfsTop10.DAT � ������� �� ������
		// ����� ����� � ����� �� 10, �� ���� ��������� public void onClick(View v) � FragmentMain.java, ��� ��� ����� �� ���������
		// �������������� �� ������� ����� �� 10 ��������� (�����)
		File Top10File = new File(getActivity().getFilesDir(), "WurfsTop10.DAT");
		if(Top10File.exists())
		{
	        WurfsBarSeries = new SimpleXYSeries("���-10 ������");
	        WurfsBarSeries.useImplicitXVals();
	        // ������� - �������� ������ ������ �����
	        BarFormatter bf1 = new BarFormatter(Color.argb(100, 0, 200, 0), Color.rgb(0, 10, 80));
	        bf1.setPointLabelFormatter(new PointLabelFormatter(Color.WHITE));
		    // ����������� ������� �����
	        Paint barFill = new Paint();
	        barFill.setAlpha(150);
	        barFill.setShader(new LinearGradient(0, 0, 50, 250, Color.CYAN, Color.BLUE, Shader.TileMode.MIRROR));
	        bf1.setFillPaint(barFill);
	        // Ҹ���-����� ����� ������ �������, �.�. �� ��������� ������ ��������� �� �� ������������ ������
			Top10WurfsPlot.setBorderStyle(XYPlot.BorderStyle.SQUARE, null, null);
			Top10WurfsPlot.getBorderPaint().setStrokeWidth(6);
			Top10WurfsPlot.getBorderPaint().setAntiAlias(false);
			Top10WurfsPlot.getBorderPaint().setColor(Color.DKGRAY);
        	// ����� "1.618"
	        Wurf_1618_Harmony_Serie = new SimpleXYSeries("1.618");
	        LineAndPointFormatter lineAndPointFormatter = new LineAndPointFormatter(Color.RED, Color.RED, null, null);
	        // ������� �����
	        Paint paint = lineAndPointFormatter.getLinePaint();
	        paint.setStrokeWidth(8);
	        lineAndPointFormatter.setLinePaint(paint);
	        Wurf_1618_Harmony_Serie.useImplicitXVals();
	        // ��������� ����� � ����� �� ������
	        Top10WurfsPlot.addSeries(WurfsBarSeries, bf1);
	        Top10WurfsPlot.addSeries(Wurf_1618_Harmony_Serie, lineAndPointFormatter);
	        Top10WurfsPlot.setDomainStepValue(3);
	        Top10WurfsPlot.setTicksPerRangeLabel(3);
			// ������� �������
			// Top10WurfsPlot.getLegendWidget().setSize(new SizeMetrics(15, SizeLayoutType.ABSOLUTE, 200, SizeLayoutType.ABSOLUTE));
	        // �������� ����� �� ���� ������
	        Top10WurfsPlot.setGridPadding(25, 0, 25, 0);
	        // ������� �����
	        BarRenderer<?> barRenderer = (BarRenderer<?>) Top10WurfsPlot.getRenderer(BarRenderer.class);
	        if(barRenderer != null) { barRenderer.setBarWidth(32); }
	        // ��������� ����� �� ����� � ������ series1Numbers
	        Double[] series1Numbers = new Double[10];
			int i = 0, j = 0;

				try {
					FileInputStream inputStream = getActivity().openFileInput(Top10File.getName());
					InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
					BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
					//StringBuilder sb = new StringBuilder();
					String line;
					//series1Numbers[0] = 1.257;
					try {
						while ((line=bufferedReader.readLine()) != null) {
						    //sb.append(line);
							series1Numbers[i] = Double.parseDouble(line);
					        Wurf_1618_Harmony_Serie.addLast(null, 1.618);
							i++;
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/* ���������� Max ������� series1Numbers */
				/* � i ����� ���-�� ����������� ����� series1Numbers */
				double Max = series1Numbers[0];
				for(j = 1; j <= i-1; j++)
				{
					if(series1Numbers[j] > Max) { Max = series1Numbers[j]; }
				}

		        // ��������������� ����� �� ������: �������� �� ����� ������� ��� (������ ��������) + 0.2 (����� ����������� ������� �� ��������� �����)
		        Top10WurfsPlot.setRangeBoundaries(0, Max + 0.2, BoundaryMode.FIXED);
				// setDomainBoundaries - ���������� ������� ��� ����� (�� ���-�� ����� � �����)
				// ���� i == 1, �� � ����� ���� ������. ������ ��� �� ������
		        if (i == 1) { Top10WurfsPlot.setDomainBoundaries(-0.5, 0.5, BoundaryMode.FIXED); }
		        else { Top10WurfsPlot.setDomainBoundaries(0, i-1, BoundaryMode.FIXED); }
		        WurfsBarSeries.setModel(Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY);
		        // �������������� ������
		        Top10WurfsPlot.redraw();
			}
			else
			{	// ���� "WurfsTop10.DAT" �� ������? �������� ��������������� ������
				DialogFragment FileNotExistsDialog = new AlertDialogsFragment();
				FileNotExistsDialog.show(getFragmentManager(), "WurfsFileNotExistsDialog");
			}
	  }
}
