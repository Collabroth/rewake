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

package com.codebroth.drowse.reminder.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codebroth.drowse.core.data.local.UserPreferencesRepository
import com.codebroth.drowse.core.ui.component.snackbar.SnackBarEvent
import com.codebroth.drowse.core.ui.component.snackbar.SnackbarController
import com.codebroth.drowse.reminder.data.scheduling.ReminderSchedulerService
import com.codebroth.drowse.reminder.domain.model.Reminder
import com.codebroth.drowse.reminder.domain.model.formattedTime
import com.codebroth.drowse.reminder.domain.usecase.AddReminderUseCase
import com.codebroth.drowse.reminder.domain.usecase.DeleteReminderUseCase
import com.codebroth.drowse.reminder.domain.usecase.GetAllRemindersUseCase
import com.codebroth.drowse.reminder.domain.usecase.UpdateReminderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    getAllReminders: GetAllRemindersUseCase,
    private val addReminder: AddReminderUseCase,
    private val updateReminder: UpdateReminderUseCase,
    private val deleteReminder: DeleteReminderUseCase,
    private val schedulerService: ReminderSchedulerService,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReminderUiState())
    val uiState: StateFlow<ReminderUiState> =
        combine(
            _uiState,
            preferencesRepository.userPreferencesFlow
        ) { state, userPreferences ->
            state.copy(is24HourFormat = userPreferences.is24HourFormat)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = _uiState.value.copy(is24HourFormat = false)
            )

    val reminders: StateFlow<List<Reminder>> =
        getAllReminders()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun showDialog(editing: Reminder? = null) {
        _uiState.update { currentState ->
            currentState.copy(
                isDialogOpen = true,
                editingReminder = editing
            )
        }
    }

    fun dismissDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                isDialogOpen = false,
                editingReminder = null
            )
        }
    }

    fun onSaveReminder(reminder: Reminder) {
        viewModelScope.launch {
            if (reminder.id == 0) {
                val newId = addReminder(reminder)
                schedulerService.scheduleNext(reminder, newId)
            } else {
                updateReminder(reminder)
                schedulerService.reschedule(reminder, reminder.id)
            }
            val message = if (reminder.daysOfWeek.isEmpty()) {
                "Reminder set for ${reminder.formattedTime()}"
            } else {
                "Reminder set for ${reminder.formattedTime()} on ${
                    reminder.daysOfWeek.joinToString(
                        ", "
                    ) { it.name }
                }"
            }
            showSnackbar(message)
            dismissDialog()
        }
    }

    fun onDeleteReminder(reminder: Reminder) {
        viewModelScope.launch {
            deleteReminder(reminder)
            schedulerService.cancel(reminder.id)
        }
    }

    private fun showSnackbar(message: String) {
        viewModelScope.launch {
            SnackbarController.sendEvent(
                event = SnackBarEvent(
                    message = message
                )
            )
        }
    }

    data class ReminderUiState(
        val isDialogOpen: Boolean = false,
        val editingReminder: Reminder? = null,
        val is24HourFormat: Boolean = false,
    )
}