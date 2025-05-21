package com.example.completepokemondex

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale

@HiltAndroidApp
class CompletePokemonDexApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setupLocales()
    }

    /**
     * Configura los idiomas soportados por la aplicación.
     * Si el dispositivo está configurado en un idioma que no es español ni inglés,
     * se utilizará inglés como idioma predeterminado.
     */
    private fun setupLocales() {
        val currentLocale = getCurrentLocale(this)
        val languageCode = currentLocale.language
        if (languageCode != "es" && languageCode != "en") {
            val localeList = LocaleListCompat.create(Locale("en"))
            AppCompatDelegate.setApplicationLocales(localeList)
        }
    }

    /**
     * Obtiene el idioma actual del dispositivo de forma compatible con todas las versiones de Android.
     */
    private fun getCurrentLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            context.resources.configuration.locale
        }
    }
}
