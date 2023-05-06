package com.ayoub.electricitybill.ui.common

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.CoroutineScope


val LocalAdminRole = compositionLocalOf<Boolean> {
    error("no admin role has provided!")
}

val LocalCoroutineScope = staticCompositionLocalOf<CoroutineScope> {
    error("No coroutine scope provided!")
}