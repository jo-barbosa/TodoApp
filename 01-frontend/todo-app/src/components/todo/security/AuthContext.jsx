/* eslint-disable react-refresh/only-export-components */
import { createContext, useContext, useState } from "react";

// 1: Create a Context
export const AuthContext = createContext();

// 2: Create a custom hook to use the AuthContext easily
export const useAuth = () => useContext(AuthContext);

// 3: Share the created context with other components
export default function AuthProvider({ children }) {

    const [isAuthenticated, setAuthenticated] = useState(false);
    const [username, setUsername] = useState(null);

    function login(username, password) {
        if ((username === 'jo-barbosa' || username === 'teste') && password === 'dummy') {
            setAuthenticated(true);
            setUsername(username);
            return true;
        } else {
            setAuthenticated(false);
            setUsername(null);
            return false;
        }
    }

    function logout() {
        setAuthenticated(false);
        setUsername(null);
    }

    return (
        <AuthContext.Provider value={ { isAuthenticated, username, login, logout } }>
            {children}
        </AuthContext.Provider>
    )
}
