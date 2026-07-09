import React, { useState, useEffect } from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

// Paleta de colores agresiva
const THEME = {
  bg: '#001111',
  panelBg: '#002222',
  cyan: '#003333',
  neonCyan: '#00ffff',
  alertRed: '#ff0033'
};

export default function OrionDashboard() {
  const [data, setData] = useState([]);
  const [alerts, setAlerts] = useState([]);

  useEffect(() => {
    // Simulación de WebSocket para datos en tiempo real
    const interval = setInterval(() => {
      const temp = 40 + Math.random() * 40;
      setData(prev => [...prev.slice(-20), { time: new Date().toLocaleTimeString(), temp, hashrate: 1500 - temp*5 }]);
      
      if (temp > 75) {
        setAlerts(prev => [{id: Date.now(), msg: `[CRÍTICO] Nodo-702 superó 75°C. Estrangulamiento térmico inminente.`}, ...prev.slice(0, 4)]);
      }
    }, 2000);
    return () => clearInterval(interval);
  }, []);

  return (
    <div style={{ backgroundColor: THEME.bg, color: THEME.neonCyan, minHeight: '100vh', padding: '20px', fontFamily: 'monospace' }}>
      <header style={{ borderBottom: `2px solid ${THEME.cyan}`, paddingBottom: '10px', marginBottom: '20px' }}>
        <h1 style={{ color: THEME.alertRed, margin: 0, textShadow: `0 0 10px ${THEME.alertRed}` }}>ORION THOR // OMNI-MAX DASHBOARD</h1>
        <p style={{ margin: 0 }}>ESTADO DEL CLÚSTER: EN LÍNEA | NODOS: 12,042</p>
      </header>

      <div style={{ display: 'grid', gridTemplateColumns: '2fr 1fr', gap: '20px' }}>
        <section style={{ backgroundColor: THEME.panelBg, padding: '20px', border: `1px solid ${THEME.cyan}` }}>
          <h3>TELEMETRÍA GLOBAL (TEMPERATURA)</h3>
          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={data}>
              <CartesianGrid strokeDasharray="3 3" stroke={THEME.cyan} />
              <XAxis dataKey="time" stroke={THEME.neonCyan} />
              <YAxis stroke={THEME.neonCyan} />
              <Tooltip contentStyle={{ backgroundColor: THEME.bg, border: `1px solid ${THEME.cyan}` }} />
              <Line type="monotone" dataKey="temp" stroke={THEME.alertRed} strokeWidth={2} dot={false} />
            </LineChart>
          </ResponsiveContainer>
        </section>

        <section style={{ backgroundColor: THEME.panelBg, padding: '20px', border: `1px solid ${THEME.alertRed}` }}>
          <h3 style={{ color: THEME.alertRed }}>AI PREDICTIVE ALERTS</h3>
          <ul style={{ listStyle: 'none', padding: 0 }}>
            {alerts.map(a => (
              <li key={a.id} style={{ color: THEME.alertRed, borderBottom: `1px solid ${THEME.alertRed}`, padding: '10px 0' }}>
                {a.msg}
              </li>
            ))}
          </ul>
        </section>
      </div>

      <div style={{ marginTop: '20px', display: 'flex', gap: '10px' }}>
        <button style={{ backgroundColor: THEME.alertRed, color: 'white', border: 'none', padding: '15px 30px', fontWeight: 'bold', cursor: 'pointer' }}>
          PAUSAR MINERÍA (TODOS LOS NODOS)
        </button>
        <button style={{ backgroundColor: THEME.cyan, color: THEME.neonCyan, border: `1px solid ${THEME.neonCyan}`, padding: '15px 30px', fontWeight: 'bold', cursor: 'pointer' }}>
          ACTUALIZAR SCRIPT
        </button>
      </div>
    </div>
  );
}
