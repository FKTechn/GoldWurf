package com.gold.wurf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.DecimalFormat;

import com.gold.wurf.R;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class FragmentMain extends Fragment implements android.view.View.OnClickListener {
	// Логгирование внутри IDE
	private static final String LOG_TAG = "FlankLOGS";

	// Кнопка "Рассчитать вурф"
	Button BtnWurf;
	// TextView с результатами (вычисленный вурф и первичное объяснение)
	TextView tvWurfResult;
	// 4 выпадающих списка и наполнение каждого (числа от 1 до 10)
	Spinner r1, r2, r3, r4;
	String[] spinner_values = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
	// Настройки, которые хранятся в файле. "Хранить ТОП-10" фрагмента настроек
	SharedPreferences sPref;

	// Шрифтовые переменные
	public static final String MYFONT = "fonts/Monotype_corsiva.ttf";
	private static Typeface myfont = null;

	// Создаём объект, который содержит метод setBackgroundImage замены фоновой картинки 
	// в зависимости от ориентации экрана
    DynamicOrientationDetection DOD = new DynamicOrientationDetection();

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    // Ставим фоновую картинку в зависимости от ориентации экрана
	    // getClass().getSimpleName() - это "FragmentMain", имя класса
	    DOD.setBackgroundImage(this.getResources().getConfiguration().orientation, getClass().getSimpleName(), getView());
    }

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    // Отображаем содержимое фрагмента fragment_main
	    setRetainInstance(true);
	    // Получаем вид фрагмента (по умолчанию с вертикальной фоновой картинкой снежинки)
	    View v = inflater.inflate(R.layout.fragment_main, null);
	    // Вызываем метод, который проверяет ориентацию экрана и ставит соответствующую фоновую картинку со снежинкой
	    DOD.setBackgroundImage(this.getResources().getConfiguration().orientation, getClass().getSimpleName(), v);
	    // Возвращаем вид с нужной фоновой картинкой
	    return v;
	}

	// Обработчик нажатия кнопки "Рассчитать вурф"
	public void onClick(View v)
	{
		// Получаем значения спиннеров r1, r2, r3, r4
		float R1 = Integer.parseInt(r1.getSelectedItem().toString());
		float R2 = Integer.parseInt(r2.getSelectedItem().toString());
		float R3 = Integer.parseInt(r3.getSelectedItem().toString());
		float R4 = Integer.parseInt(r4.getSelectedItem().toString());

		// Вычисляем ВУРФ
		float WurfValue =  ((R1 + 2*R2 + R3)*(R2 + 2*R3 + R4)) / ((R2 + R3)*(R1 + 2*R2 + 2*R3 + R4));		

		// Выводим результат в tvWurfResult
		// Вурф будет вида 1.618
		DecimalFormat WurfFormat = new DecimalFormat("#.###");
		String Explanation = getString(R.string.YourWurf) + WurfFormat.format(WurfValue) + " ";
		// Разница между идеальным вурфом и вычисленным
		float Difference = Math.abs(Math.round(1000*(1.6180339887498948482045868343655 - WurfValue)));
		// Если разница близка к нулю, выводим поздравление
		if (Difference == 0)
		{
			Explanation = getString(R.string.YourWurfIsIdeal);
		}
		// иначе - обычный текст с вычисленным вурфом и кол-вом шагов, оставшихся до гармонии
		else
		{
			Explanation += getString(R.string.WurfResultExplanation)+ " " + get_correct_str(Math.abs(Math.round(1000*(1.6180339887498948482045868343655 - WurfValue))));
		}
		// Выводим вычисленный вурф и сообщение
		tvWurfResult.setText(Explanation);
		tvWurfResult.setBackgroundResource(R.color.tvExplanationColor);
		Log.d(LOG_TAG, "Отработала функция расчёта вурфа с параметрами "+(int)R1+" "+(int)R2+" "+(int)R3+" "+(int)R4);

	    // Получаем статус галки "Хранить ТОП-10" фрагмента настроек (true/false)
	    Boolean pref_SaveWurfs = true;
	    sPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
	    if(sPref.contains("pref_SaveWurfs"))
	    {
		    pref_SaveWurfs = sPref.getBoolean("pref_SaveWurfs", true);
	    }
		// Пишем или НЕ пишем рассчитанный вурф в файл. Зависит от статуса галки "Хранить ТОП-10" в фрагменте настроек
		// Если галка УСТАНОВЛЕНА
		if(pref_SaveWurfs)
		{
		    // Пишем вурф в файл WurfsTop10.DAT в режиме append:
		    // 1.324
		    // 1.115
		    // 1.895
		    // ...
		    File Top10File = new File(getActivity().getFilesDir(), "WurfsTop10.DAT");
		    // Если файл WurfsTop10.DAT не существует (например, при первом запуске приложения), создадим его
		    if(!Top10File.exists()) {
				try {
					Top10File.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
		   	}

		    // Топ-10 означает, что хранить надо только крайние 10 вурфов => нам нужен 10-строчный файл
		    // Если кол-во строк в файле == 10, копируем строки 2..9 в массив, потом в этот же массив на 10-е место добавляем новый вурф
		    // и записываем массив в файл построчно
		    FileOutputStream outputStream;
		    try {
		        outputStream = getActivity().openFileOutput(Top10File.getName(), Context.MODE_PRIVATE | Context.MODE_APPEND);
			    // Проверим кол-во строк в файле
			    int lines = 0;
			    try {lines=CountLines(Top10File.getAbsolutePath());} catch (IOException e2) {e2.printStackTrace();/*e2.getMessage()*/}
			    if (lines == 10)
			    {
			    	String WurfTmpArray[] = new String[10];
			    	// Для построчного чтения файла в массив WurfTmpArray
					FileInputStream inputStream = getActivity().openFileInput(Top10File.getName());
					InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
					BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
					String line;
					lines = -1;
					while ((line=bufferedReader.readLine()) != null)
					{
						if (lines > -1)
						{
							WurfTmpArray[lines] = line;	
						}
						lines++;
					}
					// 10-я строка
					WurfTmpArray[lines] = WurfFormat.format(WurfValue);//String.valueOf((Math.round(WurfValue*1000)/1000.0d))+"\n";
					// Очищаем файл WurfsTop10.DAT
					FileWriter fw = new FileWriter(Top10File, false);
			        fw.write("");
			        fw.close();
			        // Пишем в файл WurfsTop10.DAT массив WurfTmpArray построчно
			        for (lines = 0; lines < 9; lines++)
			        {
			        	outputStream.write((WurfTmpArray[lines]+"\n").getBytes());
			        }
			    }
		      outputStream.write(((Math.round(WurfValue*1000)/1000.0d)+"\n").getBytes());
		      outputStream.close();
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
	}
	}

	// Метод выдаёт правильное склонение слова "шаг" в зависимости от переданного в параметре числа шагов, оставшихся до гармонии
	// Например, результатом выполнения будет "20 шагов."
	public String get_correct_str(long l) 
	 {
		String str1 = "шаг.";
		String str2 = "шага.";
		String str3 = "шагов.";
		
		if (l < 0) l *= (-1);
	    int val = (int) (l % 100);

	    if (val > 10 && val < 20) return l +" "+ str3;
	    else {
	        val = (int) (l % 10);
	        if (val == 1) return l +" "+ str1;
	        else if (val > 1 && val < 5) return l +" "+ str2;
	        else return l +" "+ str3;
	    }
	 }

	// Метод подсчитывает кол-во строк в переданном файле (передаётся полное (с путём) имя файла WurfsTop10.DAT)
	public int CountLines(String filename) throws IOException {
		LineNumberReader reader  = new LineNumberReader(new FileReader(filename));
		int cnt = 0;
		String lineRead = "";
		while ((lineRead = reader.readLine()) != null) {}
		cnt = reader.getLineNumber(); 
		reader.close();
		return cnt;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	    // Восстанавливаем элеметы выпадающих списков r1, r2, r3, r4
	    // spinner_layout - мой стиль для спиннеров (размер текста, gravity по центру)
		ArrayAdapter<String> adapter = new ArrayAdapter<String> (getActivity(), R.layout.spinner_layout, spinner_values)
		{
			// Текст в спиннерах будет расположен по центру
			public View getDropDownView(int position, View convertView, ViewGroup parent) {
			       View v = super.getDropDownView(position, convertView, parent);
			       ((TextView) v).setGravity(Gravity.CENTER);
			       return v;
			}
		};

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Log.d(LOG_TAG, "Стиль для спиннеров подключен");

		// Находим спиннеры (выпад. списки) r1, r2, r3, r4 и присваиваем им адаптер (содержимое 1..10)
		r1 = (Spinner) getView().findViewById(R.id.r1); r1.setAdapter(adapter);
		r2 = (Spinner) getView().findViewById(R.id.r2); r2.setAdapter(adapter);
		r3 = (Spinner) getView().findViewById(R.id.r3); r3.setAdapter(adapter);
		r4 = (Spinner) getView().findViewById(R.id.r4); r4.setAdapter(adapter);
		Log.d(LOG_TAG, "Спиннеры найдены");

		// Находим кнопку "Рассчитать вурф" и текстовое поле с пояснением результата
		BtnWurf = (Button) getView().findViewById(R.id.BtnWurf);
		BtnWurf.setOnClickListener(this);
		tvWurfResult = (TextView) getView().findViewById(R.id.textView_WURF_Value);
		tvWurfResult.setMovementMethod(new ScrollingMovementMethod());
		//tvWurfResult.setScrollbarFadingEnabled(false);
		// Устанавливаем им шрифт Monotype corsiva
		if (myfont == null)
		{
			Typeface Monotype_corsiva = Typeface.createFromAsset(getActivity().getAssets(), MYFONT);
			tvWurfResult.setTypeface(Monotype_corsiva);
			BtnWurf.setTypeface(Monotype_corsiva);
		}
		Log.d(LOG_TAG, "Визуальные элементы найдены");
	}

}
