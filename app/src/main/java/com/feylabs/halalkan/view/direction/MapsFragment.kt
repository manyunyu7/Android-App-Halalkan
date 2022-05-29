package com.feylabs.halalkan.view.direction

import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.feylabs.halalkan.MainViewModel
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.utils.Network
import com.feylabs.halalkan.utils.base.BaseFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import org.json.JSONArray
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class MapsFragment : BaseFragment() {

    val mainViewModel: MainViewModel by sharedViewModel()
    val viewModel: DirectionViewModel by viewModel()

   private lateinit var googleMap : GoogleMap

    private val callback = OnMapReadyCallback { googleMapInstance ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
//        val sydney = LatLng(-34.0, 151.0)
        googleMap = googleMapInstance
//        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        viewModel.getDirection(
            origin = "-6.9218571, 107.6048254",
            destination = "-6.9249233, 107.6345122",
            key = Network.MAP_API
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun initUI() {

    }

    override fun initObserver() {
        viewModel.directionLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Success -> {
                    val routes = it.data!!
                    var points: ArrayList<LatLng?>
                    var polylineOptions: PolylineOptions? = null

                    for (i in 0 until routes.length()) {
                        points = ArrayList()
                        polylineOptions = PolylineOptions()
                        val legs: JSONArray = routes.getJSONObject(i).getJSONArray("legs")
                        for (j in 0 until legs.length()) {
                            val steps = legs.getJSONObject(j).getJSONArray("steps")
                            for (k in 0 until steps.length()) {
                                val polyline = steps.getJSONObject(k).getJSONObject("polyline")
                                    .getString("points")
                                val list: List<LatLng> = decodePolyLines(polyline)
                                for (l in list.indices) {
                                    val position = LatLng(list[l].latitude, list[l].longitude)
                                    points.add(position)
                                }
                            }
                        }
                        polylineOptions.addAll(points)
                        polylineOptions.width(10f)
                        polylineOptions.color(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.purple_500
                            )
                        )
                        polylineOptions.geodesic(true)

                        googleMap.addPolyline(polylineOptions)
                        googleMap.addMarker(
                            MarkerOptions().position(LatLng(-6.9249233, 107.6345122))
                                .title("Marker 1")
                        )
                        googleMap.addMarker(
                            MarkerOptions().position(LatLng(-6.9218571, 107.6048254))
                                .title("Marker 1")
                        )

                        val bounds = LatLngBounds.Builder()
                            .include(LatLng(-6.9249233, 107.6345122))
                            .include(LatLng(-6.9218571, 107.6048254)).build()
                        val point = Point()
                        requireActivity().windowManager.defaultDisplay.getSize(point)
                        googleMap.animateCamera(
                            CameraUpdateFactory.newLatLngBounds(
                                bounds,
                                point.x,
                                150,
                                30
                            )
                        )
                    }
                }
                else -> {
                    showToast(it.message.toString())
                }
            }
        }
    }

    override fun initAction() {
    }

    override fun initData() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    fun decodePolyLines(poly: String): List<LatLng> {
        val len = poly.length
        var index = 0
        val decoded: MutableList<LatLng> = ArrayList()
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = poly[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = poly[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            decoded.add(
                LatLng(
                    lat / 100000.0,
                    lng / 100000.0
                )
            )
        }
        return decoded
    }
}