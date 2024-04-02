package com.example.weather.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weather.dataBase.WeatherDao
import com.example.weather.dataBase.WeatherDatabase
import com.example.weather.model.WeatherEntity
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue

@SmallTest
@RunWith(AndroidJUnit4::class)
class WeatherDaoTest {
    lateinit var database: WeatherDatabase
    lateinit var dao :WeatherDao


    @get:Rule
    val rule = InstantTaskExecutorRule()
    @Before
    fun setUp(){
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            WeatherDatabase::class.java
        ).build()
        dao = database.weatherDao()
    }

    @After
    fun tearDown(){
        database.close()
    }

    //test getall in fav
    @Test
    fun getAllLocations_locationWith_theSameLocation() = runBlocking {

        val favoriteLocation1 = WeatherEntity(1,0.0,0.0,0,"cloud","cloud",
            "cloud","assuit","egypt",0.0,0.0,0.0,0.0,0,0,
            0.0,0,0.0,0,0,0)

        dao.insertWeather(favoriteLocation1)

        val favoriteLocation2 = WeatherEntity(2,0.0,0.0,0,"cloud","cloud",
            "cloud","assuit","egypt",0.0,0.0,0.0,0.0,0,0,
            0.0,0,0.0,0,0,0)
        dao.insertWeather(favoriteLocation2)

        val favoriteLocation3 = WeatherEntity(3,0.0,0.0,0,"cloud","cloud",
            "cloud","assuit","egypt",0.0,0.0,0.0,0.0,0,0,
            0.0,0,0.0,0,0,0)

        dao.insertWeather(favoriteLocation3)

        val result = dao.getAllWeather().getOrAwaitValue()

        assertThat(result, not(nullValue()))
        val collection = listOf(favoriteLocation1,favoriteLocation2, favoriteLocation3)
        assertThat(result , `is`(collection))

    }

    //test insert
@Test
fun insert_locationWith_theSameLocation() = runBlocking {

    val favoriteLocation1 = WeatherEntity(1,0.0,0.0,0,"cloud","cloud",
        "cloud","assuit","egypt",0.0,0.0,0.0,0.0,0,0,
        0.0,0,0.0,0,0,0)

    dao.insertWeather(favoriteLocation1)

    val favoriteLocation2 = WeatherEntity(2,0.0,0.0,0,"cloud","cloud",
        "cloud","assuit","egypt",0.0,0.0,0.0,0.0,0,0,
        0.0,0,0.0,0,0,0)
    dao.insertWeather(favoriteLocation2)

    val favoriteLocation3 = WeatherEntity(3,0.0,0.0,0,"cloud","cloud",
        "cloud","assuit","egypt",0.0,0.0,0.0,0.0,0,0,
        0.0,0,0.0,0,0,0)

    dao.insertWeather(favoriteLocation3)

    val result = dao.getAllWeather().getOrAwaitValue()

    assertThat(result, not(nullValue()))
    val collection = listOf(favoriteLocation1,favoriteLocation2, favoriteLocation3)
    assertThat(result , `is`(collection))

  }

    //test delete
    @Test
    fun delete_locationWith_theSameLocation() = runBlocking {

        val favoriteLocation1 = WeatherEntity(1,0.0,0.0,0,"cloud","cloud",
            "cloud","assuit","egypt",0.0,0.0,0.0,0.0,0,0,
            0.0,0,0.0,0,0,0)

        dao.insertWeather(favoriteLocation1)

        val favoriteLocation2 = WeatherEntity(2,0.0,0.0,0,"cloud","cloud",
            "cloud","assuit","egypt",0.0,0.0,0.0,0.0,0,0,
            0.0,0,0.0,0,0,0)
        dao.insertWeather(favoriteLocation2)

        val favoriteLocation3 = WeatherEntity(3,0.0,0.0,0,"cloud","cloud",
            "cloud","assuit","egypt",0.0,0.0,0.0,0.0,0,0,
            0.0,0,0.0,0,0,0)

        dao.insertWeather(favoriteLocation3)

        dao.deleteWeather(favoriteLocation1)

        val result = dao.getAllWeather().getOrAwaitValue()

        assertThat(result, not(nullValue()))
        val collection = listOf(favoriteLocation2, favoriteLocation3)
        assertThat(result , `is`(collection))

    }

}