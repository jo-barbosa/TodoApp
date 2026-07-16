import './TodoApp.css'
import { useState } from 'react'

export default function TodoApp() {
    return (
        <div className="todo-app">
            Todo Management Application
            <LoginComponent />
        </div>
    )
}

function LoginComponent() {

    const [username, setUsername] = useState('teste')

    const [password, setPassword] = useState('')

    const [showSuccessMessage, setShowSuccessMessage] = useState(false)

    const [showErrorMessage, setShowErrorMessage] = useState(false)

    function handleUsernameChange(event) {
        setUsername(event.target.value)
    }

    function handlePasswordChange(event) {
        setPassword(event.target.value)
    }

    function handleSubmit() {
        if ((username === 'in28minutes' || username === 'teste') && password === 'dummy') {
            setShowSuccessMessage(true)
            setShowErrorMessage(false)
        } else {
            setShowSuccessMessage(false)
            setShowErrorMessage(true)
        }
    }

    return (
        <div className="Login">
            {showSuccessMessage && <div className="sucessMessage">Authentication Successful</div>}
            {showErrorMessage && <div className="errorMessage">Authentication Failed. Please check your credentials and try again.</div>}
            <div className="login-form">
                <div>
                    <label>Username:</label>
                    <input type="text" name="username" value={username} onChange={handleUsernameChange}/>
                </div>
                <div>
                    <label>Password:</label>
                    <input type="password" name="password" value={password} onChange={handlePasswordChange}/>
                </div>
                <button type="submit" onClick={handleSubmit}>Login</button>
            </div>
        </div>
    )
}

export function WelcomeComponent() {
    return (
        <div className="Welcome">
            Welcome Component
        </div>
    )
}
