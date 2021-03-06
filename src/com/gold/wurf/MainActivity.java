package com.gold.wurf;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
//import android.app.ActionBar.LayoutParams;
//import android.app.ActionBar.Tab;
//import android.app.Activity;
//import android.app.DialogFragment;

public class MainActivity extends ActionBarActivity implements TabListener{

// Логгирование внутри IDE
private static final String LOG_TAG = "FlankLOGS";
// Фрагменты "Домой" (главная страница), "Топ-10", "О вурфе" и "Настройки"
Fragment frag_main, frag_top10, frag_about, frag_settings;
// Всплывающее окошко с подсказкой, чем являются 4 выпадающих списка (ФИСМ)
PopupWindow PopupStartExplanation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Log.d(LOG_TAG, "[ЗОЛОТОЙ ВУРФ СТАРТОВАЛ]");

		// Навигационная панель
		android.support.v7.app.ActionBar navbar = getSupportActionBar();
		navbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		/* 1-я панелька "Домой" */
		Tab tab = navbar.newTab();
		tab.setIcon(R.drawable.ic_home);
		tab.setTabListener(this);
		navbar.addTab(tab);
		/* 2-я панелька */
		/* Попытка установить custom layout - вверху картинка, внизу текст
		View tabView = this.getLayoutInflater().inflate(R.layout.navbar_layout, null);
		TextView tabText = (TextView) tabView.findViewById(R.id.tabText);
		tabText.setText(R.string.calculate_wurf1);
		ImageView tabImage = (ImageView) tabView.findViewById(R.id.tabIcon);
		tabImage.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_top10));*/
		tab = navbar.newTab();
		//tab.setText("Топ-10");
		tab.setIcon(R.drawable.ic_top10);
		tab.setTabListener(this);
		navbar.addTab(tab);
		/* 3-я панелька */
		tab = navbar.newTab();
		//tab.setText("Настройки");
		tab.setIcon(R.drawable.ic_settings);
		tab.setTabListener(this);
		navbar.addTab(tab);
		/* 4-я панелька */
		tab = navbar.newTab();
		//tab.setText("О вурфе");
		tab.setIcon(R.drawable.ic_about);
		tab.setTabListener(this);
		navbar.addTab(tab);
		Log.d(LOG_TAG, "Табы созданы");

		// Если активити была пересоздана (поменяли ориентацию), выбираем отмеченный ранее таб ActionBar
        if(savedInstanceState != null) 
        {
            int index = savedInstanceState.getInt("TabIndex");
            getSupportActionBar().setSelectedNavigationItem(index);
        }

    	// Статус галки настроек "Хранить ТОП-10" берётся из своего предыдущего состояния (отмечена/не отмечена)
		PreferenceManager.setDefaultValues(this, R.xml.preferences, true);

		/* Показываем попап-окно с разъяснением, что означают выпадающие списки */
		ShowStartExplanatonDialog();
	}

	/* Метод отображает попап-окно с разъяснением, что означают выпадающие списки (4 картинки ФИСМ) */
	public void ShowStartExplanatonDialog()
	{
		// Находим попап-окно
	    TableLayout viewGroup = (TableLayout) this.findViewById(R.id.popup_start_explanation);
	    final LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View layout = layoutInflater.inflate(R.layout.popup_start_explanation, viewGroup);
	    // Создаём его
	    PopupStartExplanation = new PopupWindow(layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    PopupStartExplanation.setContentView(layout);
	    // Обрабатываем ширину поясняющего попап-окна PopupStartExplanation
	    // На маленьких экранах оно отображается некрасиво, поэтому задаём его размеры вручную только на больших экранах,
	    // в остальных случаях предоставляем масштабирование самому андроиду.
	    // Размер самой фоновой картинки start_explanation_background.png 465x445
	    //
	    // Получаем параметры экрана
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		// Ширина и высота экрана
		int screenWidthInPix  = displayMetrics.widthPixels;
		int screenHeightInPix = displayMetrics.heightPixels;
		// Если экран большой, выводим окно в полном размере (иначе андроид сам масштабирует)
		if ((screenWidthInPix >= 465) && (screenHeightInPix >= 445))
			{
			    PopupStartExplanation.setWidth(465);
			    PopupStartExplanation.setHeight(445);
			}
	    // Отображаем попап-окно в новом потоке через интерфейс Runnable(), 
		// т.к. в текущем потоке MainActivity ещё не до конца загрузилась
	    findViewById(R.id.FragmentContainer).post(new Runnable() {
		public void run() {
			    PopupStartExplanation.setFocusable(true);
			    PopupStartExplanation.setOutsideTouchable(true);
			    //PopupStartExplanation.setTouchable(true);
			    //PopupStartExplanation.getContentView().setScrollbarFadingEnabled(false);
			    PopupStartExplanation.setBackgroundDrawable(getResources().getDrawable(R.drawable.start_explanation_background));
			    PopupStartExplanation.setAnimationStyle(R.style.PopupWindowAnimation);
			    //PopupStartExplanation.getContentView().setAnimation(animation);
			    /*PopupStartExplanation.setTouchInterceptor(new OnTouchListener(){
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						PopupStartExplanation.dismiss();
						return true;
					}});*/

			    PopupStartExplanation.showAtLocation(findViewById(R.id.FragmentContainer), Gravity.CENTER_VERTICAL, 0, 100);

			    // Находим 4 картинки ФИСМ в поясняющем попап-окне
			    ImageView ivPhysiology = (ImageView)PopupStartExplanation.getContentView().findViewById(R.id.imageView_physiology);
			    ImageView ivIntellect = (ImageView)PopupStartExplanation.getContentView().findViewById(R.id.imageView_intellect);
			    ImageView ivSocium = (ImageView)PopupStartExplanation.getContentView().findViewById(R.id.imageView_socium);
			    ImageView ivMaterial = (ImageView)PopupStartExplanation.getContentView().findViewById(R.id.imageView_material);
			    // Поясняющий диалог ExplainDialog, который выводится по клику на одну из 4 картинок (ФИСМ) попап-окна
			    // Ф
			    ivPhysiology.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ShowExplanationDialog("PHYS");
					}});
			    // И
			    ivIntellect.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ShowExplanationDialog("INT");
					}});
			    // С
			    ivSocium.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ShowExplanationDialog("SOC");
					}});
			    // М
			    ivMaterial.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ShowExplanationDialog("MAT");
					}});
		   }
		});
	}

	/* Метод выводит на экран поясняющий диалог по одному из 4 критериев (ФИСМ)
	 * @param s - критерий "PHYS", "INT", "SOC" или "MAT"
	 * @ExplainDialog - поясняющий диалог
	 * */
	public void ShowExplanationDialog(String s) {
		// Если андроид 2.xx, то он криво передаёт цвета диалогового окошка - слабый серый текст на чёрном фоне.
		// Исправляем выставлением другой темы
		if(android.os.Build.VERSION.RELEASE.startsWith("2")) {setTheme(R.style.Translucent);}
		//
		DialogFragment ExplainDialog = StartExplanationDialog.newInstance(s);
		ExplainDialog.show(getSupportFragmentManager(), "ExplainDialog");
	}

	// Добавляем пункты меню (меню показывается по нажатию на аппаратную кнопку меню)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.main, menu);
		/* menu.add(groupId, itemId, order, title); */
		menu.add(0, 1, 0, "Домой");
		menu.add(0, 2, 0, "ТОП-10 вурфов");
		menu.add(0, 3, 0, "Настройки");
		menu.add(0, 4, 0, "О вурфе");
		menu.add(0, 5, 0, "Поделиться");
		menu.add(0, 6, 0, "Выход");
		//menu.getItem(1).getActionView().setBackgroundResource(R.color.Gold);

		return super.onCreateOptionsMenu(menu);
	}

	// Обработчик пунктов меню (НЕ ActionBar)
	public boolean onOptionsItemSelected(MenuItem item)
	{
		//Toast.makeText(this, Integer.toString(item.getItemId()), Toast.LENGTH_SHORT).show();
		switch (item.getItemId())
		{
			case 1:
				// ДОМОЙ
				getSupportActionBar().setSelectedNavigationItem(0);
				return true;
			case 2:
				// ТОП-10
				getSupportActionBar().setSelectedNavigationItem(1);
				return true;
			case 3:
				// НАСТРОЙКИ
				getSupportActionBar().setSelectedNavigationItem(2);
				return true;
			case 4:
				// О ВУРФЕ
				getSupportActionBar().setSelectedNavigationItem(3);
				/* Показываем попап-окно с разъяснением, что означают выпадающие списки */
				ShowStartExplanatonDialog();
				return true;
			case 5:
				// ПОДЕЛИТЬСЯ. Вызываем стандартный диалог со всеми подключенными share провайдерами
				TextView tvWurfResult = (TextView) findViewById(R.id.textView_WURF_Value);
				// Если поле с рассчитанным вурфом пустое, значит, вурф ещё не рассчитали. Выводим предупреждающий диалог
				if (tvWurfResult.getText().toString().equals(""))
				{
					Dialog CalculateWurfDialog;
					AlertDialog.Builder adb = new AlertDialog.Builder(this);
					adb.setTitle(R.string.app_name);
					adb.setMessage(R.string.CalculateWurfDialog);
					adb.setNeutralButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// По нажатию на кнопку "ОК" закрываем диалог
							dialog.dismiss();
						}});
					CalculateWurfDialog = adb.create();
					CalculateWurfDialog.show();
				}
				// Если поле с рассчитанным вурфом заполнено, вызываем стандартный диалог со всеми подключенными share провайдерами
				else
				{
					final Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("text/plain");
					intent.putExtra(Intent.EXTRA_SUBJECT, "Золотой вурф");
					intent.putExtra(Intent.EXTRA_TEXT, tvWurfResult.getText()+"\nhttp://result.zz.mu/wurf");
					startActivity(Intent.createChooser(intent, getString(R.string.app_name)));
				}
				return true;
			case 6:
				// ВЫХОД. Завершаем работу приложения
				finish();
				return true;
			default: return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		//Log.d(LOG_TAG, "Выбран уже выбранный таб: " + tab.getText());
	}

	String CurrentFragmentTag = "fragment_main";
	// Метод выводит переданный в параметрах фрагмент
	public void LoadFragment(Fragment NewFragment, String NewFragmentTag) {
	    FragmentManager fm = getSupportFragmentManager();
	    Fragment currFragment  =  fm.findFragmentByTag(CurrentFragmentTag);
	    Fragment newFragment   =  fm.findFragmentByTag(NewFragmentTag);
	    FragmentTransaction ft =  fm.beginTransaction();/*.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)*/

	    if(currFragment != null) {ft.hide(currFragment);}

	    if(newFragment == null) {
	    	// Создаём фрагменты впервые (затем будем просто показывать/прятать нужный фрагмент)
	    	if (NewFragmentTag.equals(getString(R.string.fragment_main)))  	  {newFragment  = new FragmentMain();}
	    	else
	    	if (NewFragmentTag.equals(getString(R.string.fragment_top10))) 	  {newFragment  = new FragmentTop10();}
	    	else
	    	if (NewFragmentTag.equals(getString(R.string.fragment_settings))) {newFragment  = new FragmentSettings();}
	    	else
	    	if (NewFragmentTag.equals(getString(R.string.fragment_about))) 	  {newFragment  = new FragmentAbout();}
	    	// Добавляем фрагент в менеджер транзакций
	        ft.add(R.id.FragmentContainer, newFragment, NewFragmentTag);
	    } else {
	        // Фрагмент ТОП-10 при каждом вызове должен подгружать вурфы из файла WurfsTop10.DAT
		    if (NewFragmentTag.equals(getString(R.string.fragment_top10))) 
		    {
				((FragmentTop10) newFragment).LoadAndDisplayWurfs();
		    }
		    // Остальные фрагменты статичные, просто прячем текущий и показываем нужный
		    // Проверка для начального запуска, когда NewFragmentTag == CurrentFragmentTag == "fragment_main"
		    if (NewFragmentTag != CurrentFragmentTag)
		    {
			    ft.show(newFragment);
		    }
	    }
	    ft.commit();
	    this.CurrentFragmentTag = NewFragmentTag;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// Устанавливаем светлую тему приложения (по умолчанию)
		// Для андроида 3.xx сначала ставим другую тему, чтобы меню (по аппаратной кнопке) не выглядело неактивным
		if(android.os.Build.VERSION.RELEASE.startsWith("3")) {setTheme(R.style.AppCompatLight);}
		else
		{
			// Для андроида 2.xx сначала ставим другую тему (Translucent), чтобы меню (по аппаратной кнопке) не выглядело неактивным
			if(android.os.Build.VERSION.RELEASE.startsWith("2")) {setTheme(R.style.Translucent);}
		}
		// Ставим светлую тему
		setTheme(R.style.AppTheme);
		// 0,1,2,3 - загружаем соответствующие фрагменты по порядку табов в navbar
		switch (getSupportActionBar().getSelectedNavigationIndex())
		{
			case 0: LoadFragment(frag_main, getString(R.string.fragment_main));
				break;
			case 1: LoadFragment(frag_top10, getString(R.string.fragment_top10));
				break;
			case 2:
		    	// Устанавливаем светлую тему приложения
				setTheme(R.style.AppCompatLight);
				LoadFragment(frag_settings, getString(R.string.fragment_settings));
				break;
			case 3: LoadFragment(frag_about, getString(R.string.fragment_about));
				break;
			default: Toast.makeText(this, getString(R.string.You_did_not_select_any_menu_item), Toast.LENGTH_SHORT).show();
				break;
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		//Log.d(LOG_TAG, "Таб больше не выбран: " + tab.getText());
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	  super.onSaveInstanceState(savedInstanceState);
	  // Сохраняем номер выбранного таба в ActionBar
	  int index = getSupportActionBar().getSelectedNavigationIndex();
	  savedInstanceState.putInt("TabIndex", index);
	}

}