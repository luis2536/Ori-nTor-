from fastapi import FastAPI, Request
from pydantic import BaseModel
import time
import numpy as np
from sklearn.linear_model import LinearRegression
import uvicorn

app = FastAPI(title="Orion Thor Master Backend")

class TelemetryData(BaseModel):
    node_id: str
    cpu_temp: float
    ram_usage: float
    hashrate: float
    status: str
    timestamp: int

# Simulación de InfluxDB / Time Series storage
telemetry_db = []

# Modelo Predictivo (AI Thermal Throttling Predictor)
model = LinearRegression()
X_train = np.array([[1], [2], [3], [4], [5]]) # Tiempo
y_train = np.array([[40], [45], [55], [62], [70]]) # Temperaturas
model.fit(X_train, y_train)

@app.post("/telemetry")
async def receive_telemetry(data: TelemetryData):
    telemetry_db.append(data.dict())
    
    # Análisis Predictivo Básico (Scikit-Learn)
    # Proyecta la temperatura en los próximos 2 ciclos
    future_time = np.array([[6], [7]])
    predicted_temps = model.predict(future_time)
    
    warning = False
    if predicted_temps[1][0] >= 75.0:
        warning = True
        print(f"[AI ALERT] Nodo {data.node_id} proyectado a sufrir Thermal Throttling en breve. Temp actual: {data.cpu_temp}C")
        
    return {"status": "ok", "ai_throttle_warning": warning}

@app.get("/nodes/status")
async def get_status():
    return {"total_nodes": len(set(d['node_id'] for d in telemetry_db)), "latest_data": telemetry_db[-50:]}

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
