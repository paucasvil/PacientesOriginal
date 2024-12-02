package com.vector.pacientscrud.ui.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vector.pacientscrud.data.implementations.ApiServiceRetrofitImp
import com.vector.pacientscrud.data.network.PacientApiFacade

class ViewModelFactory(
    private val apiFacade: PacientApiFacade
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelExample::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ViewModelExample(apiFacade) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

