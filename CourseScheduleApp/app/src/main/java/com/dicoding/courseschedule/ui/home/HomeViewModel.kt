package com.dicoding.courseschedule.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.data.DataRepository
import com.dicoding.courseschedule.util.QueryType

class HomeViewModel(repository: DataRepository): ViewModel() {

    private val _queryType = MutableLiveData<QueryType>()
    private val _todaySchedule = MutableLiveData<List<Course>>()
    val todaySchedule: LiveData<List<Course>> = _todaySchedule

    init {
        _queryType.value = QueryType.CURRENT_DAY
        _todaySchedule.value = repository.getTodaySchedule()
    }

    fun setQueryType(queryType: QueryType) {
        _queryType.value = queryType
    }
}
