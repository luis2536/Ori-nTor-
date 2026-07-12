/**
 * Global centralized error handling middleware.
 * @param {Error} err - Error object
 * @param {import('express').Request} req - Express request object
 * @param {import('express').Response} res - Express response object
 * @param {import('express').NextFunction} next - Next middleware function
 */
function errorHandler(err, req, res, next) {
    const statusCode = err.statusCode || 500;
    const isProduction = process.env.NODE_ENV === 'production';

    // Log the error for internal auditing (could connect to a logging service like Winston or Datadog)
    console.error(`[Error] [${req.method}] ${req.originalUrl}: ${err.message}`, err.stack);

    res.status(statusCode).json({
        status: 'error',
        statusCode,
        message: err.message || 'Internal Server Error',
        ...(isProduction ? {} : { stack: err.stack })
    });
}

module.exports = { errorHandler };
