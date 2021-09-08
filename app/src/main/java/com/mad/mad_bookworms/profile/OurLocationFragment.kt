package com.mad.mad_bookworms.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.databinding.FragmentOurLocationBinding
import com.mad.mad_bookworms.databinding.FragmentProfileBinding

class OurLocationFragment : Fragment() , OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentOurLocationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_our_location, container, false)
        // Inflate the layout for this fragment
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment =  childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return binding.root

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        // Add a marker in Sydney and move the camera
        val pavillion = LatLng(3.149102188343638, 101.71328688413611)
        val midvalley = LatLng(3.1176376263288392, 101.6780759548061)
        mMap.addMarker(
            MarkerOptions()
                .position(pavillion)
                .title("Booksworms in Pavillion")
        )
        mMap.addMarker(
            MarkerOptions()
                .position(midvalley)
                .title("Bookworms in Mid-Valley")
        )
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pavillion, 12f))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(midvalley, 12f))
    }

}