package com.gold.wurf;

import java.io.File;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class AlertDialogsFragment extends DialogFragment implements OnClickListener {

	// � ����������� �� ���� ������� (����������� � ���� ��������� ��� ������ ������� ����� show) ��������� ������:
	// ����� ������ ������ (��-���-������), ���������, ����� �������
	// enum �����, ����� ���������� ����������� switch(String), ��� ��� �������� switch(String) ������� ������ (�� java 7, 1.7)
	// ���������� ��� ���� � switch(dialogTag.CorrectTagProcessing(getTag()))
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

	// ��� �������� �������
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());

		// ��������� ������ �� ��� ����
		switch(dialogTag.CorrectTagProcessing(getTag()))
		{
			// ������ "���-10: ���� �� ������", ������������ ��� ������ ������ �� ���-10 � ActionBar,
			// ����� ���������� ������ ��� ����������, ������ ���� ��� �� ������������
			// � ������� ������ ������ �� �� ���� (�� ������ ���� WurfsTop10.DAT)
			// "��" - ��������� ������
			case WurfsFileNotExistsDialog:
				adb.setTitle(R.string.dialog_WurfsFileNotExistsDialog_title);
				adb.setMessage(R.string.dialog_WurfsFileNotExistsDialog_message);
				adb.setNeutralButton(R.string.dialog_ok, this);
				break;
			// ������ "���������: ���� �� ������"
			// ������������, ����� ������������ ������� "��" � ������� �������� ����� ������ (�������� ��������),
			// �� ���� ������ �� ����������
			// "��" - ��������� ������
			case prefsWurfsFileNotExistsDialog:
				adb.setTitle(R.string.dialog_prefsWurfsFileNotExistsDialog_title);
				adb.setMessage(R.string.dialog_prefsWurfsFileNotExistsDialog_message);
				adb.setNeutralButton(R.string.dialog_ok, this);
				break;
			// ������ "���������: �������� ������" ������������, ����� ������������ ������� ����� "������� ���-10" � ��������� ��������
			// "��" - ������� ���� ������, "���" - ������ ��������� ������
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

	// ����� ������ � ������� ������
	public void onClick(DialogInterface dialog, int which) {
	    switch (which) {
	    case Dialog.BUTTON_POSITIVE:
		  switch(dialogTag.CorrectTagProcessing(getTag()))
		  {
		    // ���� "��" ���� ������ � ������� "�������� ������", ������� ���� WurfsTop10.DAT
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
				{	// ���� "WurfsTop10.DAT" �� ������? �������� ��������������� ������
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
