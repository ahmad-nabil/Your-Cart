package com.ejabi.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ejabi.domain.model.AuthSession
import com.ejabi.domain.model.User
import kotlinx.coroutines.flow.first
import kotlin.text.orEmpty

class SessionPreferences(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val USER_ID = intPreferencesKey("user_id")
        private val USERNAME = stringPreferencesKey("username")
        private val EMAIL = stringPreferencesKey("email")
        private val FIRST_NAME = stringPreferencesKey("first_name")
        private val LAST_NAME = stringPreferencesKey("last_name")
        private val IMAGE = stringPreferencesKey("image")
    }

    suspend fun saveSession(session: AuthSession) {
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = session.accessToken
            session.refreshToken?.let { prefs[REFRESH_TOKEN] = it }
            prefs[USER_ID] = session.user.id
            prefs[USERNAME] = session.user.username
            prefs[EMAIL] = session.user.email
            prefs[FIRST_NAME] = session.user.firstName
            prefs[LAST_NAME] = session.user.lastName
            prefs[IMAGE] = session.user.image.orEmpty()
        }
    }

    suspend fun getSession(): AuthSession? {
        val prefs = dataStore.data.first()
        val token = prefs[ACCESS_TOKEN] ?: return null

        return AuthSession(
            accessToken = token,
            refreshToken = prefs[REFRESH_TOKEN],
            user = User(
                id = prefs[USER_ID] ?: -1,
                username = prefs[USERNAME].orEmpty(),
                email = prefs[EMAIL].orEmpty(),
                firstName = prefs[FIRST_NAME].orEmpty(),
                lastName = prefs[LAST_NAME].orEmpty(),
                image = prefs[IMAGE]
            )
        )
    }

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}