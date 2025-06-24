package com.jesushz.notemarkmilestone.note.domain

import com.jesushz.notemarkmilestone.core.domain.networking.DataError
import com.jesushz.notemarkmilestone.core.domain.networking.Result

class DefaultPaginator<Key, Item>(
    private val initialKey: Key,
    private val onLoadUpdated: (Boolean) -> Unit,
    private val onRequest: suspend (nextKey: Key) -> Result<List<Item>, DataError.Network>,
    private val getNextKey: suspend (List<Item>) -> Key,
    private val onError: suspend (DataError.Network) -> Unit,
    private val onSuccess: suspend (items: List<Item>, newKey: Key) -> Unit
): Paginator<Key, Item> {

    private var currentKey = initialKey
    private var isMakingRequest = false

    override suspend fun loadNextItems() {
        if(isMakingRequest) {
            return
        }
        isMakingRequest = true
        onLoadUpdated(true)
        val result = onRequest(currentKey)
        isMakingRequest = false
        when (result) {
            is Result.Success -> {
                currentKey = getNextKey(result.data)
                onSuccess(result.data, currentKey)
                onLoadUpdated(false)
            }
            is Result.Error -> {
                onError(result.error)
                onLoadUpdated(false)
            }
        }
    }

    override fun reset() {
        currentKey = initialKey
    }

}
