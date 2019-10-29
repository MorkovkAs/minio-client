package ru.morkovka.minio.client.example

import io.minio.MinioClient
import org.junit.Test
import ru.morkovka.minio.client.S3Client
import ru.morkovka.minio.client.S3ClientFactory
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.util.UUID.randomUUID
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CreateAndDeleteSubfolderTest {

    private val s3Client: S3Client = S3ClientFactory().createInstanceRemoteNodeDepers()

    @Test
    fun doCreateAndDeleteSubfolderTest() {
        val minioClient: MinioClient = s3Client.getS3Client()

        val bucketName = randomUUID().toString()
        assertFalse(s3Client.isBucketExists(bucketName))
        minioClient.makeBucket(bucketName)
        assertTrue(s3Client.isBucketExists(bucketName))
        assertEquals(0, s3Client.getObjects(bucketName).size)

        val objectName = randomUUID().toString()
        assertFalse(s3Client.isObjectExists(bucketName, objectName))
        makeObject(minioClient, bucketName, objectName)
        assertTrue(s3Client.isObjectExists(bucketName, objectName))
        assertEquals(1, s3Client.getObjects(bucketName).size)

        s3Client.removeObject(bucketName, objectName)
        assertFalse(s3Client.isObjectExists(bucketName, objectName))
        assertEquals(0, s3Client.getObjects(bucketName).size)

        s3Client.removeBucket(bucketName)
        assertFalse(s3Client.isBucketExists(bucketName))
    }

    private fun makeObject(minioClient: MinioClient, bucketName: String, objectName: String) {
        val buffer = ByteArray(8192)
        val inputStream: InputStream = ByteArrayInputStream(buffer)
        val contentType = "application/octet-stream"
        minioClient.putObject(bucketName, objectName, inputStream, contentType)
    }
}

