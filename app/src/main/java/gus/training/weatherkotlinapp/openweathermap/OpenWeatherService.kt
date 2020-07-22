package gus.training.weatherkotlinapp.openweathermap

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "2fb68fec95c27399d3e47698ade912c0"

interface OpenWeatherService {

    @GET("data/2.5/weather?units=metric&lang=fr")
    fun getWeather(@Query("q") cityName: String, @Query("appid") apiKey:String = API_KEY) : Call<WeatherWrapper>
}