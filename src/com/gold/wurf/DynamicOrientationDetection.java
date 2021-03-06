package com.gold.wurf;

import android.app.Activity;
import android.content.res.Configuration;
import android.view.View;
import android.widget.TableLayout;

public class DynamicOrientationDetection extends Activity {

	   /**
	   * Меняем фоновую картинку в зависимости от ориентации.
	   * @param 
	   * orientation - текущая ориентация экрана
	   * v - текущий вид (view), с горизонтальной или вертикальной картинкой снежинки
	   * @return
	   * v - вид с фоновой картинкой, которая соответствует ориентации 
	   */
	   public View setBackgroundImage(int orientation, String FragmentName, View v){
		  // Находим наш вид (разметку)
		  TableLayout tl = null;
		  // Определяем, какой фрагмент вызвал данный метод setBackgroundImage
		  if (FragmentName.equals("FragmentMain"))
		  {
			  tl = (TableLayout) v.findViewById(R.id.RootMainLayout);  
		  }
		  else
		  if (FragmentName.equals("FragmentAbout"))
		  {
			  tl = (TableLayout) v.findViewById(R.id.RootAboutLayout);  
		  }
		  // Меняем фоновую картинку со снежинкой
		  //
		  // Для горизонтальной ориентации:
	      if (orientation == Configuration.ORIENTATION_LANDSCAPE)
	      {
				tl.setBackgroundResource(R.drawable.back_horiz);
	      }
	      else
	      // Для вертикальной ориентации:
	      if (orientation == Configuration.ORIENTATION_PORTRAIT)
	      {
				tl.setBackgroundResource(R.drawable.back_vert);
	      }
	      // Возвращаем вид с обновлённой фоновой картинкой
	      return v;
	    }
	}