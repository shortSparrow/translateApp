package com.ovolk.dictionary.util

import androidx.compose.ui.unit.dp

// DATABASE
const val TRANSLATED_WORDS_TABLE_NAME = "translated_words"
const val TRANSLATED_WORDS_TRANSLATIONS = "word_translations"
const val TRANSLATED_WORDS_HINTS = "word_hints"
const val TRANSLATED_WORDS_LISTS = "word_lists"
const val DELAYED_UPDATE_WORDS_PRIORITY = "delayed_updated_words_priority"
const val DICTIONARIES = "dictionaries"

const val EXAM_WORD_ANSWERS_TABLE_NAME = "exam_word_answers" // TODO must be dropped in 1.0.9

const val EXAM_WORD_ANSWER_LIST_SIZE = 6

// PREFERENCES
const val SETTINGS_PREFERENCES = "my_preferences"
const val USER_STATE_PREFERENCES = "user_state_preferences"
const val APP_SETTINGS = "app_settings"

// PREFERENCES KEYS
const val EXAM_REMINDER_FREQUENCY = "exam_reminder_frequency"
const val EXAM_REMINDER_TIME = "exam_reminder_time"
const val IS_DOUBLE_LANGUAGE_EXAM_ENABLE = "is_double_language_exam_enable"
const val IS_CHOOSE_LANGUAGE = "is_choose_language" // TODO remove after migration in next version
const val IS_WELCOME_SCREEN_PASSED = "is_welcome_screen_passed"
const val DAILY_EXAM_SETTINGS = "daily_exam_settings"
const val SETTINGS_VERSION = "settings_version" // was added to version 1.0.6
const val IS_EXAM_AUTO_SUGGEST_ENABLE = "is_exam_auto_suggest_enable"
const val APP_LANGUAGE_CODE = "app_language"
const val PERMISSIONS_WAS_REQUESTED_ONCE = "permissions_was_requested_once"

// LANGUAGES
const val UKRAINE_COUNTRY_CODE = "ua"
const val UKRAINE_LANGUAGE_CODE = "uk"
val showVariantsAvailableLanguages = listOf("UK", "EN")
val supportedAppLanguages = listOf("uk", "en")
const val DEFAULT_APP_LANGUAGE = "en"

const val DEFAULT_IS_DOUBLE_LANGUAGE_EXAM_ENABLE = false
const val EXAM_REMINDER_INTENT_CODE = 101
const val DEFAULT_PRIORITY_VALUE = 5
const val DEFAULT_DAILY_EXAM_WORDS_COUNT = 10

val MAX_BUTTON_WIDTH = 300.dp

const val DEEP_LINK_BASE = "https://www.personal-dictionary"

// PENDING INTENTS
const val PASSED_SEARCH_WORD = 10