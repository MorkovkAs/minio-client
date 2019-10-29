package ru.morkovka.minio.client.impl

import io.minio.MinioClient
import io.minio.Result
import io.minio.messages.Item
import ru.morkovka.minio.client.S3Client
import ru.morkovka.minio.client.S3Object
import java.io.File

class S3MinioClient(url: String, publicKey: String, secureKey: String) : S3Client {
    private var minioClient: MinioClient = MinioClient(url, publicKey, secureKey)

    override fun getS3Client(): MinioClient {
        return this.minioClient
    }

    override fun isBucketExists(bucketName: String): Boolean {
        return this.getS3Client().bucketExists(bucketName)
    }

    override fun isObjectExists(bucketName: String, objectName: String): Boolean {
        return try {
            this.getS3Client().statObject(bucketName, objectName) != null
        } catch (ex: Exception) {
            //println(ex.toString())
            false
        }
    }

    override fun getObjects(bucketName: String): List<S3Object> {
        val objectsIterator: Iterable<Result<Item>> = this.getS3Client().listObjects(bucketName)
        val result: ArrayList<S3Object> = ArrayList()
        objectsIterator.forEach {
            run {
                val i: Item = it.get()
                result.add(S3Object(bucketName, i.objectName(), i.objectSize()))
            }
        }
        return result
    }

    override fun uploadObject(bucketName: String, objectName: String): Boolean {
        val file = File(objectName)
        this.getS3Client().putObject(bucketName, file.name, file.absolutePath)
        return true
    }

    override fun downloadObject(bucketName: String, objectName: String): Boolean {
        this.getS3Client().getObject(bucketName, objectName)
        return true
    }

    override fun removeObject(bucketName: String, objectName: String): Boolean {
        this.getS3Client().removeObject(bucketName, objectName)
        return true
    }

    override fun removeBucket(bucketName: String): Boolean {
        this.getS3Client().removeBucket(bucketName)
        return true
    }

    override fun cleanAllBuckets(bucketName: String, removeBucket: Boolean) {
        minioClient.listBuckets().listIterator().forEach { bucket ->
            run {
                cleanBucket(bucket.name(), removeBucket)
            }
        }
    }

    override fun cleanBucket(bucketName: String, removeBucket: Boolean) {
        println("Start of bucket cleaning: [$bucketName] .....")
        var bucketEmpty: Boolean
        var itemCounter = 0
        if (this.getS3Client().bucketExists(bucketName)) {
            do {
                val listObjects = this.getS3Client().listObjects(bucketName)
                if (listObjects.count() == 0)
                    bucketEmpty = true
                else {
                    bucketEmpty = false
                    listObjects.forEach {
                        run {
                            val fileName: String = it.get().objectName()
                            this.getS3Client().removeObject(bucketName, fileName)
                            println("File is Removed : [$bucketName] [$fileName], itemCounter = $itemCounter")
                            itemCounter++
                        }
                    }
                }
            } while (!bucketEmpty)
            println("Bucket cleaned OK: [$bucketName] ")
            println("$itemCounter item(s) removed from bucked [$bucketName]")

            if (removeBucket) {
                println("Start of bucket removing: [$bucketName] .....")
                this.getS3Client().removeBucket(bucketName)
                println("Bucket removed OK: [$bucketName]")
            }
        } else {
            println("Bucket [$bucketName] is not found")
        }
    }
}
