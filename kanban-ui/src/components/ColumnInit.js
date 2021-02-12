import React, { useState } from 'react'
import axios from 'axios'

import './ColumnInit.css'

const initFormVal = { title: '' }

const ColumnInit = (props) => {
  const [formValue, setFormValue] = useState(initFormVal)
  const [isFormOpen, setFormOpen] = useState(false)
  // const [formError, setFormError] = useState({})
  const handleInputChange = (event) => {
    const target = event.target
    const value =  target.value
    const name = target.name

    setFormValue({
      ...formValue,
      [name]: value
    })
  }

  const handleNewColumn = (column) => {
    axios.post(`http://localhost:8080/boardColumn`, column)
    .then(res => {
      setFormValue(initFormVal)
      setFormOpen(false)
      props.setColumns([{ ...res.data, cards: [] }])
      // props.addColumn({ ...res.data, cards: [] })
    })
    .catch(err => console.error(err))
  }

  const handleSubmit = () => {
    // const currHighestOrderId = Math.max(...props.columns.map(c => c.displayOrder))
    // const newOrderId = currHighestOrderId > 0 ? currHighestOrderId + 1 : 0
    handleNewColumn({ ...formValue, displayOrder: 0 })
  }

  return (
    <div className='column-init__container'>
      <header className='column-init__header'>
        Add Column
      </header>
      {isFormOpen ? (
        <form onSubmit={e => e.preventDefault()} className='column-init__form'>
          <div className='column-init__input-container'>
            <label htmlFor='title'>
              Title:
            </label>
            <input
              name='title'
              type='text'
              value={formValue['title']}
              onChange={handleInputChange}
            />
          </div>
          <div
            type="submit"
            className='column-init__form-submit'
            onClick={() => handleSubmit()}
          >
            Submit
          </div>
          <div
            onClick={() => setFormOpen(false)}
            className='column-init__form-cancel'
          >
            Cancel
          </div>
        </form>
      ) : (
        <div
          onClick={() => setFormOpen(true)}
          className='column-init__form-toggle'
        >
          Add Card
        </div>
      )}
    </div>
  )
}

export default ColumnInit