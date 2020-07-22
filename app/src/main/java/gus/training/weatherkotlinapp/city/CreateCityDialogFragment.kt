package gus.training.weatherkotlinapp.city

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import gus.training.weatherkotlinapp.R

class CreateCityDialogFragment: DialogFragment(){
    interface CreateCityDialogListener{
        fun onDialogPositiveClick(cityName: String)
        fun onDialogNegativeClick()
    }
    
    var listener: CreateCityDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)
        
        val input = EditText(context)
        with(input){
            inputType = InputType.TYPE_CLASS_TEXT
            hint = context.getString(R.string.createcity_hint)
        }
        
        builder.setTitle(R.string.createcity_title)
            .setView(input)
            .setPositiveButton(getString(R.string.createcity_positibe), DialogInterface.OnClickListener{ _, _->
                listener?.onDialogPositiveClick(input.text.toString())
            })
            .setNegativeButton(getString(R.string.createcity_cancel), DialogInterface.OnClickListener { dialog, _ ->
                dialog.cancel()
                listener?.onDialogNegativeClick()
            })
        return builder.create()
    }
}