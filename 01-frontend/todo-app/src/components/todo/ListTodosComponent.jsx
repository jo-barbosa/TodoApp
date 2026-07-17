import { useState, useEffect, useCallback } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from './security/AuthContext'
import { retrieveAllTodosForUser, deleteTodo } from './api/TodoApiService'

export default function ListTodosComponent() {

    const authContext = useAuth()
    const userId = authContext.userId

    const [todos, setTodos] = useState([])
    const [message, setMessage] = useState(null)
    const navigate = useNavigate()

    const refreshTodos = useCallback(() => {
        retrieveAllTodosForUser(userId)
            .then(response => {
                setTodos(response.data)
            })
            .catch(error => console.error("Error fetching todos:", error))
    }, [userId])

    useEffect(() => {
        refreshTodos()
    }, [refreshTodos])

    function handleDeleteTodo(id) {
        deleteTodo(userId, id)
            .then(() => {
                setMessage(`Delete of todo with id ${id} successful`)
                refreshTodos()
            })
            .catch(error => console.error("Error deleting todo:", error))
    }

    function handleUpdateTodo(id) {
        navigate(`/todo/${id}`)
    }

    function addNewTodo() {
        navigate('/todo/-1')
    }

    return (
        <div className="container">
            <h1>Things You Want To Do!</h1>
            {message && <div className="alert alert-warning">{message}</div>}
            <div>
                <table className="table">
                    <thead>
                        <tr>
                            <th>Description</th>
                            <th>Is Done?</th>
                            <th>Target Date</th>
                            <th>Delete</th>
                            <th>Update</th>
                        </tr>
                    </thead>
                    <tbody>
                    {
                        todos.map(
                            todo => (
                                <tr key={todo.id}>
                                    <td>{todo.description}</td>
                                    <td>{todo.completed.toString()}</td>
                                    <td>{todo.dueDate}</td>
                                    <td><button className="btn btn-warning" onClick={() => handleDeleteTodo(todo.id)}>Delete</button></td>
                                    <td><button className="btn btn-success" onClick={() => handleUpdateTodo(todo.id)}>Update</button></td>
                                </tr>
                            )
                        )
                    }
                    </tbody>
                </table>
            </div>
            <button className="btn btn-success m-3" onClick={addNewTodo}>Add New Todo</button>
        </div>
    )
}
