package com.saggy.vasukaminternship.data.model.post

data class Comment(
    var commentId: String = "",
    var postId : String = "",
    var userUid: String = "",
    var msg: String = "",
    var timestamp: String = ""
)
