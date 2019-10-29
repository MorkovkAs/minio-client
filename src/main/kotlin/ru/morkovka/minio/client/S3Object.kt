package ru.morkovka.minio.client

data class S3Object (
    val bucketName : String,
    val objectName: String,
    val size: Long
)