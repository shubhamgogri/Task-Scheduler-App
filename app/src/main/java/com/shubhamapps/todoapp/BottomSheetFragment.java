package com.shubhamapps.todoapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.shubhamapps.todoapp.model.Priority;
import com.shubhamapps.todoapp.model.SharedViewModel;
import com.shubhamapps.todoapp.model.Task;
import com.shubhamapps.todoapp.model.TaskViewModel;
import com.shubhamapps.todoapp.util.Utils;

import java.util.Calendar;
import java.util.Date;

public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private EditText enterTodo;
    private ImageButton calenderButton;
    private ImageButton priorityButton;
    private RadioGroup priorityRadioGroup;
    private RadioButton selectedRadioButton;
    private int selectedButton;
    private ImageButton saveButton;
    private CalendarView calendarView;
    private Group calenderGroup;
    private Date duedate;
    private final Calendar calendar = Calendar.getInstance();
    private SharedViewModel sharedViewModel;
    private boolean isEdit;
    private Priority priority;

    public BottomSheetFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.bottom_sheet, container, false);
        calenderGroup = view.findViewById(R.id.calendar_group);
        calendarView = view.findViewById(R.id.calendar_view);
        calenderButton = view.findViewById(R.id.today_calendar_button);
        enterTodo = view.findViewById(R.id.enter_todo_et);
        saveButton = view.findViewById(R.id.save_todo_button);
        priorityButton = view.findViewById(R.id.priority_todo_button);
        priorityRadioGroup = view.findViewById(R.id.radioGroup_priority);

        Chip todayChip = view.findViewById(R.id.today_chip);
        todayChip.setOnClickListener(this);
        Chip tomorrowChip = view.findViewById(R.id.tomorrow_chip);
        Chip nextWeekChip = view.findViewById(R.id.next_week_chip);
        tomorrowChip.setOnClickListener(this);
        nextWeekChip.setOnClickListener(this);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (sharedViewModel.getSelectedItem().getValue() != null){
            isEdit = sharedViewModel.getIsEdit();
            Task task = sharedViewModel.getSelectedItem().getValue();
            Log.d("sharedViewModel", "onViewCreated: " + task.getTask());
            enterTodo.setText(task.getTask());
            duedate = task.getDueDate();
            priority = task.getPriority();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (sharedViewModel.getSelectedItem().getValue() !=null)
            sharedViewModel.setIsEdit(false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity())
                .get(SharedViewModel.class);

        calenderButton.setOnClickListener(v -> {
                    calenderGroup.setVisibility(calenderGroup.getVisibility() == View.GONE? View.VISIBLE:View.GONE);
                    Utils.hideSoftKeyboard(v);
                }
            );

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//                Log.d("Cal", "onSelectedDayChange: " + (month+1) + " " + dayOfMonth+ " "+ year);
                calendar.clear();
                calendar.set(year,month,dayOfMonth);
                duedate = calendar.getTime();

            }
        });

        priorityButton.setOnClickListener(view13 ->{
            Utils.hideSoftKeyboard(view13);
            priorityRadioGroup.setVisibility(priorityRadioGroup.getVisibility() == View.GONE? View.VISIBLE: View.GONE);

        });

        priorityRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
//                      Which Button is Checked
            if (priorityRadioGroup.getVisibility() == View.VISIBLE){
                selectedButton = checkedId;
                selectedRadioButton = view.findViewById(selectedButton);
                switch (selectedRadioButton.getId()){
                    case R.id.radioButton_high:
                        priority = Priority.HIGH;
                        break;
                    case R.id.radioButton_med:
                        priority = Priority.MEDIUM;
                        break;
                    default:
                        priority = Priority.LOW;
                }
            }else {
                priority = Priority.LOW;
            }
        });

        saveButton.setOnClickListener(view1 ->{
            String task = enterTodo.getText().toString().trim();
            if (!TextUtils.isEmpty(task) && duedate!=null && priority !=null) {
                Task my_task = new Task(task, priority,
                        duedate,
                        Calendar.getInstance().getTime(),
                        false);
                if (isEdit){
                    Task updateTask = sharedViewModel.getSelectedItem().getValue();
                    updateTask.setTask(task);
                    updateTask.setPriority(priority);
                    updateTask.setDueDate(duedate);
                    updateTask.setDateCreated(Calendar.getInstance().getTime());
                    TaskViewModel.update(updateTask);
                    sharedViewModel.setIsEdit(false);
                    enterTodo.setText("");
                }else{
                    TaskViewModel.insert(my_task);
                    sharedViewModel.setIsEdit(false);
                }

                enterTodo.setText("");

                if (this.isVisible()){
                    this.dismiss();
                    sharedViewModel.setIsEdit(false);
                }

            }else {
                Toast.makeText(getContext(), R.string.empty_field , Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.today_chip){
            calendar.add(Calendar.DAY_OF_MONTH,0);
            duedate = calendar.getTime();
        }else if (id == R.id.tomorrow_chip){
            calendar.add(Calendar.DAY_OF_MONTH,1);
            duedate = calendar.getTime();
        }else if (id == R.id.next_week_chip){
            calendar.add(Calendar.DAY_OF_MONTH,7);
            duedate = calendar.getTime();
        }
        calendarView.setDate(duedate.getTime());
    }
}