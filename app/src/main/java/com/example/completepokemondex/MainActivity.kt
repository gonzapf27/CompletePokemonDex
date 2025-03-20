package com.example.completepokemondex

        import android.os.Bundle
        import androidx.appcompat.app.AppCompatActivity
        import com.example.completepokemondex.databinding.ActivityMainBinding
        import com.example.completepokemondex.services.RetrofitInstance
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
                    val response = RetrofitInstance.api.getPokemon("pikachu").execute()
                    if (response.isSuccessful) {
                        val pokemon = response.body()
                        withContext(Dispatchers.Main) {
                            println("Nombre: ${pokemon?.name}")
                            println("Altura: ${pokemon?.height}")
                            println("Peso: ${pokemon?.weight}")
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            println("Error: ${response.errorBody()}")
                        }
                    }
                }
            }
        }