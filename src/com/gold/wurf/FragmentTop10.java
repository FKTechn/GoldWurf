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
	// График вурфов
	private XYPlot Top10WurfsPlot = null;
	private SimpleXYSeries WurfsBarSeries, Wurf_1618_Harmony_Serie;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Отображаем содержимое фрагмента fragment_top10
		View view =  inflater.inflate(R.layout.fragment_top10, null);
		setRetainInstance(true);
		return view;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Подгружаем ранее рассчитаные вурфы из файла и выводим на график
		LoadAndDisplayWurfs();
	}

	/** Метод подгружает и отображает вурфы, записанные построчно в файл WurfsTop10.DAT
	 * 
	 */
	public void LoadAndDisplayWurfs()
	  {
        // График вурфов
        Top10WurfsPlot = (XYPlot) getView().findViewById(R.id.Top10Plot);
        Top10WurfsPlot.clear();

		// Считываем вурфы построчно из файла WurfsTop10.DAT и выводим на график
		// Всего строк в файле до 10, об этом заботится public void onClick(View v) в FragmentMain.java, так что здесь не проверяем
		// Соответственно на графике будет до 10 столбиков (баров)
		File Top10File = new File(getActivity().getFilesDir(), "WurfsTop10.DAT");
		if(Top10File.exists())
		{
	        WurfsBarSeries = new SimpleXYSeries("ТОП-10 ВУРФОВ");
	        WurfsBarSeries.useImplicitXVals();
	        // Подписи - значения вурфов вверху баров
	        BarFormatter bf1 = new BarFormatter(Color.argb(100, 0, 200, 0), Color.rgb(0, 10, 80));
	        bf1.setPointLabelFormatter(new PointLabelFormatter(Color.WHITE));
		    // Градиентная заливка баров
	        Paint barFill = new Paint();
	        barFill.setAlpha(150);
	        barFill.setShader(new LinearGradient(0, 0, 50, 250, Color.CYAN, Color.BLUE, Shader.TileMode.MIRROR));
	        bf1.setFillPaint(barFill);
	        // Тёмно-серая рамка вокруг графика, т.к. по умолчанию график заполняет не всё пространство экрана
			Top10WurfsPlot.setBorderStyle(XYPlot.BorderStyle.SQUARE, null, null);
			Top10WurfsPlot.getBorderPaint().setStrokeWidth(6);
			Top10WurfsPlot.getBorderPaint().setAntiAlias(false);
			Top10WurfsPlot.getBorderPaint().setColor(Color.DKGRAY);
        	// Линия "1.618"
	        Wurf_1618_Harmony_Serie = new SimpleXYSeries("1.618");
	        LineAndPointFormatter lineAndPointFormatter = new LineAndPointFormatter(Color.RED, Color.RED, null, null);
	        // Толщина линии
	        Paint paint = lineAndPointFormatter.getLinePaint();
	        paint.setStrokeWidth(8);
	        lineAndPointFormatter.setLinePaint(paint);
	        Wurf_1618_Harmony_Serie.useImplicitXVals();
	        // Добавляем серии и сетку на график
	        Top10WurfsPlot.addSeries(WurfsBarSeries, bf1);
	        Top10WurfsPlot.addSeries(Wurf_1618_Harmony_Serie, lineAndPointFormatter);
	        Top10WurfsPlot.setDomainStepValue(3);
	        Top10WurfsPlot.setTicksPerRangeLabel(3);
			// Позиция легенды
			// Top10WurfsPlot.getLegendWidget().setSize(new SizeMetrics(15, SizeLayoutType.ABSOLUTE, 200, SizeLayoutType.ABSOLUTE));
	        // Смещение баров от краёв экрана
	        Top10WurfsPlot.setGridPadding(25, 0, 25, 0);
	        // Толщина баров
	        BarRenderer<?> barRenderer = (BarRenderer<?>) Top10WurfsPlot.getRenderer(BarRenderer.class);
	        if(barRenderer != null) { barRenderer.setBarWidth(32); }
	        // Загружаем вурфы из файла в массив series1Numbers
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
				/* Определяем Max массива series1Numbers */
				/* В i лежит кол-во заполненных ячеек series1Numbers */
				double Max = series1Numbers[0];
				for(j = 1; j <= i-1; j++)
				{
					if(series1Numbers[j] > Max) { Max = series1Numbers[j]; }
				}

		        // Масштабирование баров по высоте: равнение на самый высокий бар (второй параметр) + 0.2 (чтобы поместилась надпись со значением вурфа)
		        Top10WurfsPlot.setRangeBoundaries(0, Max + 0.2, BoundaryMode.FIXED);
				// setDomainBoundaries - количество позиций для баров (по кол-ву строк в файле)
				// Если i == 1, то в файле одна строка. Строим бар по центру
		        if (i == 1) { Top10WurfsPlot.setDomainBoundaries(-0.5, 0.5, BoundaryMode.FIXED); }
		        else { Top10WurfsPlot.setDomainBoundaries(0, i-1, BoundaryMode.FIXED); }
		        WurfsBarSeries.setModel(Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY);
		        // Перерисовываем график
		        Top10WurfsPlot.redraw();
			}
			else
			{	// Файл "WurfsTop10.DAT" не найден? Вызываем соответствующий диалог
				DialogFragment FileNotExistsDialog = new AlertDialogsFragment();
				FileNotExistsDialog.show(getFragmentManager(), "WurfsFileNotExistsDialog");
			}
	  }
}
