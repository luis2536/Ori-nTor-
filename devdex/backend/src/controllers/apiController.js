const devDexService = require('../services/devDexService');

/**
 * Controller to handle all operations regarding the API specifications.
 */
class ApiController {
    /**
     * GET /api/specs - Get list of APIs with filters
     */
    async getApis(req, res, next) {
        try {
            const { category, search } = req.query;
            const apis = await devDexService.getAllApis({ category, search });
            res.status(200).json({
                status: 'success',
                results: apis.length,
                data: apis
            });
        } catch (error) {
            next(error);
        }
    }

    /**
     * GET /api/specs/:id - Fetch details of an API spec
     */
    async getApiById(req, res, next) {
        try {
            const { id } = req.params;
            const api = await devDexService.getApiById(id);
            if (!api) {
                const error = new Error(`API with ID '${id}' not found.`);
                error.statusCode = 404;
                throw error;
            }
            res.status(200).json({
                status: 'success',
                data: api
            });
        } catch (error) {
            next(error);
        }
    }

    /**
     * POST /api/specs - Register a new API spec
     */
    async registerApi(req, res, next) {
        try {
            const newApi = await devDexService.registerApi(req.body);
            res.status(201).json({
                status: 'success',
                message: 'API Specification registered successfully.',
                data: newApi
            });
        } catch (error) {
            next(error);
        }
    }
}

module.exports = new ApiController();
