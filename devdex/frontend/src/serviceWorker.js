const CACHE_NAME = 'devdex-v1-cache';
const ASSETS_TO_CACHE = [
    '/',
    '/index.html',
    '/src/App.jsx',
    '/src/components/ApiCard.jsx',
    '/src/main.jsx',
    '/src/index.css'
];

// Install Event - Pre-cache critical assets
self.addEventListener('install', (event) => {
    event.waitUntil(
        caches.open(CACHE_NAME).then((cache) => {
            console.log('[DevDex ServiceWorker] Pre-caching Core Assets');
            return cache.addAll(ASSETS_TO_CACHE);
        }).then(() => self.skipWaiting())
    );
});

// Activate Event - Clean up stale cache databases
self.addEventListener('activate', (event) => {
    event.waitUntil(
        caches.keys().then((cacheNames) => {
            return Promise.all(
                cacheNames.map((cache) => {
                    if (cache !== CACHE_NAME) {
                        console.log('[DevDex ServiceWorker] Clearing Stale Cache:', cache);
                        return caches.delete(cache);
                    }
                })
            );
        }).then(() => self.clients.claim())
    );
});

// Fetch Event - Cache-First fallback to Network with dynamic caching
self.addEventListener('fetch', (event) => {
    // Skip non-GET requests (such as POST registrations)
    if (event.request.method !== 'GET') return;

    event.respondWith(
        caches.match(event.request).then((cachedResponse) => {
            if (cachedResponse) {
                // Background update cache if network is available
                event.waitUntil(
                    fetch(event.request).then((networkResponse) => {
                        if (networkResponse.status === 200) {
                            caches.open(CACHE_NAME).then((cache) => cache.put(event.request, networkResponse));
                        }
                    }).catch(() => {/* Ignore network update errors when offline */})
                );
                return cachedResponse;
            }

            return fetch(event.request).then((networkResponse) => {
                if (!networkResponse || networkResponse.status !== 200 || networkResponse.type !== 'basic') {
                    return networkResponse;
                }

                const responseToCache = networkResponse.clone();
                caches.open(CACHE_NAME).then((cache) => {
                    cache.put(event.request, responseToCache);
                });

                return networkResponse;
            }).catch(() => {
                // If fetching api specifications offline, return local json-like data fallback
                if (event.request.url.includes('/api/specs')) {
                    return new Response(JSON.stringify({
                        status: 'success',
                        data: []
                    }), { headers: { 'Content-Type': 'application/json' } });
                }
            });
        })
    );
});
