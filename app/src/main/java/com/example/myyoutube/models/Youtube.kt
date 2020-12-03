package com.example.myyoutube.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Youtube(var id: String? = "", var title: String? = "", var link: String? = "", var rank: Int = 0, var reason: String? = "") {
    override fun toString(): String {
        return "($rank)\t $title \nLink: $link\nReason: $reason"
    }


}