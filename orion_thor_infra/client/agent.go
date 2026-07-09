package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
	"os"
	"runtime"
	"strconv"
	"strings"
	"time"
)

const (
	MasterNodeURL = "http://master-node.orionthor.local/telemetry"
	MaxTemp       = 75.0 // Grados Celsius
)

type Telemetry struct {
	NodeID    string  `json:"node_id"`
	CPUTemp   float64 `json:"cpu_temp"`
	RAMUsage  float64 `json:"ram_usage"`
	Hashrate  float64 `json:"hashrate"`
	Status    string  `json:"status"`
	Timestamp int64   `json:"timestamp"`
}

func getCPUTemp() float64 {
	// Lee temperatura de thermal zone en Android/Linux
	data, err := ioutil.ReadFile("/sys/class/thermal/thermal_zone0/temp")
	if err != nil {
		return 45.0 // Fallback simulado
	}
	tempInt, _ := strconv.Atoi(strings.TrimSpace(string(data)))
	return float64(tempInt) / 1000.0
}

func getRAMUsage() float64 {
	var m runtime.MemStats
	runtime.ReadMemStats(&m)
	return float64(m.Alloc) / 1024 / 1024 // MB
}

func main() {
	nodeID, _ := os.Hostname()
	status := "MINING"
	hashrate := 1500.0 // Hashes/s base

	for {
		temp := getCPUTemp()
		ram := getRAMUsage()

		if temp > MaxTemp {
			status = "THROTTLED"
			hashrate = 0.0
			log.Printf("[WARNING] Temperatura %.1fC excede límite. Pausando minería...", temp)
		} else {
			status = "MINING"
			hashrate = 1500.0 - (temp * 10) // Ligera reducción con calor
		}

		payload := Telemetry{
			NodeID:    nodeID,
			CPUTemp:   temp,
			RAMUsage:  ram,
			Hashrate:  hashrate,
			Status:    status,
			Timestamp: time.Now().Unix(),
		}

		jsonData, _ := json.Marshal(payload)
		resp, err := http.Post(MasterNodeURL, "application/json", bytes.NewBuffer(jsonData))
		
		if err != nil {
			log.Printf("[ERROR] Conexión con Nodo Maestro fallida: %v", err)
		} else {
			resp.Body.Close()
			log.Printf("[TELEMETRY] Enviada - Temp: %.1fC | Hashrate: %.1f", temp, hashrate)
		}

		time.Sleep(5 * time.Second) // Ligero para minimizar uso de red
	}
}
