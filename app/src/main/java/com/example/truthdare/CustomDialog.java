package com.example.truthdare;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

/**
 * Created by Dhiraj Poddar on 19-10-2019.
 */
public class CustomDialog extends AlertDialog {
    protected CustomDialog(Context context) {
        super(context);
    }

    protected CustomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    public void setContentView(@NonNull View view) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.option_dialog,null,false);
        setContentView(view);
    }
}
