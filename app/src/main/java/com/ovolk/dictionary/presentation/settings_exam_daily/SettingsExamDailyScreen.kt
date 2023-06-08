package com.ovolk.dictionary.presentation.settings_exam_daily

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
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
import androidx.compose.ui.unit.sp
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
    val viewModel = hiltViewModel<SettingsExamDailyViewModel>()
    val state = viewModel.state
    val onAction = viewModel::onAction

    SettingsExamDailyPresenter(navController = navController, state = state, onAction = onAction)

}

@Composable
fun SettingsExamDailyPresenter(
    navController: NavHostController,
    state: SettingsExamDailyState,
    onAction: (SettingsExamDailyAction) -> Unit
) {
    val focusManager = LocalFocusManager.current
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
                FlowRow {
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

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(id = R.string.settings_daily_exam_words_is_double_lang_exam_label),
                        )
                        Text(
                            text = stringResource(
                                id = R.string.settings_daily_exam_words_is_double_lang_exam_description,
                                "uk", // TODO get current dictionary (will have dictionary on each languages pare)
                                "en",
                                "en",
                                "uk"
                            ),
                            fontSize = 10.sp,
                            color = colorResource(id = R.color.light_grey)
                        )

                    }
                    Switch(
                        checked = state.isDoubleLanguageExamEnable,
                        onCheckedChange = { onAction(SettingsExamDailyAction.OnToggleDoubleLanguageExam) },
                        modifier = Modifier.padding(start = 5.dp),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = colorResource(id = R.color.blue),
                            checkedTrackColor =  colorResource(id = R.color.blue),
                        )
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
    SettingsExamDailyPresenter(
        navController = rememberNavController(),
        state = SettingsExamDailyState(countOfWords = "10"),
        onAction = {})
}