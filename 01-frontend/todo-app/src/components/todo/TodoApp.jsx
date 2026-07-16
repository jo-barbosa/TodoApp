import './TodoApp.css'
import { useState } from 'react'
import { BrowserRouter, Routes, Route, useNavigate, useParams, Link } from 'react-router-dom'

export default function TodoApp() {
    return (
        <div className="todo-app">
            <BrowserRouter>
                <HeaderComponent />
                <Routes>
                    <Route path="/" element={<LoginComponent />} />
                    <Route path="/login" element={<LoginComponent />} />
                    <Route path="/welcome/:username" element={<WelcomeComponent />} />
                    <Route path="/todos" element={<ListTodosComponent />} />
                    <Route path="/logout" element={<LogoutComponent />} />
                    <Route path="*" element={<ErrorComponent />} />
                </Routes>
                <FooterComponent />
            </BrowserRouter>
        </div>
    )
}

function HeaderComponent() {
    return (
        <header className="header">
            <nav className="navbar">
                <div className="navbar-brand">
                    <Link to="/">To Do Application</Link>
                </div>
                <ul className="navbar-nav left-nav">
                    <li><Link className="nav-link" to="/welcome/jo-barbosa">Home</Link></li>
                    <li><Link className="nav-link" to="/todos">Todos</Link></li>
                </ul>
                <ul className="navbar-nav right-nav">
                    <li><Link className="nav-link" to="/login">Login</Link></li>
                    <li><Link className="nav-link" to="/logout">Logout</Link></li>
                </ul>
            </nav>
        </header>
    )
}

function FooterComponent() {
    return (
        <footer className="footer">
            <div className="container">
                <p>To Do Application &copy; 2026 - jo-barbosa</p>
            </div>
        </footer>
    )
}

function LogoutComponent() {
    return (
        <div className="LogoutComponent">
            <h1>You are logged out!</h1>
            <div>
                Thank you for using our Application. Come back soon!
            </div>
        </div>
    )
}

function LoginComponent() {

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

export function WelcomeComponent() {
    const { username } = useParams()

    return (
        <div className="Welcome">
            <h1>Welcome {username}!</h1>
            <div>
                Manage your todos - <Link to="/todos">Go here</Link>
            </div>
        </div>
    )
}

function ListTodosComponent() {

    const today = new Date()

    const targetDate = new Date(today.getFullYear()+12, today.getMonth(), today.getDate())

    const todos = [
        {id: 1, description: 'Learn AWS', done: false, targetDate: targetDate},
        {id: 2, description: 'Learn Full Stack', done: false, targetDate: targetDate},
        {id: 3, description: 'Learn DevOps', done: false, targetDate: targetDate},
    ]

    return (
        <div className="container">
            <h1>Things You Want To Do!</h1>
            <div>
                <table className="table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Description</th>
                            <th>Is Done?</th>
                            <th>Target Date</th>
                        </tr>
                    </thead>
                    <tbody>
                    {
                        todos.map(
                            todo => (
                                <tr key={todo.id}>
                                    <td>{todo.id}</td>
                                    <td>{todo.description}</td>
                                    <td>{todo.done.toString()}</td>
                                    <td>{todo.targetDate.toDateString()}</td>
                                </tr>
                            )
                        )
                    }
                    </tbody>
                </table>
            </div>
        </div>
    )
}

function ErrorComponent() {
    return (
        <div className="ErrorComponent">
            <h1>We are working really hard!</h1>
            <div>
                Apologies for the 404. Reach out to our support team.
            </div>
        </div>
    )
}
