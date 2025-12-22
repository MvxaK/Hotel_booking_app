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

async function logout() {
    try {
        await fetch('/api/auth/logout', {
            method: 'POST',
            credentials: 'include'
        });
    } catch (error) {
        console.error('Logout error:', error);
    }

    localStorage.clear();
    window.location.href = '/';
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
    if (!isAuthenticated()) return false;

    const user = getUser();
    if (user && user.role) {
        return user.role === 'ROLE_ADMIN';
    }

    try {
        const token = getToken();
        if (!token) return false;
        const payload = JSON.parse(atob(token.split('.')[1]));
        return payload.role === 'ROLE_ADMIN';
    } catch (err) {
        return false;
    }
}

function checkHouseKeeperAccess() {
    if (!isAuthenticated()) return false;

    const user = getUser();
    if (user && user.role) {
        return user.role === 'ROLE_HOUSE_KEEPER';
    }

    try {
        const token = getToken();
        if (!token) return false;
        const payload = JSON.parse(atob(token.split('.')[1]));
        return payload.role === 'ROLE_HOUSE_KEEPER';
    } catch (err) {
        return false;
    }
}

function checkHotelKeeperAccess() {
    if (!isAuthenticated()) return false;

    const user = getUser();
    if (user && user.role) {
        return user.role === 'ROLE_HOTEL_KEEPER';
    }

    try {
        const token = getToken();
        if (!token) return false;
        const payload = JSON.parse(atob(token.split('.')[1]));
        return payload.role === 'ROLE_HOTEL_KEEPER';
    } catch (err) {
        return false;
    }
}

function updateNavigation() {
    const rightSideLinks = document.getElementById('right-side-links');
    let adminHtml = '';
    let authHtml = '';

    const isAdmin = checkAdminAccess();
    const isHouseKeeper = checkHouseKeeperAccess();
    const isHotelKeeper = checkHotelKeeperAccess();

    if (rightSideLinks) {
        if (isAuthenticated()) {
            const user = getUser();
            const userName = user ? (user.userName || user.username) : 'User';

            authHtml = `
            <li class="nav-item">
                <a class="nav-link" href="/users/my-profile">
                    <i class="bi bi-person-circle me-1"></i>${userName}
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#" onclick="logout()">
                    <i class="bi bi-box-arrow-right me-1"></i>Logout
                </a>
            </li>`;

            let adminDropdownLinks = '';
            let deletedDropdownLinks = '';

            if (isAdmin) {
                adminDropdownLinks += `<li><a class="dropdown-item" href="/houses/new">New House</a></li>
                                       <li><a class="dropdown-item" href="/hotels/new">New Hotel</a></li>
                                       <li><a class="dropdown-item" href="/rooms/new">New Room</a></li>
                                       <li><a class="dropdown-item" href="/room-types/new">New Room Type</a></li>
                                       <li><a class="dropdown-item" href="/room-types">Show Room Types</a></li>
                                       <li><a class="dropdown-item" href="/users/create">New User</a></li>
                                       <li><a class="dropdown-item" href="/users/all-users">Show All Users</a></li>`;
            } else if (isHouseKeeper) {
                adminDropdownLinks += `<li><a class="dropdown-item" href="/houses/new">New House</a></li>`;
            } else if (isHotelKeeper) {
                adminDropdownLinks += `<li><a class="dropdown-item" href="/hotels/new">New Hotel</a></li>
                                       <li><a class="dropdown-item" href="/rooms/new">New Room</a></li>
                                       <li><a class="dropdown-item" href="/room-types/new">New Room Type</a></li>
                                       <li><a class="dropdown-item" href="/room-types">Show Room Types</a></li>`;
            }

            if (isAdmin) {
                deletedDropdownLinks += `<li><a class="dropdown-item" href="/houses/deleted">Deleted Houses</a></li>
                                         <li><a class="dropdown-item" href="/hotels/deleted">Deleted Hotels</a></li>
                                         <li><a class="dropdown-item" href="/rooms/deleted">Deleted Rooms</a></li>
                                         <li><a class="dropdown-item" href="/room-types/deleted">Deleted Room Types</a></li>`;
            } else if (isHouseKeeper) {
                deletedDropdownLinks += `<li><a class="dropdown-item" href="/houses/deleted">Deleted Houses</a></li>`;
            } else if (isHotelKeeper) {
                deletedDropdownLinks += `<li><a class="dropdown-item" href="/hotels/deleted">Deleted Hotels</a></li>
                                       <li><a class="dropdown-item" href="/rooms/deleted">Deleted Rooms</a></li>
                                       <li><a class="dropdown-item" href="/room-types/deleted">Deleted Room Types</a></li>`;
            }


            if (adminDropdownLinks) {
                adminHtml += `
                 <li class="nav-item dropdown">
                     <a class="nav-link dropdown-toggle" href="#" id="adminDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                         <i class="bi bi-gear me-1"></i> Manage
                     </a>
                     <ul class="dropdown-menu" aria-labelledby="adminDropdown">
                         ${adminDropdownLinks}
                     </ul>
                 </li>`;
            }

            if (deletedDropdownLinks) {
                adminHtml += `
                 <li class="nav-item dropdown">
                     <a class="nav-link dropdown-toggle" href="#" id="deletedDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                         <i class="bi bi-trash me-1"></i> Deleted
                     </a>
                     <ul class="dropdown-menu" aria-labelledby="deletedDropdown">
                         ${deletedDropdownLinks}
                     </ul>
                 </li>`;
            }
        } else {
            authHtml = `
            <li class="nav-item">
                <a class="nav-link" href="/login">
                    <i class="bi bi-box-arrow-in-right me-1"></i>Login
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/register">
                    <i class="bi bi-person-plus me-1"></i>Register
                </a>
            </li>`;
            adminHtml = '';
        }

        rightSideLinks.innerHTML = adminHtml + authHtml;
    }
}

async function makeAuthenticatedRequest(url, options = {}) {
    const authHeaders = getAuthHeaders();
    options.headers = {
        ...authHeaders,
        ...options.headers
    };

    options.credentials = 'include';

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