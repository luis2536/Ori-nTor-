require('dotenv').config();
const express = require('express');
const { setupSecurity } = require('./middleware/security');
const { errorHandler } = require('./middleware/errorHandler');
const apiRoutes = require('./routes/apiRoutes');

const app = express();
const PORT = process.env.PORT || 5000;

// Parse incoming payload JSON
app.use(express.json());

// Set up security layers (Helmet, CORS, RateLimiting)
setupSecurity(app);

// Simple health-check endpoint
app.get('/health', (req, res) => {
    res.status(200).json({
        status: 'healthy',
        timestamp: new Date().toISOString()
    });
});

// Register primary API Routes
app.use('/api', apiRoutes);

// central error handling middleware
app.use(errorHandler);

// Start server
app.listen(PORT, () => {
    console.log(`[DevDex Server] running in ${process.env.NODE_ENV || 'development'} mode on port ${PORT}`);
});

module.exports = app;
