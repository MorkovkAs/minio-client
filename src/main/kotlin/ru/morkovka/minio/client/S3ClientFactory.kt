package ru.morkovka.minio.client

import ru.morkovka.minio.client.impl.S3MinioClient

class S3ClientFactory {
    private val urlDepers = ""
    private val urlPers = ""
    private val remoteLogin: String = ""
    private val remotePass: String = ""


    fun createInstanceRemoteNodeDepers(): S3Client {
        return S3MinioClient(urlDepers, remoteLogin, remotePass)
    }

    fun createInstanceRemoteNodePers(): S3Client {
        return S3MinioClient(urlPers, remoteLogin, remotePass)
    }
}
