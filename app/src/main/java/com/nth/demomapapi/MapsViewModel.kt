package com.nth.demomapapi

import androidx.lifecycle.MutableLiveData
import com.nth.demomapapi.model.DirectionsObj
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Nguyen Tien Hoa on 09/16/2020
 */

const val KEY = "AIzaSyDFWa-0NcyAk0VaZCRc2v4IcctBcr8g5R4"
class MapsViewModel {
    private val repository: APIRepository = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(
            RxJava2CallAdapterFactory.create()
        ).build()
        .create(APIRepository::class.java)

    val directionsObj = MutableLiveData<DirectionsObj>()

    fun getDirection(origin:String,destination:String):Disposable{
        return repository.getDirection(origin,destination,KEY)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                directionsObj.value = it
            },{
                it.printStackTrace()
            })
    }

}