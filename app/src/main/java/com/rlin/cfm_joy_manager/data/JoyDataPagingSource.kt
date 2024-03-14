package com.rlin.cfm_joy_manager.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rlin.cfm_joy_manager.entity.JoyData
import com.rlin.cfm_joy_manager.utils.DATABASE_TABLE_NAME
import com.rlin.cfm_joy_manager.utils.SupabaseHelper
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import java.io.IOException
import kotlin.math.max

private const val STARTING_KEY = 0

class JoyDataPagingSource : PagingSource<Int, JoyData>() {
    private var isResponseEmpty = false
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, JoyData> {
        // Start paging with the STARTING_KEY if this is the first load
        val start = params.key ?: STARTING_KEY
        // Load as many items as hinted by params.loadSize
        val range = start.until(start + params.loadSize)


        return try {
            val res =
                SupabaseHelper.client.postgrest[DATABASE_TABLE_NAME].select {
                    order(column = "created_at", order = Order.DESCENDING)
                    range(range.first.toLong()..range.last.toLong())
                }
                    .decodeList<JoyData>()
            Log.d("Paging请求", "请求${start}-${range.last}, 返回了了${res.size}个数据")
            if (res.isEmpty()) {
                return LoadResult.Error(throwable = Exception("end"))
            }
            LoadResult.Page(
                data = res,

                // Make sure we don't try to load items behind the STARTING_KEY
                prevKey = when (start) {
                    STARTING_KEY -> null
                    else -> ensureValidKey(key = range.first - params.loadSize)
                },
                nextKey = range.last + 1
            )
        } catch (e: Exception) {
            if (e is IOException) {
                Log.d("测试错误数据", "-------连接失败")
            }
            Log.d("测试错误数据", "-------${e.toString()}")
            if (!e.message?.contains("cancelled")!!) {
                load(params)
            } else {
                LoadResult.Error(throwable = e)
            }
        }
    }

    // The refresh key is used for the initial load of the next PagingSource, after invalidation
    override fun getRefreshKey(state: PagingState<Int, JoyData>): Int? {
        // In our case we grab the item closest to the anchor position
        // then return its id - (state.config.pageSize / 2) as a buffer
        val anchorPosition = state.anchorPosition ?: return null
        val article = state.closestItemToPosition(anchorPosition) ?: return null
        return null//ensureValidKey(key = article.id - (state.config.pageSize / 2))
    }

    private fun ensureValidKey(key: Int) = max(STARTING_KEY, key)

}