package com.gold.wurf;

import java.io.File;

import android.support.v4.app.DialogFragment;
import android.support.v4.preference.PreferenceFragment;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentSettings extends PreferenceFragment  {
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        // ��������� ��������� �� ����� xml/preferences.xml
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // ������� ���� ��������� �������� - ������
        //view.setBackgroundColor(getResources().getColor(R.color.FragmentSettingsBackground));// .setBackgroundResource(R.drawable.background_vert);

        // ������� ����� "������� ���-10"
        final CheckBoxPreference checkboxPref = (CheckBoxPreference) getPreferenceManager().findPreference("pref_SaveWurfs");
        // ���������� ����� ��������� ����� "������� ���-10"
        checkboxPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(newValue instanceof Boolean){
                    Boolean boolVal = (Boolean)newValue;
                    File Top10File = new File(getActivity().getFilesDir(), "WurfsTop10.DAT");
                    // ���� ����� ����� ������������� � ���� ������ ����������, �������� ������ "�������� ������"
                    if (!boolVal && Top10File.exists())
                    {
        				DialogFragment ClearWurfsDialog = new AlertDialogsFragment();
        				ClearWurfsDialog.show(getFragmentManager(), "ClearWurfsDialog");
                    }
                }
                return true;
            }
        }); 

		return view;
    }
}
