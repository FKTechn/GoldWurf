package com.gold.wurf;

import java.io.File;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class AlertDialogsFragment extends DialogFragment implements OnClickListener {

	// В зависимости от тега диалога (назначается в коде фрагмента при вызове диалога через show) формируем диалог:
	// задаём нужные кнопки (да-нет-отмена), заголовок, текст диалога
	// enum нужен, чтобы заработала конструкция switch(String), так как напрямую switch(String) сделать нельзя (до java 7, 1.7)
	// используем его ниже в switch(dialogTag.CorrectTagProcessing(getTag()))
	public enum dialogTag {
		WurfsFileNotExistsDialog, prefsWurfsFileNotExistsDialog, ClearWurfsDialog, NOVALUE;
	    public static dialogTag CorrectTagProcessing(String tag)
	    {
	        try {
	            return valueOf(tag);
	        } 
	        catch (Exception ex) {
	            return NOVALUE;
	        }
	    } 
	};

	// При создании диалога
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());

		// Формируем диалог по его тегу
		switch(dialogTag.CorrectTagProcessing(getTag()))
		{
			// Диалог "ТОП-10: Файл не найден", показывается при первом щелчке по ТОП-10 в ActionBar,
			// когда приложение только что установили, первый вурф ещё не рассчитывали
			// и строить график вурфов не из чего (не создан файл WurfsTop10.DAT)
			// "ОК" - закрывает диалог
			case WurfsFileNotExistsDialog:
				adb.setTitle(R.string.dialog_WurfsFileNotExistsDialog_title);
				adb.setMessage(R.string.dialog_WurfsFileNotExistsDialog_message);
				adb.setNeutralButton(R.string.dialog_ok, this);
				break;
			// Диалог "НАСТРОЙКИ: Файл не найден"
			// Отображается, когда пользователь кликнул "Да" в диалоге удаления файла вурфов (фрагмент настроек),
			// но файл вурфов не существует
			// "ОК" - закрывает диалог
			case prefsWurfsFileNotExistsDialog:
				adb.setTitle(R.string.dialog_prefsWurfsFileNotExistsDialog_title);
				adb.setMessage(R.string.dialog_prefsWurfsFileNotExistsDialog_message);
				adb.setNeutralButton(R.string.dialog_ok, this);
				break;
			// Диалог "НАСТРОЙКИ: Удаление вурфов" показывается, когда пользователь снимает галку "Хранить ТОП-10" в фрагменте настроек
			// "Да" - удаляет файл вурфов, "Нет" - просто закрывает диалог
			case ClearWurfsDialog:
				adb.setTitle(R.string.dialog_ClearWurfsDialog_title);
				adb.setMessage(R.string.dialog_ClearWurfsDialog_message);
				adb.setPositiveButton(R.string.dialog_yes, this);
				adb.setNegativeButton(R.string.dialog_no, this);
				break;
			default:
				break;
		}
	return adb.create();
	}

	// Какую кнопку в диалоге нажали
	public void onClick(DialogInterface dialog, int which) {
	    switch (which) {
	    case Dialog.BUTTON_POSITIVE:
		  switch(dialogTag.CorrectTagProcessing(getTag()))
		  {
		    // Если "Да" было нажато в диалоге "Удаление вурфов", удаляем файл WurfsTop10.DAT
			case ClearWurfsDialog:
				File Top10File = new File(getActivity().getFilesDir(), "WurfsTop10.DAT");
				if(Top10File.exists())
				{
					try
					{
						Top10File.delete();
						/*FragmentTop10 FTOP10 = new FragmentTop10();
						FTOP10.ClearWurfsGraphic();*/
					}
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				else
				{	// Файл "WurfsTop10.DAT" не найден? Вызываем соответствующий диалог
					DialogFragment FileNotExistsDialog = new AlertDialogsFragment();
					FileNotExistsDialog.show(getFragmentManager(), "prefsWurfsFileNotExistsDialog");
				}
				break;
			default:
				break;
		  }
	      break;
	    case Dialog.BUTTON_NEGATIVE:
	      break;
	    case Dialog.BUTTON_NEUTRAL:
	      break;
	    }
	    dismiss();
	  }

	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
	}

	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
	}
}
