package com.saggy.vasukaminternship.utils

import android.os.Environment

object FilePaths {
    //"storage/emulated/0"
    var ROOT_DIR: String = Environment.getExternalStorageDirectory().path

    var PICTURES = "$ROOT_DIR/Pictures"
    var CAMERA = "$ROOT_DIR/DCIM"
    var WHATSAPP = "$ROOT_DIR/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Images"
    var STORIES = "$ROOT_DIR/Stories"

    var FIREBASE_STORY_STORAGE = "stories/users"
    var FIREBASE_IMAGE_STORAGE = "photos/users/"

}