package com.saggy.vasukaminternship.models

import java.io.Serializable

data class Messages(
    var from: String,
    var message: String,
    var type: String,
    var to: String,
    var messageID: String,
    var time: String,
    var date: String
) : Serializable