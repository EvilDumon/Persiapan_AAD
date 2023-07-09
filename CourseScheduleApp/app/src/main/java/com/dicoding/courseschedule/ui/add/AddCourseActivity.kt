package com.dicoding.courseschedule.ui.add

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.data.DataRepository
import com.dicoding.courseschedule.ui.ViewModelFactory
import com.dicoding.courseschedule.ui.home.HomeActivity
import com.dicoding.courseschedule.ui.home.HomeViewModel
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener{

    private lateinit var viewModel : AddCourseViewModel

    private lateinit var courseName: TextInputEditText
    private lateinit var lecturer: TextInputEditText
    private lateinit var note: TextInputEditText
    private lateinit var Ib_startTime: ImageButton
    private lateinit var Ib_endTime: ImageButton
    private lateinit var startTime: TextView
    private lateinit var endTime: TextView
    private lateinit var day: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory).get(AddCourseViewModel::class.java)

        courseName = findViewById(R.id.ed_course_name)
        day = findViewById(R.id.spinner_day)
        Ib_startTime = findViewById(R.id.ib_start_time)
        Ib_endTime = findViewById(R.id.ib_end_time)
        lecturer = findViewById(R.id.ed_lecturer)
        note = findViewById(R.id.ed_note)
        startTime = findViewById(R.id.tv_start_time)
        endTime = findViewById(R.id.tv_end_time)

        Ib_startTime.setOnClickListener {
            TimePickerFragment().show(supportFragmentManager, "startTime")
        }

        Ib_endTime.setOnClickListener {
            TimePickerFragment().show(supportFragmentManager, "endTime")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent: Intent = when (item.itemId) {
            R.id.action_insert -> {
                viewModel.insertCourse(
                    courseName.text.toString(),
                    day.selectedItemPosition,
                    startTime.text.toString(),
                    endTime.text.toString(),
                    lecturer.text.toString(),
                    note.text.toString()
                )
                Intent(this, HomeActivity::class.java)
            }
            else -> null
        } ?: return super.onOptionsItemSelected(item)

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        return true
    }

    @SuppressLint("SetTextI18n")
    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        if (tag == "startTime") {
            startTime.text = "$hour:$minute"
        }else{
            endTime.text = "$hour:$minute"
        }
    }
}