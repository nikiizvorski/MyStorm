package com.androidchill.niki.mystorm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by NIKI on 8/31/2016.
 */
public class AlertDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        //Builder class is part of the Factory Design Pattern
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.error_title))
                .setMessage(context.getString(R.string.error_msg))
                .setPositiveButton(context.getString(R.string.error_ok), null);

        AlertDialog dialog = builder.create();
        return dialog;
    }
}
