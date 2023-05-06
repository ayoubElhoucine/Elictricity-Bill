package com.ayoub.electricitybill.data.firebase

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@Singleton
class FirebaseUserAuth @Inject constructor(
    private val application: Application,
) {

    private val auth = FirebaseAuth.getInstance()

    init {
        auth.setLanguageCode(Locale.getDefault().language)
    }

    fun signeOut() = auth.signOut()

    fun getUser(): FirebaseUser? = auth.currentUser

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                FirebaseCrashlytics.getInstance().recordException(it)
                onFail()
            }
    }

    suspend fun refreshToken(): Boolean = suspendCoroutine { continuation ->
        auth.currentUser?.let { user ->
            user.getIdToken(true)
                .addOnSuccessListener {

                }
                .addOnFailureListener { e ->

                }
        } ?: run {
            continuation.resume(false)
        }
    }
}