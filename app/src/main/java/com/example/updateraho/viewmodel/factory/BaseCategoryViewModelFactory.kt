package com.example.updateraho.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.updateraho.data.Category
import com.example.updateraho.viewmodel.CategoryViewModel
import com.google.firebase.firestore.FirebaseFirestore

class BaseCategoryViewModelFactory (
    private val firestore: FirebaseFirestore,
    private val category: Category
): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryViewModel(firestore,category)as T
    }

}