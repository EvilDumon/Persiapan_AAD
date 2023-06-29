package com.dicoding.todoapp.ui.detail

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.todoapp.R
import com.dicoding.todoapp.ui.list.TaskViewModel
import com.dicoding.todoapp.utils.TASK_ID
import com.google.android.material.textfield.TextInputEditText

class DetailTaskActivity : AppCompatActivity() {

    private lateinit var detailTaskViewModel: DetailTaskViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        //TODO 11 : Show detail task and implement delete action
        val id = intent.extras?.getInt(TASK_ID)

        detailTaskViewModel.setTaskId(id)
        val edTitle = findViewById<TextInputEditText>(R.id.detail_ed_title)
        val edDescription = findViewById<TextInputEditText>(R.id.detail_ed_description)
        val edDueDate = findViewById<TextInputEditText>(R.id.detail_ed_due_date)
        val btnDelete = findViewById<Button>(R.id.btn_delete_task)

        detailTaskViewModel.task.observe(this){
            edTitle.setText(it.title)
            edDescription.setText(it.description)
            edDueDate.setText(it.dueDateMillis.toString())
        }

        btnDelete.setOnClickListener{
            detailTaskViewModel.deleteTask()
        }
    }
}