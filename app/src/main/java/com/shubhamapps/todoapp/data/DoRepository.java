package com.shubhamapps.todoapp.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.shubhamapps.todoapp.model.Task;
import com.shubhamapps.todoapp.util.TaskRoomDatabase;

import java.util.List;

public class DoRepository {
//        Only For Organization....
    private final TaskDao taskDao;
    private final LiveData<List<Task>> allTask;

    public DoRepository(Application application) {
        TaskRoomDatabase database = TaskRoomDatabase.getDatabase(application);
        taskDao = database.taskDao();
        allTask = taskDao.getTasks();
    }
    public LiveData<List<Task>> getAllTask(){
        return allTask;
    }
    public void insert (Task task){
        TaskRoomDatabase.databaseWriterExecutor.execute(()->{
            taskDao.insertTask(task);
        });
    }
    public void update(Task task){
        TaskRoomDatabase.databaseWriterExecutor.execute(()-> taskDao.update(task));
    }

    public void delete(Task task){
        TaskRoomDatabase.databaseWriterExecutor.execute(()-> taskDao.delete(task));
    }

    public LiveData<Task> get(long id){
        return taskDao.get(id);
    }

}
