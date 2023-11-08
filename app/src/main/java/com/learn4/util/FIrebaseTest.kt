package com.learn4.util

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class FIrebaseTest {
    val db = FirebaseFirestore.getInstance().collection("AdTest")

    fun add_data(){
        var dataToSave: MutableMap<String, Any> = mutableMapOf()

        dataToSave?.put("num" , "스위치 센서로 LED 켜기")  // String type
        dataToSave?.put("supplies" , listOf<String>("0, 1, 3", "0, 1, 3", "0, 1, 2-1, 3"))   //arrau type


        db.document("1")
            //set("저장할 데이터")
            .set(dataToSave)
            .addOnSuccessListener { documentReference ->
                Log.e("data input", "저장 성공")
            }
            .addOnFailureListener { e ->
                Log.e("data input", "Error adding document", e)
            }
    }

    fun update_data(){
        //field 추가 및 field 값 업데이트 !UPDATE
        db.document("2")
            .update("test", "Test")
            .addOnSuccessListener { Log.e("TAG", "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w("TAG", "Error updating document", e) }
    }

    fun read_data(){
        db.get().addOnCompleteListener {
            if (it.isSuccessful){
                it.getResult().forEach {
                    Log.e("it", it.data.get("deepen_problem2").toString())
                }
            }else{

            }
        }
    }
}