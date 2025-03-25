package com.example.completepokemondex.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.completepokemondex.R
class FragmentPrueba : Fragment() {

    private var nombreRecibido: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            nombreRecibido = it.getString(ARG_NOMBRE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_prueba, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mostrar el nombre del Pokémon como un Toast
        nombreRecibido?.let { nombre ->
            Toast.makeText(requireContext(), "Nombre del Pokémon: $nombre", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        const val ARG_NOMBRE = "nombre"

        @JvmStatic
        fun newInstance(nombre: String) =
            FragmentPrueba().apply {
                arguments = Bundle().apply {
                    putString(ARG_NOMBRE, nombre)
                }
            }
    }
}
