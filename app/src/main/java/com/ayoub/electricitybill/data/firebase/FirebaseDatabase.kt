package com.ayoub.electricitybill.data.firebase

import android.app.Application
import android.net.Uri
import com.ayoub.electricitybill.model.Bill
import com.ayoub.electricitybill.model.Consumer
import com.ayoub.electricitybill.model.Consumption
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.ktx.storage
import javax.inject.Inject
import javax.inject.Singleton


const val billsRef = "bills"
const val draftBillRef = "draftBill"
const val consumptionsRef = "consumptions"
const val consumersRef = "consumers"

@Singleton
class FirebaseDatabase @Inject constructor(
    private val application: Application,
) {
    private val database = Firebase.database.reference
    private val storage = Firebase.storage.reference

    fun createDraftBill(bill: Bill, onSuccess: () -> Unit, onFail: () -> Unit){
        database.child(draftBillRef).setValue(bill)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener {
                FirebaseCrashlytics.getInstance().recordException(it)
                onFail()
            }
    }

    fun createNewBill(bill: Bill, onSuccess: () -> Unit, onFail: () -> Unit) {
        database.child(billsRef).push().setValue(bill)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener {
                FirebaseCrashlytics.getInstance().recordException(it)
                onFail()
            }
    }

    fun clearDraftBill(
        onSuccess: () -> Unit,
        onFail: () -> Unit,
    ) {
        database.child(draftBillRef).setValue(null)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFail() }
    }

    fun createNewConsumption(consumption: Consumption, onSuccess: () -> Unit, onFail: () -> Unit) {
        database.child(consumptionsRef).push().setValue(consumption)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener {
                FirebaseCrashlytics.getInstance().recordException(it)
                onFail()
            }
    }

    fun getDraftBill(
        onFail: (() -> Unit)? = null,
        onSuccess: (Bill) -> Unit,
    ) {
        database.child(draftBillRef).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(Bill::class.java)?.let {
                    onSuccess(it)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                onFail?.invoke()
            }
        })
    }

    fun getLatestBill(
        onSuccess: (Bill) -> Unit,
        onFail: (() -> Unit) ? = null,
    ) {
        val lastQuery: Query = database.child(billsRef).orderByKey().limitToLast(1)
        lastQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    it.getValue(Bill::class.java)?.let(block = onSuccess) ?: onFail?.invoke()
                }
            }
            override fun onCancelled(databaseError: DatabaseError) { onFail?.invoke() }
        })
    }

    fun getBills(
        onSuccess: (List<Bill>) -> Unit,
        onFail: () -> Unit,
    ) {
        val data = mutableListOf<Bill>()
        val query = database.child(billsRef).orderByChild("createdAt")
        query.limitToLast(20).addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(Bill::class.java)?.let {
                    data.add(it)
                    onSuccess(data)
                } ?: run {
                    onFail()
                }
            }
            override fun onCancelled(error: DatabaseError) { onFail() }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        })
        database.child(billsRef).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) onFail()
            }
            override fun onCancelled(error: DatabaseError) { onFail() }
        })
    }

    fun getConsumers(
        onFail: (() -> Unit)? = null,
        onSuccess: (List<Consumer>) -> Unit,
    ) {
        val data = mutableListOf<Consumer>()
        database.child(consumersRef).addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(Consumer::class.java)?.let {
                    data.add(it)
                    onSuccess(data)
                } ?: run {
                    onFail?.invoke()
                }
            }
            override fun onCancelled(error: DatabaseError) { onFail?.invoke() }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        })
        database.child(billsRef).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) onFail?.invoke()
            }
            override fun onCancelled(error: DatabaseError) { onFail?.invoke() }
        })
    }

    fun getConsumerById(
        id: String,
        onComplete: (Consumer?) -> Unit,
    ) {
        val query: Query = database.child(consumersRef).orderByChild("id").equalTo(id)
        query.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    it.getValue(Consumer::class.java)?.let(block = onComplete) ?: onComplete(null)
                }
            }
            override fun onCancelled(error: DatabaseError) { onComplete(null) }
        })
    }

    fun toggleConsumptionPayed(
        id: String,
        value: Boolean,
        onSuccess: () -> Unit,
        onFail: (() -> Unit)? = null,
    ) {
        val query: Query = database.child(consumptionsRef).orderByChild("id").equalTo(id)
        query.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        it.ref.child("payed").setValue(value)
                            .addOnSuccessListener { onSuccess() }
                            .addOnFailureListener { onFail?.invoke() }
                    }
                } else onFail?.invoke()
            }
            override fun onCancelled(error: DatabaseError) { onFail?.invoke() }
        })
    }

    fun updateConsumptionCost(
        id: String,
        value: Double,
        onSuccess: () -> Unit,
        onFail: (() -> Unit)? = null,
    ) {
        val query: Query = database.child(consumptionsRef).orderByChild("id").equalTo(id)
        query.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        it.ref.child("cost").setValue(value)
                            .addOnSuccessListener { onSuccess() }
                            .addOnFailureListener { onFail?.invoke() }
                    }
                } else onFail?.invoke()
            }
            override fun onCancelled(error: DatabaseError) { onFail?.invoke() }
        })
    }

    fun getBillConsumptions(
        id: String,
        onSuccess: (List<Consumption>) -> Unit,
        onFail: () -> Unit,
    ){
        val data = mutableListOf<Consumption>()
        val query: Query = database.child(consumptionsRef).orderByChild("bill").equalTo(id)
        query.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(Consumption::class.java)?.let {
                    data.add(it)
                    onSuccess(data)
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) { onFail() }
        })
        query.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) onSuccess(emptyList())
            }
            override fun onCancelled(error: DatabaseError) { onFail() }
        })
    }

    fun updateFcmToken(id: String) {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            val query: Query = database.child(consumersRef).orderByChild("id").equalTo(id)
            query.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            it.ref.child("token").setValue(token)
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) { }
            })
        }
    }

    fun uploadBillImage(
        uri: Uri,
        onComplete: (String?) -> Unit,
    ) {
        val ref = storage.child(billsRef).child(System.currentTimeMillis().toString())
        val uploadTask = ref.putFile(uri)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onComplete(task.result.toString())
            } else onComplete(null)
        }
    }

    fun uploadConsumptionImage(
        uri: Uri,
        onComplete: (String?) -> Unit,
    ) {
        val ref = storage.child(consumptionsRef).child(System.currentTimeMillis().toString())
        val uploadTask = ref.putFile(uri)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onComplete(task.result.toString())
            } else onComplete(null)
        }
    }
}