package com.example.criminalintent


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time

import java.util.*
@Entity
data class Crime(@PrimaryKey val id: UUID = UUID.randomUUID(),
                 var title: String = "",
                 var date: Date = Date(),
                 var isSolved: Boolean = false,
                 var suspect: String = ""
)




{

    val photoFileName
        get() = "IMG_$id.jpg"

    override fun equals(other: Any?): Boolean {
       if (javaClass!= other?.javaClass){

           return false
       }
        other as Crime

        if (id != other.id){
         return false

        }
        if (title != other.title){
         return false

        }
        if (date != other.date){
            return false

        }
        if (isSolved != other.isSolved){
            return false

        }
        if (suspect != other.suspect){
            return false

        }

        return true
    }

    override fun toString(): String {
        return "Crime(id=$id,title=$title,date=$date,isSolved=$isSolved)"
    }

    data class  Time(var time:Time)
}


