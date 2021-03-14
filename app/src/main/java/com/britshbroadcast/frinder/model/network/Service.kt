package com.britshbroadcast.frinder.model.network

import com.britshbroadcast.frinder.model.data.CafeResult
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    //RxJava - React X implementation for Java - build on top of the Publisher-Subscriber design pattern
    //Observables
    //* Single<T> - it emits a single value
    //* Observable<T> - emits 0 to many values
    //* Flowable<T> - emits 0 to many values but - has back pressure handling
    //* Completable - emit only the method onCompleted or onError
    //* Maybe - Decides whether a call/subscription can be completed or not. Value, no value or exception

    @GET("maps/api/place/nearbysearch/json")
    fun getNearByCafes(@Query("location")location: String, @Query("radius") radius: String, @Query("type") type: String, @Query("key") apiKey: String) : Single<CafeResult>

}