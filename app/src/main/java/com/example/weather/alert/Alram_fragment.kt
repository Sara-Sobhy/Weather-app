package com.example.weather.alert




import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.alert.viewmodel.AlarmViewModel
import com.example.weather.alert.viewmodel.AlarmViewModelFactory
import com.example.weather.dataBase.LocalDataSource
import com.example.weather.dataBase.WeatherDatabase
import com.example.weather.dataBase.WeatherRepo
import com.example.weather.dataBase.WeatherRepoImp
import com.example.weather.databinding.FragmentAlramFragmentBinding
import com.example.weather.home.modeView.WeatherViewModel
import com.example.weather.home.modeView.WeatherViewModelFactory
import com.example.weather.network.API
import com.example.weather.network.RemoteDataSource


class AlarmFragment : Fragment() {

    lateinit var binding: FragmentAlramFragmentBinding
    lateinit var  viewModel: AlarmViewModel
    private lateinit var alarmAdapter: AlarmAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlramFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alarmAdapter = AlarmAdapter { alarm ->
            viewModel.deleteAlram(alarm)
        }

        binding.RCAlram.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = alarmAdapter
        }

        val productDao = WeatherDatabase.getInstance(requireContext().applicationContext).weatherDao()
        val alramDao = WeatherDatabase.getInstance(requireContext().applicationContext).alarmDao()
        val localDataSource = LocalDataSource(productDao,alramDao)
        val remoteDataSource = RemoteDataSource(API.retrofitService)
        val productRepo = WeatherRepoImp(remoteDataSource, localDataSource)
        val viewModelFactory = AlarmViewModelFactory(productRepo)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AlarmViewModel::class.java)

        lifecycleScope.launchWhenStarted {
            viewModel.getAllAlrams().collect { alarmList ->
                alarmAdapter.submitList(alarmList)
            }
        }
    }

}

