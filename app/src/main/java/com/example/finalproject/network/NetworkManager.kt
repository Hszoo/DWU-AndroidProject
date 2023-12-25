//package com.example.finalproject.network
//
//import android.content.Context
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import com.example.finalproject.R
//import com.example.finalproject.data.Place
//import java.io.BufferedReader
//import java.io.IOException
//import java.io.InputStream
//import java.io.InputStreamReader
//import java.lang.StringBuilder
//import java.net.HttpURLConnection
//import java.net.URL
//import kotlin.jvm.Throws
//
//
//class NetworkManager(val context: Context) {
//    private val TAG = "NetworkManager"
//
//    val openApiUrl by lazy {
//        /* Resource 의 strings.xml 에서 필요한 정보를 읽어옴 */
//        context.resources.getString(R.string.api_url)
//
//    }
//
//    @Throws(IOException::class)
//    fun downloadXml(keyword: String) : List<Place>? {
//        var movies : List<Place>? = null
//        var key : String = context.resources.getString(R.string.api_key)
//        val inputStream = downloadUrl( openApiUrl + "?ServiceKey=${key}" +
//                                                    "&keyword=${keyword}")
//
//        /*Parser 생성 및 parsing 수행*/
//        val parser = PlaceParser()
//        movies = parser.parse(inputStream)
//
//        return movies
//    }
//
//
//    @Throws(IOException::class)
//    private fun downloadUrl(urlString: String) : InputStream? {
//        val receivedContents : List<Place>? = null
//
//        val url = URL(urlString)
//
//        return (url.openConnection() as? HttpURLConnection)?.run {
//            readTimeout = 5000
//            connectTimeout = 5000
//            requestMethod = "GET"
//            doInput = true
//
//            connect()
//            inputStream
//        }
//    }
//
//
//    // InputStream 을 String 으로 변환
//    private fun readStreamToString(iStream : InputStream?) : String {
//        val resultBuilder = StringBuilder()
//
//        val inputStreamReader = InputStreamReader(iStream)
//        val bufferedReader = BufferedReader(inputStreamReader)
//
//        var readLine : String? = bufferedReader.readLine()
//        while (readLine != null) {
//            resultBuilder.append(readLine + System.lineSeparator())
//            readLine = bufferedReader.readLine()
//        }
//
//        bufferedReader.close()
//        return resultBuilder.toString()
//    }
//
//
//    // InputStream 을 Bitmap 으로 변환
//    private fun readStreamToImage(iStream: InputStream?) : Bitmap {
//        val bitmap = BitmapFactory.decodeStream(iStream)
//        return bitmap
//    }
//
//}