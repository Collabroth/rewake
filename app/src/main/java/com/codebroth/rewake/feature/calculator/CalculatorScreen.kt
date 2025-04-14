package com.codebroth.rewake.feature.calculator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessHigh
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.outlined.BrightnessHigh
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(modifier: Modifier = Modifier) {

    val calculatorTabItems = listOf(
        CalculatorTabItem(
            title = "Wake Up At",
            selectedIcon = Icons.Default.BrightnessHigh,
            unselectedIcon = Icons.Outlined.BrightnessHigh
        ),
        CalculatorTabItem(
            title = "Sleep At",
            selectedIcon = Icons.Default.DarkMode,
            unselectedIcon = Icons.Outlined.DarkMode
        )
    )

    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }

    val pagerState = rememberPagerState {
        calculatorTabItems.size
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState.targetPage) {
        selectedTabIndex = pagerState.targetPage
    }

    Column(
        modifier = modifier
    ) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            calculatorTabItems.forEachIndexed { index, item ->
                Tab(
                    selected = index == selectedTabIndex,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                        selectedTabIndex = index
                    },
                    text = {
                        Text(text = item.title)
                    },
                    icon = {
                        Icon(
                            imageVector = if (index == selectedTabIndex) {
                                item.selectedIcon
                            } else {
                                item.unselectedIcon
                            },
                            contentDescription = item.title
                        )
                    }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
                .weight(1f)
        ) { index ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = calculatorTabItems[index].title)
            }
        }
    }
}

data class CalculatorTabItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CalculatorScreenPreview() {
    Surface {
        CalculatorScreen(
            modifier = Modifier
                .fillMaxSize()
        )
    }
}