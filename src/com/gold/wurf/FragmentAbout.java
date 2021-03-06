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

	// Создаём объект, который содержит метод замены фоновой картинки в зависимости от ориентации экрана
    DynamicOrientationDetection DOD = new DynamicOrientationDetection();

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	   super.onConfigurationChanged(newConfig);
       // Ставим фоновую картинку в зависимости от ориентации экрана
	   DOD.setBackgroundImage(this.getResources().getConfiguration().orientation, getClass().getSimpleName(), getView());
	}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Отображаем содержимое фрагмента fragment_about
	  	setRetainInstance(true);
		// Получаем вид фрагмента (по умолчанию с вертикальной фоновой картинкой снежинки)
		View v = inflater.inflate(R.layout.fragment_about, null);
		// Вызываем метод, который проверяет ориентацию экрана и ставит соответствующую фоновую картинку со снежинкой
	    DOD.setBackgroundImage(this.getResources().getConfiguration().orientation, getClass().getSimpleName(), v);
	    // Возвращаем вид с нужной фоновой картинкой
		return v;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Делаем текст фрагмента прокручиваемым
		TextView tvAboutWurf = (TextView) getView().findViewById(R.id.textViewAbout);
		tvAboutWurf.setMovementMethod(new ScrollingMovementMethod());
		//tvAboutWurf.setMovementMethod(LinkMovementMethod.getInstance());
		//tvAboutWurf.setLinksClickable(true);
		//tvAboutWurf.setAutoLinkMask(Linkify.WEB_URLS);
	}
}
