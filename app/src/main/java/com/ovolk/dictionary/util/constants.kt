package com.ovolk.dictionary.util

import androidx.compose.ui.unit.dp

// DATABASE
const val TRANSLATED_WORDS_TABLE_NAME = "translated_words"
const val TRANSLATED_WORDS_TRANSLATIONS = "word_translations"
const val TRANSLATED_WORDS_HINTS = "word_hints"
const val TRANSLATED_WORDS_LISTS = "word_lists"
const val DELAYED_UPDATE_WORDS_PRIORITY = "delayed_updated_words_priority"

const val EXAM_WORD_ANSWERS_TABLE_NAME = "exam_word_answers"

const val EXAM_WORD_ANSWER_LIST_SIZE = 6

// PREFERENCES
const val SETTINGS_PREFERENCES = "my_preferences"
const val USER_STATE_PREFERENCES = "user_state_preferences"

// PREFERENCES KEYS
const val EXAM_REMINDER_FREQUENCY = "exam_reminder_frequency"
const val EXAM_REMINDER_TIME = "exam_reminder_time"
const val IS_CHOOSE_LANGUAGE = "is_choose_language"
const val SHOW_VARIANTS_EXAM_AVAILABLE_LANGUAGES = "show_variants_exam_available_languages"
const val DAILY_EXAM_SETTINGS = "daily_exam_settings"

// LANGUAGES
const val UKRAINE_COUNTRY_CODE = "ua"
const val UKRAINE_LANGUAGE_CODE = "uk"
val showVariantsAvailableLanguages = listOf("UK")

const val EXAM_REMINDER_INTENT_CODE = 101
const val DEFAULT_PRIORITY_VALUE = 5
const val DEFAULT_DAILY_EXAM_WORDS_COUNT = 10

val MAX_BUTTON_WIDTH = 300.dp

const val DEEP_LINK_BASE = "https://www.personal-dictionary"