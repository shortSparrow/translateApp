package com.ovolk.dictionary.presentation.settings_exam_daily

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.core.flow_row.FlowRow
import com.ovolk.dictionary.presentation.core.header.Header
import com.ovolk.dictionary.util.MAX_BUTTON_WIDTH
import com.ovolk.dictionary.util.compose.click_effects.clickWithoutFeedback

@Composable
fun SettingsExamDailyScreen(
    navController: NavHostController,
) {
    val focusManager = LocalFocusManager.current

    val viewModel = hiltViewModel<SettingsExamDailyViewModel>()
    val state = viewModel.state
    val onAction = viewModel::onAction

    val isSaveEnabled = state.isStateChanges && state.countOfWords.isNotEmpty()

    Column {
        Header(
            title = stringResource(id = R.string.settings_daily_exam_screen_title),
            withBackButton = true,
            onBackButtonClick = { navController.popBackStack() }
        )
        Column(
            Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.gutter))
                .clickWithoutFeedback { focusManager.clearFocus() }) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(1f),
            ) {
                FlowRow() {
                    Text(
                        text = stringResource(id = R.string.settings_daily_exam_words_count_description),
                        modifier = Modifier.padding(top = 20.dp, end = 20.dp)
                    )
                    OutlinedTextField(
                        value = state.countOfWords,
                        onValueChange = { onAction(SettingsExamDailyAction.ChangeCountOfWords(it)) },
                        label = { Text(text = stringResource(id = R.string.settings_daily_exam_words_count_label)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                    )
                }

            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = dimensionResource(id = R.dimen.small_gutter))
            ) {
                Button(
                    onClick = { onAction(SettingsExamDailyAction.OnSaveChanges) },
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .widthIn(100.dp, MAX_BUTTON_WIDTH),
                    enabled = isSaveEnabled
                ) {
                    Text(
                        text = stringResource(id = R.string.save).uppercase(),
                        color = colorResource(id = R.color.white)
                    )
                }
            }
        }
    }

}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SettingsExamDailyScreenPreview() {
    SettingsExamDailyScreen(navController = rememberNavController())
}