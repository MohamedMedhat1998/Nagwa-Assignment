package com.mohamed.medhat.nagwaassignment.model

import com.google.gson.annotations.SerializedName

/**
 * A POJO for the json data item coming from the server response.
 */
data class DataItem(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("url")
    val url: String
)
