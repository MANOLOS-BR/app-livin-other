//package com.manoloscorp.livinother.view.dialogs
//
//import android.app.AlertDialog
//import android.app.Dialog
//import android.os.Bundle
//import android.view.View
//import android.widget.Button
//import android.widget.EditText
//import androidx.fragment.app.DialogFragment
//import com.manoloscorp.livinother.R
//
//class CustomContentDialog : DialogFragment() {
//
//    var edit1: EditText? = null
//    var edit2: EditText? = null
//    var button: Button? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//
//        val _view: View = requireActivity().layoutInflater.inflate(R.layout.content_dialog_view, null)
//
//        this.edit1 = _view.findViewById(R.id.edit1)
//        this.edit2 = _view.findViewById(R.id.edit2)
//        this.button = _view.findViewById(R.id.somar)
//
//        val alert = AlertDialog.Builder(activity)
//        alert.setView(_view)
//
//        this.button!!.setOnClickListener {
//
//            val a: Int = Integer.parseInt(edit1!!.text.toString())
//            val b: Int = Integer.parseInt(edit2!!.text.toString())
//
//            val s: Int = a + b //soma os valores
//
//            (activity as(Soma)).onResultado(s)
//            dismiss()
//
//            // Toast.makeText(activity, "Resultdo: " + s, Toast.LENGTH_LONG).show() //mostra o resultado da soma
//
//        }
//
//        return alert.create()
//    }
//
//    interface  Soma{
//        fun onResultado(result : Int) // metodo que sera chamado para retornar o resultado da operação
//    }
//}
