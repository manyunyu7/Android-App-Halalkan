package com.feylabs.halalkan.data.remote.reqres.resto

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.annotation.Nullable
import com.feylabs.halalkan.utils.PaginationPlaceholder
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class RestoModelResponse(
    @SerializedName("address")
    var address: String = "",
    @SerializedName("kecamatan")
    var kecamatan: String? = "",
    @SerializedName("kelurahan")
    var kelurahan: String? = "",
    @SerializedName("certification_id")
    var certificationId: Int = 0,
    @SerializedName("certification_name")
    var certificationName: String = "",
    @SerializedName("food_type_name")
    var foodTypeName: String = "",
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("image")
    var image: String = "",
    @SerializedName("is_visible")
    var isVisible: Int = 0,
    @SerializedName("lat")
    var lat: String = "",
    @SerializedName("long")
    var long: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("review_avg")
    var review_avg: Double = 0.0,
    @SerializedName("phone_number")
    var phoneNumber: String = "",
    @SerializedName("type_food_id")
    var typeFoodId: Int = 0,
    @SerializedName("updated_at")
    var updatedAt: String = "",
    @SerializedName("img_full_path")
    var image_full_path: String = "",
    @SerializedName("user_id")
    var userId: Int = 0,
    @SerializedName("is_resto_schedule_open")
    var isRestoScheduleOpen: Boolean = false,
    @SerializedName("is_claimed")
    var isClaimed: Boolean = false,
    @SerializedName("is_favorited")
    val isFavorited: Boolean = false,
    var distanceKm: String? = null,
    var distanceKmDoubleRounded: Double? = null,
    var distanceKmDouble: Double? = null,
    @SerializedName("data_bangunan")
    val data_bangunan: DataBangunan?=null,
    val ViewType: Int = PaginationPlaceholder.VNormal,
    var isFooterVisible: Boolean = true,
) : Parcelable {

    @Parcelize
    data class DataBangunan(
        @SerializedName("bukti_pbb_photo_path")
        @Expose
        val buktiPbbPhotoPath: String? = null,
        @SerializedName("bukti_pbb_tahun")
        @Expose
        val buktiPbbTahun: String? = null,
        @SerializedName("created_at")
        @Expose
        val createdAt: String? = null,
        @SerializedName("fasilitas_air")
        @Expose
        val fasilitasAir: String? = null,
        @SerializedName("fasilitas_listrik_watt")
        @Expose
        val fasilitasListrikWatt: String? = null,
        @SerializedName("fasilitas_telp")
        @Expose
        val fasilitasTelp: String? = null,
        @SerializedName("id")
        @Expose
        val id: Int? = null,
        @SerializedName("ijin_domisili")
        @Expose
        val ijinDomisili: String? = null,
        @SerializedName("is_bukti_pbb_available")
        @Expose
        val isBuktiPbbAvailable: String? = null,
        @SerializedName("is_sewa")
        @Expose
        val isSewa: String? = null,
        @SerializedName("jangka_sewa")
        @Expose
        val jangkaSewa: String? = null,
        @SerializedName("jenis_jalan")
        @Expose
        val jenisJalan: String? = null,
        @SerializedName("jenis_pemilik_sertifikat")
        @Expose
        val jenisPemilikSertifikat: String? = null,
        @SerializedName("jenis_sertifikat")
        @Expose
        val jenisSertifikat: String? = null,
        @SerializedName("kondisi_bangunan")
        @Expose
        val kondisiBangunan: String? = null,
        @SerializedName("lebar_bangunan")
        @Expose
        val lebarBangunan: String? = null,
        @SerializedName("lebar_tanah")
        @Expose
        val lebarTanah: String? = null,
        @SerializedName("masa_berlaku_sertifikat")
        @Expose
        val masaBerlakuSertifikat: String? = null,
        @SerializedName("nama_pemilik_sertifikat")
        @Expose
        val namaPemilikSertifikat: String? = null,
        @SerializedName("panjang_bangunan")
        @Expose
        val panjangBangunan: String? = null,
        @SerializedName("panjang_tanah")
        @Expose
        val panjangTanah: String? = null,
        @SerializedName("jumlah_lantai")
        @Expose
        val jumlahLantai: String? = null,
        @SerializedName("penggantian_jenis_wilayah")
        @Expose
        val penggantianJenisWilayah: String? = null,
        @SerializedName("peruntukan_bangunan")
        @Expose
        val peruntukanBangunan: String? = null,
        @SerializedName("rencana_pelebaran")
        @Expose
        val rencanaPelebaran: String? = null,
        @SerializedName("resto_id")
        @Expose
        val restoId: Int? = null,
        @SerializedName("updated_at")
        @Expose
        val updatedAt: String? = null
    ):Parcelable



    fun getOperatingHours(): String = "-"

}