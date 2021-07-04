package com.shubhamapps.todoapp.model;

import android.app.Application;
import android.net.wifi.aware.IdentityChangedListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.shubhamapps.todoapp.data.DoRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    public static DoRepository repository;
    public final LiveData<List<Task>> allTasks;


    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new DoRepository(application);
        allTasks = repository.getAllTask();
    }
//    CRUD
    public static void insert(Task task){ repository.insert(task); }
    public LiveData<List<Task>> getAllTasks(){ return allTasks; }
    public static void update(Task task){ repository.update(task); }
    public LiveData<Task> get(long id){ return repository.get(id); }
    public static void delete(Task task){ repository.delete(task); }

}
