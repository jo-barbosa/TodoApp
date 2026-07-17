import { Link } from 'react-router-dom'
import { useAuth } from './security/AuthContext'

export default function WelcomeComponent() {
    const authContext = useAuth()

    return (
        <div className="Welcome">
            <h1>Welcome {authContext.username}!</h1>
            <div>
                Manage your todos - <Link to="/todos">Go here</Link>
            </div>
        </div>
    )
}
