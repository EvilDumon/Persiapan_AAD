package com.dicoding.todoapp.ui.detail

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.R
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.utils.DateConverter.convertMillisToString
import com.dicoding.todoapp.utils.TASK_ID
import com.google.android.material.textfield.TextInputEditText

class DetailTaskActivity : AppCompatActivity() {

    private lateinit var detailTaskViewModel: DetailTaskViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        //TODO 11 : Show detail task and implement delete action
        val factory = ViewModelFactory.getInstance(this)
        detailTaskViewModel = ViewModelProvider(this, factory).get( DetailTaskViewModel::class.java )

        val id = intent.extras?.getInt(TASK_ID)

        detailTaskViewModel.setTaskId(id)
        val edTitle = findViewById<TextInputEditText>(R.id.detail_ed_title)
        val edDescription = findViewById<TextInputEditText>(R.id.detail_ed_description)
        val edDueDate = findViewById<TextInputEditText>(R.id.detail_ed_due_date)
        val btnDelete = findViewById<Button>(R.id.btn_delete_task)

        detailTaskViewModel.task.observe(this){
            edTitle.setText(it.title)
            edDescription.setText(it.description)
            edDueDate.setText(convertMillisToString(it.dueDateMillis))
        }

        btnDelete.setOnClickListener{
            detailTaskViewModel.deleteTask()
        }
    }
}