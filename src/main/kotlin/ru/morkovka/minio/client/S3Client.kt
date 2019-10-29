package ru.morkovka.minio.client

import io.minio.MinioClient

interface S3Client {
    fun getS3Client(): MinioClient
    fun isObjectExists(bucketName: String, objectName: String): Boolean
    fun isBucketExists(bucketName: String): Boolean
    fun uploadObject(bucketName: String, objectName: String): Boolean
    fun downloadObject(bucketName: String, objectName: String): Boolean
    fun getObjects(bucketName: String): List<S3Object>
    fun removeObject(bucketName: String, objectName: String): Boolean
    fun removeBucket(bucketName: String): Boolean
    fun cleanAllBuckets(bucketName: String, removeBucket: Boolean)
    fun cleanBucket(bucketName: String, removeBucket: Boolean)
}