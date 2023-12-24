package com.example.comics.repository

import com.example.comics.util.orNull
import com.example.comics.view.ItemVO

private const val EMPTY = "Sem descricao"

data class ItemModel(
    val data: DataModel
)

data class DataModel(
    val results: List<ResultModel>
)

data class ResultModel(
    val title: String,
    val description: String?,
    val thumbnail: ThumbnailModel
)

data class ThumbnailModel(
    val path: String,
    val extension: String,
)

fun ItemModel.toListItemVO() = data.results.map {
    ItemVO(
        image = "${it.thumbnail.path}.${it.thumbnail.extension}",
        title = it.title,
        subtitle = it.description.orNull { EMPTY }
    )
}