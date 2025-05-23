package com.codebroth.rewake.calculator.domain.usecase

import com.codebroth.rewake.calculator.domain.model.SleepRecommendation
import java.time.LocalTime

/**
 * Use case to calculate recommended wake up times based on sleep cycles.
 *
 * @property sleepCycleDurationMinutes Duration of a single sleep cycle in minutes.
 * @property fallAsleepBufferMinutes Buffer time to fall asleep in minutes.
 */
class CalculateWakeTimesUseCase {

    private val sleepCycleDurationMinutes = 90
    private val fallAsleepBufferMinutes = 15 //can be changed by user in future

    /**
     * Invokes the use case to calculate recommended wake up times.
     *
     * @param wakeUpTime The time the user wants to wake up.
     * @return A list of sleep recommendations based on the provided wake up time.
     */
    operator fun invoke(wakeUpTime: LocalTime): List<SleepRecommendation> {
        return (1..6).map { cycles ->
            val totalSleepMinutes = cycles * sleepCycleDurationMinutes + fallAsleepBufferMinutes
            val recommendedTime = wakeUpTime.plusMinutes(totalSleepMinutes.toLong())
            SleepRecommendation(cycles, cycles * 1.5, recommendedTime)
        }
    }
}