const express = require('express');
const apiController = require('../controllers/apiController');

const router = express.Router();

// Define clean REST endpoints for managing API specifications
router.get('/specs', apiController.getApis);
router.get('/specs/:id', apiController.getApiById);
router.post('/specs', apiController.registerApi);

module.exports = router;
