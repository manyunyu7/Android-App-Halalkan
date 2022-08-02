package com.feylabs.halalkan.view.products

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.halalkan.data.remote.MuskoResource
import com.feylabs.halalkan.data.remote.RemoteDataSource
import com.feylabs.halalkan.data.remote.reqres.product.ProductCateogryResponse
import com.feylabs.halalkan.data.remote.reqres.resto.RestaurantCertificationResponse
import kotlinx.coroutines.launch

class ProductViewModel(
    val ds: RemoteDataSource
    ) : ViewModel() {

    private var _ProductCategoryLiveData =
        MutableLiveData<MuskoResource<ProductCateogryResponse?>>()
    val ProductCategoryLiveData get() = _ProductCategoryLiveData

    fun getProductCategory() {
        _ProductCategoryLiveData.postValue(MuskoResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getProductCategory()
                if (res.isSuccessful) {
                    _ProductCategoryLiveData.postValue(MuskoResource.Success(res.body()))
                } else {
                    _ProductCategoryLiveData.postValue(
                        MuskoResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _ProductCategoryLiveData.postValue(MuskoResource.Error(e.message.toString()))
            }
        }
    }

}