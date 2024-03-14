package com.rlin.cfm_joy_manager.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rlin.cfm_joy_manager.entity.JoyDataResponse
import kotlinx.coroutines.flow.Flow

private const val ITEMS_PER_PAGE = 10

class JoyDataViewModel(
    private val repository: JoyDataRepository = JoyDataRepository(),
) : ViewModel() {

    val items: Flow<PagingData<JoyDataResponse>> = Pager(
        config = PagingConfig(pageSize = ITEMS_PER_PAGE, enablePlaceholders = true, initialLoadSize = 2 * ITEMS_PER_PAGE),
        pagingSourceFactory = { repository.joyDataSource() }
    )
        .flow
        .cachedIn(viewModelScope)
}