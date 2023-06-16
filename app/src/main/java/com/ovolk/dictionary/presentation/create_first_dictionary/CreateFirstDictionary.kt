package com.ovolk.dictionary.presentation.create_first_dictionary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.core.header.Header
import com.ovolk.dictionary.presentation.core.text_field.OutlinedErrableTextField
import com.ovolk.dictionary.presentation.modify_dictionary.ModifyDictionaryAction
import com.ovolk.dictionary.presentation.modify_dictionary.ModifyDictionaryModes
import com.ovolk.dictionary.presentation.modify_dictionary.components.LanguagesPicker
import com.ovolk.dictionary.presentation.modify_dictionary.components.SelectLanguageBottomSheet
import com.ovolk.dictionary.presentation.navigation.graph.Graph
import com.ovolk.dictionary.presentation.navigation.stack.CommonRotes

@Composable
fun CreateFirstDictionary(navController: NavHostController) {
    val viewModel = hiltViewModel<CreateFirstDictionaryViewModel>()
    val state = viewModel.state
    val onAction = viewModel::onAction



    LaunchedEffect(Unit) {
        viewModel.listener = object : CreateFirstDictionaryViewModel.Listener {
            override fun goToHome() {
                navController.navigate(Graph.MAIN_TAB_BAR)
            }
        }
    }

    SelectLanguageBottomSheet(
        isBottomSheetOpen = state.languageBottomSheet.isOpen,
        languageList = state.languageBottomSheet.languageList,
        onSelectLanguage = { langCode ->
            onAction(FirstDictionaryAction.OnSelectLanguage(languageCode = langCode))
        },
        onSearchLanguage = { value -> onAction(FirstDictionaryAction.OnSearchLanguage(value)) },
        closeLanguageBottomSheet = { onAction(FirstDictionaryAction.CloseLanguageBottomSheet) },
        preferredLanguages = state.languageBottomSheet.preferredLanguageList,
        body = {
            Column() {
                Header(title = "Create first dictionary", withBackButton = false)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column() {
                        OutlinedErrableTextField(
                            value = state.dictionaryName,
                            onValueChange = { onAction(FirstDictionaryAction.OnInputTitle(it)) },
                            label = { Text(text = "Dictionary name") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = state.dictionaryNameError
                        )


                        Box(modifier = Modifier.padding(top = 40.dp)) {
                            LanguagesPicker(
                                languageToName = state.languageToCode,
                                languageFromName = state.languageFromCode,
                                langFromError = state.langFromError,
                                langToError = state.langToError,
                                openLanguageBottomSheet = { languageType ->
                                    onAction(
                                        FirstDictionaryAction.OpenLanguageBottomSheet(languageType)
                                    )
                                }
                            )
                        }

                        if (state.langErrorMessage != null) {
                            Text(
                                text = state.langErrorMessage,
                                color = colorResource(id = R.color.red),
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(top = 20.dp)
                            )
                        }
                    }

                    Button(
                        onClick = { onAction(FirstDictionaryAction.SaveDictionary) },
                        modifier = Modifier.fillMaxWidth(1f)
                    ) {
                        Text(text = "Save".uppercase(),color = colorResource(id = R.color.white) )
                    }
                }
            }
        },
    )

}

