/**
 * @typedef {Object} ApiSpecification
 * @property {string} id - Unique identifier
 * @property {string} name - API Name
 * @property {string} category - Category (e.g., AI, Payment, Auth, Weather)
 * @property {string} description - Brief summary of what the API does
 * @property {string} endpoint - Base endpoint URL
 * @property {string} docsUrl - Official documentation URL
 * @property {string[]} authTypes - Supported authorization methods (e.g., APIKey, OAuth2)
 * @property {boolean} isFree - Whether the API has a free tier
 * @property {string} status - Operational status (active, deprecated, beta)
 */

class DevDexService {
    constructor() {
        /** @type {ApiSpecification[]} */
        this.apis = [
            {
                id: "gemini-api",
                name: "Google Gemini API",
                category: "AI & Machine Learning",
                description: "Access Google's state-of-the-art generative AI models for text generation, multimodal prompts, and structural reasoning.",
                endpoint: "https://generativelanguage.googleapis.com",
                docsUrl: "https://ai.google.dev/docs",
                authTypes: ["APIKey"],
                isFree: true,
                status: "active"
            },
            {
                id: "stripe-api",
                name: "Stripe API",
                category: "Payments",
                description: "Industry-standard suite of APIs powering payment processing, checkout flows, and complex subscription billing cycles.",
                endpoint: "https://api.stripe.com/v1",
                docsUrl: "https://stripe.com/docs/api",
                authTypes: ["APIKey", "OAuth2"],
                isFree: false,
                status: "active"
            },
            {
                id: "github-api",
                name: "GitHub REST API",
                category: "Developer Tools",
                description: "Integrate, automate, and build on top of repositories, organizations, issues, commits, and user profiles.",
                endpoint: "https://api.github.com",
                docsUrl: "https://docs.github.com/rest",
                authTypes: ["OAuth2", "APIKey"],
                isFree: true,
                status: "active"
            },
            {
                id: "openweathermap-api",
                name: "OpenWeatherMap API",
                category: "Weather & Environment",
                description: "Get current weather data, minute forecasts, hourly predictions, historical weather charts, and severe alerts globally.",
                endpoint: "https://api.openweathermap.org/data/2.5",
                docsUrl: "https://openweathermap.org/api",
                authTypes: ["APIKey"],
                isFree: true,
                status: "active"
            }
        ];
    }

    /**
     * Retrieve all API specifications with optional querying.
     * @param {Object} [filters] - Filter parameters
     * @param {string} [filters.category] - Filter by category
     * @param {string} [filters.search] - Case insensitive text search on name/description
     * @returns {Promise<ApiSpecification[]>}
     */
    async getAllApis(filters = {}) {
        let results = [...this.apis];

        if (filters.category) {
            results = results.filter(api => api.category.toLowerCase() === filters.category.toLowerCase());
        }

        if (filters.search) {
            const query = filters.search.toLowerCase();
            results = results.filter(api => 
                api.name.toLowerCase().includes(query) || 
                api.description.toLowerCase().includes(query)
            );
        }

        return results;
    }

    /**
     * Fetch a single API specification by ID.
     * @param {string} id - The API specification ID
     * @returns {Promise<ApiSpecification|null>}
     */
    async getApiById(id) {
        const api = this.apis.find(api => api.id === id);
        return api || null;
    }

    /**
     * Register a new API specification.
     * @param {Omit<ApiSpecification, 'id'>} apiData - API fields
     * @returns {Promise<ApiSpecification>}
     */
    async registerApi(apiData) {
        if (!apiData.name || !apiData.endpoint) {
            throw new Error("Missing required fields: 'name' and 'endpoint' are mandatory.");
        }

        const id = apiData.name.toLowerCase().replace(/[^a-z0-9]+/g, '-').replace(/(^-|-$)/g, '');
        
        // Prevent duplicate IDs
        if (this.apis.some(api => api.id === id)) {
            throw new Error(`An API with the name '${apiData.name}' already exists.`);
        }

        const newApi = {
            id,
            name: apiData.name,
            category: apiData.category || "General",
            description: apiData.description || "",
            endpoint: apiData.endpoint,
            docsUrl: apiData.docsUrl || "",
            authTypes: apiData.authTypes || ["None"],
            isFree: apiData.isFree === undefined ? true : apiData.isFree,
            status: apiData.status || "active"
        };

        this.apis.push(newApi);
        return newApi;
    }
}

module.exports = new DevDexService();
