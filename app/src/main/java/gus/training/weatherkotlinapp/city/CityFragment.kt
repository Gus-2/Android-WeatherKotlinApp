package gus.training.weatherkotlinapp.city

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gus.training.weatherkotlinapp.App
import gus.training.weatherkotlinapp.Database
import gus.training.weatherkotlinapp.R
import gus.training.weatherkotlinapp.utils.toasts
import kotlinx.android.synthetic.main.fragment_city.*

class CityFragment : Fragment(), CityAdapter.CityItemListener {
    
    
    interface CityFragmentListener {
        fun onCitySelected(city: City)
        fun onEmptyCities()
    }
    
    var listener: CityFragmentListener? = null

    private lateinit var database: Database
    private lateinit var cities: MutableList<City>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        cities = mutableListOf()
        database = App.database
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.fragment_city, container, false)
        recyclerView = view.findViewById(R.id.cities_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cities = database.getAllCities()
        adapter = CityAdapter(cities, this)
        recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.fragment_city, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.action_create_city -> {
                showCreateCityDialog()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showCreateCityDialog() {
        val createCityFragment = CreateCityDialogFragment()
        createCityFragment.listener = object: CreateCityDialogFragment.CreateCityDialogListener {
            override fun onDialogPositiveClick(cityName: String) {
                saveCity(City(cityName))
            }

            override fun onDialogNegativeClick() { }

        }

        createCityFragment.show(fragmentManager!!, "CreateCityDialogFragment")
    }

    override fun onCitySelected(city: City) {
        listener?.onCitySelected(city)
    }

    override fun onCityDeleted(city: City) {
        showDeleteCityDialog(city)
    }

    private fun showDeleteCityDialog(city: City) {
        val deleteCityFragment = DeleteCityDialogFragment.newInstance(city.name)
        deleteCityFragment.listener = object : DeleteCityDialogFragment.DeleteCityDialogListener{
            override fun onDialogPositiveClick() {
                deleteCity(city)
            }

            override fun onDialogNegativeClick() { }

        }
        deleteCityFragment.show(fragmentManager!!, "DeleteCityDialogFragment")
    }

    private fun deleteCity(city: City) {
        if(database.deleteCity(city)){
            cities.remove(city)
            adapter.notifyDataSetChanged()
            selectFirstCity()
            context?.toasts(getString(R.string.city_message_info_city_delete, city.name))
        } else{
            context?.toasts(getString(R.string.city_message_error_city_delete, city.name))
        }
    }



    private fun saveCity(city: City) {
        if(database.createCity(city)){
            cities.add(city)
            adapter.notifyDataSetChanged()
        }else{
            context?.toasts(getString(R.string.citymessage_error_couldnt_create_city))
        }
    }

    fun selectFirstCity(){
        when(cities.isEmpty()){
            true -> listener?.onEmptyCities()
            false -> listener?.onCitySelected(cities[0])
        }
    }
}
