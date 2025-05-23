package com.codebroth.rewake.reminder.domain.model

import com.codebroth.rewake.core.domain.util.toBitmask
import com.codebroth.rewake.reminder.data.local.ReminderEntity
import java.time.DayOfWeek

data class Reminder(
    val id: Int = 0,
    val hour: Int,
    val minute: Int,
    val daysOfWeek: Set<DayOfWeek>,
    val label: String? = null
)

fun Reminder.fromDomainModel(): ReminderEntity =
    ReminderEntity(
        id = id,
        hour = hour,
        minute = minute,
        daysOfWeekBitmask = daysOfWeek.toBitmask(),
        label = label
    )
