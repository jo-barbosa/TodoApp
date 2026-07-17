/* eslint-disable react-refresh/only-export-components */
import { createContext, useContext, useState } from "react";
import { executeJwtAuthenticationService } from "../api/TodoApiService";

// 1: Create a Context
export const AuthContext = createContext();

// 2: Create a custom hook to use the AuthContext easily
export const useAuth = () => useContext(AuthContext);

// 3: Share the created context with other components
export default function AuthProvider({ children }) {

    const [isAuthenticated, setAuthenticated] = useState(false);
    const [username, setUsername] = useState(null);
    const [userId, setUserId] = useState(null);
    const [token, setToken] = useState(null);

    async function login(email, password) {
        try {
            const response = await executeJwtAuthenticationService(email, password)
            if (response.status === 200) {
                const jwtToken = response.data.token
                setAuthenticated(true)
                setUsername(response.data.name)
                setUserId(response.data.userId)
                setToken(jwtToken)
                localStorage.setItem('token', jwtToken)
                return true
            } else {
                logout()
                return false
            }
        } catch (error) {
            console.error("JWT Authentication error:", error)
            logout()
            return false
        }
    }

    function logout() {
        setAuthenticated(false);
        setUsername(null);
        setUserId(null);
        setToken(null);
        localStorage.removeItem('token');
    }

    return (
        <AuthContext.Provider value={ { isAuthenticated, username, userId, token, login, logout } }>
            {children}
        </AuthContext.Provider>
    )
}
