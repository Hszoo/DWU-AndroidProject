//package com.example.finalproject.network
//
//
//import android.net.wifi.ScanResult.InformationElement
//import android.util.Xml
//import com.example.finalproject.data.Place
//import org.xmlpull.v1.XmlPullParser
//import org.xmlpull.v1.XmlPullParserException
//import java.io.IOException
//import java.io.InputStream
//
//class PlaceParser {
//    private val ns: String? = null
//
//    companion object {
//        val FAULT_RESULT = "fault_result"
//        val PLACE_TAG = "item"
//        val TITLE_TAG = "title"
//        val INFORMATION_TAG = "information"
//        val ADDR_TAG = "address"
//
//    }
//
//    @Throws(XmlPullParserException::class, IOException::class)
//    fun parse(inputStream: InputStream?) : List<Place> {
//
//        inputStream.use { inputStream ->
//            val parser : XmlPullParser = Xml.newPullParser()
//
//            /*Parser 의 동작 정의, next() 호출 전 반드시 호출 필요*/
//            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
//
//            /* Paring 대상이 되는 inputStream 설정 */
//            parser.setInput(inputStream, null)
//
//            /*Parsing 대상 태그의 상위 태그까지 이동*/
//            while(parser.name != "items") {
//                parser.next()
//            }
//
//
//
//            return readPlaceList(parser)
//        }
//    }
//
//
//    @Throws(XmlPullParserException::class, IOException::class)
//    private fun readPlaceList(parser: XmlPullParser) : List<Place> {
//        val places = mutableListOf<Place>()
//
//        parser.require(XmlPullParser.START_TAG, ns, "items")
//
//        while(parser.next() != XmlPullParser.END_TAG) {
//            if(parser.eventType != XmlPullParser.START_TAG) {
//                continue
//            }
//            if(parser.name == PLACE_TAG) {
//                places.add(readPlaceItem(parser))
//            } else {
//                skip(parser)
//            }
//        }
//        return places
//    }
//
//
//    @Throws(XmlPullParserException::class, IOException::class)
//    private fun readPlaceItem(parser: XmlPullParser) : Place {
//        parser.require(XmlPullParser.START_TAG, ns, PLACE_TAG)
//
//        lateinit var title : String
//        lateinit var address : String
//        lateinit var information : String
//
//        while(parser.next() != XmlPullParser.END_TAG) {
//            if(parser.eventType != XmlPullParser.START_TAG) {
//                continue
//            }
//            when(parser.name) {
//                TITLE_TAG -> title = readItemInText(parser, TITLE_TAG)
//                ADDR_TAG -> address = readItemInText(parser, ADDR_TAG)
//                INFORMATION_TAG -> information = readItemInText(parser, INFORMATION_TAG)
//                else -> skip(parser)
//            }
//        }
//        return Place(title, information, address)
//
//    }
//
//
//
//    @Throws(IOException::class, XmlPullParserException::class)
//    private fun readItemInText (parser: XmlPullParser, tag: String): String {
//        parser.require(XmlPullParser.START_TAG, ns, tag)
//        var text = ""
//        if (parser.next() == XmlPullParser.TEXT) {
//            text = parser.text
//            parser.nextTag()
//        }
//        parser.require(XmlPullParser.END_TAG, ns, tag)
//        return text
//    }
//
//
//    @Throws(XmlPullParserException::class, IOException::class)
//    private fun skip(parser: XmlPullParser) {
//        if (parser.eventType != XmlPullParser.START_TAG) {
//            throw IllegalStateException()
//        }
//        var depth = 1
//        while (depth != 0) {
//            when (parser.next()) {
//                XmlPullParser.END_TAG -> depth--
//                XmlPullParser.START_TAG -> depth++
//            }
//        }
//    }
//}