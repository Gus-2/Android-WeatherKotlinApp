package gus.training.weatherkotlinapp.openweathermap

import gus.training.weatherkotlinapp.weather.Weather

fun mapOpenWeatherDataToWeather(weatherWrapper: WeatherWrapper) : Weather {
    val weatherFirst = weatherWrapper.weather.first()

    return Weather(description = weatherFirst.description, temperature = weatherWrapper.main.temperature, humidity = weatherWrapper.main.humidity, pressure = weatherWrapper.main.pressure, icon = "https://openweathermap.org/img/w/${weatherFirst.icon}.png")
}