package com.nth.demomapapi

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.nth.demomapapi.databinding.ActivityMapsBinding
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding:ActivityMapsBinding
    private lateinit var model:MapsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps)
        model = MapsViewModel()
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnSearch.setOnClickListener {
            val origin:String = binding.txtOrigin.text.toString().trim()
            val direction = binding.txtDestination.text.toString().trim()
            if (origin.isBlank() || direction.isBlank()){
                Toast.makeText(this, "Cần nhập đủ thông tin", Toast.LENGTH_SHORT).show()
            }else{
                model.getDirection(origin, direction)
            }
        }

        model.directionsObj.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                val point = decodePolyLine(it.routes[0].overview_polyline.points)
                binding.tvDistance.text = it.routes[0].legs[0].distance.text
                binding.tvDuration.text = it.routes[0].legs[0].duration.text
                val origin = it.routes[0].legs[0].start_location
                val locationStart = LatLng(origin.lat, origin.lng)
                val destination = it.routes[0].legs[0].end_location
                val locationEnd = LatLng(destination.lat, destination.lng)

                val markerStart = MarkerOptions().position(locationStart)
                    .title(it.routes[0].legs[0].start_address)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                val markerEnd = MarkerOptions().position(locationEnd)
                    .title(it.routes[0].legs[0].end_address)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))

                mMap.addMarker(markerStart)
                mMap.addMarker(markerEnd)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationStart, 9f))

                if (point != null) {
                    var pOption = PolylineOptions()
                    pOption.addAll(point)
                    pOption.color(Color.BLUE)
                    pOption.width(10f)
                    pOption.geodesic(true)
                    mMap.addPolyline(pOption)
                }
            }
        })

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val hvmm = LatLng(20.980978, 105.796192)
        mMap.addMarker(
            MarkerOptions().position(hvmm).title("Học Viện Kỹ Thuật Mật Mã").icon(
                BitmapDescriptorFactory.fromResource(
                    R.drawable.pushpin
                )
            )
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hvmm, 16f))
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
    }

    private fun decodePolyLine(poly: String): List<LatLng>? {
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
                b = poly[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = poly[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            decoded.add(
                LatLng(
                    lat / 100000.0, lng / 100000.0
                )
            )
        }
        return decoded
    }

}