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

package com.codebroth.drowse.core.domain.model

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

/**
 * Enum class representing the user's theme preference.
 */
enum class ThemePreference(val value: Int) {
    /**
     * System default theme.
     */
    AUTO(0),

    /**
     * Light theme.
     */
    LIGHT(1),

    /**
     * Dark theme.
     */
    DARK(2);

    companion object {
        /**
         * Checks if the given theme value is a dark theme.
         */
        @Composable
        fun isDarkTheme(themeValue: Int): Boolean {
            return when (themeValue) {
                AUTO.value -> isSystemInDarkTheme()
                DARK.value -> true
                else -> false
            }
        }
    }
}