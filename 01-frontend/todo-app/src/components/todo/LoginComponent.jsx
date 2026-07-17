import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from './security/AuthContext'

export default function LoginComponent() {

    const [email, setEmail] = useState('joao@teste.com')

    const [password, setPassword] = useState('')

    const [showErrorMessage, setShowErrorMessage] = useState(false)

    const navigate = useNavigate()

    const authContext = useAuth()

    function handleEmailChange(event) {
        setEmail(event.target.value)
    }

    function handlePasswordChange(event) {
        setPassword(event.target.value)
    }

    async function handleSubmit() {
        if (await authContext.login(email, password)) {
            navigate(`/welcome/${email}`)
        } else {
            setShowErrorMessage(true)
        }
    }

    return (
        <div className="Login">
            {showErrorMessage && <div className="errorMessage">Authentication Failed. Please check your credentials and try again.</div>}
            <div className="login-form">
                <div>
                    <label>Email:</label>
                    <input type="text" name="email" value={email} onChange={handleEmailChange}/>
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
