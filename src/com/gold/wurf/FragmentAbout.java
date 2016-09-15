package com.gold.wurf;

import com.gold.wurf.R;

import android.support.v4.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentAbout extends Fragment{

	// ������ ������, ������� �������� ����� ������ ������� �������� � ����������� �� ���������� ������
    DynamicOrientationDetection DOD = new DynamicOrientationDetection();

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	   super.onConfigurationChanged(newConfig);
       // ������ ������� �������� � ����������� �� ���������� ������
	   DOD.setBackgroundImage(this.getResources().getConfiguration().orientation, getClass().getSimpleName(), getView());
	}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// ���������� ���������� ��������� fragment_about
	  	setRetainInstance(true);
		// �������� ��� ��������� (�� ��������� � ������������ ������� ��������� ��������)
		View v = inflater.inflate(R.layout.fragment_about, null);
		// �������� �����, ������� ��������� ���������� ������ � ������ ��������������� ������� �������� �� ���������
	    DOD.setBackgroundImage(this.getResources().getConfiguration().orientation, getClass().getSimpleName(), v);
	    // ���������� ��� � ������ ������� ���������
		return v;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// ������ ����� ��������� ��������������
		TextView tvAboutWurf = (TextView) getView().findViewById(R.id.textViewAbout);
		tvAboutWurf.setMovementMethod(new ScrollingMovementMethod());
		//tvAboutWurf.setMovementMethod(LinkMovementMethod.getInstance());
		//tvAboutWurf.setLinksClickable(true);
		//tvAboutWurf.setAutoLinkMask(Linkify.WEB_URLS);
	}
}
