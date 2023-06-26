package com.ovolk.dictionary.domain.use_case.modify_dictionary

import android.app.Application
import com.ovolk.dictionary.R
import com.ovolk.dictionary.data.mapper.DictionaryMapper
import com.ovolk.dictionary.domain.model.dictionary.Dictionary
import com.ovolk.dictionary.domain.model.dictionary.SelectableDictionary
import com.ovolk.dictionary.domain.repositories.DictionaryRepository
import com.ovolk.dictionary.domain.response.Either
import com.ovolk.dictionary.domain.response.Failure
import com.ovolk.dictionary.domain.response.FailureMessage
import com.ovolk.dictionary.domain.response.FailureWithCode
import com.ovolk.dictionary.domain.response.Success
import com.ovolk.dictionary.domain.use_case.modify_dictionary.util.validateNewDictionary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

const val DICTIONARY_NOT_EXIST = 1
const val DICTIONARY_ALREADY_EXIST = 2
const val UNKNOWN_ERROR = 6


class CrudDictionaryUseCase @Inject constructor(
    private val dictionaryRepository: DictionaryRepository,
    private val mapper: DictionaryMapper,
    private val application: Application,
) {
    fun getDictionaryList(): Flow<List<Dictionary>> {
        return dictionaryRepository.getDictionaryList()
    }

    fun getSelectableDictionaryList(): Flow<List<SelectableDictionary>> {
        return getDictionaryList().map { list ->
            list.map {
                mapper.dictionaryToSelectableDictionary(
                    it
                )
            }
        }
    }

    fun getDictionaryFlow(dictionaryId: Long): Flow<Dictionary?> {
        return dictionaryRepository.getDictionaryFlow(dictionaryId = dictionaryId)
    }

    suspend fun getDictionary(dictionaryId: Long): Either<Dictionary, FailureWithCode> {
        val response = dictionaryRepository.getDictionary(dictionaryId = dictionaryId)
        if (response == null) {
            return Either.Failure(
                FailureWithCode(
                    application.getString(R.string.crud_dictionary_use_case_dictionary_not_exist),
                    DICTIONARY_NOT_EXIST
                )
            )
        }
        return Either.Success(response)
    }

    suspend fun addDictionary(
        title: String,
        langFromCode: String?,
        langToCode: String?,
    ): Either<Success, Failure> {
        val validation = validateNewDictionary(
            title = title,
            langFromCode = langFromCode,
            langToCode = langToCode
        )

        if (validation is Either.Failure) {
            return validation
        }

        val isTheSameDictionaryExist = dictionaryRepository.getDictionaryByLang(
            langCodeFrom = langFromCode as String,
            langCodeTo = langToCode as String
        ) != null

        if (isTheSameDictionaryExist) {
            return Either.Failure(
                FailureWithCode(
                    application.getString(R.string.crud_dictionary_use_case_dictionary_already_exist),
                    code = DICTIONARY_ALREADY_EXIST
                )
            )
        }

        val dictionary = Dictionary(
            id = 0L,
            title = title,
            langToCode = langToCode,
            langFromCode = langFromCode,
            isActive = dictionaryRepository.getDictionaryListSize() == 0, // if it is first dictionary make it active
        )
        val response = dictionaryRepository.addNewDictionary(dictionary = dictionary)

        return if (response == -1L) return Either.Failure(
            FailureWithCode(
                application.getString(R.string.something_wrong_try_again),
                code = UNKNOWN_ERROR
            )
        )
        else Either.Success(Success)
    }


    suspend fun editDictionary(
        id: Long,
        title: String,
        langFromCode: String?,
        langToCode: String?,
        isActive: Boolean,
    ): Either<Success, Failure> {
        val validation = validateNewDictionary(
            title = title,
            langFromCode = langFromCode,
            langToCode = langToCode
        )

        if (validation is Either.Failure) {
            return validation
        }

        val dictionary = Dictionary(
            id = id,
            title = title,
            langToCode = langToCode as String,
            langFromCode = langFromCode as String,
            isActive = isActive,
        )
        val response = dictionaryRepository.addNewDictionary(dictionary = dictionary)

        return if (response == -1L) return Either.Failure(
            FailureWithCode(
                application.getString(R.string.something_wrong_try_again),
                code = UNKNOWN_ERROR
            )
        )
        else Either.Success(Success)
    }

    suspend fun deleteDictionaries(dictionaryListId: List<Long>): Either<Success, FailureMessage> {
        val isDeleteSuccess = dictionaryRepository.deleteDictionaries(dictionaryListId)

        if (isDeleteSuccess) {
            return Either.Success(Success)
        }

        val errorMessage = application.resources.getQuantityString(
            R.plurals.crud_dictionary_use_case_cant_delete_dictionary,
            dictionaryListId.size,
        )
        return Either.Failure(FailureMessage(errorMessage))
    }

}