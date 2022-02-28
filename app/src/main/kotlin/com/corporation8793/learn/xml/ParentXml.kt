package com.corporation8793.learn.xml

import android.content.Context
import android.content.res.AssetManager
import org.w3c.dom.Document
import org.xml.sax.InputSource
import org.xmlunit.builder.Input
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream
import java.nio.charset.StandardCharsets
import javax.xml.XMLConstants
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Source
import javax.xml.transform.TransformerFactory
import javax.xml.transform.sax.SAXSource
import javax.xml.transform.stream.StreamResult

/**
 * 배울래? 애플리케이션에서 블록 코드 XML 파일 처리를 담당하는 클래스입니다.
 * @author  두동근
 * @param   context 애플리케이션 콘텍스트
 * @param   solutionXmlAssetFilePath Asset 폴더에 위치한 정답지 XML 파일의 Path
 * @param   submittedXml 현재 Workspace 에서 추출된 답안지 XML 의 [String]
 * @see     [com.google.blockly.android.codegen]
 * @see     AssetManager
 */
class ParentXml(val context : Context, solutionXmlAssetFilePath : String, submittedXml : String) {
    /**
     * [Source] 타입 정답지
     */
    val solutionSource : Source = getSourceFromString(getPreprocessedStringForSource(getStringFromXmlAsset(solutionXmlAssetFilePath, context)))
    /**
     * [Source] 타입 답안지
     */
    val submittedSource : Source = getSourceFromString(getPreprocessedStringForSource(submittedXml))
    /**
     * [String] 타입 정답지
     */
    val solutionString : String = getPreprocessedString(getStringFromXmlAsset(solutionXmlAssetFilePath, context))
    /**
     * [String] 타입 답안지
     */
    val submittedString : String = getPreprocessedString(submittedXml)
    /**
     * [InputSource] 타입 정답지
     */
    val solutionInputSource : InputSource? = SAXSource.sourceToInputSource(solutionSource)
    /**
     * [InputSource] 타입 답안지
     */
    val submittedInputSource : InputSource? = SAXSource.sourceToInputSource(submittedSource)
    /**
     * [Document] 타입 정답지
     */
    var solutionDocument : Document? = null
    /**
     * [Document] 타입 답안지
     */
    var submittedDocument : Document? = null

    var docBuilderFactory = DocumentBuilderFactory.newInstance()
    var docBuilder: DocumentBuilder = docBuilderFactory.newDocumentBuilder()

    /**
     * [solutionDocument], [submittedDocument] 를 초기화합니다.
     * @author  두동근
     */
    fun initDocument() {
        solutionDocument = docBuilder.parse(solutionInputSource).apply { normalizeDocument() }
        submittedDocument = docBuilder.parse(submittedInputSource).apply { normalizeDocument() }
    }

    /**
     * 올바른 [Source] 타입 변환을 위해 전처리된 XML [String] 을 반환하는 함수입니다.
     * @author  두동근
     * @param   string      Raw XML String
     * @return  [String]    Preprocessed XML String
     * @see     Regex
     */
    fun getPreprocessedStringForSource(string: String): String {
        val regexRemoveID = Regex("id=\".*?\"")
        val regexRemoveX = Regex("x=\".*?\"")
        val regexRemoveY = Regex("y=\".*?\"")

        return string.replace(regexRemoveID, "")
            .replace(regexRemoveX, "")
            .replace(regexRemoveY, "")
    }

    /**
     * 전처리된 XML [String] 을 반환하는 함수입니다.
     * @author  두동근
     * @param   string      Raw XML String
     * @return  [String]    Preprocessed XML String
     * @see     Regex
     */
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

    /**
     * XML [String] 을 XML [Source] 로 반환하는 함수입니다.
     * @author  두동근
     * @param   string      XML [String]
     * @return  [Source]    XML [Source]
     */
    fun getSourceFromString(string : String) : Source {
        return Input.fromString(string).build()
    }

    /**
     * XML [Source] 을 XML [String] 으로 반환하는 함수입니다.
     * @author  두동근
     * @param   s           XML [Source]
     * @return  [String]    XML [String]
     */
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

    /**
     * Asset 폴더에 위치한 XML 파일을 [String] 으로 반환하는 함수입니다.
     * @author  두동근
     * @param   XmlAssetFilePath        Asset 폴더에 위치한 XML 파일의 Path
     * @param   c                       애플리케이션 콘텍스트
     * @return  [String]                XML [String]
     */
    fun getStringFromXmlAsset(XmlAssetFilePath: String, c:Context): String {
        var xmlString = ""
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