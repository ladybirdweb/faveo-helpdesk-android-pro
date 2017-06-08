package co.helpdesk.faveo.pro.frontend.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.frontend.activities.MainActivity;
import es.dmoral.toasty.Toasty;

public class Settings extends Fragment implements CompoundButton.OnClickListener {
    SwitchCompat switchCompatCrashReports;
    public static boolean isCheckedCrashReports = true;

    View rootView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static Settings newInstance(String param1, String param2) {
        Settings fragment = new Settings();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Settings() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_settings, container, false);

            switchCompatCrashReports = (SwitchCompat) rootView.findViewById(R.id.switch_crash_reports);
            switchCompatCrashReports.setChecked(Prefs.getBoolean("CRASH_REPORT", false));
            // switchCompatCrashReports.setOnCheckedChangeListener(this);
            switchCompatCrashReports.setOnClickListener(this);

        }
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.settings));
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {

            case R.id.switch_crash_reports: {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.restart_app_tittle)
                        .setMessage(R.string.restart_app)
                        .setCancelable(false)
                        .setIcon(R.drawable.ic_warning_black_36dp)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                switchCompatCrashReports.setChecked(Prefs.getBoolean("CRASH_REPORT", false));
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!switchCompatCrashReports.isChecked()) {
                                    isCheckedCrashReports = false;
                                    SharedPreferences.Editor authenticationEditor = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).edit();
                                    authenticationEditor.putBoolean("CRASH_REPORT", isCheckedCrashReports);
                                    authenticationEditor.apply();

                                } else {
                                    isCheckedCrashReports = true;
                                    SharedPreferences.Editor authenticationEditor = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).edit();
                                    authenticationEditor.putBoolean("CRASH_REPORT", isCheckedCrashReports);
                                    authenticationEditor.apply();
                                }
                                Intent i = getContext().getPackageManager()
                                        .getLaunchIntentForPackage(getContext().getPackageName());
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                Toasty.info(getContext(), getString(R.string.restarting), Toast.LENGTH_LONG).show();
                            }
                        });

                builder.create();
                builder.show();

                break;
            }
        }

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
