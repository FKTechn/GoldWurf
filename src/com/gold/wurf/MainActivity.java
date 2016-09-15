package com.gold.wurf;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

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

// ������������ ������ IDE
private static final String LOG_TAG = "FlankLOGS";
// ��������� "�����" (������� ��������), "���-10", "� �����" � "���������"
Fragment frag_main, frag_top10, frag_about, frag_settings;
// ����������� ������ � ����������, ��� �������� 4 ���������� ������ (����)
PopupWindow PopupStartExplanation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Log.d(LOG_TAG, "[������� ���� ���������]");

		// ������������� ������
		android.support.v7.app.ActionBar navbar = getSupportActionBar();
		navbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		/* 1-� �������� "�����" */
		Tab tab = navbar.newTab();
		tab.setIcon(R.drawable.ic_home);
		tab.setTabListener(this);
		navbar.addTab(tab);
		/* 2-� �������� */
		/* ������� ���������� custom layout - ������ ��������, ����� �����
		View tabView = this.getLayoutInflater().inflate(R.layout.navbar_layout, null);
		TextView tabText = (TextView) tabView.findViewById(R.id.tabText);
		tabText.setText(R.string.calculate_wurf1);
		ImageView tabImage = (ImageView) tabView.findViewById(R.id.tabIcon);
		tabImage.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_top10));*/
		tab = navbar.newTab();
		//tab.setText("���-10");
		tab.setIcon(R.drawable.ic_top10);
		tab.setTabListener(this);
		navbar.addTab(tab);
		/* 3-� �������� */
		tab = navbar.newTab();
		//tab.setText("���������");
		tab.setIcon(R.drawable.ic_settings);
		tab.setTabListener(this);
		navbar.addTab(tab);
		/* 4-� �������� */
		tab = navbar.newTab();
		//tab.setText("� �����");
		tab.setIcon(R.drawable.ic_about);
		tab.setTabListener(this);
		navbar.addTab(tab);
		Log.d(LOG_TAG, "���� �������");

		// ���� �������� ���� ����������� (�������� ����������), �������� ���������� ����� ��� ActionBar
        if(savedInstanceState != null) 
        {
            int index = savedInstanceState.getInt("TabIndex");
            getSupportActionBar().setSelectedNavigationItem(index);
        }

    	// ������ ����� �������� "������� ���-10" ������ �� ������ ����������� ��������� (��������/�� ��������)
		PreferenceManager.setDefaultValues(this, R.xml.preferences, true);

		/* ���������� �����-���� � ������������, ��� �������� ���������� ������ */
		ShowStartExplanatonDialog();
	}

	/* ����� ���������� �����-���� � ������������, ��� �������� ���������� ������ (4 �������� ����) */
	public void ShowStartExplanatonDialog()
	{
		// ������� �����-����
	    TableLayout viewGroup = (TableLayout) this.findViewById(R.id.popup_start_explanation);
	    final LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View layout = layoutInflater.inflate(R.layout.popup_start_explanation, viewGroup);
	    // ������ ���
	    PopupStartExplanation = new PopupWindow(layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    PopupStartExplanation.setContentView(layout);
	    // ������������ ������ ����������� �����-���� PopupStartExplanation
	    // �� ��������� ������� ��� ������������ ���������, ������� ����� ��� ������� ������� ������ �� ������� �������,
	    // � ��������� ������� ������������� ��������������� ������ ��������.
	    // ������ ����� ������� �������� start_explanation_background.png 465x445
	    //
	    // �������� ��������� ������
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		// ������ � ������ ������
		int screenWidthInPix  = displayMetrics.widthPixels;
		int screenHeightInPix = displayMetrics.heightPixels;
		// ���� ����� �������, ������� ���� � ������ ������� (����� ������� ��� ������������)
		if ((screenWidthInPix >= 465) && (screenHeightInPix >= 445))
			{
			    PopupStartExplanation.setWidth(465);
			    PopupStartExplanation.setHeight(445);
			}
	    // ���������� �����-���� � ����� ������ ����� ��������� Runnable(), 
		// �.�. � ������� ������ MainActivity ��� �� �� ����� �����������
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

			    // ������� 4 �������� ���� � ���������� �����-����
			    ImageView ivPhysiology = (ImageView)PopupStartExplanation.getContentView().findViewById(R.id.imageView_physiology);
			    ImageView ivIntellect = (ImageView)PopupStartExplanation.getContentView().findViewById(R.id.imageView_intellect);
			    ImageView ivSocium = (ImageView)PopupStartExplanation.getContentView().findViewById(R.id.imageView_socium);
			    ImageView ivMaterial = (ImageView)PopupStartExplanation.getContentView().findViewById(R.id.imageView_material);
			    // ���������� ������ ExplainDialog, ������� ��������� �� ����� �� ���� �� 4 �������� (����) �����-����
			    // �
			    ivPhysiology.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ShowExplanationDialog("PHYS");
					}});
			    // �
			    ivIntellect.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ShowExplanationDialog("INT");
					}});
			    // �
			    ivSocium.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ShowExplanationDialog("SOC");
					}});
			    // �
			    ivMaterial.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ShowExplanationDialog("MAT");
					}});
		   }
		});
	}

	/* ����� ������� �� ����� ���������� ������ �� ������ �� 4 ��������� (����)
	 * @param s - �������� "PHYS", "INT", "SOC" ��� "MAT"
	 * @ExplainDialog - ���������� ������
	 * */
	public void ShowExplanationDialog(String s) {
		// ���� ������� 2.xx, �� �� ����� ������� ����� ����������� ������ - ������ ����� ����� �� ������ ����.
		// ���������� ������������ ������ ����
		if(android.os.Build.VERSION.RELEASE.startsWith("2")) {setTheme(R.style.Translucent);}
		//
		DialogFragment ExplainDialog = StartExplanationDialog.newInstance(s);
		ExplainDialog.show(getSupportFragmentManager(), "ExplainDialog");
	}

	// ��������� ������ ���� (���� ������������ �� ������� �� ���������� ������ ����)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.main, menu);
		/* menu.add(groupId, itemId, order, title); */
		menu.add(0, 1, 0, "�����");
		menu.add(0, 2, 0, "���-10 ������");
		menu.add(0, 3, 0, "���������");
		menu.add(0, 4, 0, "� �����");
		menu.add(0, 5, 0, "����������");
		menu.add(0, 6, 0, "�����");
		//menu.getItem(1).getActionView().setBackgroundResource(R.color.Gold);

		return super.onCreateOptionsMenu(menu);
	}

	// ���������� ������� ���� (�� ActionBar)
	public boolean onOptionsItemSelected(MenuItem item)
	{
		//Toast.makeText(this, Integer.toString(item.getItemId()), Toast.LENGTH_SHORT).show();
		switch (item.getItemId())
		{
			case 1:
				// �����
				getSupportActionBar().setSelectedNavigationItem(0);
				return true;
			case 2:
				// ���-10
				getSupportActionBar().setSelectedNavigationItem(1);
				return true;
			case 3:
				// ���������
				getSupportActionBar().setSelectedNavigationItem(2);
				return true;
			case 4:
				// � �����
				getSupportActionBar().setSelectedNavigationItem(3);
				/* ���������� �����-���� � ������������, ��� �������� ���������� ������ */
				ShowStartExplanatonDialog();
				return true;
			case 5:
				// ����������. �������� ����������� ������ �� ����� ������������� share ������������
				TextView tvWurfResult = (TextView) findViewById(R.id.textView_WURF_Value);
				// ���� ���� � ������������ ������ ������, ������, ���� ��� �� ����������. ������� ��������������� ������
				if (tvWurfResult.getText().toString().equals(""))
				{
					Dialog CalculateWurfDialog;
					AlertDialog.Builder adb = new AlertDialog.Builder(this);
					adb.setTitle(R.string.app_name);
					adb.setMessage(R.string.CalculateWurfDialog);
					adb.setNeutralButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// �� ������� �� ������ "��" ��������� ������
							dialog.dismiss();
						}});
					CalculateWurfDialog = adb.create();
					CalculateWurfDialog.show();
				}
				// ���� ���� � ������������ ������ ���������, �������� ����������� ������ �� ����� ������������� share ������������
				else
				{
					final Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("text/plain");
					intent.putExtra(Intent.EXTRA_SUBJECT, "������� ����");
					intent.putExtra(Intent.EXTRA_TEXT, tvWurfResult.getText()+"\nhttp://result.zz.mu/wurf");
					startActivity(Intent.createChooser(intent, getString(R.string.app_name)));
				}
				return true;
			case 6:
				// �����. ��������� ������ ����������
				finish();
				return true;
			default: return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		//Log.d(LOG_TAG, "������ ��� ��������� ���: " + tab.getText());
	}

	String CurrentFragmentTag = "fragment_main";
	// ����� ������� ���������� � ���������� ��������
	public void LoadFragment(Fragment NewFragment, String NewFragmentTag) {
	    FragmentManager fm = getSupportFragmentManager();
	    Fragment currFragment  =  fm.findFragmentByTag(CurrentFragmentTag);
	    Fragment newFragment   =  fm.findFragmentByTag(NewFragmentTag);
	    FragmentTransaction ft =  fm.beginTransaction();/*.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)*/

	    if(currFragment != null) {ft.hide(currFragment);}

	    if(newFragment == null) {
	    	// ������ ��������� ������� (����� ����� ������ ����������/������� ������ ��������)
	    	if (NewFragmentTag.equals(getString(R.string.fragment_main)))  	  {newFragment  = new FragmentMain();}
	    	else
	    	if (NewFragmentTag.equals(getString(R.string.fragment_top10))) 	  {newFragment  = new FragmentTop10();}
	    	else
	    	if (NewFragmentTag.equals(getString(R.string.fragment_settings))) {newFragment  = new FragmentSettings();}
	    	else
	    	if (NewFragmentTag.equals(getString(R.string.fragment_about))) 	  {newFragment  = new FragmentAbout();}
	    	// ��������� ������� � �������� ����������
	        ft.add(R.id.FragmentContainer, newFragment, NewFragmentTag);
	    } else {
	        // �������� ���-10 ��� ������ ������ ������ ���������� ����� �� ����� WurfsTop10.DAT
		    if (NewFragmentTag.equals(getString(R.string.fragment_top10))) 
		    {
				((FragmentTop10) newFragment).LoadAndDisplayWurfs();
		    }
		    // ��������� ��������� ���������, ������ ������ ������� � ���������� ������
		    // �������� ��� ���������� �������, ����� NewFragmentTag == CurrentFragmentTag == "fragment_main"
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
		// ������������� ������� ���� ���������� (�� ���������)
		// ��� �������� 3.xx ������� ������ ������ ����, ����� ���� (�� ���������� ������) �� ��������� ����������
		if(android.os.Build.VERSION.RELEASE.startsWith("3")) {setTheme(R.style.AppCompatLight);}
		else
		{
			// ��� �������� 2.xx ������� ������ ������ ���� (Translucent), ����� ���� (�� ���������� ������) �� ��������� ����������
			if(android.os.Build.VERSION.RELEASE.startsWith("2")) {setTheme(R.style.Translucent);}
		}
		// ������ ������� ����
		setTheme(R.style.AppTheme);
		// 0,1,2,3 - ��������� ��������������� ��������� �� ������� ����� � navbar
		switch (getSupportActionBar().getSelectedNavigationIndex())
		{
			case 0: LoadFragment(frag_main, getString(R.string.fragment_main));
				break;
			case 1: LoadFragment(frag_top10, getString(R.string.fragment_top10));
				break;
			case 2:
		    	// ������������� ������� ���� ����������
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
		//Log.d(LOG_TAG, "��� ������ �� ������: " + tab.getText());
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	  super.onSaveInstanceState(savedInstanceState);
	  // ��������� ����� ���������� ���� � ActionBar
	  int index = getSupportActionBar().getSelectedNavigationIndex();
	  savedInstanceState.putInt("TabIndex", index);
	}

}