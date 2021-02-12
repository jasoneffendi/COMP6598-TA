import React, { useState } from 'react'
import axios from 'axios'

import './ColumnAdder.css'

const initFormVal = { title: '' }

const ColumnAdder = (props) => {
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
      props.fetchBoard()
      props.addColumn({ ...res.data, cards: [] })
    })
    .catch(err => console.error(err))
  }

  const handleSubmit = () => {
    const currHighestOrderId = Math.max(...props.columns.map(c => c.displayOrder))
    const newOrderId = currHighestOrderId > 0 ? currHighestOrderId + 1 : 0
    handleNewColumn({ ...formValue, displayOrder: newOrderId })
  }

  return (
    <div className='column-adder__container'>
      <header className='column-adder__header'>
        Add Column
      </header>
      {isFormOpen ? (
        <form onSubmit={e => e.preventDefault()} className='column-adder__form'>
          <div className='column-adder__input-container'>
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
            className='column-adder__form-submit'
            onClick={() => handleSubmit()}
          >
            Submit
          </div>
          <div
            onClick={() => setFormOpen(false)}
            className='column-adder__form-cancel'
          >
            Cancel
          </div>
        </form>
      ) : (
        <div
          onClick={() => setFormOpen(true)}
          className='column-adder__form-toggle'
        >
          Add Card
        </div>
      )}
    </div>
  )
}

export default ColumnAdder