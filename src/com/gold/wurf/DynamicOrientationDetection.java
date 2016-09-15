package com.gold.wurf;

import android.app.Activity;
import android.content.res.Configuration;
import android.view.View;
import android.widget.TableLayout;

public class DynamicOrientationDetection extends Activity {

	   /**
	   * ������ ������� �������� � ����������� �� ����������.
	   * @param 
	   * orientation - ������� ���������� ������
	   * v - ������� ��� (view), � �������������� ��� ������������ ��������� ��������
	   * @return
	   * v - ��� � ������� ���������, ������� ������������� ���������� 
	   */
	   public View setBackgroundImage(int orientation, String FragmentName, View v){
		  // ������� ��� ��� (��������)
		  TableLayout tl = null;
		  // ����������, ����� �������� ������ ������ ����� setBackgroundImage
		  if (FragmentName.equals("FragmentMain"))
		  {
			  tl = (TableLayout) v.findViewById(R.id.RootMainLayout);  
		  }
		  else
		  if (FragmentName.equals("FragmentAbout"))
		  {
			  tl = (TableLayout) v.findViewById(R.id.RootAboutLayout);  
		  }
		  // ������ ������� �������� �� ���������
		  //
		  // ��� �������������� ����������:
	      if (orientation == Configuration.ORIENTATION_LANDSCAPE)
	      {
				tl.setBackgroundResource(R.drawable.back_horiz);
	      }
	      else
	      // ��� ������������ ����������:
	      if (orientation == Configuration.ORIENTATION_PORTRAIT)
	      {
				tl.setBackgroundResource(R.drawable.back_vert);
	      }
	      // ���������� ��� � ���������� ������� ���������
	      return v;
	    }
	}