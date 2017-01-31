package com.alsum.todo;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Todohelper dbHelper;        //Initial Parameters
    ArrayAdapter<String> mAdapter;
    ListView listtask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("ToDo App");
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        dbHelper = new Todohelper(this); //object from Todohelper
        listtask = (ListView)findViewById(R.id.listtasks);
        getCurrentTasks();

//Customize Fab Button : when it clicked it will Show Alert Dialoge Asking For Create New Task
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText taskEditText = new EditText(view.getContext());
                AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                        .setTitle("Create New Task ")
                        .setMessage("What is Your New Task ?")
                        .setView(taskEditText)
                        .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                if (task.equals("")){
                                    //do nothing
                                }
                                else {

                                    dbHelper.insertNewTask(task);
                                    getCurrentTasks();
                                }
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .create();
                dialog.show();

            }
        });
    }


//get Current Task From  db and list them on ListView using Array Adapter
    private void getCurrentTasks() {
        ArrayList<String> taskList = dbHelper.getTaskList();
        if(mAdapter==null){
            mAdapter = new ArrayAdapter<String>(this,R.layout.row_in_list,R.id.textView,taskList);
            listtask.setAdapter(mAdapter);
        }
        else{
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) { //when choose delete all task it will remove all tasks
            case  R.id.DeleteAllTasks:

                mAdapter.clear();
                dbHelper.clearAll();
                mAdapter.notifyDataSetChanged();

        }
        return super.onOptionsItemSelected(item);
    }
    //to clear one task
    public void deleteTask(View view){
        View parent = (View)view.getParent();
        TextView taskTextView = (TextView)parent.findViewById(R.id.textView);
        String task = String.valueOf(taskTextView.getText());
        dbHelper.deleteTask(task);
        getCurrentTasks();
    }
}
