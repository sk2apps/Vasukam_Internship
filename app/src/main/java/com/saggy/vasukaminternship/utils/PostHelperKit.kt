package com.saggy.vasukaminternship.utils

import android.net.Uri
import com.saggy.vasukaminternship.data.model.post.Post

public object PostHelperKit {

    var post: Post? = null

    var selectedImage: String? = null
    var selectedImageURI: Uri? = null

    fun clearPostData() {
        post = null
    }

    fun clearSelectedImage() {
        selectedImage = null
        selectedImageURI = null
    }

}