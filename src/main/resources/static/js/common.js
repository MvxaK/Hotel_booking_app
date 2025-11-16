function getToken() {
    return localStorage.getItem('token');
}

function setToken(token) {
    localStorage.setItem('token', token);
}

function isAuthenticated() {
    return !!getToken();
}

function getUser() {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
}

function setUser(user) {
    localStorage.setItem('user', JSON.stringify(user));
}

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    window.location.href = '/login';
}

function getAuthHeaders() {
    const token = getToken();
    const headers = {
        'Content-Type': 'application/json'
    };
    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }
    return headers;
}

function checkAuth() {
    const publicPaths = ['/login', '/register', '/', '/hotels', '/houses', '/rooms'];
    const currentPath = window.location.pathname;

    const isPublicPath = publicPaths.some(path => currentPath === path || currentPath.startsWith(path + '/'));

    if (!isAuthenticated() && !isPublicPath) {
        window.location.href = '/login';
        return false;
    }
    return true;
}

function checkAdminAccess() {
    if (!isAuthenticated()) {
        return false;
    }

    const user = getUser();
    if (user && user.roles) {
        return user.roles.includes('ROLE_ADMIN');
    }

    try {
        const token = getToken();
        const payload = JSON.parse(atob(token.split('.')[1]));
        return payload.roles && payload.roles.includes('ROLE_ADMIN');
    } catch (err) {
        console.error('Error decoding token:', err);
        return false;
    }
}

function updateNavigation() {
    const authLinks = document.getElementById('auth-links');
    if (authLinks) {
        if (isAuthenticated()) {
            const user = getUser();
            const userName = user ? user.username : 'User';

            authLinks.innerHTML = `
                <li class="nav-item">
                    <a class="nav-link" href="/users/my-profile">
                        <i class="bi bi-person-circle me-1"></i>${userName}
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#" onclick="logout()">
                        <i class="bi bi-box-arrow-right me-1"></i>Logout
                    </a>
                </li>
            `;
        } else {
            authLinks.innerHTML = `
                <li class="nav-item">
                    <a class="nav-link" href="/login">
                        <i class="bi bi-box-arrow-in-right me-1"></i>Login
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/register">
                        <i class="bi bi-person-plus me-1"></i>Register
                    </a>
                </li>
            `;
        }
    }
}

async function makeAuthenticatedRequest(url, options = {}) {
    const authHeaders = getAuthHeaders();
    options.headers = {
        ...authHeaders,
        ...options.headers
    };

    const response = await fetch(url, options);

    if (response.status === 401 || response.status === 403) {
        logout();
        throw new Error('Authentication required');
    }

    return response;
}

document.addEventListener('DOMContentLoaded', function() {
    checkAuth();
    updateNavigation();
});

window.addEventListener('storage', function(e) {
    if (e.key === 'token' || e.key === 'user') {
        updateNavigation();
    }
});