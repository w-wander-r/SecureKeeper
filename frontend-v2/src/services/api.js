const API_BASE_URL = 'http://localhost:8090';

// Helper function to get auth token from localStorage
const getAuthToken = () => {
  return localStorage.getItem('authToken');
};

// Helper function to set auth token
const setAuthToken = (token) => {
  localStorage.setItem('authToken', token);
};

// Helper function to remove auth token
const removeAuthToken = () => {
  localStorage.removeItem('authToken');
};

// Check if user is authenticated
const isAuthenticated = () => {
  return !!getAuthToken();
};

// Generic fetch wrapper with auth headers
const fetchWithAuth = async (url, options = {}) => {
  const token = getAuthToken();
  const headers = {
    'Content-Type': 'application/json',
    ...options.headers,
  };

  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  const response = await fetch(`${API_BASE_URL}${url}`, {
    ...options,
    headers,
  });

  if (!response.ok) {
    if (response.status === 401) {
      // Unauthorized - clear token and redirect to login
      removeAuthToken();
      window.location.href = '/login';
    }
    const error = await response.text();
    throw new Error(error || 'Request failed');
  }

  return response.json();
};

// Auth API calls
export const authAPI = {
  register: async (username, password) => {
    const response = await fetch(`${API_BASE_URL}/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ username, password }),
    });

    if (!response.ok) {
      const error = await response.text();
      throw new Error(error);
    }

    return response.json();
  },

  login: async (username, password) => {
    const response = await fetch(`${API_BASE_URL}/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ username, password }),
    });

    if (!response.ok) {
      const error = await response.text();
      throw new Error(error);
    }

    const data = await response.json();
    if (data.token) {
      setAuthToken(data.token);
    }
    return data;
  },

  logout: () => {
    removeAuthToken();
  },
};

// Folder API calls
export const folderAPI = {
  getAll: async () => {
    return fetchWithAuth('/api/folders');
  },

  getById: async (folderId) => {
    return fetchWithAuth(`/api/folders/${folderId}`);
  },

  create: async (name) => {
    return fetchWithAuth('/api/folders', {
      method: 'POST',
      body: JSON.stringify({ name }),
    });
  },

  update: async (folderId, name) => {
    return fetchWithAuth(`/api/folders/${folderId}`, {
      method: 'PATCH',
      body: JSON.stringify({ name }),
    });
  },

  delete: async (folderId) => {
    return fetchWithAuth(`/api/folders/${folderId}`, {
      method: 'DELETE',
    });
  },
};

// Note API calls
export const noteAPI = {
  getAllByFolder: async (folderId) => {
    return fetchWithAuth(`/api/notes/folder/${folderId}`);
  },

  getById: async (noteId) => {
    return fetchWithAuth(`/api/notes/${noteId}`);
  },

  create: async (noteData) => {
    return fetchWithAuth('/api/notes', {
      method: 'POST',
      body: JSON.stringify(noteData),
    });
  },

  update: async (noteId, updateData) => {
    return fetchWithAuth(`/api/notes/${noteId}`, {
      method: 'PUT',
      body: JSON.stringify(updateData),
    });
  },

  delete: async (noteId) => {
    return fetchWithAuth(`/api/notes/${noteId}`, {
      method: 'DELETE',
    });
  },
};

export { getAuthToken, isAuthenticated };