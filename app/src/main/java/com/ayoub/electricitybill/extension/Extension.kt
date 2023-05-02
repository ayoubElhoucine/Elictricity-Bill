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

fun Double.toNiceFormat(
    aroundUp: Int = 2,
    hardDecimal: Boolean = false
): String {
    return if (this > this.toInt() || hardDecimal) String.format("%.${aroundUp}f", this)
    else String.format("%.0f", this)
}