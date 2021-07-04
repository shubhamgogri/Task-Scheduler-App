package com.shubhamapps.todoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shubhamapps.todoapp.adapter.OnToDoClickListener;
import com.shubhamapps.todoapp.adapter.RecyclerViewAdapter;
import com.shubhamapps.todoapp.model.Priority;
import com.shubhamapps.todoapp.model.SharedViewModel;
import com.shubhamapps.todoapp.model.Task;
import com.shubhamapps.todoapp.model.TaskViewModel;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.lang.invoke.ConstantCallSite;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnToDoClickListener {

    private static final String TAG ="ITEM";
    private TaskViewModel taskViewModel;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
//    private int counter = 0;
    private BottomSheetFragment bottomSheetFragment;
    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bottomSheetFragment = new BottomSheetFragment();
        ConstraintLayout constraintLayout = findViewById(R.id.bottomSheet);
        BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);

//        Instantiate View Model
        taskViewModel = new ViewModelProvider.AndroidViewModelFactory(
                MainActivity.this.getApplication())
                .create(TaskViewModel.class);

        taskViewModel.getAllTasks().observe(this, tasks -> {
            adapter = new RecyclerViewAdapter(tasks, this);
            recyclerView.setAdapter(adapter);
        });

        sharedViewModel = new ViewModelProvider(this)
                .get(SharedViewModel.class);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            showBottomSheetDialog();
//            Task task = new Task("ToDo" + counter++, Priority.HIGH, Calendar.getInstance().getTime(),
//                    Calendar.getInstance().getTime(), false);
//
//            TaskViewModel.insert(task);
//            taskViewModel.getAllTasks();
        });

    }

    private void showBottomSheetDialog() {
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onToDoClick(Task task) {
//        Log.d("Click", "onToDoClick: "+ adapterPosition);
        sharedViewModel.selectItem(task);
        showBottomSheetDialog();
        sharedViewModel.setIsEdit(true);
    }

    @Override
    public void onToDoRadioButton(Task task) {
//        Log.d("Click", "onRadioButton: "+ task.getTask());
        AlertDialog.Builder builder ;
        AlertDialog dialog;

        View view = View.inflate(MainActivity.this, R.layout.popup_box_delete_confirm,null);

        builder = new AlertDialog.Builder(this);
        TextView title = view.findViewById(R.id.popup_textView);
        Button delete = view.findViewById(R.id.popup_delete);
        Button cancel = view.findViewById(R.id.popup_cancel);

        title.setText(task.getTask());
        builder.setView(view);
        dialog = builder.create();
        dialog.show();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskViewModel.delete(task);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}