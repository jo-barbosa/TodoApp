import { Link } from 'react-router-dom'
import { useAuth } from './security/AuthContext'

export default function HeaderComponent() {
    const authContext = useAuth()
    const isAuthenticated = authContext.isAuthenticated

    function handleLogout() {
        authContext.logout()
    }

    return (
        <header className="header">
            <div className="container">
                <nav className="navbar navbar-expand-md">
                    <div className="navbar-brand">
                        <Link to="/">To Do Application</Link>
                    </div>
                    <ul className="navbar-nav left-nav">
                        {isAuthenticated && <li><Link className="nav-link" to={`/welcome/${authContext.username}`}>Home</Link></li>}
                        {isAuthenticated && <li><Link className="nav-link" to="/todos">Todos</Link></li>}
                    </ul>
                    <ul className="navbar-nav right-nav">
                        {!isAuthenticated && <li><Link className="nav-link" to="/login">Login</Link></li>}
                        {isAuthenticated && <li><Link className="nav-link" to="/logout" onClick={handleLogout}>Logout</Link></li>}
                    </ul>
                </nav>
            </div>
        </header>
    )
}
