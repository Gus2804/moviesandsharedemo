package com.gperalta.moviesandshare.ui.locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.gperalta.moviesandshare.R
import com.gperalta.moviesandshare.databinding.FragmentLocationsBinding
import com.gperalta.moviesandshare.model.Location
import com.gperalta.moviesandshare.ui.UIState
import com.gperalta.moviesandshare.utils.BitmapHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class LocationsFragment : Fragment() {

    val viewModel : LocationViewModel by activityViewModels()

    private var _binding: FragmentLocationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var deviceIcon: BitmapDescriptor

    private fun addMarkers(googleMap: GoogleMap, locations : List<Location>) {
        locations.forEach {
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .title(it.date.format())
                    .position(LatLng(it.latitude, it.longitude))
                    .icon(deviceIcon)
            )

            marker?.tag = it
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.toolbar) {
            title = getString(R.string.locations)
            setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)

            setNavigationOnClickListener {
                activity?.onBackPressed()
            }

        }


        val color = ContextCompat.getColor(context!!, R.color.teal_200)
        deviceIcon = BitmapHelper.vectorToBitmap(context!!, R.drawable.cellphone_wireless, color)

        lifecycleScope.launch {
            viewModel.locations.collect {
                when(it) {
                    is UIState.Success -> {
                        val map = childFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
                        map?.getMapAsync { googleMap ->
                            //googleMap.setInfoWindowAdapter(LocationInfoAdapter(activity!!))
                            googleMap.setOnMapLoadedCallback {
                                if(it.data?.isNotEmpty() == true) {
                                    val bounds = LatLngBounds.builder()
                                    it.data.forEach { loc -> bounds.include(loc.position) }
                                    googleMap.moveCamera(
                                        CameraUpdateFactory.newLatLngBounds(
                                            bounds.build(),
                                            20
                                        )
                                    )
                                }
                            }
                            addClusteredMarkers(googleMap, it.data?: listOf())
                            addMarkers(googleMap, it.data?: listOf())
                        }
                    }
                }
            }
        }

        viewModel.getLocations()

    }

    private fun addClusteredMarkers(googleMap: GoogleMap, locations: List<Location>) {
        val clusterManager = ClusterManager<Location>(context, googleMap)
        clusterManager.renderer =
            LocationGroupRenderer(
                context!!,
                googleMap,
                clusterManager
            )

        clusterManager.setOnClusterItemClickListener { false }
        clusterManager.markerCollection.setInfoWindowAdapter(LocationInfoAdapter(context!!))

        clusterManager.addItems(locations)
        clusterManager.cluster()

        googleMap.setOnCameraIdleListener {
            clusterManager.onCameraIdle()
        }
    }
}

fun Date.format(): String? {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return dateFormat.format(this)
}
