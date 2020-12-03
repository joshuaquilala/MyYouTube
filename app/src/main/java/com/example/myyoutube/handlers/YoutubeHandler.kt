package com.example.myyoutube.handlers


import com.example.myyoutube.models.Youtube
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class YoutubeHandler {
    var database: FirebaseDatabase
    var youtubeVideosReference: DatabaseReference

    init {
        database = FirebaseDatabase.getInstance()
        youtubeVideosReference = database.getReference("youtube_videos")
    }

    fun create(youtube: Youtube): Boolean{
        val id = youtubeVideosReference.push().key
        youtube.id = id

        youtubeVideosReference.child(id!!).setValue(youtube)


        return true
    }

    fun update(youtube: Youtube):Boolean {
        youtubeVideosReference.child(youtube.id!!).setValue(youtube)
        return true
    }

    fun delete(youtube: Youtube): Boolean {
        youtubeVideosReference.child(youtube.id!!).removeValue()
        return true
    }
}