package com.saggy.vasukaminternship.data.model.post

data class Post (
    var post_id: String = "",
    var audio: List<String>? = null,
    var title: String = "",
    var desc: String = "",
    var imageUrl: List<String>? = null,
    var video: List<String>? = null,
    var owner: List<Owner>? = null,
    var postType: String = "",
    var timestamp: String = "",
    var shares: Int = 0,
    var likes: Int = 0,
    var comment: Int = 0,
    var on_market: Boolean = false,
    var share_allowed: Boolean = true,
    var comment_allowed: Boolean = true,
    var where: WherePost? = null,
    var CreatorUid: String = "",
    var location: Location? = null
)

data class Owner(
    var uid: String = ""
)

data class Location(
    var country: String = "",
    var city: String = "",
    var street: String = "",
    var zipCode: String =""
)

data class WherePost(
    var group_id: String = "",
)