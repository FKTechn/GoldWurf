package com.gold.wurf;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class StartExplanationDialog extends DialogFragment {

	public String mTag;

	public static StartExplanationDialog newInstance(String tag) {
		StartExplanationDialog f = new StartExplanationDialog();
		Bundle args = new Bundle();
		args.putString("TAG", tag);
		f.setArguments(args);
		return f;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Получаем тег, по которому определяем, какое из 4 объяснений (ФИСМ) показывать
		mTag = getArguments().getString("TAG");

		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());

		LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = layoutInflater.inflate(R.layout.dialog_start_explanation, null);

		//ImageView imgView = (ImageView)layout.findViewById(R.id.imageView_FISM);
		TextView  txtView = (TextView)layout.findViewById(R.id.textView_FISM);

		if(mTag.equals("PHYS"))
			{
				adb.setIcon(R.drawable.dialog_start_explanation_phys);
				adb.setTitle(R.string.physiology_level);
				//imgView.setImageResource(R.drawable.dialog_start_explanation_phys);
				txtView.setText(R.string.dialog_start_explanation_F);
			}
		else
		if(mTag.equals("INT"))
			{
				adb.setIcon(R.drawable.dialog_start_explanation_int);
				adb.setTitle(R.string.intellect_level);
				//imgView.setImageResource(R.drawable.dialog_start_explanation_int);
				txtView.setText(R.string.dialog_start_explanation_I);
			}
		else
		if(mTag.equals("SOC"))
			{
				adb.setIcon(R.drawable.dialog_start_explanation_soc);
				adb.setTitle(R.string.socium_level);
				//imgView.setImageResource(R.drawable.dialog_start_explanation_soc);
				txtView.setText(R.string.dialog_start_explanation_S);
			}
		else
		if(mTag.equals("MAT"))
			{
				adb.setIcon(R.drawable.dialog_start_explanation_mat);
				adb.setTitle(R.string.material_level);
				//imgView.setImageResource(R.drawable.dialog_start_explanation_mat);
				txtView.setText(R.string.dialog_start_explanation_M);
			}

		adb.setView(layout);
		adb.setNeutralButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// По нажатию на кнопку "ОК" закрываем диалог
				dismiss();
				// Ставим первоначальную тему (это для старых версий андроида, чтобы цвета передавались корректно)
				getActivity().setTheme(R.style.AppTheme);
			}});

        return adb.create();
	}

}
