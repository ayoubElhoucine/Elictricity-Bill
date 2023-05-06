package com.ayoub.electricitybill

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.MutableLiveData
import com.ayoub.electricitybill.firebase.FirebaseDatabase
import com.ayoub.electricitybill.firebase.FirebaseUserAuth
import com.ayoub.electricitybill.ui.MyApp
import com.ayoub.electricitybill.ui.theme.ElectricityBillTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var firebaseDatabase: FirebaseDatabase
    @Inject lateinit var firebaseUserAuth: FirebaseUserAuth

    private val isAdmin = MutableLiveData(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseUserAuth.getUser()?.let {
            firebaseDatabase.getConsumerById(it.uid) {
                it?.let { consumer ->
                    isAdmin.postValue(consumer.isAdmin)
                }
            }
        }

        setContent {
            MyApp(finishActivity = ::finish, isAdmin = isAdmin)
        }
    }
}
