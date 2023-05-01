package com.ayoub.electricitybill.extension

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun Context.createImageUri(): Uri {
    val directory = File(cacheDir, "images")
    directory.mkdirs()
    val file = File.createTempFile(
        "bills",
        ".jpg",
        directory,
    )
    val authority = "$packageName.fileprovider"
    return FileProvider.getUriForFile(
        this,
        authority,
        file,
    )
}