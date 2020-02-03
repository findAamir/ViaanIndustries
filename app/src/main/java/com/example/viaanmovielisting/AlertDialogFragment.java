package com.example.viaanmovielisting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class AlertDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Context context = getActivity();
       AlertDialog.Builder builder  = new AlertDialog.Builder(context);
       builder.setTitle(getArguments().getString("error_title"))
       .setMessage(getArguments().getString("error_message"))
       .setPositiveButton(R.string.error_button_ok_text,null);

       return builder.create();

    }
}
