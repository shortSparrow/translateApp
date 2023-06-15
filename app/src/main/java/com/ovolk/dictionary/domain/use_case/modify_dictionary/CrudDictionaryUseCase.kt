package com.ovolk.dictionary.domain.use_case.modify_dictionary

import com.ovolk.dictionary.domain.model.dictionary.Dictionary
import com.ovolk.dictionary.domain.repositories.DictionaryRepository
import com.ovolk.dictionary.domain.response.Either
import com.ovolk.dictionary.domain.response.Failure
import com.ovolk.dictionary.domain.response.FailureMessage
import com.ovolk.dictionary.domain.response.FailureWithCode
import com.ovolk.dictionary.domain.response.Success
import com.ovolk.dictionary.domain.use_case.modify_dictionary.util.validateNewDictionary
import javax.inject.Inject

const val DICTIONARY_NOT_EXIST = 1
const val FAILED_DELETE_DICTIONARY = 5
const val UNKNOWN_ERROR = 6


class CrudDictionaryUseCase @Inject constructor(private val dictionaryRepository: DictionaryRepository) {
    fun getDictionaryList() = dictionaryRepository.getDictionaryList()

    suspend fun getDictionary(dictionaryId: Long): Either<Dictionary, FailureWithCode> {
        val response = dictionaryRepository.getDictionary(dictionaryId = dictionaryId)
        if (response == null) {
            return Either.Failure(
                FailureWithCode(
                    "this dictionary does not exist",
                    DICTIONARY_NOT_EXIST
                )
            );
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

        if (validation.isErrorExist()) {
            return validation.returnValidationFailure()
        }

        val dictionary = Dictionary(
            id = 0L,
            title = title,
            langToCode = langToCode as String,
            langFromCode = langFromCode as String,
            isActive = dictionaryRepository.getDictionaryListSize() == 0, // if it is first dictionary make it active
            isSelected = false,
        )
        val response = dictionaryRepository.addNewDictionary(dictionary = dictionary)

        return if (response == -1L) return Either.Failure(
            FailureWithCode(
                "Something went wrong, please, try again",
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

        if (validation.isErrorExist()) {
            return validation.returnValidationFailure()
        }

        val dictionary = Dictionary(
            id = id,
            title = title,
            langToCode = langToCode as String,
            langFromCode = langFromCode as String,
            isActive = isActive,
            isSelected = false,
        )
        val response = dictionaryRepository.addNewDictionary(dictionary = dictionary)

        return if (response == -1L) return Either.Failure(
            FailureWithCode(
                "Something went wrong, please, try again",
                code = UNKNOWN_ERROR
            )
        )
        else Either.Success(Success)
    }


    suspend fun deleteDictionary(dictionaryId: Long): Either<Success, FailureMessage> {
        val isDeleteSuccess = dictionaryRepository.deleteDictionary(dictionaryId)

        if (isDeleteSuccess) {
            return Either.Success(Success)
        }
        return Either.Failure(FailureMessage("Can't delete this dictionary"))
    }


}