package com.example.weather.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.weather.alert.viewmodel.AlarmViewModel
import com.example.weather.dataBase.WeatherRepo
import com.example.weather.database.FakeRepoImpl
import com.example.weather.favourit.modelView.FavouritViewModel
import com.example.weather.getOrAwaitValue
import com.example.weather.model.Alarm
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
class AlarmViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var repo: WeatherRepo
    private lateinit var viewModel: AlarmViewModel
    @Before
    fun setup()
    {
        repo = FakeRepoImpl()
        viewModel = AlarmViewModel(repo)
    }
    @Test
    fun addAlram_theSameLocation() = runBlockingTest {        //done
        //Given
        val alram1 = Alarm(1,0,true,0.0,0.0)
        viewModel.insertAlram(alram1)

        val collection = listOf(alram1)

        //When

        val result = viewModel.getAllAlrams().getOrAwaitValue()

        // Then
        MatcherAssert.assertThat(result, CoreMatchers.not(CoreMatchers.nullValue()))
        MatcherAssert.assertThat(result, CoreMatchers.`is`(collection))

    }
    @Test
    fun deleteAlram_theSameLocation() = runBlocking {        //done
        //Given
        val alram1 = Alarm(1,0,true,0.0,0.0)
        val alram2= Alarm(2,0,true,0.0,0.0)
        val alram3 = Alarm(3,0,true,0.0,0.0)

        viewModel.insertAlram(alram1)
        viewModel.insertAlram(alram2)
        viewModel.insertAlram(alram3)


        viewModel.deleteAlram(alram1)

        val collection = listOf(alram2,alram3)
        //When
        val result = viewModel.getAllAlrams().getOrAwaitValue()
        // Then
        MatcherAssert.assertThat(result, CoreMatchers.not(CoreMatchers.nullValue()))
        MatcherAssert.assertThat(result, CoreMatchers.`is`(collection))

    }
    @Test
    fun getAllAlrams_returnFavPlaces() = runBlockingTest {
        //Given
        val alram1 = Alarm(1,0,true,0.0,0.0)
        viewModel.insertAlram(alram1)

        val alram2 = Alarm(2,0,true,0.0,0.0)
        viewModel.insertAlram(alram2)

        val alram3 = Alarm(3,0,true,0.0,0.0)
        viewModel.insertAlram(alram3)

        val collection = listOf(alram1,alram2,alram3)

        //When

        val result = viewModel.getAllAlrams().getOrAwaitValue()

        // Then
        MatcherAssert.assertThat(result, CoreMatchers.not(CoreMatchers.nullValue()))
        MatcherAssert.assertThat(result, CoreMatchers.`is`(collection))
    }
}