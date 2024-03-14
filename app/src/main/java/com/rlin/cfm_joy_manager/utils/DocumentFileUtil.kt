package com.rlin.cfm_joy_manager.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader


//获取指定目录的访问权限
fun startFor(path: String, context: Activity, REQUEST_CODE_FOR_DIR: Int) {
    val uri: String = changeToUri(path) //调用方法，把path转换成可解析的uri文本，这个方法在下面会公布
    val parse = Uri.parse(uri)
    val intent = Intent("android.intent.action.OPEN_DOCUMENT_TREE")
    intent.addFlags(
        Intent.FLAG_GRANT_READ_URI_PERMISSION
                or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                or Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
    )
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, parse)
    }
    context.startActivityForResult(intent, REQUEST_CODE_FOR_DIR) //开始授权
}

//转换至uriTree的路径
fun changeToUri(path: String): String {
    var path = path
    if (path.endsWith("/")) {
        path = path.substring(0, path.length - 1)
    }
    val path2 = path.replace("/storage/emulated/0/", "").replace("/", "%2F")
    return "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3A$path2"
}

//转换至uriTree的路径
fun changeToUri3(path: String): String {
    var path = path
    path = path.replace("/storage/emulated/0/", "").replace("/", "%2F")
    return "content://com.android.externalstorage.documents/tree/primary%3A$path"
}

//遍历示例，不进行额外逻辑处理
fun getFiles(documentFile: DocumentFile) {
    Log.d("文件:", documentFile.name!!)
    if (documentFile.isDirectory) {
        for (file in documentFile.listFiles()) {
            Log.d("子文件", file.name!!)
        }
    }
}

suspend fun getJoyFiles(context: Context): List<String> {
    val documentFile: DocumentFile? = DocumentFile.fromTreeUri(
        context, Uri.parse(
            changeToUri3(CFM_DIR)
        )
    )
    val list = mutableListOf<String>()
    if (documentFile != null) {
        if (documentFile.isDirectory) {
            for (file in documentFile.listFiles()) {
                if (file.isFile && file.name!!.startsWith("CustomJoySticksConfig") && file.name!!.endsWith(
                        ".json"
                    )
                ) {
                    Log.d("键位文件", file.name!!)
                    list.add(file.name!!)
                }
            }
        }
    }
    return list
}

fun readJoyFile(fileName: String, context: Context): String {
    var fileContent = ""
    val documentFile: DocumentFile? = DocumentFile.fromTreeUri(
        context, Uri.parse(
            changeToUri3(CFM_DIR)
        )
    )
    var joyFile: DocumentFile? = null
    if (documentFile != null) {
        if (documentFile.isDirectory) {
            for (file in documentFile.listFiles()) {
                if (file.isFile && file.name!!.endsWith(fileName)
                ) {
                    joyFile = file
                    break
                }
            }
        }
    }
    if (joyFile != null && joyFile.canRead()) {
        fileContent = readDocumentFile(joyFile, context)
    }
    Log.d("文件IO", fileContent.length.toString())
    return fileContent
}

fun readDocumentFile(documentFile: DocumentFile, context: Context): String {
    val stringBuilder = StringBuilder()
    try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(documentFile.uri)
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        var line: String?
        while (bufferedReader.readLine().also { line = it } != null) {
            stringBuilder.append(line)
        }
        bufferedReader.close()
        inputStream?.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return stringBuilder.toString()
}