package com.britshbroadcast.frinder.viewmodel

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Ignore
import androidx.room.Room
import com.britshbroadcast.frinder.model.data.Response
import com.britshbroadcast.frinder.model.data.Result
import com.britshbroadcast.frinder.model.db.AppDataBase
import com.britshbroadcast.frinder.model.network.FrinderRetrofit
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FrinderViewModel(applpication: Application): AndroidViewModel(applpication) {


    private val retrofit = FrinderRetrofit()

    val searchResultLiveData: MutableLiveData<List<Result>> = MutableLiveData()

    private val db = Room.databaseBuilder(
        applpication.applicationContext,
        AppDataBase::class.java,
        "response.db"
    ).build()
    private var dbIndex: Int = 0

    private val compositeDisposable = CompositeDisposable()

    fun getCafeResult(location: String,radius: String, type: String){
        // Composite Disposable

        compositeDisposable.add(
            retrofit.getNearByCafes(location,radius , type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map {
                    it.results
                }
                .subscribe({
//                    dbIndex++
                    Thread{
                        db.responseDao().insertResponseItem(
                            Response(it.toString())
                        )

                    }.start()

                    searchResultLiveData.postValue(it)
                    compositeDisposable.clear()
                }, {
                    Log.d("TAG_J", it.localizedMessage)
                })
        )





//        retrofit.getNearByCafes(location, type).enqueue(object : Callback<CafeResult> {
//            override fun onResponse(call: Call<CafeResult>, response: Response<CafeResult>) {
//                response.body()?.let {
//                    liveData.value = it.results
//                }
//            }
//
//            override fun onFailure(call: Call<CafeResult>, t: Throwable) {
//                Log.d("TAGJ", t.localizedMessage)
//            }
//        })
    }


    fun getAllResponseFromDB(){
        Log.d("TAGJ", db.responseDao().getAllResponses().toString())
    }

}