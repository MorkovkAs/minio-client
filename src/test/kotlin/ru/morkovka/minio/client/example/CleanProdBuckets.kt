package ru.morkovka.minio.client.example

import org.junit.Test
import ru.morkovka.minio.client.S3Client
import ru.morkovka.minio.client.S3ClientFactory

class CleanProdBuckets {

    private val s3ClientPers: S3Client = S3ClientFactory().createInstanceRemoteNodePers()
    private val s3ClientDepers: S3Client = S3ClientFactory().createInstanceRemoteNodeDepers()

    @Test
    fun doCleanProdBucketPers() {
        val bucketNamePers = "personal-data"
        s3ClientPers.cleanBucket(bucketNamePers, removeBucket = false)
    }

    @Test
    fun doCleanProdBucketDepers() {
        val bucketNameDepers = "registry-record"
        s3ClientDepers.cleanBucket(bucketNameDepers, removeBucket = false)
    }
}