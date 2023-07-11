package com.dicoding.courseschedule.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.dicoding.courseschedule.util.QueryType
import com.dicoding.courseschedule.util.QueryUtil
import com.dicoding.courseschedule.util.SortType
import com.dicoding.courseschedule.util.executeThread
import java.time.LocalDateTime
import java.util.Calendar

//TODO 4 : Implement repository with appropriate dao
class DataRepository(private val dao: CourseDao) {
    fun getNearestSchedule(queryType: QueryType) : LiveData<Course?> {
        val query = QueryUtil.nearestQuery(queryType)
        return dao.getNearestSchedule(query)
    }

    fun getAllCourse(sortType: SortType): LiveData<PagedList<Course>> {
        val query = QueryUtil.sortedQuery(sortType)
        val student = dao.getAll(query)

        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(30)
            .setPageSize(PAGE_SIZE)
            .build()

        return LivePagedListBuilder(student, config).build()
    }

    fun getCourse(id: Int) : LiveData<Course> {
        return dao.getCourse(id)
    }

    fun getTodaySchedule() : List<Course> {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_WEEK)
        return dao.getTodaySchedule(day)
    }

    fun insert(course: Course) = executeThread {
        return@executeThread dao.insert(course)
    }

    fun delete(course: Course) = executeThread {
        return@executeThread dao.delete(course)
    }

    companion object {
        private const val PAGE_SIZE = 10

        @Volatile
        private var instance: DataRepository? = null

        fun getInstance(context: Context): DataRepository? {
            return instance ?: synchronized(DataRepository::class.java) {
                if (instance == null) {
                    val database = CourseDatabase.getInstance(context)
                    instance = DataRepository(database.courseDao())
                }
                return instance
            }
        }
    }
}