package com.example.gitapp.models

import android.os.Parcelable
import androidx.room.*
import com.google.gson.annotations.JsonAdapter
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
@JsonClass(generateAdapter = true)
data class GitProperty(
    @PrimaryKey
        val id: Int,
    val name: String,
    val full_name: String?,
    @Embedded
    val owner: Another?,
    val html_url: String?,
    var description: String?//some values are null

) : Parcelable//{
//
//        class OwnerTypeConverter{
//                @TypeConverter
//                fun AnotherToString(input: Another?): String? {
//
//                        return Moshi.Builder().build().adapter(Another::class.java).toJson(input)
//
//                }
//                @TypeConverter
//                fun StringToAnother(input:String?): Another?{
//
//                        return input?.let { Moshi.Builder().build().adapter(Another::class.java).fromJson(it) }
//                }
//        }
//}



@Parcelize
data class Another(
        val login: String,
        val type: String?,
        @Json(name = "avatar_url") val imgSrcUrl: String
) : Parcelable


//@PrimaryKey
//private val uId = 0
//private val uName: String? = null
//private val uPets: ArrayList<String> = ArrayList()
//fun User() {}
//fun User(
//        id: Int,
//        name: String?,
//        pets: List<String?>?
//) {
//        this.uId = id
//        this.uName = name
//        this.uPets.addAll(pets)
//}


//
//@TypeConverters(SourceTypeConverter::class)
//@ColumnInfo(name = "source")
//var source: Source?
//                ){
//                        class SourceTypeConverter {
//                                @TypeConverter
//                                fun fromDeliveryExchangeList(source: Source?): String? {
//                                        if (source == null) {
//                                                return null
//                                        }
//                                        val gson = Gson()
//                                        val type = object : TypeToken<Source>() {
//
//                                        }.type
//                                        return gson.toJson(source, type)
//                                }
//
//                                @TypeConverter
//                                fun toDeliveryExchangeList(source: String?): Source? {
//                                        if (source == null) {
//                                                return null
//                                        }
//                                        val gson = Gson()
//                                        val type = object : TypeToken<Source>() {
//
//                                        }.type
//                                        return gson.fromJson(source, type)
//                                }
////        }