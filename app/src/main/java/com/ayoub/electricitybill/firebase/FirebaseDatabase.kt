package com.ayoub.electricitybill.firebase

import android.app.Application
import com.ayoub.electricitybill.model.Bill
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

private const val billsRef = "Bills"
private const val draftBillRef = "DraftBill"

@Singleton
class FirebaseDatabase @Inject constructor(
    application: Application,
) {
    private val database = Firebase.database.reference

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

    fun getDraftBill(
        onSuccess: (Bill) -> Unit,
        onFail: () -> Unit,
    ) {
        database.child(draftBillRef).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(Bill::class.java)?.let {
                    onSuccess(it)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                onFail()
            }
        })
    }

    fun getBills(
        onSuccess: (List<Bill>) -> Unit,
        onFail: () -> Unit,
    ) {
        val data = mutableListOf<Bill>()
        database.child(billsRef).limitToLast(20).addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(Bill::class.java)?.let {
                    data.add(it)
                    onSuccess(data)
                }
            }
            override fun onCancelled(error: DatabaseError) { onFail() }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        })
    }
}