package com.feylabs.halalkan.view.resto.admin_resto

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.RemoteDataSource
import com.feylabs.halalkan.data.remote.reqres.GeneralApiResponse
import com.feylabs.halalkan.data.remote.reqres.resto.*
import com.feylabs.halalkan.data.remote.reqres.resto.food.FoodModelResponse
import com.feylabs.halalkan.data.remote.reqres.resto.operating_hour.RestoOperatingHourResponse
import com.feylabs.halalkan.data.remote.reqres.resto.update.UpdateRestoColumnResponse
import com.feylabs.halalkan.utils.Network.BASE_URL_V1
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.File


class AdminRestoViewModel(
    val ds: RemoteDataSource
) : ViewModel() {

    // for mapbox direction
    val targetLat: MutableLiveData<Double> = MutableLiveData(-99.0)
    val targetLong: MutableLiveData<Double> = MutableLiveData(-99.0)

    val currentMenuTab: MutableLiveData<Pair<Int, String>> = MutableLiveData()

    private var _certLiveData =
        MutableLiveData<QumparanResource<RestaurantCertificationResponse?>>()
    val certLiveData get() = _certLiveData

    private var _updateRestoColumn =
        MutableLiveData<QumparanResource<UpdateRestoColumnResponse?>>()
    val updateRestoColumnLiveData get() = _updateRestoColumn

    private var _foodCategoryLiveData =
        MutableLiveData<QumparanResource<FoodCategoryResponse?>>()
    val foodCategoryLiveData get() = _foodCategoryLiveData

    private var _foodByCategoryLiveData =
        MutableLiveData<QumparanResource<AllFoodByRestoResponse?>>()
    val foodByCategoryLiveData get() = _foodByCategoryLiveData

    private var _restoOperatingHourLiveData =
        MutableLiveData<QumparanResource<RestoOperatingHourResponse?>>()
    val restoOperatingHourLiveData get() = _restoOperatingHourLiveData

    private var _createEditRestoOperatingHourLiveData =
        MutableLiveData<QumparanResource<GeneralApiResponse?>>()
    val createEditRestoOperatingHourLiveData get() = _createEditRestoOperatingHourLiveData

    private var _editFoodAvailability =
        MutableLiveData<QumparanResource<GeneralApiResponse?>>()
    val editFoodAvailability get() = _editFoodAvailability

    //saved
    private var _allFoodLiveData =
        MutableLiveData<QumparanResource<AllFoodByRestoResponse?>>()
    val allFoodLiveData get() = _allFoodLiveData

    private var _foodTypeLiveData =
        MutableLiveData<QumparanResource<FoodTypeResponse?>>()
    val foodTypeLiveData get() = _foodTypeLiveData

    private var _restoFoodByCategoryLiveData =
        MutableLiveData<QumparanResource<AllFoodByRestoResponse?>>()
    val restoFoodByCategoryLiveData get() = _restoFoodByCategoryLiveData

    private var _allRestoRawLiveData =
        MutableLiveData<QumparanResource<AllRestoNoPagination?>>()
    val allRestoRawLiveData get() = _allRestoRawLiveData

    private var _myRestoLiveData =
        MutableLiveData<QumparanResource<AllRestoNoPagination?>>()
    val myRestoLiveData get() = _myRestoLiveData

    private var _detailRestoLiveData =
        MutableLiveData<QumparanResource<RestoDetailResponse?>>()
    val detailRestoLiveData get() = _detailRestoLiveData

    private var _createRestoLiveData =
        MutableLiveData<QumparanResource<ResponseBody?>>()
    val createRestoLiveData get() = _createRestoLiveData

    private var _createUpdateFoodLiveData =
        MutableLiveData<QumparanResource<GeneralApiResponse?>>()
    val createUpdateFoodLiveData get() = _createUpdateFoodLiveData

    private var _updateCommonLiveData =
        MutableLiveData<QumparanResource<GeneralApiResponse?>>()
    val updateCommonLiveData get() = _updateCommonLiveData

    private var _foodDetailLiveData =
        MutableLiveData<QumparanResource<FoodModelResponse?>>()
    val foodDetailLiveData get() = _foodDetailLiveData

    private var _createRestoFoodCategoryLiveData =
        MutableLiveData<QumparanResource<ResponseBody?>>()
    val createRestoFoodCategoryLiveData get() = _createRestoFoodCategoryLiveData

    private var _editRestoFoodCategoryLiveData =
        MutableLiveData<QumparanResource<GeneralApiResponse?>>()
    val editRestoFoodCategoryLiveData get() = _editRestoFoodCategoryLiveData

    fun addFoodCategoryForResto(
        name: String,
        restoId: String,
    ) {
        viewModelScope.launch {
            _createRestoFoodCategoryLiveData.postValue(QumparanResource.Loading())
            val builder = MultipartBody.Builder()
            builder.setType(MultipartBody.FORM)
            builder.addFormDataPart("name", name)
            val requestBody: MultipartBody = builder.build()
            try {
                val req = ds.createRestoFoodCategory(restoId, requestBody)
                req?.let {
                    if (req.isSuccessful) {
                        _createRestoFoodCategoryLiveData.postValue(QumparanResource.Success(req.body()))
                    } else {
                        var message = req.message().toString()
                        req.errorBody()?.let {
                            val jsonObj = JSONObject(it.charStream().readText())
                            message = jsonObj.getString("message")
                        }
                        _createRestoFoodCategoryLiveData.postValue(QumparanResource.Error(message))
                    }
                } ?: run {
                    _createRestoFoodCategoryLiveData.postValue(QumparanResource.Error("Null"))
                }
            } catch (e: Exception) {
                _createRestoFoodCategoryLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun editFoodCategoryForResto(
        name: String,
        restoId: String,
        categoryId: String,
    ) {
        viewModelScope.launch {
            _editRestoFoodCategoryLiveData.postValue(QumparanResource.Loading())
            try {
                val req = ds.editRestoFoodCategory(
                    id = categoryId, restoid = restoId, category_name = name
                )
                req?.let {
                    if (req.isSuccessful) {
                        _editRestoFoodCategoryLiveData.postValue(QumparanResource.Success(req.body()))
                    } else {
                        var message = req.message().toString()
                        req.errorBody()?.let {
                            val jsonObj = JSONObject(it.charStream().readText())
                            message = jsonObj.getString("message")
                        }
                        _editRestoFoodCategoryLiveData.postValue(QumparanResource.Error(message))
                    }
                }
            } catch (e: Exception) {
                _editRestoFoodCategoryLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun deleteFoodCategoryForResto(
        categoryId: String,
    ) {
        viewModelScope.launch {
            _editRestoFoodCategoryLiveData.postValue(QumparanResource.Loading())
            try {
                val req = ds.deleteRestoFoodCategory(
                    id = categoryId
                )
                req?.let {
                    if (req.isSuccessful) {
                        _editRestoFoodCategoryLiveData.postValue(QumparanResource.Success(req.body()))
                    } else {
                        var message = req.message().toString()
                        req.errorBody()?.let {
                            val jsonObj = JSONObject(it.charStream().readText())
                            message = jsonObj.getString("message")
                        }
                        _editRestoFoodCategoryLiveData.postValue(QumparanResource.Error(message))
                    }
                }
            } catch (e: Exception) {
                _editRestoFoodCategoryLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun editFoodAvailability(
        foodId: String,
        isAvailable: Int,
    ) {
        viewModelScope.launch {
            _editFoodAvailability.postValue(QumparanResource.Loading())
            try {
                val req = ds.updateFoodAvailability(
                    id = foodId,
                    isAvailable
                )
                req.let {
                    if (req.isSuccessful) {
                        _editFoodAvailability.postValue(QumparanResource.Success(req.body()))
                    } else {
                        var message = req.message().toString()
                        req.errorBody()?.let {
                            val jsonObj = JSONObject(it.charStream().readText())
                            message = jsonObj.getString("message")
                        }
                        _editFoodAvailability.postValue(QumparanResource.Error(message))
                    }
                }
            } catch (e: Exception) {
                _editFoodAvailability.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun addRestaurant(
        body: SaveRestoPayload,
        file: File
    ) {
        viewModelScope.launch {
            _createRestoLiveData.postValue(QumparanResource.Loading())
            val builder = MultipartBody.Builder()
            builder.setType(MultipartBody.FORM)
            builder.addFormDataPart("name", body.name)
            builder.addFormDataPart("type_food_id", body.typeFoodId)
            builder.addFormDataPart("certification_id", body.certificationId)
            builder.addFormDataPart("description", body.description)
            builder.addFormDataPart("phone_number", body.phoneNumber)
            builder.addFormDataPart("lat", body.lat)
            builder.addFormDataPart("long", body.long)
            builder.addFormDataPart("is_visible", "1")
            builder.addFormDataPart("address", body.address)
            builder.addFormDataPart(
                "image",
                file.name,
                RequestBody.create(("multipart/form-data").toMediaTypeOrNull(), file)
            )
            val requestBody: MultipartBody = builder.build()

            try {
                val req = ds.createResto(requestBody)
                req?.let {
                    if (req.isSuccessful) {
                        _createRestoLiveData.postValue(QumparanResource.Success(req.body()))
                    } else {
                        var message = req.message().toString()
                        req.errorBody()?.let {
                            val jsonObj = JSONObject(it.charStream().readText())
                            message = jsonObj.getString("message")
                        }
                        _createRestoLiveData.postValue(QumparanResource.Error(message))
                    }
                } ?: run {
                    _createRestoLiveData.postValue(QumparanResource.Error("Null"))
                }
            } catch (e: Exception) {
                _createRestoLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun createFood(
        typeFoodId: Int?,
        categoryId: Int?,
        restoran_id: Int?,
        description: String?,
        name: String?,
        price: Int?,
        file: File
    ) {
        viewModelScope.launch {
            _createUpdateFoodLiveData.postValue(QumparanResource.Loading())
            val fBody: RequestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            try {
                val req = ds.createRestoFood(
                    typeFoodId = typeFoodId,
                    categoryId = categoryId,
                    restoran_id = restoran_id,
                    description = description,
                    name = name,
                    price = price,
                    image = fBody
                )
                req?.let {
                    if (req.isSuccessful) {
                        _createUpdateFoodLiveData.postValue(QumparanResource.Success(req.body()))
                    } else {
                        var message = req.message().toString()
                        req.errorBody()?.let {
                            val jsonObj = JSONObject(it.charStream().readText())
                            message = jsonObj.getString("message")
                        }
                        _createUpdateFoodLiveData.postValue(QumparanResource.Error(message))
                    }
                } ?: run {
                    _createUpdateFoodLiveData.postValue(QumparanResource.Error("Null"))
                }
            } catch (e: Exception) {
                _createUpdateFoodLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun updateFood(
        foodId: String,
        typeFoodId: Int,
        categoryId: Int,
        restoran_id: Int,
        description: String,
        name: String,
        price: Int,
        file: File?
    ) {
        viewModelScope.launch {
            _createUpdateFoodLiveData.postValue(QumparanResource.Loading())
            val fBody: RequestBody? = file?.asRequestBody("image/jpeg".toMediaTypeOrNull())
            try {
                val req = ds.editRestoFood(
                    foodId = foodId,
                    typeFoodId = typeFoodId,
                    categoryId = categoryId,
                    restoran_id = restoran_id,
                    description = description,
                    name = name,
                    price = price,
                    image = fBody
                )
                req?.let {
                    if (req.isSuccessful) {
                        _createUpdateFoodLiveData.postValue(QumparanResource.Success(req.body()))
                    } else {
                        var message = req.message().toString()
                        req.errorBody()?.let {
                            val jsonObj = JSONObject(it.charStream().readText())
                            message = jsonObj.getString("message")
                        }
                        _createUpdateFoodLiveData.postValue(QumparanResource.Error(message))
                    }
                } ?: run {
                    _createUpdateFoodLiveData.postValue(QumparanResource.Error("Null"))
                }
            } catch (e: Exception) {
                _createUpdateFoodLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getMyResto() {
        _myRestoLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getMyResto()
                if (res.isSuccessful) {
                    _myRestoLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _myRestoLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _myRestoLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun updateRestoLuasTanahBangunan(
        idResto: String,
        lt: String,
        lb: String,
        pt: String,
        pb: String,
        jumlahLantai: String,
        bisaDimajukan: String,
        ijinDomisiliAda: String,
        peruntukanBangunan: String,
    ) {
        AndroidNetworking.post(BASE_URL_V1 + "fe/restoran/$idResto/update/lt-lb")
            .addBodyParameter("idResto", idResto)
            .addBodyParameter("lebar_tanah", lt)
            .addBodyParameter("panjang_tanah", pt)
            .addBodyParameter("lebar_bangunan", lb)
            .addBodyParameter("panjang_bangunan", pb)
            .addBodyParameter("ijin_domisili", ijinDomisiliAda)
            .addBodyParameter("bisa_dimajukan", bisaDimajukan)
            .addBodyParameter("jumlah_lantai", jumlahLantai)
            .addBodyParameter("peruntukan_bangunan", peruntukanBangunan)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response?.getBoolean("status") == true) {
                        _updateCommonLiveData.postValue(
                            QumparanResource.Success(
                                GeneralApiResponse(
                                    message = ""
                                )
                            )
                        )
                    } else {
                        _updateCommonLiveData.postValue(QumparanResource.Error(response.toString()))
                    }
                }

                override fun onError(anError: ANError?) {
                    _updateCommonLiveData.postValue(QumparanResource.Error(message = anError.toString()))
                }
            })
    }

    fun updateListrikAir(
        idResto: String,
        fasilitasListrikWatt: String,
        fasilitasAir: String,
        saluranAir: String,
    ) {
        AndroidNetworking.post(BASE_URL_V1 + "fe/restoran/$idResto/update/listrik-air")
            .addBodyParameter("fasilitas_air", fasilitasAir)
            .addBodyParameter("saluran_air", saluranAir)
            .addBodyParameter("fasilitas_listrik_watt", fasilitasListrikWatt)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response?.getBoolean("status") == true) {
                        _updateCommonLiveData.postValue(
                            QumparanResource.Success(
                                GeneralApiResponse(
                                    message = ""
                                )
                            )
                        )
                    } else {
                        _updateCommonLiveData.postValue(QumparanResource.Error(response.toString()))
                    }
                }

                override fun onError(anError: ANError?) {
                    _updateCommonLiveData.postValue(QumparanResource.Error(message = anError.toString()))
                }
            })
    }

    fun updateKeadaanLingkungan(
        idResto: String,
        alfamart: String,
        indomart: String,
        spbu: String,
        univ: String,
        counterUsahaLain: String,
        masjid: String,
        gereja: String,
        sekolah: String,
        bengkel: String,
    ) {
        AndroidNetworking.post(BASE_URL_V1 + "fe/restoran/$idResto/update/info-lingkungan")
            .addBodyParameter("is_alfamart_100_exist", alfamart)
            .addBodyParameter("is_indomaret_100_exist", indomart)
            .addBodyParameter("is_spbu_100_exist",spbu)
            .addBodyParameter("is_univ_100_exist", univ)
            .addBodyParameter("is_counter_usaha_lain_100_exist", counterUsahaLain)
            .addBodyParameter("is_masjid_100_exist", masjid)
            .addBodyParameter("is_gereja_100_exist", gereja)
            .addBodyParameter("is_sekolah_100_exist", sekolah)
            .addBodyParameter("is_bengkel_100_exist", bengkel)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response?.getBoolean("status") == true) {
                        _updateCommonLiveData.postValue(
                            QumparanResource.Success(
                                GeneralApiResponse(
                                    message = ""
                                )
                            )
                        )
                    } else {
                        _updateCommonLiveData.postValue(QumparanResource.Error(response.toString()))
                    }
                }

                override fun onError(anError: ANError?) {
                    _updateCommonLiveData.postValue(QumparanResource.Error(message = anError.toString()))
                }
            })
    }

    fun updateLegal(
        idResto: String,
        namaPemilikSertifikat: String,
        jenisPemilikSertifikat: String,
        jenisSertifikat: String,
        jenisSewa: String,
        hargaSewa: String,
        masaBerlaku: String
    ) {
        AndroidNetworking.post(BASE_URL_V1 + "fe/restoran/$idResto/update/legal")
            .addBodyParameter("nama_pemilik_sertifikat", namaPemilikSertifikat)
            .addBodyParameter("jenis_sertifikat", jenisSertifikat)
            .addBodyParameter("jenis_pemilik_sertifikat", jenisPemilikSertifikat)
            .addBodyParameter("is_sewa", jenisSewa)
            .addBodyParameter("harga_sewa", hargaSewa)
            .addBodyParameter("masa_berlaku_sertifikat", masaBerlaku)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response?.getBoolean("status") == true) {
                        _updateCommonLiveData.postValue(
                            QumparanResource.Success(
                                GeneralApiResponse(
                                    message = ""
                                )
                            )
                        )
                    } else {
                        _updateCommonLiveData.postValue(QumparanResource.Error(response.toString()))
                    }
                }

                override fun onError(anError: ANError?) {
                    _updateCommonLiveData.postValue(QumparanResource.Error(message = anError.toString()))
                }
            })
    }


    fun updateRestoLalinAndParkir(
        idResto: String,
        fiveMinuteBusTruk: String,
        fiveMinuteMotor: String,
        fiveMinuteMobil: String,
        parkirMotor: String,
        parkirMobil: String,
        jenisJalan: String,
        rencanaPelebaranJalan: String,
    ) {
        AndroidNetworking.post(BASE_URL_V1 + "fe/restoran/$idResto/update/lalin-parkir")
            .addBodyParameter("parkir_mobil", parkirMobil)
            .addBodyParameter("parkir_motor", parkirMotor)
            .addBodyParameter("5_menit_motor", fiveMinuteMotor)
            .addBodyParameter("5_menit_mobil", fiveMinuteMobil)
            .addBodyParameter("5_menit_truk", fiveMinuteBusTruk)
            .addBodyParameter("rencana_pelebaran", rencanaPelebaranJalan)
            .addBodyParameter("jenis_jalan", jenisJalan)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response?.getBoolean("status") == true) {
                        _updateCommonLiveData.postValue(
                            QumparanResource.Success(
                                GeneralApiResponse(
                                    message = ""
                                )
                            )
                        )
                    } else {
                        _updateCommonLiveData.postValue(QumparanResource.Error(response.toString()))
                    }
                }

                override fun onError(anError: ANError?) {
                    _updateCommonLiveData.postValue(QumparanResource.Error(message = anError.toString()))
                }
            })
    }


    fun updateRestoAddress(
        restoId: String,
        lat: Double,
        long: Double,
        address: String,
        kelurahan: String,
        kecamatan: String
    ) {
        _updateCommonLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.updateRestoAddress(
                    restoId,
                    lat = lat,
                    long = long,
                    address = address,
                    kecamatan = kecamatan,
                    kelurahan = kelurahan
                )
                if (res.isSuccessful) {
                    _updateCommonLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _updateCommonLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _updateCommonLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getAllRestoRaw() {
        _allRestoRawLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getRestoAll()
                if (res.isSuccessful) {
                    _allRestoRawLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _allRestoRawLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _allRestoRawLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getRestoFoodByCategory(restoId: String, categoryId: String) {
        _restoFoodByCategoryLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getRestoFoodByCommonCategory(restoId, categoryId)
                if (res.isSuccessful) {
                    _restoFoodByCategoryLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _restoFoodByCategoryLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _restoFoodByCategoryLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getDetailResto(id: String) {
        _detailRestoLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getRestoDetail(id)
                if (res.isSuccessful) {
                    _detailRestoLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _detailRestoLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _detailRestoLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getRestoCert() {
        _certLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getRestoCert()
                if (res.isSuccessful) {
                    _certLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _certLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _certLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun updateRestoColumn(restoId: String, name: String, updatepath: String, columnValue: String) {
        viewModelScope.launch {
            _updateRestoColumn.postValue(QumparanResource.Loading())
            val builder = MultipartBody.Builder()
            builder.setType(MultipartBody.FORM)
            builder.addFormDataPart(name, columnValue)
            val requestBody: MultipartBody = builder.build()
            try {
                val req = ds.updateRestoColumn(
                    id = restoId, pathupdate = updatepath, body = requestBody
                )
                req?.let {
                    if (req.isSuccessful) {
                        _updateRestoColumn.postValue(QumparanResource.Success(req.body()))
                    } else {
                        var message = req.message().toString()
                        req.errorBody()?.let {
                            val jsonObj = JSONObject(it.charStream().readText())
                            message = jsonObj.getString("message")
                        }
                        _updateRestoColumn.postValue(QumparanResource.Error(message))
                    }
                } ?: run {
                    _updateRestoColumn.postValue(QumparanResource.Error("Null"))
                }
            } catch (e: Exception) {
                _updateRestoColumn.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getFoodType() {
        _foodTypeLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getFoodType()
                if (res.isSuccessful) {
                    _foodTypeLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _foodTypeLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _foodTypeLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getFoodDetail(foodId: String) {
        _foodDetailLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getFoodDetail(foodId)
                if (res.isSuccessful) {
                    _foodDetailLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _foodDetailLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _foodDetailLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun deleteFood(foodId: String) {
        _createUpdateFoodLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.deleteFood(foodId)
                if (res.isSuccessful) {
                    _createUpdateFoodLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _createUpdateFoodLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _createUpdateFoodLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getFoodCategoryOnResto(restoId: String) {
        _foodCategoryLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getFoodCategoryOnResto(restoId)
                if (res.isSuccessful) {
                    _foodCategoryLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _foodCategoryLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _foodCategoryLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getAllFoodByResto(restoId: String) {
        viewModelScope.launch {
            try {
                val res = ds.getAllFoodByResto(restoId)
                if (res.isSuccessful) {
                    _allFoodLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _allFoodLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _allFoodLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getRestoOperatingHour(restoId: String) {
        viewModelScope.launch {
            try {
                val res = ds.getRestoOperatingHour(restoId)
                if (res.isSuccessful) {
                    _restoOperatingHourLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _restoOperatingHourLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _restoOperatingHourLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun createRestoOperatingHour(restoId: String, dayCode: Int, start: String, end: String) {
        viewModelScope.launch {
            _createEditRestoOperatingHourLiveData.postValue(QumparanResource.Loading())
            try {
                val res = ds.createRestoOperatingHour(
                    restoId,
                    startHour = start,
                    endHour = end,
                    dayCode = dayCode
                )
                if (res.isSuccessful) {
                    _createEditRestoOperatingHourLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    var message = res.message().toString()
                    res.errorBody()?.let {
                        val jsonObj = JSONObject(it.charStream().readText())
                        message = jsonObj.getString("message")
                    }
                    _createEditRestoOperatingHourLiveData.postValue(QumparanResource.Error(message))
                }
            } catch (e: Exception) {
                _createEditRestoOperatingHourLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun updateRestoOperatingHour(
        hourId: String,
        restoId: String,
        dayCode: Int,
        start: String,
        end: String
    ) {
        viewModelScope.launch {
            _createEditRestoOperatingHourLiveData.postValue(QumparanResource.Loading())
            try {
                val res = ds.updateRestoOperatingHour(
                    hourId = hourId,
                    restoId = restoId,
                    startHour = start,
                    endHour = end,
                    dayCode = dayCode
                )
                if (res.isSuccessful) {
                    _createEditRestoOperatingHourLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    var message = res.message().toString()
                    res.errorBody()?.let {
                        val jsonObj = JSONObject(it.charStream().readText())
                        message = jsonObj.getString("message")
                    }
                    _createEditRestoOperatingHourLiveData.postValue(QumparanResource.Error(message))
                }
            } catch (e: Exception) {
                _createEditRestoOperatingHourLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun deleteRestoOperatingHour(hourId: String, restoId: String) {
        viewModelScope.launch {
            _createEditRestoOperatingHourLiveData.postValue(QumparanResource.Loading())
            try {
                val res = ds.deleteRestoOperatingHour(
                    hourId = hourId,
                    restoId = restoId,
                )
                if (res.isSuccessful) {
                    _createEditRestoOperatingHourLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    var message = res.message().toString()
                    res.errorBody()?.let {
                        val jsonObj = JSONObject(it.charStream().readText())
                        message = jsonObj.getString("message")
                    }
                    _createEditRestoOperatingHourLiveData.postValue(QumparanResource.Error(message))
                }
            } catch (e: Exception) {
                _createEditRestoOperatingHourLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun fireEditRestoFoodCategory() {
        _editRestoFoodCategoryLiveData.postValue(QumparanResource.Default())
    }


}