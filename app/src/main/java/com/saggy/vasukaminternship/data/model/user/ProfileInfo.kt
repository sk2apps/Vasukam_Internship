package com.saggy.vasukaminternship.data.model.user

import java.io.Serializable

data class ProfileInfo(
    var name: String = "",
    var uid: String = "",
    var username: String = "",
    var profile_pic: String = "",
    var location: String = "",
    var timezone: String = "",
    var gender: String = "",
    var title: String = "",
    var age: String = "",
    var phoneNumber: String = "",
    var emailId: String = "",
    var dateOfBirth: String = "",
    var date_of_registration: String = "",
    var description: String = "",
    var followers: Int = 0,
    var following: Int = 0,
    var total_posts: Int = 0,
    var allPosts: UserPost? = null,
    var last_online: String = "",
    var wallet_Money: Double = 0.0,
    var credibility_score: Int = 100
) : Serializable

data class UserPost(
    var feed: List<feedPost>? = null,
    var live: List<livePost>? = null,
    var flicks: List<flicksPost>? = null,
    var buy: List<buyPost>? = null,
    var sold: List<soldPost>? = null
)

data class feedPost(
    var id: String = ""
)

data class livePost(
    var id: String = ""
)

data class flicksPost(
    var id: String = ""
)

data class buyPost(
    var id: String = ""
)

data class soldPost(
    var id: String = ""
)