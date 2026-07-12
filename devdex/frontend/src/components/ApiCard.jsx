import React from 'react';

/**
 * ApiCard Component displaying API specification details
 * @param {Object} props
 * @param {Object} props.api - API specification object
 */
export default function ApiCard({ api }) {
    const getStatusColor = (status) => {
        switch (status) {
            case 'active': return 'bg-emerald-500/10 text-emerald-400 border-emerald-500/20';
            case 'deprecated': return 'bg-rose-500/10 text-rose-400 border-rose-500/20';
            case 'beta': return 'bg-amber-500/10 text-amber-400 border-amber-500/20';
            default: return 'bg-slate-500/10 text-slate-400 border-slate-500/20';
        }
    };

    return (
        <div className="flex flex-col h-full bg-slate-900 border border-slate-800 rounded-xl p-6 transition-all duration-300 hover:border-violet-500/30 hover:shadow-[0_8px_30px_rgb(0,0,0,0.12)] hover:-translate-y-1">
            <div className="flex justify-between items-start mb-4">
                <div>
                    <span className="text-xs font-semibold uppercase tracking-wider text-violet-400 bg-violet-500/10 px-2.5 py-1 rounded-full">
                        {api.category}
                    </span>
                    <h3 className="text-lg font-bold text-slate-100 mt-2 hover:text-white transition-colors">
                        {api.name}
                    </h3>
                </div>
                <span className={`text-xs font-medium border px-2.5 py-0.5 rounded-full capitalize ${getStatusColor(api.status)}`}>
                    {api.status}
                </span>
            </div>

            <p className="text-sm text-slate-400 line-clamp-3 mb-6 flex-grow">
                {api.description}
            </p>

            <div className="space-y-3 pt-4 border-t border-slate-800/60 mt-auto text-xs">
                <div className="flex items-center text-slate-400">
                    <span className="font-semibold text-slate-300 w-16">Base URL:</span>
                    <code className="bg-slate-950/60 text-slate-300 px-2 py-1 rounded select-all font-mono truncate max-w-[180px]">
                        {api.endpoint}
                    </code>
                </div>
                <div className="flex items-center text-slate-400">
                    <span className="font-semibold text-slate-300 w-16">Auth:</span>
                    <div className="flex gap-1.5 flex-wrap">
                        {api.authTypes.map((type, idx) => (
                            <span key={idx} className="bg-slate-800 text-slate-300 px-1.5 py-0.5 rounded">
                                {type}
                            </span>
                        ))}
                    </div>
                </div>
                <div className="flex items-center text-slate-400">
                    <span className="font-semibold text-slate-300 w-16">Pricing:</span>
                    <span className={api.isFree ? 'text-emerald-400 font-semibold' : 'text-amber-400 font-semibold'}>
                        {api.isFree ? 'Free Tier Available' : 'Paid / Enterprise Only'}
                    </span>
                </div>
            </div>

            <div className="mt-6">
                <a
                    href={api.docsUrl}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="flex items-center justify-center gap-2 w-full bg-violet-600 hover:bg-violet-500 text-white text-sm font-semibold py-2.5 px-4 rounded-lg transition-colors duration-200"
                >
                    View API Docs
                    <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14" />
                    </svg>
                </a>
            </div>
        </div>
    );
}
