package com.britshbroadcast.frinder.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.britshbroadcast.frinder.model.data.Result
import com.britshbroadcast.frinder.model.network.FrinderRetrofit
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FrinderViewModel: ViewModel() {


    private val retrofit = FrinderRetrofit()

    val searchResultLiveData: MutableLiveData<List<Result>> = MutableLiveData()

    var fragmentLiveData: MutableLiveData<Result> = MutableLiveData()

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


}