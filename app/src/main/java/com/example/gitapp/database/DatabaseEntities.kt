package com.example.gitapp.database

import androidx.room.PrimaryKey
import com.example.gitapp.models.Another
import com.example.gitapp.models.GitProperty
import com.example.gitapp.network.GitApiService
import com.example.gitapp.network.GitClient
import retrofit2.Retrofit

//data class GitPropertyList constructor(
//        @PrimaryKey
//        val id: Int,
//        val name: String,
//        val full_name: String?,
//        val owner: Another,
//        val html_url: String?,
//        var description: String?
//
//)
//
//
//fun List<GitPropertyList>.asDomainModel(): List<GitProperty> {
//    return map {
////        DevByteVideo(
////                url = it.url,
////                title = it.title,
////                description = it.description,
////                updated = it.updated,
////                thumbnail = it.thumbnail)
//        GitProperty(
//                id = it.id,
//                name = it.name,
//                full_name = it.full_name,
//                owner = it.owner,
//                html_url = it.html_url,
//                description = it.description)
//    }
//}
