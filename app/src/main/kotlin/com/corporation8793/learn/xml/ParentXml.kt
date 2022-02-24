package com.corporation8793.learn.xml

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import org.xmlunit.builder.Input
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream
import java.nio.charset.StandardCharsets
import javax.xml.XMLConstants
import javax.xml.transform.OutputKeys
import javax.xml.transform.Source
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult

class ParentXml {
    fun getPreprocessedStringForSource(string: String): String {
        val regexRemoveID = Regex("id=\".*?\"")
        val regexRemoveX = Regex("x=\".*?\"")
        val regexRemoveY = Regex("y=\".*?\"")

        return string.replace(regexRemoveID, "")
            .replace(regexRemoveX, "")
            .replace(regexRemoveY, "")
    }

    fun getPreprocessedString(string: String): String {
        val regexRemoveID = Regex("id=\".*?\"")
        val regexRemoveX = Regex("x=\".*?\"")
        val regexRemoveY = Regex("y=\".*?\"")
        val regexRemoveETC = Regex("[^\uAC00-\uD7A30-9a-zA-Z]")

        return string.replace(regexRemoveID, "")
            .replace(regexRemoveX, "")
            .replace(regexRemoveY, "")
            .replace(regexRemoveETC, "")
            .trim()
    }

    fun getSourceFromStream(XmlAssetFilePath: String, c : Context) : Source {
        return Input.fromStream(c.assets.open(XmlAssetFilePath)).build()
    }

    fun getSourceFromString(string : String) : Source {
        return Input.fromString(string).build()
    }

    fun getStringFromSource(s: Source): String? {
        val result = ""
        try {
            val transformerFactory = TransformerFactory.newInstance()
            transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true)
            val transformer = transformerFactory.newTransformer()
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes")
            transformer.setOutputProperty(OutputKeys.METHOD, "xml")
            val out: OutputStream = ByteArrayOutputStream()
            val streamResult = StreamResult()
            streamResult.outputStream = out
            transformer.transform(s, streamResult)
            return streamResult.outputStream.toString()
        } catch (e: Exception) {
            //do nothing
        }
        return result
    }

    fun getStringFromXmlAsset(XmlAssetFilePath: String, c:Context): String? {
        var xmlString: String? = null
        val am: AssetManager = c.assets
        try {
            val `is` = am.open(XmlAssetFilePath)
            val length = `is`.available()
            val data = ByteArray(length)
            `is`.read(data)
            xmlString = String(data, StandardCharsets.UTF_8)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return xmlString
    }
}