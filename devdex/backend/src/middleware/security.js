const helmet = require('helmet');
const cors = require('cors');
const rateLimit = require('express-rate-limit');

/**
 * Configure global security middlewares for Express.
 * @param {import('express').Express} app - Express application instance
 */
function setupSecurity(app) {
    // 1. HTTP Headers Security via Helmet
    app.use(helmet());

    // 2. CORS Policy Configuration
    const corsOptions = {
        origin: process.env.CORS_ORIGIN || '*',
        methods: ['GET', 'POST', 'PUT', 'DELETE', 'OPTIONS'],
        allowedHeaders: ['Content-Type', 'Authorization'],
        credentials: true
    };
    app.use(cors(corsOptions));

    // 3. API Rate Limiting to prevent DoS attacks
    const limiter = rateLimit({
        windowMs: parseInt(process.env.RATE_LIMIT_WINDOW_MS, 10) || 15 * 60 * 1000, // default 15 mins
        max: parseInt(process.env.RATE_LIMIT_MAX_REQUESTS, 10) || 100, // limit each IP to 100 requests per window
        message: {
            status: 429,
            message: 'Too many requests from this IP, please try again later.'
        },
        standardHeaders: true,
        legacyHeaders: false
    });
    
    // Apply rate limiter to API routes
    app.use('/api/', limiter);
}

module.exports = { setupSecurity };
