package com.example.completepokemondex

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.completepokemondex.databinding.ActivityMainBinding
import com.example.completepokemondex.services.RetrofitInstance
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnPrueba.setOnClickListener { btnPruebaClicked() }
    }

    private fun btnPruebaClicked() {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                binding.progressBar.visibility = View.VISIBLE
                binding.imgPokemon.visibility = View.GONE
            }
            var pokemonName = binding.txtNombrePokemon.text.toString()
            pokemonName = "Pikachu"
            val response = RetrofitInstance.api.getPokemon(pokemonName).execute()
            if (response.isSuccessful) {
                val pokemon = response.body()
                val imageUrl = pokemon?.sprites?.front_default
                withContext(Dispatchers.Main) {
                    println("Nombre: ${pokemon?.name}")
                    println("Altura: ${pokemon?.height}")
                    println("Peso: ${pokemon?.weight}")
                    binding.progressBar.visibility = View.GONE

                    if(pokemon?.sprites?.front_default != null){
                        Picasso.get().load(pokemon.sprites.front_default).into(binding.imgPokemon)
                        binding.imgPokemon.visibility = View.VISIBLE
                    }

                    println(response.body())
                }
            } else {
                withContext(Dispatchers.Main) {
                    println("Error: ${response.errorBody()}")
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }
}
