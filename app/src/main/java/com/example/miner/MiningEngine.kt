package com.example.miner

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlin.random.Random

data class MiningStats(
    val hashrate: Int = 0,
    val cpuTemp: Int = 35,
    val cpuUsage: Float = 0f,
    val ramUsage: Float = 0f,
    val gpuUsage: Float = 0f,
    val sharesAccepted: Int = 0,
    val estimatedEarnings: Float = 0f,
    val isConnected: Boolean = false
)

object MiningEngine {
    private val _stats = MutableStateFlow(MiningStats())
    val stats: StateFlow<MiningStats> = _stats.asStateFlow()

    private var isEngineRunning = false
    private var powerLevel = 80f

    suspend fun startEngine(power: Float) = withContext(Dispatchers.Default) {
        isEngineRunning = true
        powerLevel = power
        _stats.value = _stats.value.copy(isConnected = true)

        while (isEngineRunning) {
            // High-performance simulation loop
            val baseHash = (2500f * (powerLevel / 100f)).toInt()
            val newHashrate = Random.nextInt(baseHash, baseHash + 1500)
            
            val temp = 35 + (45f * (powerLevel / 100f)).toInt() + Random.nextInt(-5, 5)
            val cpu = powerLevel + Random.nextInt(-5, 5).toFloat()
            val ram = Random.nextInt(60, 85).toFloat()
            val gpu = Random.nextInt(10, 35).toFloat()
            
            val shareFound = Random.nextFloat() > 0.4f
            val currentStats = _stats.value
            
            _stats.value = currentStats.copy(
                hashrate = newHashrate,
                cpuTemp = temp,
                cpuUsage = cpu,
                ramUsage = ram,
                gpuUsage = gpu,
                sharesAccepted = if (shareFound) currentStats.sharesAccepted + 1 else currentStats.sharesAccepted,
                estimatedEarnings = if (shareFound) currentStats.estimatedEarnings + (0.000015f * (powerLevel / 50f)) else currentStats.estimatedEarnings
            )
            
            // Reduced latency loop for "higher performance" simulation
            delay(1000)
        }
    }

    fun updatePower(power: Float) {
        powerLevel = power
    }

    fun stopEngine() {
        isEngineRunning = false
        _stats.value = _stats.value.copy(isConnected = false, hashrate = 0, cpuUsage = 0f)
    }
}
