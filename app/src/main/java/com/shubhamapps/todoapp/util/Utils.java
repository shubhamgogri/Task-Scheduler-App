package com.shubhamapps.todoapp.util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.lifecycle.ViewModel;

import com.shubhamapps.todoapp.model.Priority;
import com.shubhamapps.todoapp.model.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;

public class Utils {
    public static String formatDate(Date dueDate) {
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        simpleDateFormat.applyPattern("EEE, MMM d");
        return simpleDateFormat.format(dueDate);
    }

    public static void hideSoftKeyboard(View view){
        InputMethodManager im = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    public static int priorityColor(Task task) {
        int color;
        if (task.getPriority()== Priority.HIGH){
            color = Color.argb(200,201,21,23);
        }else if (task.getPriority()== Priority.MEDIUM){
            color = Color.argb(200,155,179,0);
        }else{
            color = Color.argb(200,51,181,129);
        }
        return color;
    }
}
