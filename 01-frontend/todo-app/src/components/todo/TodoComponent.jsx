import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { Formik, Form, Field, ErrorMessage } from 'formik'
import moment from 'moment'
import { useAuth } from './security/AuthContext'
import { retrieveTodo, createTodo, updateTodo } from './api/TodoApiService'

export default function TodoComponent() {
    const { id } = useParams()
    const navigate = useNavigate()
    const authContext = useAuth()
    const userId = authContext.userId

    const [description, setDescription] = useState('')
    const [targetDate, setTargetDate] = useState('')
    const [completed, setCompleted] = useState(false)

    useEffect(() => {
        if (id !== '-1') {
            retrieveTodo(userId, id)
                .then(response => {
                    setDescription(response.data.description)
                    setTargetDate(response.data.dueDate)
                    setCompleted(response.data.completed)
                })
                .catch(error => console.error("Error loading todo:", error))
        }
    }, [userId, id])

    function onSubmit(values) {
        const todo = {
            description: values.description,
            dueDate: values.targetDate,
            completed: values.completed
        }

        if (id === '-1') {
            createTodo(userId, todo)
                .then(() => {
                    navigate('/todos')
                })
                .catch(error => console.error("Error creating todo:", error))
        } else {
            updateTodo(userId, id, todo)
                .then(() => {
                    navigate('/todos')
                })
                .catch(error => console.error("Error updating todo:", error))
        }
    }

    function validate(values) {
        let errors = {}

        if (!values.description) {
            errors.description = 'Enter a description'
        } else if (values.description.length < 5) {
            errors.description = 'Enter at least 5 characters in Description'
        }

        if (!values.targetDate || !moment(values.targetDate).isValid()) {
            errors.targetDate = 'Enter a valid Target Date'
        }

        return errors
    }

    return (
        <div className="container mt-5">
            <h1>Enter Todo Details</h1>
            <div className="row justify-content-center mt-4">
                <Formik
                    initialValues={{ description, targetDate, completed }}
                    enableReinitialize={true}
                    onSubmit={onSubmit}
                    validate={validate}
                >
                    {
                        () => (
                            <Form className="w-50 text-start">
                                <div className="mb-3">
                                    <label htmlFor="id" className="form-label">Todo ID</label>
                                    <input type="text" id="id" className="form-control" value={id === '-1' ? 'New (Auto-generated)' : id} disabled />
                                </div>

                                <div className="mb-3">
                                    <label htmlFor="description" className="form-label">Description</label>
                                    <Field type="text" className="form-control" name="description" id="description" />
                                    <ErrorMessage name="description" component="div" className="alert alert-warning mt-2" />
                                </div>

                                <div className="mb-3">
                                    <label htmlFor="targetDate" className="form-label">Target Date</label>
                                    <Field type="date" className="form-control" name="targetDate" id="targetDate" />
                                    <ErrorMessage name="targetDate" component="div" className="alert alert-warning mt-2" />
                                </div>

                                {id !== '-1' && (
                                    <div className="mb-3 form-check">
                                        <Field type="checkbox" className="form-check-input" name="completed" id="completed" />
                                        <label htmlFor="completed" className="form-check-label">Is Completed?</label>
                                    </div>
                                )}

                                <button className="btn btn-success" type="submit">Save</button>
                            </Form>
                        )
                    }
                </Formik>
            </div>
        </div>
    )
}
