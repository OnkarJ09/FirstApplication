package com.onkar.androidtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/** Title shown at the top of the verification screen. */
const val APP_TITLE = "Android Environment Test"

/** Status text before the test button is pressed. */
const val STATUS_READY = "Ready"

/** Status text after the test button has been pressed at least once. */
const val STATUS_BUTTON_WORKED = "Test button works!"

/** Label of the single interactive control on the screen. */
const val TEST_BUTTON_LABEL = "TEST BUTTON"

/** Test tags used by the instrumented UI tests. */
object TestTags {
    const val COUNTER = "counter"
    const val STATUS = "status"
    const val TEST_BUTTON = "test_button"
}

/**
 * The app's only piece of logic: the status message is derived from the counter,
 * which keeps the status a pure function of visible state and unit-testable.
 */
fun statusForCount(count: Int): String =
    if (count > 0) STATUS_BUTTON_WORKED else STATUS_READY

/** Formats the counter line, e.g. `Counter: 0`. */
fun counterLabel(count: Int): String = "Counter: $count"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    EnvironmentTestScreen()
                }
            }
        }
    }
}

@Composable
fun EnvironmentTestScreen() {
    var count by remember { mutableIntStateOf(0) }
    val status = statusForCount(count)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
    ) {
        Text(
            text = APP_TITLE,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
        )

        Text("\u2713 Android application started")
        Text("\u2713 Kotlin compiled successfully")
        Text("\u2713 Jetpack Compose loaded")

        Text(
            text = counterLabel(count),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.testTag(TestTags.COUNTER),
        )

        Button(
            onClick = { count++ },
            modifier = Modifier.testTag(TestTags.TEST_BUTTON),
        ) {
            Text(TEST_BUTTON_LABEL)
        }

        Text(
            text = "Status: $status",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.testTag(TestTags.STATUS),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EnvironmentTestScreenPreview() {
    MaterialTheme { EnvironmentTestScreen() }
}
