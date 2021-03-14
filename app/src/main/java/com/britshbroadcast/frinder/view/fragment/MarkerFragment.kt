package com.britshbroadcast.frinder.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.britshbroadcast.frinder.R
import com.britshbroadcast.frinder.model.data.Result
import com.britshbroadcast.frinder.viewmodel.FrinderViewModel
import kotlinx.android.synthetic.main.map_marker_item_layout.*

class MarkerFragment: Fragment() {

    private val frinderViewModel by activityViewModels<FrinderViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.map_marker_item_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            val bundle: Bundle? = arguments
        if(bundle != null){
            var title: String = bundle.getString("KEY").toString()
            frinderViewModel.searchResultLiveData.value?.forEach {
                if(it.name == title){
                    item_title_textView.text = it.name
                    item_address_textView.text = it.vicinity
                }
            }

        }

    }





}