package com.shubhamapps.todoapp.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Task> selected_item = new MutableLiveData<>();

    private boolean isEdit;

    public boolean getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean edit) {
        isEdit = edit;
    }

    public void selectItem(Task task){
        selected_item.setValue(task);
    }

    public LiveData<Task> getSelectedItem(){
        return selected_item;
    }

}
