package com.example.network

import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Zero-Trust Network Configuration
 */
object NetworkConfig {
    
    // Example domain for Cerebro Engine
    private const val HOSTNAME = "api.orionthor.io"
    
    // Example pins
    private const val PIN_1 = "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA="
    
    val certificatePinner = CertificatePinner.Builder()
        .add(HOSTNAME, PIN_1)
        .build()

    val secureOkHttpClient: OkHttpClient = OkHttpClient.Builder()
        .certificatePinner(certificatePinner)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()
}
