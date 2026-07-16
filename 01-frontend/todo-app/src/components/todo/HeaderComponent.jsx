import { Link } from 'react-router-dom'

export default function HeaderComponent() {
    return (
        <header className="header">
            <div className="container">
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
            </div>
        </header>
    )
}
