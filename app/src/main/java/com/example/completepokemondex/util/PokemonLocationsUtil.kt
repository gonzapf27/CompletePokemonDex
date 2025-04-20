package com.example.completepokemondex.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
/**
 * Utilidad para manejar las coordenadas de ubicaciones en el mapa de Kanto
 */
class PokemonLocationsUtil {
    companion object {
        /**
         * Mapa que contiene las coordenadas (x, y) de las diferentes ubicaciones en Kanto
         */
        val LOCATION_MAP = mapOf(
            "pallet-town-area" to Pair(1555, 734),
            "kanto-route-2-south-towards-viridian-city" to Pair(1557, 447),
            "kanto-route-24-area" to Pair(1940, 121),
            "kanto-route-25-area" to Pair(2025, 53),
            "viridian-forest-area" to Pair(1554, 383),
            "kanto-route-12-area" to Pair(2186, 625),
            "kanto-route-1-area" to Pair(1554, 636),
            "kanto-route-3-area" to Pair(1652, 205),
            "kanto-route-5-area" to Pair(1945, 302),
            "kanto-route-6-area" to Pair(1942, 476),
            "kanto-route-7-area" to Pair(1846, 404),
            "kanto-route-8-area" to Pair(2065, 406),
            "kanto-route-13-area" to Pair(2114, 694),
            "kanto-route-14-area" to Pair(2055, 755),
            "kanto-route-15-area" to Pair(1991, 824),
            "kanto-sea-route-21-area" to Pair(1553, 822),
            "kanto-route-4-area" to Pair(1847, 205),
            "kanto-route-9-area" to Pair(2069, 210),
            "kanto-route-16-area" to Pair(1695, 404),
            "kanto-route-22-area" to Pair(1443, 522),
            "kanto-route-17-area" to Pair(1685, 630),
            "kanto-route-18-area" to Pair(1780, 827),
            "kanto-route-10-area" to Pair(2187, 334),
            "kanto-route-11-area" to Pair(2112, 560),
            "kanto-route-23-area" to Pair(1363, 419),
            "cerulean-cave-1f" to Pair(1901, 152),
            "cerulean-cave-b1f" to Pair(1901, 152),
            "power-plant-area" to Pair(2219, 277),
            "kanto-safari-zone-middle" to Pair(30, 586),
            "kanto-safari-zone-area-1-east" to Pair(30, 586),
            "kanto-safari-zone-area-2-north" to Pair(30, 586),
            "kanto-safari-zone-area-3-west" to Pair(30, 586),
            "mt-moon-1f" to Pair(1756, 216),
            "mt-moon-b1f" to Pair(1756, 216),
            "mt-moon-b2f" to Pair(1756, 216),
            "pokemon-mansion-1f" to Pair(1560, 923),
            "pokemon-mansion-2f" to Pair(1560, 923),
            "pokemon-mansion-3f" to Pair(1560, 923),
            "pokemon-mansion-b1f" to Pair(1560, 923),
            "cerulean-cave-2f" to Pair(1969, 185),
            "seafoam-islands-1f" to Pair(1725, 921),
            "rock-tunnel-b1f" to Pair(2914, 207),
            "rock-tunnel-b2f" to Pair(2914, 207),
            "kanto-victory-road-2-1f" to Pair(1359, 300),
            "kanto-victory-road-2-2f" to Pair(1359, 300),
            "kanto-victory-road-2-3f" to Pair(1359, 300),
            "seafoam-islands-b2f" to Pair(1742, 921),
            "seafoam-islands-b4f" to Pair(1742, 921),
            "digletts-cave-area" to Pair(2023, 556),
            "seafoam-islands-b1f" to Pair(1742, 921),
            "seafoam-islands-b3f" to Pair(1742, 921),
            "cerulean-city-area" to Pair(1946, 210),
            "kanto-sea-route-19-area" to Pair(1833, 927),
            "kanto-sea-route-20-area" to Pair(1642, 922),
            "cinnabar-island-area" to Pair(1562, 922),
            "viridian-city-area" to Pair(1555, 518),
            "vermilion-city-area" to Pair(1941, 558),
            "celadon-city-area" to Pair(1780, 399),
            "fuchsia-city-area" to Pair(1881, 815),
            "vermilion-city-ss-anne-dock" to Pair(1912, 563),
            "pokemon-tower-3f" to Pair(2188, 401),
            "pokemon-tower-4f" to Pair(2188, 401),
            "pokemon-tower-5f" to Pair(2188, 401),
            "pokemon-tower-6f" to Pair(2188, 401),
            "pokemon-tower-7f" to Pair(2188, 401),
            "saffron-city-fighting-dojo" to Pair(1939, 399),
            "kanto-route-4-pokemon-center" to Pair(1852, 214),
            "kanto-route-3-pokemon-center" to Pair(1657, 206),
            "saffron-city-silph-co-7f" to Pair(1939, 397),
            "celadon-city-celadon-mansion" to Pair(1770, 401),
            "cinnabar-island-cinnabar-lab" to Pair(1546, 921)
        )

        /**
         * Obtiene las coordenadas de una ubicación específica
         * @param locationId El ID de la ubicación
         * @return Par de coordenadas (x,y) o null si no se encuentra
         */
        fun getCoordinates(locationId: String): Pair<Int, Int>? {
            return LOCATION_MAP[locationId]
        }

        /**
         * Resalta una o varias ubicaciones en un mapa base
         *
         * @param context Contexto de la aplicación
         * @param mapImage La imagen base del mapa
         * @param locationIds Lista de IDs de ubicaciones a resaltar
         * @return Bitmap con las ubicaciones resaltadas o la imagen original si falla
         */
        fun highlightLocations(context: Context, mapImage: Bitmap, locationIds: List<String>): Bitmap {
            try {
                // Crear una copia mutable del bitmap original
                val resultBitmap = mapImage.copy(Bitmap.Config.ARGB_8888, true)
                val canvas = Canvas(resultBitmap)

                // Configurar el círculo para resaltar la ubicación
                val paint = Paint().apply {
                    color = Color.RED
                    style = Paint.Style.STROKE
                    strokeWidth = 8f
                    isAntiAlias = true
                }

                // Configurar círculo interior semitransparente
                val fillPaint = Paint().apply {
                    color = Color.RED
                    alpha = 100 // Semitransparente
                    style = Paint.Style.FILL
                    isAntiAlias = true
                }

                // Procesar cada ubicación
                locationIds.forEach { locationId ->
                    val coordinates = getCoordinates(locationId)
                    coordinates?.let { (x, y) ->
                        // Dibujar círculo exterior
                        canvas.drawCircle(x.toFloat(), y.toFloat(), 40f, paint)

                        // Dibujar círculo interior
                        canvas.drawCircle(x.toFloat(), y.toFloat(), 30f, fillPaint)
                    }
                }

                return resultBitmap
            } catch (e: Exception) {
                Log.e("PokemonLocationsUtil", "Error al resaltar ubicaciones: ${e.message}")
                return mapImage // Devolver la imagen original si falla
            }
        }

        /**
         * Resalta una ubicación en un mapa base
         *
         * @param context Contexto de la aplicación
         * @param mapImage La imagen base del mapa
         * @param x Coordenada x de la ubicación
         * @param y Coordenada y de la ubicación
         * @return Bitmap con la ubicación resaltada o la imagen original si falla
         */
        fun highlightLocation(context: Context, mapImage: Bitmap, x: Int, y: Int): Bitmap {
            try {
                // Crear una copia mutable del bitmap original
                val resultBitmap = mapImage.copy(Bitmap.Config.ARGB_8888, true)
                val canvas = Canvas(resultBitmap)

                // Configurar el círculo para resaltar la ubicación
                val paint = Paint().apply {
                    color = Color.RED
                    style = Paint.Style.STROKE
                    strokeWidth = 8f
                    isAntiAlias = true
                }

                // Dibujar círculo exterior
                canvas.drawCircle(x.toFloat(), y.toFloat(), 40f, paint)

                // Configurar círculo interior semitransparente
                val fillPaint = Paint().apply {
                    color = Color.RED
                    alpha = 100 // Semitransparente
                    style = Paint.Style.FILL
                    isAntiAlias = true
                }

                // Dibujar círculo interior
                canvas.drawCircle(x.toFloat(), y.toFloat(), 30f, fillPaint)

                return resultBitmap
            } catch (e: Exception) {
                Log.e("PokemonLocationsUtil", "Error al resaltar ubicación: ${e.message}")
                return mapImage // Devolver la imagen original si falla
            }
        }
    }
}