package com.example.kotlinstudy.config


import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties
import org.jasypt.encryption.StringEncryptor
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

@Configuration
@EnableEncryptableProperties
class JasyptConfig {
    @Value("\${jasypt.encryptor.algorithm}")
    private val algorithm: String? = null

    @Value("\${jasypt.encryptor.pool-size}")
    private val poolSize = 0

    @Value("\${jasypt.encryptor.string-output-type}")
    private val stringOutputType: String? = null

    @Value("\${jasypt.encryptor.key-obtention-iterations}")
    private val keyObtentionIterations = 0

    val log = LoggerFactory.getLogger(javaClass)


    @Bean
    fun jasyptStringEncryptor(): StringEncryptor {
        val encryptor = PooledPBEStringEncryptor()
        encryptor.setPoolSize(poolSize)
        encryptor.setAlgorithm(algorithm)
        encryptor.setPassword(jasyptEncryptorPassword)
        encryptor.setStringOutputType(stringOutputType)
        encryptor.setKeyObtentionIterations(keyObtentionIterations)

        return encryptor
    }

    private val jasyptEncryptorPassword: String
        private get() = try {
            val resource = ClassPathResource("jasypt-encryptor-password.txt")
            Files.readAllLines(Paths.get(resource.uri)).stream().collect(Collectors.joining(""))
        } catch (e: IOException) {
            throw RuntimeException("Not found Jasypt password file.")
        }
}