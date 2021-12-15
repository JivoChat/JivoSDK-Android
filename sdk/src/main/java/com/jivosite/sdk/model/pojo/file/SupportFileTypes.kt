package com.jivosite.sdk.model.pojo.file

/**
 * Created on 02.11.2020.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class SupportFileTypes {

    companion object {

        const val TYPE_IMAGE = "photo"
        const val TYPE_VIDEO = "video"
        const val TYPE_AUDIO = "audio"
        const val TYPE_DOCUMENT = "document"
        const val TYPE_UNKNOWN = "unknown"

        val FILE_TYPES = mapOf(
            //image
            "jpg" to TYPE_IMAGE,
            "jpeg" to TYPE_IMAGE,
            "png" to TYPE_IMAGE,
            "gif" to TYPE_IMAGE,
            "svg" to TYPE_IMAGE,
            "webp" to TYPE_IMAGE,
            "psd" to TYPE_IMAGE,
            "djvu" to TYPE_IMAGE,

            //document
            "txt" to TYPE_DOCUMENT,
            "pdf" to TYPE_DOCUMENT,
            "csv" to TYPE_DOCUMENT,
            "log" to TYPE_DOCUMENT,
            "ics" to TYPE_DOCUMENT,
            "odt" to TYPE_DOCUMENT,
            "html" to TYPE_DOCUMENT,
            "css" to TYPE_DOCUMENT,
            "xlsx" to TYPE_DOCUMENT,
            "pptx" to TYPE_DOCUMENT,
            "odp" to TYPE_DOCUMENT,
            "ods" to TYPE_DOCUMENT,
            "vsd" to TYPE_DOCUMENT,
            "rtf" to TYPE_DOCUMENT,
            "ttf" to TYPE_DOCUMENT,
            "doc" to TYPE_DOCUMENT,
            "docx" to TYPE_DOCUMENT,
            "json" to TYPE_DOCUMENT,
            "ppt" to TYPE_DOCUMENT,
            "xls" to TYPE_DOCUMENT,
            "epub" to TYPE_DOCUMENT,

            //archive
            "rar" to TYPE_DOCUMENT,
            "tar" to TYPE_DOCUMENT,
            "7z" to TYPE_DOCUMENT,
            "gz" to TYPE_DOCUMENT,
            "zip" to TYPE_DOCUMENT,

            //audio
            "aac" to TYPE_AUDIO,
            "mp3" to TYPE_AUDIO,
            "oga" to TYPE_AUDIO,
            "wav" to TYPE_AUDIO,

            //video
            "mp4" to TYPE_VIDEO,
            "mp4a" to TYPE_VIDEO,
            "mov" to TYPE_VIDEO,
            "webm" to TYPE_VIDEO,
            "weba" to TYPE_VIDEO,
            "ogv" to TYPE_VIDEO,
            "avi" to TYPE_VIDEO,
            "mpeg" to TYPE_VIDEO
        )

        val SUPPORT_FILE_TYPES = mapOf(
            //image
            "jpg" to TYPE_IMAGE,
            "jpeg" to TYPE_IMAGE,
            "png" to TYPE_IMAGE,

            //document
            "txt" to TYPE_DOCUMENT,
        )
    }
}