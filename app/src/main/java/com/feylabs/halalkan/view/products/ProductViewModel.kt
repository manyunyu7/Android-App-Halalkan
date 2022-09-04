package com.feylabs.halalkan.view.products

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.RemoteDataSource
import com.feylabs.halalkan.data.remote.reqres.GeneralApiResponse
import com.feylabs.halalkan.data.remote.reqres.order.CreateCartPayload
import com.feylabs.halalkan.data.remote.reqres.order.CreateCartResponse
import com.feylabs.halalkan.data.remote.reqres.order.DetailOrderResponse
import com.feylabs.halalkan.data.remote.reqres.order.OrderStatusResponse
import com.feylabs.halalkan.data.remote.reqres.order.history.OrderHistoryResponse
import com.feylabs.halalkan.data.remote.reqres.order.resto.OrderByRestoPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.product.ProductCategoryResponse
import com.feylabs.halalkan.data.remote.reqres.product.ProductDetailResponse
import com.feylabs.halalkan.data.remote.reqres.product.ProductListPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.product.SearchProductResponse
import com.feylabs.halalkan.data.remote.reqres.resto.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.http.Query
import java.io.File

class ProductViewModel(
    val ds: RemoteDataSource
) : ViewModel() {


    private var currentFilterStatus = MutableLiveData("")

    private var _productCategoryLiveData =
        MutableLiveData<QumparanResource<ProductCategoryResponse?>>()
    val productCategoryLiveData get() = _productCategoryLiveData

    private var _productDetailLiveData =
        MutableLiveData<QumparanResource<ProductDetailResponse?>>()
    val productDetailLiveData get() = _productDetailLiveData

    private var _searchProductLiveData =
        MutableLiveData<QumparanResource<SearchProductResponse?>>()
    val searchProductLiveData get() = _searchProductLiveData

    private var _productsLiveData =
        MutableLiveData<QumparanResource<ProductListPaginationResponse?>>()
    val productsLiveData get() = _productsLiveData

    fun getProductCategory() {
        viewModelScope.launch {
            _productCategoryLiveData.postValue(QumparanResource.Loading())
            try {
                val res = ds.getAllProductCategory()
                if (res.isSuccessful) {
                    _productCategoryLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    var message = res.message().toString()
                    res.errorBody()?.let {
                        val jsonObj = JSONObject(it.charStream().readText())
                        message = jsonObj.getString("message")
                    }
                    _productCategoryLiveData.postValue(QumparanResource.Error(message))
                }
            } catch (e: Exception) {
                _productCategoryLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun searchProduct(name: String, category: String? = null) {
        viewModelScope.launch {
            _searchProductLiveData.postValue(QumparanResource.Loading())
            try {
                val res = ds.searchProduct(categoryId = category?.toIntOrNull(), name)
                if (res.isSuccessful) {
                    _searchProductLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    var message = res.message().toString()
                    res.errorBody()?.let {
                        val jsonObj = JSONObject(it.charStream().readText())
                        message = jsonObj.getString("message")
                    }
                    _searchProductLiveData.postValue(QumparanResource.Error(message))
                }
            } catch (e: Exception) {
                _searchProductLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }


    fun getProductDetail(productId: String) {
        viewModelScope.launch {
            _productDetailLiveData.postValue(QumparanResource.Loading())
            try {
                val res = ds.getProductDetail(productId)
                if (res.isSuccessful) {
                    _productDetailLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    var message = res.message().toString()
                    res.errorBody()?.let {
                        val jsonObj = JSONObject(it.charStream().readText())
                        message = jsonObj.getString("message")
                    }
                    _productDetailLiveData.postValue(QumparanResource.Error(message))
                }
            } catch (e: Exception) {
                _productDetailLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }


    fun getProductOnCategory(
        perPage: Int = 5,
        page: Int = 1,
        categoryId: String
    ) {
        viewModelScope.launch {
            _productsLiveData.postValue(QumparanResource.Loading())
            try {
                val res = ds.getProductOnCategory(categoryId, page = page, perPage = perPage)
                if (res.isSuccessful) {
                    _productsLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    var message = res.message().toString()
                    res.errorBody()?.let {
                        val jsonObj = JSONObject(it.charStream().readText())
                        message = jsonObj.getString("message")
                    }
                    _productsLiveData.postValue(QumparanResource.Error(message))
                }
            } catch (e: Exception) {
                _productsLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getCurrentFilterStatus(): String {
        return currentFilterStatus.value.orEmpty()
    }


}