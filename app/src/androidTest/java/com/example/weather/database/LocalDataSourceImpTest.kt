package com.example.weather.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.weather.dataBase.LocalDataSource
import com.example.weather.dataBase.WeatherDatabase
import com.example.weather.dataBase.WeatherRepoImp
import com.example.weather.model.Alarm
import com.example.weather.model.WeatherEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class LocalDataSourceImpTest {
    private lateinit var database: WeatherDatabase
    private lateinit var localDataSourceImp: LocalDataSource


    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule = MainRule()


    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).allowMainThreadQueries().build()
        localDataSourceImp = LocalDataSource(database.weatherDao(),database.alarmDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    //test add location ..local
    @Test
    fun addLocation_theSameLocation() = runBlockingTest {        //done
        //Given
        val favoriteLocation1 = WeatherEntity(1,0.0,0.0,0,"cloud","cloud",
            "cloud","assuit","egypt",0.0,0.0,0.0,0.0,0,0,
            0.0,0,0.0,0,0,0)
        localDataSourceImp.insertWeather(favoriteLocation1)

        val collection = listOf(favoriteLocation1)

        //When

        val result = localDataSourceImp.getAllWeather().getOrAwaitValue()

        // Then
        assertThat(result, not(nullValue()))
        assertThat(result , `is`(collection))

    }

    //test delete location ..local
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

        localDataSourceImp.insertWeather(favoriteLocation1)
        localDataSourceImp.insertWeather(favoriteLocation2)
        localDataSourceImp.insertWeather(favoriteLocation3)

        localDataSourceImp.deleteWeather(favoriteLocation1)

        //When

        val result = localDataSourceImp.getAllWeather().getOrAwaitValue()

        // Then
        assertThat(result , `is`(listOf(favoriteLocation2,favoriteLocation3)))

    }


    @Test
    fun getAllFavPlaces_returnFavPlaces() =rule.runBlockingTest {
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

        localDataSourceImp.insertWeather(place1)
        localDataSourceImp.insertWeather(place2)

        //when

        var result = listOf<WeatherEntity>()
        result = localDataSourceImp.getAllWeather().first()
        //Return

        assertThat(result, `is`(list))
    }

    //test add Alram ..local
    @Test
    fun addAlram_theSameLocation() = runBlockingTest {        //done
        //Given
        val alram1 = Alarm(1,0,true,0.0,0.0)
        localDataSourceImp.insertAlarm(alram1)

        val collection = listOf(alram1)

        //When

        val result = localDataSourceImp.getAllAlarms().getOrAwaitValue()

        // Then
        assertThat(result, not(nullValue()))
        assertThat(result , `is`(collection))

    }

    //test delete Alram ..local
    @Test
    fun deleteAlram_theSameLocation() = runBlocking {        //done
        //Given
        val alram1 = Alarm(1,0,true,0.0,0.0)
        val alram2= Alarm(2,0,true,0.0,0.0)
        val alram3 = Alarm(3,0,true,0.0,0.0)

        localDataSourceImp.insertAlarm(alram1)
        localDataSourceImp.insertAlarm(alram2)
        localDataSourceImp.insertAlarm(alram3)


        localDataSourceImp.deleteAlarm(alram1)

        val collection = listOf(alram2,alram3)

        //When

        val result = localDataSourceImp.getAllAlarms().getOrAwaitValue()

        // Then
        assertThat(result, not(nullValue()))
        assertThat(result , `is`(collection))

    }


    @Test
    fun getAllAlrams_returnFavPlaces() =rule.runBlockingTest {
        //Given
        val alram1 = Alarm(1,0,true,0.0,0.0)
        localDataSourceImp.insertAlarm(alram1)

        val alram2 = Alarm(2,0,true,0.0,0.0)
        localDataSourceImp.insertAlarm(alram2)

        val alram3 = Alarm(3,0,true,0.0,0.0)
        localDataSourceImp.insertAlarm(alram3)

        val collection = listOf(alram1,alram2,alram3)

        //When

        val result = localDataSourceImp.getAllAlarms().getOrAwaitValue()

        // Then
        assertThat(result, not(nullValue()))
        assertThat(result , `is`(collection))
    }



}