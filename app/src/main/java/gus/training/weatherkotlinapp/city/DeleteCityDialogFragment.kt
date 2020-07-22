package gus.training.weatherkotlinapp.city

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import gus.training.weatherkotlinapp.R

class DeleteCityDialogFragment : DialogFragment() {
    interface DeleteCityDialogListener{
        fun onDialogPositiveClick()
        fun onDialogNegativeClick()
    }

    companion object{
        val EXTRA_CITY_NAME = "EXTRA_CITY_NAME"
        fun newInstance(cityName: String) : DeleteCityDialogFragment {
            val fragment = DeleteCityDialogFragment()
            fragment.arguments = Bundle().apply {
                putString(EXTRA_CITY_NAME, cityName)
            }
            return fragment
        }
    }

    private lateinit var cityName: String
    var listener: DeleteCityDialogListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cityName = arguments?.getString(EXTRA_CITY_NAME)!!
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.delete_city_title, cityName))
            .setPositiveButton(getString(R.string.deletecity_positive)) { _, _ -> listener?.onDialogPositiveClick()}
            .setNegativeButton(getString(R.string.deletecity_negativ)) { _, _ -> listener?.onDialogNegativeClick()}
        return builder.create()
    }
}