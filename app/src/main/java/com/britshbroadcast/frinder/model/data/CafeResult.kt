package com.britshbroadcast.frinder.model.data

data class CafeResult(
    val html_attributions: List<Any>,
    val next_page_token: String,
    val results: List<Result>,
    val status: String
)