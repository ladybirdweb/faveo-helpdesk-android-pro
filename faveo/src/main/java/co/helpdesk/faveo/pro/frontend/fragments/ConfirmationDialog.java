package co.helpdesk.faveo.pro.frontend.fragments;


import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import co.helpdesk.faveo.pro.Constants;
import co.helpdesk.faveo.pro.FaveoApplication;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.frontend.activities.LoginActivity;
import es.dmoral.toasty.Toasty;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmationDialog extends DialogFragment {
private Context context;

    public ConfirmationDialog() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm log out....");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NotificationManager notificationManager =
                        (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancelAll();
                FaveoApplication.getInstance().clearApplicationData();
                String url=Prefs.getString("URLneedtoshow",null);
                Prefs.clear();
                Prefs.putString("URLneedtoshow",url);
                getActivity().getSharedPreferences(Constants.PREFERENCE, Context.MODE_PRIVATE).edit().clear().apply();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Toasty.success(getActivity(), "Logged out successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        return builder.create();

    }
}
