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
	// ������������ ������ IDE
	private static final String LOG_TAG = "FlankLOGS";

	// ������ "���������� ����"
	Button BtnWurf;
	// TextView � ������������ (����������� ���� � ��������� ����������)
	TextView tvWurfResult;
	// 4 ���������� ������ � ���������� ������� (����� �� 1 �� 10)
	Spinner r1, r2, r3, r4;
	String[] spinner_values = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
	// ���������, ������� �������� � �����. "������� ���-10" ��������� ��������
	SharedPreferences sPref;

	// ��������� ����������
	public static final String MYFONT = "fonts/Monotype_corsiva.ttf";
	private static Typeface myfont = null;

	// ������ ������, ������� �������� ����� setBackgroundImage ������ ������� �������� 
	// � ����������� �� ���������� ������
    DynamicOrientationDetection DOD = new DynamicOrientationDetection();

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    // ������ ������� �������� � ����������� �� ���������� ������
	    // getClass().getSimpleName() - ��� "FragmentMain", ��� ������
	    DOD.setBackgroundImage(this.getResources().getConfiguration().orientation, getClass().getSimpleName(), getView());
    }

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    // ���������� ���������� ��������� fragment_main
	    setRetainInstance(true);
	    // �������� ��� ��������� (�� ��������� � ������������ ������� ��������� ��������)
	    View v = inflater.inflate(R.layout.fragment_main, null);
	    // �������� �����, ������� ��������� ���������� ������ � ������ ��������������� ������� �������� �� ���������
	    DOD.setBackgroundImage(this.getResources().getConfiguration().orientation, getClass().getSimpleName(), v);
	    // ���������� ��� � ������ ������� ���������
	    return v;
	}

	// ���������� ������� ������ "���������� ����"
	public void onClick(View v)
	{
		// �������� �������� ��������� r1, r2, r3, r4
		float R1 = Integer.parseInt(r1.getSelectedItem().toString());
		float R2 = Integer.parseInt(r2.getSelectedItem().toString());
		float R3 = Integer.parseInt(r3.getSelectedItem().toString());
		float R4 = Integer.parseInt(r4.getSelectedItem().toString());

		// ��������� ����
		float WurfValue =  ((R1 + 2*R2 + R3)*(R2 + 2*R3 + R4)) / ((R2 + R3)*(R1 + 2*R2 + 2*R3 + R4));		

		// ������� ��������� � tvWurfResult
		// ���� ����� ���� 1.618
		DecimalFormat WurfFormat = new DecimalFormat("#.###");
		String Explanation = getString(R.string.YourWurf) + WurfFormat.format(WurfValue) + " ";
		// ������� ����� ��������� ������ � �����������
		float Difference = Math.abs(Math.round(1000*(1.6180339887498948482045868343655 - WurfValue)));
		// ���� ������� ������ � ����, ������� ������������
		if (Difference == 0)
		{
			Explanation = getString(R.string.YourWurfIsIdeal);
		}
		// ����� - ������� ����� � ����������� ������ � ���-��� �����, ���������� �� ��������
		else
		{
			Explanation += getString(R.string.WurfResultExplanation)+ " " + get_correct_str(Math.abs(Math.round(1000*(1.6180339887498948482045868343655 - WurfValue))));
		}
		// ������� ����������� ���� � ���������
		tvWurfResult.setText(Explanation);
		tvWurfResult.setBackgroundResource(R.color.tvExplanationColor);
		Log.d(LOG_TAG, "���������� ������� ������� ����� � ����������� "+(int)R1+" "+(int)R2+" "+(int)R3+" "+(int)R4);

	    // �������� ������ ����� "������� ���-10" ��������� �������� (true/false)
	    Boolean pref_SaveWurfs = true;
	    sPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
	    if(sPref.contains("pref_SaveWurfs"))
	    {
		    pref_SaveWurfs = sPref.getBoolean("pref_SaveWurfs", true);
	    }
		// ����� ��� �� ����� ������������ ���� � ����. ������� �� ������� ����� "������� ���-10" � ��������� ��������
		// ���� ����� �����������
		if(pref_SaveWurfs)
		{
		    // ����� ���� � ���� WurfsTop10.DAT � ������ append:
		    // 1.324
		    // 1.115
		    // 1.895
		    // ...
		    File Top10File = new File(getActivity().getFilesDir(), "WurfsTop10.DAT");
		    // ���� ���� WurfsTop10.DAT �� ���������� (��������, ��� ������ ������� ����������), �������� ���
		    if(!Top10File.exists()) {
				try {
					Top10File.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
		   	}

		    // ���-10 ��������, ��� ������� ���� ������ ������� 10 ������ => ��� ����� 10-�������� ����
		    // ���� ���-�� ����� � ����� == 10, �������� ������ 2..9 � ������, ����� � ���� �� ������ �� 10-� ����� ��������� ����� ����
		    // � ���������� ������ � ���� ���������
		    FileOutputStream outputStream;
		    try {
		        outputStream = getActivity().openFileOutput(Top10File.getName(), Context.MODE_PRIVATE | Context.MODE_APPEND);
			    // �������� ���-�� ����� � �����
			    int lines = 0;
			    try {lines=CountLines(Top10File.getAbsolutePath());} catch (IOException e2) {e2.printStackTrace();/*e2.getMessage()*/}
			    if (lines == 10)
			    {
			    	String WurfTmpArray[] = new String[10];
			    	// ��� ����������� ������ ����� � ������ WurfTmpArray
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
					// 10-� ������
					WurfTmpArray[lines] = WurfFormat.format(WurfValue);//String.valueOf((Math.round(WurfValue*1000)/1000.0d))+"\n";
					// ������� ���� WurfsTop10.DAT
					FileWriter fw = new FileWriter(Top10File, false);
			        fw.write("");
			        fw.close();
			        // ����� � ���� WurfsTop10.DAT ������ WurfTmpArray ���������
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

	// ����� ����� ���������� ��������� ����� "���" � ����������� �� ����������� � ��������� ����� �����, ���������� �� ��������
	// ��������, ����������� ���������� ����� "20 �����."
	public String get_correct_str(long l) 
	 {
		String str1 = "���.";
		String str2 = "����.";
		String str3 = "�����.";
		
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

	// ����� ������������ ���-�� ����� � ���������� ����� (��������� ������ (� ����) ��� ����� WurfsTop10.DAT)
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
	    // ��������������� ������� ���������� ������� r1, r2, r3, r4
	    // spinner_layout - ��� ����� ��� ��������� (������ ������, gravity �� ������)
		ArrayAdapter<String> adapter = new ArrayAdapter<String> (getActivity(), R.layout.spinner_layout, spinner_values)
		{
			// ����� � ��������� ����� ���������� �� ������
			public View getDropDownView(int position, View convertView, ViewGroup parent) {
			       View v = super.getDropDownView(position, convertView, parent);
			       ((TextView) v).setGravity(Gravity.CENTER);
			       return v;
			}
		};

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Log.d(LOG_TAG, "����� ��� ��������� ���������");

		// ������� �������� (�����. ������) r1, r2, r3, r4 � ����������� �� ������� (���������� 1..10)
		r1 = (Spinner) getView().findViewById(R.id.r1); r1.setAdapter(adapter);
		r2 = (Spinner) getView().findViewById(R.id.r2); r2.setAdapter(adapter);
		r3 = (Spinner) getView().findViewById(R.id.r3); r3.setAdapter(adapter);
		r4 = (Spinner) getView().findViewById(R.id.r4); r4.setAdapter(adapter);
		Log.d(LOG_TAG, "�������� �������");

		// ������� ������ "���������� ����" � ��������� ���� � ���������� ����������
		BtnWurf = (Button) getView().findViewById(R.id.BtnWurf);
		BtnWurf.setOnClickListener(this);
		tvWurfResult = (TextView) getView().findViewById(R.id.textView_WURF_Value);
		tvWurfResult.setMovementMethod(new ScrollingMovementMethod());
		//tvWurfResult.setScrollbarFadingEnabled(false);
		// ������������� �� ����� Monotype corsiva
		if (myfont == null)
		{
			Typeface Monotype_corsiva = Typeface.createFromAsset(getActivity().getAssets(), MYFONT);
			tvWurfResult.setTypeface(Monotype_corsiva);
			BtnWurf.setTypeface(Monotype_corsiva);
		}
		Log.d(LOG_TAG, "���������� �������� �������");
	}

}
