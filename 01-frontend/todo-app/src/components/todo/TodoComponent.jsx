import { useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'

export default function TodoComponent() {
    const { id } = useParams()
    const navigate = useNavigate()

    const [description, setDescription] = useState('')
    const [targetDate, setTargetDate] = useState('')

    function handleDescriptionChange(event) {
        setDescription(event.target.value)
    }

    function handleTargetDateChange(event) {
        setTargetDate(event.target.value)
    }

    function handleSave(event) {
        event.preventDefault()
        console.log('Saved todo details:', { id, description, targetDate })
        navigate('/todos')
    }

    return (
        <div className="container mt-5">
            <h1>Enter Todo Details</h1>
            <form onSubmit={handleSave} className="w-50 mx-auto text-start">
                <div className="mb-3">
                    <label htmlFor="id" className="form-label">Todo ID</label>
                    <input type="text" id="id" className="form-control" value={id} disabled />
                </div>
                <div className="mb-3">
                    <label htmlFor="description" className="form-label">Description</label>
                    <input 
                        type="text" 
                        id="description" 
                        className="form-control" 
                        value={description} 
                        onChange={handleDescriptionChange} 
                        required
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="targetDate" className="form-label">Target Date</label>
                    <input 
                        type="date" 
                        id="targetDate" 
                        className="form-control" 
                        value={targetDate} 
                        onChange={handleTargetDateChange} 
                        required
                    />
                </div>
                <button type="submit" className="btn btn-success">Save</button>
            </form>
        </div>
    )
}
