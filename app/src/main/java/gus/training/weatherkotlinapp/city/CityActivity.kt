package gus.training.weatherkotlinapp.city

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import gus.training.weatherkotlinapp.R
import gus.training.weatherkotlinapp.weather.WeatherActivity
import gus.training.weatherkotlinapp.weather.WeatherFragment

class CityActivity : AppCompatActivity(), CityFragment.CityFragmentListener {

    private lateinit var cityFragment: CityFragment
    private var currentCity: City? = null
    private var weatherfragment: WeatherFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city)

        cityFragment = supportFragmentManager.findFragmentById(R.id.city_fragment) as CityFragment

        cityFragment.listener = this

        weatherfragment = supportFragmentManager.findFragmentById(R.id.weather_fragment) as WeatherFragment
    }

    override fun onCitySelected(city: City) {
        currentCity = city
        if(isHandsetLayout()){
            startWeatherActivity(city)
        }else{
            weatherfragment?.updateWeatherForCity(city.name)
        }
    }

    override fun onEmptyCities() {
        weatherfragment?.clearUi()
    }

    fun isHandsetLayout(): Boolean = weatherfragment == null

    private fun startWeatherActivity(city: City) {
       val intent = Intent(this, WeatherActivity::class.java).apply {
           putExtra(WeatherFragment.EXTRA_CITY_NAME, city.name)
       }
        startActivity(intent)
    }
}