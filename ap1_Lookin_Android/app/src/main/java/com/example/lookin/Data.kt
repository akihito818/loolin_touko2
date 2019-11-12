package com.example.lookin

data class Data(
    val Post: List<Post>,
    val Store: Store
)

data class Post(
    val category: String,
    val likeCount: Int,
    val likeState: Boolean,
    val picture: String,
    val tag: String,
    val text: String
)

data class Store(
    val businessHours: String,
    val name: String,
    val picture: String
)