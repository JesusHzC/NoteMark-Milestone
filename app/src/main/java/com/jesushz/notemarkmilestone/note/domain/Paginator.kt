package com.jesushz.notemarkmilestone.note.domain

interface Paginator<Key, Item> {

    suspend fun loadNextItems()
    fun reset()

}
