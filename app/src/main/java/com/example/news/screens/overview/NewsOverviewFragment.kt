package com.example.news.screens.overview

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.news.R
import com.example.news.databinding.FragmentNewsOverviewBinding
import com.example.news.network.weather.WeatherItem
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class NewsOverviewFragment : Fragment() {

    private lateinit var binding: FragmentNewsOverviewBinding
    private lateinit var adapter: NewsGridAdapter
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var csource: CancellationTokenSource
    private var location = MutableLiveData<Location?>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        csource = CancellationTokenSource()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_news_overview, container, false)

        binding.lifecycleOwner = this

        val app = requireNotNull(activity).application
        val factory = NewsOverviewViewModelFactory(app)

        val viewModel = ViewModelProvider(this, factory)[NewsOverviewViewModel::class.java]

        binding.viewModel = viewModel

        adapter = NewsGridAdapter(NewsListener {
            viewModel.newsClicked(it)
        })
        binding.newsList.adapter = adapter

        viewModel.navigateToWeb.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(
                    NewsOverviewFragmentDirections.actionNewsOverviewFragmentToNewsDetailFragment(
                        it
                    )
                )
                viewModel.newsClickedComplete()
            }
        })
        viewModel.response.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                adapter.data = it
            }
        })

        viewModel.searchText.observe(viewLifecycleOwner, Observer {
            if (viewModel.response.value != null) {
                adapter.data = viewModel.filterText(it)!!
            }
        })
        viewModel.weatherResponse.observe(viewLifecycleOwner, Observer {
            if(it!=null){
                showWeather(it)
            }
        })

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        checkAndRequestLocationPermission()

        location.observe(viewLifecycleOwner, Observer{
            if(it!=null){
                viewModel.getWeatherDetailProperty(it)
            }
        })

        binding.toLocation.setOnClickListener{
            checkAndRequestLocationPermission()
        }

        return binding.root
    }

    private fun showWeather(weather: WeatherItem?) {
        binding.weather = weather
        binding.weatherImage.visibility = View.VISIBLE
        binding.weatherDescription.visibility = View.VISIBLE
        binding.cityText.visibility =View.VISIBLE
        binding.degree.visibility =View.VISIBLE
        binding.toLocation.visibility = View.GONE
    }

    private fun checkAndRequestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if(it!=null) {
                    location.value = it
                }else {
                    fusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY,csource.token).addOnSuccessListener{ current ->
                        if(current != null){
                            location.value = current
                        }
                        else Toast.makeText(requireContext(), "Please enable location", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else if(ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )){
            AlertDialog.Builder(ContextThemeWrapper(requireActivity(),
                R.style.AlertDialogCustom)).setMessage("To display weather at your current location please give permission.")
                .setTitle("Location Permission Required")
                .setCancelable(true)
                .setPositiveButton("OK",DialogInterface.OnClickListener(){
                    dialog, i ->
                    ActivityCompat.requestPermissions(requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                        LOCATION_PERMISSION_REQUEST_CODE)
                    dialog.dismiss()
                })
                .setNegativeButton("Cancel",DialogInterface.OnClickListener(){
                    dialog, i ->
                    dialog.cancel()
                })
                .show()
        }else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }
}