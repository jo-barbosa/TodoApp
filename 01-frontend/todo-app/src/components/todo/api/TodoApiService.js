import axios from 'axios'

const apiClient = axios.create({
    baseURL: 'http://localhost:8080/api'
})

// Add request interceptor to attach Bearer Token to all requests if it exists
apiClient.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token')
        if (token) {
            config.headers.Authorization = `Bearer ${token}`
        }
        return config
    },
    (error) => {
        return Promise.reject(error)
    }
)

export const executeJwtAuthenticationService = (email, password) => apiClient.post('/authenticate', { email, password })

export const retrieveAllUsers = () => apiClient.get('/users')

export const createUser = (name, email, password) => apiClient.post('/users', { name, email, password })

export const retrieveAllTodosForUser = (userId) => apiClient.get(`/users/${userId}/todos`)

export const retrieveTodo = (userId, todoId) => apiClient.get(`/users/${userId}/todos/${todoId}`)

export const createTodo = (userId, todo) => apiClient.post(`/users/${userId}/todos`, todo)

export const updateTodo = (userId, todoId, todo) => apiClient.put(`/users/${userId}/todos/${todoId}`, todo)

export const deleteTodo = (userId, todoId) => apiClient.delete(`/users/${userId}/todos/${todoId}`)
