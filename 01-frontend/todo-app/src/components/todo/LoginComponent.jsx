import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

export default function LoginComponent() {

    const [username, setUsername] = useState('teste')

    const [password, setPassword] = useState('')

    const [showSuccessMessage, setShowSuccessMessage] = useState(false)

    const [showErrorMessage, setShowErrorMessage] = useState(false)

    const navigate = useNavigate()

    function handleUsernameChange(event) {
        setUsername(event.target.value)
    }

    function handlePasswordChange(event) {
        setPassword(event.target.value)
    }

    function handleSubmit() {
        if ((username === 'jo-barbosa' || username === 'teste') && password === 'dummy') {
            setShowSuccessMessage(true)
            setShowErrorMessage(false)
            navigate(`/welcome/${username}`)
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
