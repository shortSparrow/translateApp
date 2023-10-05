package com.ovolk.dictionary.data.remote.retrofit

import com.ovolk.dictionary.data.model.NearestFeatureFirestore
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


var firebaseRetrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://firestore.googleapis.com/v1/projects/translateapp-25439/databases/(default)/documents/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()


interface FirestoreService {
    @GET("nearest_features/list")
    fun getNearestFeature(@Query("key") key: String): Call<NearestFeatureFirestore>
}


var firebaseRetrofitService: FirestoreService =
    firebaseRetrofit.create(FirestoreService::class.java)
