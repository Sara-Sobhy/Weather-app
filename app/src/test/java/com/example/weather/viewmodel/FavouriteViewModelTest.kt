package com.example.weather.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.weather.dataBase.WeatherRepo
import com.example.weather.database.FakeRepoImpl
import com.example.weather.favourit.modelView.FavouritViewModel
import com.example.weather.getOrAwaitValue
import com.example.weather.model.Alarm
import com.example.weather.model.WeatherEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class FavouriteViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var repo: WeatherRepo
    private lateinit var viewModel: FavouritViewModel
    @Before
    fun setup()
    {
        repo = FakeRepoImpl()
        viewModel = FavouritViewModel(repo)
    }
    @Test
    fun saveWeatherDataTest()= runBlocking{
        val favoriteLocation1 = WeatherEntity(1,0.0,0.0,0,"cloud","cloud",
            "cloud","assuit","egypt",0.0,0.0,0.0,0.0,0,0,
            0.0,0,0.0,0,0,0)
        viewModel.saveWeatherData(favoriteLocation1)

        val collection = listOf(favoriteLocation1)

        //When

        val result = viewModel.getAllWeather().first()

        // Then
        MatcherAssert.assertThat(result, CoreMatchers.not(CoreMatchers.nullValue()))
        MatcherAssert.assertThat(result, CoreMatchers.`is`(collection))
    }
    @Test
    fun deleteLocation_theSameLocation() = runBlocking {        //done
        //Given
        val favoriteLocation1 = WeatherEntity(1,0.0,0.0,0,"cloud","cloud",
            "cloud","assuit","egypt",0.0,0.0,0.0,0.0,0,0,
            0.0,0,0.0,0,0,0)

        val favoriteLocation2 = WeatherEntity(2,0.0,0.0,0,"cloud","cloud",
            "cloud","assuit","egypt",0.0,0.0,0.0,0.0,0,0,
            0.0,0,0.0,0,0,0)

        val favoriteLocation3 = WeatherEntity(3,0.0,0.0,0,"cloud","cloud",
            "cloud","assuit","egypt",0.0,0.0,0.0,0.0,0,0,
            0.0,0,0.0,0,0,0)

        viewModel.saveWeatherData(favoriteLocation1)
        viewModel.saveWeatherData(favoriteLocation2)
        viewModel.saveWeatherData(favoriteLocation3)

        viewModel.deleteWeatherData(favoriteLocation1)

        //When

        val result = viewModel.getAllWeather().first()

        // Then
        MatcherAssert.assertThat(result , CoreMatchers.`is`(listOf(favoriteLocation2,favoriteLocation3)))

    }
    @Test
    fun getAllFavPlaces_returnFavPlaces() = runBlockingTest {
        //Given
        val list = listOf(
            WeatherEntity(2,0.0,0.0,0,"cloud","cloud",
                "cloud","assuit","egypt",0.0,0.0,0.0,0.0,0,0,
                0.0,0,0.0,0,0,0),
            WeatherEntity(3,0.0,0.0,0,"cloud","cloud",
                "cloud","assuit","egypt",0.0,0.0,0.0,0.0,0,0,
                0.0,0,0.0,0,0,0)
        )
        val place1 =WeatherEntity(2,0.0,0.0,0,"cloud","cloud",
            "cloud","assuit","egypt",0.0,0.0,0.0,0.0,0,0,
            0.0,0,0.0,0,0,0)

        val place2 =WeatherEntity(3,0.0,0.0,0,"cloud","cloud",
            "cloud","assuit","egypt",0.0,0.0,0.0,0.0,0,0,
            0.0,0,0.0,0,0,0)

        viewModel.saveWeatherData(place1)
        viewModel.saveWeatherData(place2)

        //when

        var result = listOf<WeatherEntity>()
        result =viewModel.getAllWeather().first()
        //Return

        MatcherAssert.assertThat(result, CoreMatchers.`is`(list))
    }

}