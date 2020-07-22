package gus.training.weatherkotlinapp.weather

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.squareup.picasso.Picasso
import gus.training.weatherkotlinapp.App
import gus.training.weatherkotlinapp.R
import gus.training.weatherkotlinapp.openweathermap.WeatherWrapper
import gus.training.weatherkotlinapp.openweathermap.mapOpenWeatherDataToWeather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherFragment : Fragment() {

    private lateinit var cityName: String

    companion object {
        val EXTRA_CITY_NAME  = "EXTRA_CITY_NAME"
        fun newInstance() = WeatherFragment()
    }

    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var city: TextView
    private lateinit var weatherIcon: ImageView
    private lateinit var weatherDescription: TextView
    private lateinit var temperature: TextView
    private lateinit var humidity: TextView
    private lateinit var pressure: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)

        refreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        city = view.findViewById(R.id.city)
        weatherIcon = view.findViewById(R.id.weather_icon)
        weatherDescription = view.findViewById(R.id.weather_description)
        temperature = view.findViewById(R.id.temperature)
        humidity = view.findViewById(R.id.humidity)
        pressure = view.findViewById(R.id.pressure)

        refreshLayout.setOnRefreshListener { refreshWeather() }
        return view
    }

    private fun refreshWeather() {
        updateWeatherForCity(cityName)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(activity?.intent!!.hasExtra(EXTRA_CITY_NAME)){
            updateWeatherForCity(activity!!.intent.getStringExtra(EXTRA_CITY_NAME))
        }
    }

    public fun updateWeatherForCity(cityName: String) {
        this.cityName = cityName

        if(!refreshLayout.isRefreshing){
            refreshLayout.isRefreshing = true
        }

        val call = App.weatherService.getWeather("$cityName,fr")
        call.enqueue(object: Callback<WeatherWrapper> {
            override fun onResponse(
                call: Call<WeatherWrapper>,
                response: Response<WeatherWrapper>
            ) {
                response?.body()?.let {
                    val weather = mapOpenWeatherDataToWeather(it)
                    updateUi(weather)
                }
                refreshLayout.isRefreshing = false
            }

            override fun onFailure(call: Call<WeatherWrapper>, t: Throwable) {
                Log.d("Error", t.printStackTrace().toString())
                refreshLayout.isRefreshing = false
            }
        })

    }

    private fun updateUi(weather: Weather) {

        Picasso.get()
            .load(weather.icon)
            .placeholder(R.drawable.ic_launcher_background)
            .into(weatherIcon)

        weatherDescription.text = weather.description
        temperature.text = getString(R.string.weather_temperature_value, weather.temperature.toInt())
        humidity.text = getString(R.string.weather_humidity_value, weather.humidity)
        pressure.text = getString(R.string.weather_pressure_value, weather.pressure)

    }

    fun clearUi() {
        weatherIcon.setImageResource(R.drawable.ic_launcher_background)
        cityName = ""
        city.text = ""
        weatherDescription.text = ""
        temperature.text = ""
        humidity.text = ""
        pressure.text = ""
    }

}