/*
 *
 *    Copyright 2025 Jayman Rana
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.codebroth.drowse.alarm.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codebroth.drowse.alarm.data.scheduling.AlarmAlertHandler
import com.codebroth.drowse.core.data.local.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel for the Alarm Alert screen.
 *
 * @param alarmHandler The handler for managing alarm alerts.
 * @param userPreferencesRepository The repository for user preferences.
 */
@HiltViewModel
class AlarmAlertViewModel @Inject constructor(
    private val alarmHandler: AlarmAlertHandler,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _shouldFinish = MutableLiveData<Boolean>(false)
    val shouldFinish: LiveData<Boolean> = _shouldFinish

    val is24HourFormat: StateFlow<Boolean> =
        userPreferencesRepository.userPreferencesFlow
            .map { it.is24HourFormat }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false
            )

    /**
     * Called when the alarm is dismissed.
     * This will dismiss the alarm and set the shouldFinish flag to true.
     */
    fun onDismiss() {
        alarmHandler.dismissAlarm()
        _shouldFinish.value = true
    }

    /**
     * Called when the snooze button is pressed.
     * This will snooze the alarm and set the shouldFinish flag to true.
     */
    fun onSnooze() {
        alarmHandler.snoozeAlarm()
        _shouldFinish.value = true
    }
}