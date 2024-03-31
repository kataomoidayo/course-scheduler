package com.dicoding.courseschedule.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.ui.list.ListViewModelFactory
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {
    private lateinit var viewModel: AddCourseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        val factory = ListViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this, factory)[AddCourseViewModel::class.java]

        setTimePicker()
    }

    private fun setTimePicker() {
        findViewById<ImageButton>(R.id.ib_start_time).setOnClickListener {
            val dialogTimePickerFragment = TimePickerFragment()
            dialogTimePickerFragment.show(supportFragmentManager, START_TIME)
        }
        findViewById<ImageButton>(R.id.ib_end_time).setOnClickListener {
            val dialogTimePickerFragment = TimePickerFragment()
            dialogTimePickerFragment.show(supportFragmentManager, END_TIME)
        }
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        if (tag == START_TIME) {
            findViewById<TextView>(R.id.tv_start_time).text = timeFormat.format(calendar.time)
        } else {
            findViewById<TextView>(R.id.tv_end_time).text = timeFormat.format(calendar.time)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_insert -> {
                addCourseSchedule()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addCourseSchedule() {
        val edCourseName = findViewById<TextView>(R.id.ed_course_name).text?.trim().toString()
        val day = findViewById<Spinner>(R.id.spinner_day).selectedItemPosition
        val startTime = findViewById<TextView>(R.id.tv_start_time).text.toString()
        val endTime = findViewById<TextView>(R.id.tv_end_time).text.toString()
        val edLecturer = findViewById<TextView>(R.id.ed_lecturer).text?.trim().toString()
        val edNote = findViewById<TextView>(R.id.ed_note).text?.trim().toString()

        if (edCourseName.isNotEmpty() && startTime.isNotEmpty() && endTime.isNotEmpty() && edLecturer.isNotEmpty() && edNote.isNotEmpty()) {
            viewModel.insertCourse(
                courseName = edCourseName,
                day = day,
                startTime = startTime,
                endTime = endTime,
                lecturer = edLecturer,
                note = edNote
            )
            finish()
        } else {
            Snackbar.make(findViewById(android.R.id.content), R.string.input_empty_message, Snackbar.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val START_TIME = "startTime"
        private const val END_TIME = "endTime"
    }
}