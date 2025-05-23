package com.codebroth.rewake.reminder.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.codebroth.rewake.R
import com.codebroth.rewake.reminder.domain.model.Reminder
import com.codebroth.rewake.reminder.ui.component.ReminderDetailsDialog
import com.codebroth.rewake.reminder.ui.component.ReminderItem

@Composable
fun ReminderScreen(
    modifier: Modifier = Modifier,
    viewModel: ReminderViewModel = hiltViewModel(),
    setReminderHour: Int? = null,
    setReminderMinute: Int? = null,
) {
    val uiState by viewModel.uiState.collectAsState()
    val reminders by viewModel.reminders.collectAsState()

    var hasConsumedArgs by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(setReminderHour, setReminderMinute) {
        if (!hasConsumedArgs
            && setReminderHour != null
            && setReminderMinute != null
        ) {
            viewModel.showDialog(
                Reminder(
                    id = 0,
                    hour = setReminderHour,
                    minute = setReminderMinute,
                    daysOfWeek = emptySet(),
                    label = null
                )
            )
            hasConsumedArgs = true
        }
    }
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        if (reminders.isEmpty()) {
            Text(
                text = stringResource(R.string.empty_reminders_description),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(reminders, key = { it.id }) { reminder ->
                ReminderItem(
                    reminder = reminder,
                    onClick = { viewModel.showDialog(reminder) },
                    onDelete = { viewModel.onDeleteReminder(reminder) },
                    is24Hour = uiState.is24HourFormat,
                )
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = { viewModel.showDialog() }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.description_action_add_reminder)
            )
        }
    }
    if (uiState.isDialogOpen) {
        ReminderDetailsDialog(
            initial = uiState.editingReminder,
            onCancel = { viewModel.dismissDialog() },
            onConfirm = { viewModel.onSaveReminder(it) },
            is24Hour = uiState.is24HourFormat
        )
    }
}