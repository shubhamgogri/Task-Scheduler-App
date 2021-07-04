package com.shubhamapps.todoapp.adapter;

import com.shubhamapps.todoapp.model.Task;

public interface OnToDoClickListener {

    void onToDoClick(Task task);

    void onToDoRadioButton(Task task);
}
