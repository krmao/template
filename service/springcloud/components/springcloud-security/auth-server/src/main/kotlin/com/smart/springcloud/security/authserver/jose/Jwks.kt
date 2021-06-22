package com.smart.springcloud.security.authserver.jose

import com.nimbusds.jose.jwk.Curve
import com.nimbusds.jose.jwk.ECKey
import com.nimbusds.jose.jwk.OctetSequenceKey
import com.nimbusds.jose.jwk.RSAKey
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.ECPublicKey
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*

object Jwks {
    @JvmStatic
    fun generateRsa(): RSAKey {
        val keyPair = KeyGeneratorUtils.generateRsaKey()
        val publicKey = keyPair.public as RSAPublicKey
        val privateKey = keyPair.private as RSAPrivateKey
        return RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID(UUID.randomUUID().toString())
            .build()
    }

    fun generateEc(): ECKey {
        val keyPair = KeyGeneratorUtils.generateEcKey()
        val publicKey = keyPair.public as ECPublicKey
        val privateKey = keyPair.private as ECPrivateKey
        val curve = Curve.forECParameterSpec(publicKey.params)
        return ECKey.Builder(curve, publicKey)
            .privateKey(privateKey)
            .keyID(UUID.randomUUID().toString())
            .build()
    }

    fun generateSecret(): OctetSequenceKey {
        val secretKey = KeyGeneratorUtils.generateSecretKey()
        return OctetSequenceKey.Builder(secretKey)
            .keyID(UUID.randomUUID().toString())
            .build()
    }
}
