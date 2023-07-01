package com.dicoding.courseschedule.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.sqlite.db.SupportSQLiteQuery

//TODO 2 : Define data access object (DAO)
@Dao
interface CourseDao {

    fun getNearestSchedule(query: SupportSQLiteQuery): LiveData<Course?>

    fun getAll(query: SupportSQLiteQuery): DataSource.Factory<Int, Course>

    fun getCourse(id: Int): LiveData<Course>

    fun getTodaySchedule(day: Int): List<Course>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(course: Course)

    fun delete(course: Course)
}