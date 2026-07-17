import { useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { Formik, Form, Field, ErrorMessage } from 'formik'
import moment from 'moment'

export default function TodoComponent() {
    const { id } = useParams()
    const navigate = useNavigate()

    // Mock initial values based on Todo ID
    const [description] = useState(id === '-1' ? '' : `Learn AWS (Todo #${id})`)
    const [targetDate] = useState(moment(new Date()).format('YYYY-MM-DD'))

    function onSubmit(values) {
        console.log('Saved todo details:', { id, ...values })
        navigate('/todos')
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
                    initialValues={{ description, targetDate }}
                    enableReinitialize={true}
                    onSubmit={onSubmit}
                    validate={validate}
                >
                    {
                        () => (
                            <Form className="w-50 text-start">
                                <div className="mb-3">
                                    <label htmlFor="id" className="form-label">Todo ID</label>
                                    <input type="text" id="id" className="form-control" value={id} disabled />
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

                                <button className="btn btn-success" type="submit">Save</button>
                            </Form>
                        )
                    }
                </Formik>
            </div>
        </div>
    )
}
