package com.nth.demomapapi

import com.nth.demomapapi.model.DirectionsObj
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Nguyen Tien Hoa on 09/16/2020
 */


interface APIRepository {

    @GET("/maps/api/directions/json")
    fun getDirection(
        @Query(value = "origin") origin:String,
        @Query(value = "destination") destination:String,
        @Query(value = "key") apiKey:String
    ) : Observable<DirectionsObj>


}