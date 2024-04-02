package com.example.weather.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.weather.dataBase.LocalDataSourceInterface
import com.example.weather.dataBase.WeatherRepo
import com.example.weather.dataBase.WeatherRepoImp
import com.example.weather.getOrAwaitValue
import com.example.weather.model.Alarm
import com.example.weather.model.WeatherEntity
import com.example.weather.network.FakeRemoteDataSource
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
class WeatherRepoImpTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

//    @get:Rule
//    val rule = MainRule()
    private lateinit var fakeLocalDataSource : LocalDataSourceInterface
    private lateinit var fakeRemoteDataSource: FakeRemoteDataSource
    private lateinit var repo: WeatherRepo
    @Before
    fun setup()
    {
        fakeLocalDataSource = FakeLocalDataSource()
        fakeRemoteDataSource = FakeRemoteDataSource()
        repo = WeatherRepoImp(fakeRemoteDataSource,fakeLocalDataSource)
    }

    @Test
    fun addLocation_theSameLocation() = runBlockingTest {        //done
        //Given
        val favoriteLocation1 = WeatherEntity(1,0.0,0.0,0,"cloud","cloud",
            "cloud","assuit","egypt",0.0,0.0,0.0,0.0,0,0,
            0.0,0,0.0,0,0,0)
        repo.insertWeather(favoriteLocation1)

        val collection = listOf(favoriteLocation1)

        //When

        val result = repo.getAllWeather().first()

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

        repo.insertWeather(favoriteLocation1)
        repo.insertWeather(favoriteLocation2)
        repo.insertWeather(favoriteLocation3)

        repo.deleteWeather(favoriteLocation1)

        //When

        val result = repo.getAllWeather().first()

        // Then
        MatcherAssert.assertThat(result , CoreMatchers.`is`(listOf(favoriteLocation2,favoriteLocation3)))

    }
    @Test
    fun getAllFavPlaces_returnFavPlaces() =runBlockingTest {
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

        repo.insertWeather(place1)
        repo.insertWeather(place2)

        //when

        var result = listOf<WeatherEntity>()
        result = repo.getAllWeather().first()
        //Return

        MatcherAssert.assertThat(result, CoreMatchers.`is`(list))
    }
    @Test
    fun addAlram_theSameLocation() = runBlockingTest {        //done
        //Given
        val alram1 = Alarm(1,0,true,0.0,0.0)
        repo.insertAlarm(alram1)

        val collection = listOf(alram1)

        //When

        val result = repo.getAllAlarms().getOrAwaitValue()

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

        repo.insertAlarm(alram1)
        repo.insertAlarm(alram2)
        repo.insertAlarm(alram3)


        repo.deleteAlarm(alram1)

        val collection = listOf(alram2,alram3)

        //When

        val result = repo.getAllAlarms().getOrAwaitValue()

        // Then
        MatcherAssert.assertThat(result, CoreMatchers.not(CoreMatchers.nullValue()))
        MatcherAssert.assertThat(result, CoreMatchers.`is`(collection))

    }
    @Test
    fun getAllAlrams_returnFavPlaces() =runBlockingTest {
        //Given
        val alram1 = Alarm(1,0,true,0.0,0.0)
        repo.insertAlarm(alram1)

        val alram2 = Alarm(2,0,true,0.0,0.0)
        repo.insertAlarm(alram2)

        val alram3 = Alarm(3,0,true,0.0,0.0)
        repo.insertAlarm(alram3)

        val collection = listOf(alram1,alram2,alram3)

        //When

        val result = repo.getAllAlarms().getOrAwaitValue()

        // Then
        MatcherAssert.assertThat(result, CoreMatchers.not(CoreMatchers.nullValue()))
        MatcherAssert.assertThat(result, CoreMatchers.`is`(collection))
    }

}