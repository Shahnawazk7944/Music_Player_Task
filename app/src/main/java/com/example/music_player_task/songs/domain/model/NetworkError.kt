package com.example.music_player_task.songs.domain.model

data class NetworkError(
    val error: ApiError,
    val t: Throwable? = null
)
enum class ApiError(val message:String){
    NetworkError("Network Error"),
    UnknownResponse("Unknown Response"),
    UnknownError("Unknown Error"),
}

data class ImageNetworkError(
    val error: ImageApiError,
    val t: String? = null
)
enum class ImageApiError(val message:String){
    Success("Success 200"),
    Failed("Failed NA"),
    UnknownError("Unknown Error"),
}