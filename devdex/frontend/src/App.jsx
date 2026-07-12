import React, { useState, useEffect } from 'react';
import ApiCard from './components/ApiCard';

const INITIAL_APIS = [
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
    }
];

export default function App() {
    const [apis, setApis] = useState(INITIAL_APIS);
    const [search, setSearch] = useState('');
    const [category, setCategory] = useState('All');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    // Form registration state
    const [name, setName] = useState('');
    const [endpoint, setEndpoint] = useState('');
    const [description, setDescription] = useState('');
    const [apiCategory, setApiCategory] = useState('AI & Machine Learning');
    const [docsUrl, setDocsUrl] = useState('');
    const [isFree, setIsFree] = useState(true);

    const categories = ['All', 'AI & Machine Learning', 'Payments', 'Developer Tools', 'Weather & Environment', 'General'];

    const fetchApis = async () => {
        setLoading(true);
        setError(null);
        try {
            const queryParams = new URLSearchParams();
            if (category !== 'All') queryParams.append('category', category);
            if (search) queryParams.append('search', search);

            const response = await fetch(`/api/specs?${queryParams.toString()}`);
            if (!response.ok) throw new Error('Could not contact DevDex server API. Serving from local cache.');
            const resData = await response.json();
            if (resData.status === 'success') {
                setApis(resData.data);
            }
        } catch (err) {
            console.warn("Server unavailable, relying on Local-First dataset", err);
            // Local fallback logic
            let localList = [...INITIAL_APIS];
            if (category !== 'All') {
                localList = localList.filter(api => api.category === category);
            }
            if (search) {
                const term = search.toLowerCase();
                localList = localList.filter(api => 
                    api.name.toLowerCase().includes(term) || 
                    api.description.toLowerCase().includes(term)
                );
            }
            setApis(localList);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchApis();
    }, [search, category]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        const newApiSpec = {
            name,
            endpoint,
            description,
            category: apiCategory,
            docsUrl,
            authTypes: ["APIKey"],
            isFree,
            status: "active"
        };

        try {
            const response = await fetch('/api/specs', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(newApiSpec)
            });
            
            if (response.ok) {
                // Clear form & refresh
                setName('');
                setEndpoint('');
                setDescription('');
                setDocsUrl('');
                fetchApis();
            } else {
                throw new Error("Validation or duplicated name error.");
            }
        } catch (err) {
            alert("Error registering on backend. Added to local session list instead.");
            const fakeId = name.toLowerCase().replace(/[^a-z0-9]+/g, '-');
            setApis(prev => [...prev, { ...newApiSpec, id: fakeId }]);
        }
    };

    return (
        <div className="min-h-screen bg-slate-950 text-slate-100 font-sans">
            {/* Header / Brand Nav */}
            <header className="border-b border-slate-900 bg-slate-950/80 backdrop-blur sticky top-0 z-50">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
                    <div className="flex items-center gap-3">
                        <div className="bg-violet-600 p-2 rounded-lg">
                            <svg className="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2.5" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
                            </svg>
                        </div>
                        <span className="text-xl font-black bg-gradient-to-r from-violet-400 to-indigo-400 bg-clip-text text-transparent">
                            DevDex
                        </span>
                    </div>
                    <span className="text-xs bg-slate-900 border border-slate-800 text-slate-400 px-3 py-1.5 rounded-full font-mono">
                        v1.0.0 (Local-First)
                    </span>
                </div>
            </header>

            <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
                {/* Hero section */}
                <div className="text-center max-w-3xl mx-auto mb-16">
                    <h1 className="text-4xl sm:text-5xl font-black tracking-tight mb-4">
                        Universal Directory of APIs
                    </h1>
                    <p className="text-lg text-slate-400">
                        Explore, index, and query public & private API specs in real-time. Built Offline-First with integrated caching mechanisms.
                    </p>
                </div>

                {/* Main section: columns split between registry and explorer */}
                <div className="grid grid-cols-1 lg:grid-cols-3 gap-10">
                    
                    {/* Column 1: API Register Form */}
                    <div className="lg:col-span-1 bg-slate-900 border border-slate-800 rounded-xl p-6 h-fit">
                        <h2 className="text-lg font-bold mb-6 text-slate-200">Register New API</h2>
                        <form onSubmit={handleSubmit} className="space-y-5">
                            <div>
                                <label className="block text-xs font-semibold uppercase text-slate-400 mb-2">API Name</label>
                                <input 
                                    type="text" 
                                    required 
                                    value={name} 
                                    onChange={e => setName(e.target.value)}
                                    placeholder="e.g. OpenAI REST API"
                                    className="w-full bg-slate-950 border border-slate-800 rounded-lg py-2.5 px-4 text-sm text-white focus:outline-none focus:border-violet-500 transition-colors"
                                />
                            </div>

                            <div>
                                <label className="block text-xs font-semibold uppercase text-slate-400 mb-2">Base Endpoint URL</label>
                                <input 
                                    type="url" 
                                    required 
                                    value={endpoint} 
                                    onChange={e => setEndpoint(e.target.value)}
                                    placeholder="https://api.domain.com"
                                    className="w-full bg-slate-950 border border-slate-800 rounded-lg py-2.5 px-4 text-sm text-white focus:outline-none focus:border-violet-500 transition-colors"
                                />
                            </div>

                            <div>
                                <label className="block text-xs font-semibold uppercase text-slate-400 mb-2">Category</label>
                                <select 
                                    value={apiCategory} 
                                    onChange={e => setApiCategory(e.target.value)}
                                    className="w-full bg-slate-950 border border-slate-800 rounded-lg py-2.5 px-4 text-sm text-white focus:outline-none focus:border-violet-500 transition-colors"
                                >
                                    {categories.filter(c => c !== 'All').map((cat, idx) => (
                                        <option key={idx} value={cat}>{cat}</option>
                                    ))}
                                </select>
                            </div>

                            <div>
                                <label className="block text-xs font-semibold uppercase text-slate-400 mb-2">Description</label>
                                <textarea 
                                    rows="3"
                                    value={description} 
                                    onChange={e => setDescription(e.target.value)}
                                    placeholder="Provide detailed information regarding the API surface..."
                                    className="w-full bg-slate-950 border border-slate-800 rounded-lg py-2.5 px-4 text-sm text-white focus:outline-none focus:border-violet-500 transition-colors resize-none"
                                />
                            </div>

                            <div>
                                <label className="block text-xs font-semibold uppercase text-slate-400 mb-2">Docs URL</label>
                                <input 
                                    type="url" 
                                    value={docsUrl} 
                                    onChange={e => setDocsUrl(e.target.value)}
                                    placeholder="https://api.domain.com/docs"
                                    className="w-full bg-slate-950 border border-slate-800 rounded-lg py-2.5 px-4 text-sm text-white focus:outline-none focus:border-violet-500 transition-colors"
                                />
                            </div>

                            <div className="flex items-center justify-between">
                                <span className="text-xs font-semibold uppercase text-slate-400">Has Free Tier</span>
                                <input 
                                    type="checkbox" 
                                    checked={isFree} 
                                    onChange={e => setIsFree(e.target.checked)}
                                    className="w-5 h-5 accent-violet-600 rounded"
                                />
                            </div>

                            <button 
                                type="submit"
                                className="w-full bg-violet-600 hover:bg-violet-500 text-white font-bold py-3 px-4 rounded-lg transition-colors duration-200 mt-4 text-sm"
                            >
                                Register Specification
                            </button>
                        </form>
                    </div>

                    {/* Column 2: Dashboard/Spec List Explorer */}
                    <div className="lg:col-span-2 space-y-8">
                        {/* Search & Filters */}
                        <div className="flex flex-col sm:flex-row gap-4 items-center justify-between bg-slate-900 border border-slate-800 p-4 rounded-xl">
                            <div className="relative w-full sm:w-72">
                                <span className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                    <svg className="h-5 w-5 text-slate-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                                    </svg>
                                </span>
                                <input 
                                    type="text" 
                                    value={search} 
                                    onChange={e => setSearch(e.target.value)}
                                    placeholder="Search API specs..."
                                    className="w-full bg-slate-950 border border-slate-800 rounded-lg pl-10 pr-4 py-2 text-sm text-white focus:outline-none focus:border-violet-500 transition-colors"
                                />
                            </div>

                            {/* Category tabs */}
                            <div className="flex gap-2 overflow-x-auto w-full sm:w-auto pb-2 sm:pb-0 scrollbar-none">
                                {categories.map((cat, idx) => (
                                    <button
                                        key={idx}
                                        onClick={() => setCategory(cat)}
                                        className={`px-3 py-1.5 rounded-lg text-xs font-semibold transition-colors duration-200 whitespace-nowrap ${
                                            category === cat 
                                                ? 'bg-violet-600 text-white' 
                                                : 'bg-slate-950 text-slate-400 border border-slate-800 hover:text-white'
                                        }`}
                                    >
                                        {cat}
                                    </button>
                                ))}
                            </div>
                        </div>

                        {/* API Cards Grid */}
                        {loading ? (
                            <div className="flex flex-col items-center justify-center py-20 text-slate-400">
                                <svg className="animate-spin h-8 w-8 text-violet-500 mb-4" fill="none" viewBox="0 0 24 24">
                                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
                                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z" />
                                </svg>
                                Synchronizing dataset...
                            </div>
                        ) : apis.length === 0 ? (
                            <div className="text-center py-20 border border-dashed border-slate-850 rounded-xl">
                                <svg className="w-12 h-12 text-slate-600 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="1.5" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1" />
                                </svg>
                                <p className="text-slate-400 font-medium">No API specifications found</p>
                                <p className="text-xs text-slate-500 mt-1">Try resetting the search or register a new custom spec.</p>
                            </div>
                        ) : (
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                {apis.map((api, idx) => (
                                    <ApiCard key={api.id || idx} api={api} />
                                ))}
                            </div>
                        )}
                    </div>

                </div>
            </main>
        </div>
    );
}
