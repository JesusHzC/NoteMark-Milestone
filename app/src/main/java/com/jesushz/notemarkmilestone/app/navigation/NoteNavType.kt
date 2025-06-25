package com.jesushz.notemarkmilestone.app.navigation

import android.net.Uri
import androidx.navigation.NavType
import androidx.savedstate.SavedState
import com.jesushz.notemarkmilestone.core.domain.note.NoteNavigation
import kotlinx.serialization.json.Json

object NoteNavType {

    val NoteType = object: NavType<NoteNavigation?>(
        isNullableAllowed = true
    ) {
        override fun put(
            bundle: SavedState,
            key: String,
            value: NoteNavigation?
        ) {
            bundle.putString(
                key,
                Json.encodeToString(value)
            )
        }

        override fun get(
            bundle: SavedState,
            key: String
        ): NoteNavigation? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): NoteNavigation? {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: NoteNavigation?): String {
            return Uri.encode(Json.encodeToString(value))
        }

    }

}