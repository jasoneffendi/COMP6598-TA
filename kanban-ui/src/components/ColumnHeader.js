import React, { useState } from 'react'
import axios from 'axios'

import './ColumnHeader.css'

const initFormVal = { title: '', description: '' }

const ColumnHeader = (props) => {
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

  const handleSubmit = () => {
    // Add field empty error check
    handleNewCard(props.column, formValue)
  }

  const handleNewCard = (column, card) => {
    axios.post(`http://localhost:8080/boardColumn/${column.id}/boardTasks`, {
      title: card.title,
      description: card.description
    })
    .then(res => {
      setFormValue(initFormVal)
      setFormOpen(false)
      props.addCard(res.data)
    })
    .catch(err => console.error(err))
  }

  return (
    <div className='column-header__container'>
      <header className='column-header__header'>
        <span>
          {props.column.title} <span className='column-header__task-count'>{props.column.cards.length}</span>
        </span>
        <span onClick={() => props.removeColumn()}>
          X
        </span>
      </header>
      {isFormOpen ? (
        <form onSubmit={e => e.preventDefault()} className='column-header__form'>
          <div className='column-header__input-container'>
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
          <div className='column-header__input-container'>
            <label htmlFor='description'>
              Description:
            </label>
            <textarea
              rows={4}
              name='description'
              type='text'
              value={formValue['description']}
              onChange={handleInputChange}
            />
          </div>
          <div
            type="submit"
            className='column-header__form-submit'
            onClick={() => handleSubmit()}
          >
            Submit
          </div>
          <div
            onClick={() => setFormOpen(false)}
            className='column-header__form-cancel'
          >
            Cancel
          </div>
        </form>
      ) : (
        <div
          onClick={() => setFormOpen(true)}
          className='column-header__form-toggle'
        >
          Add Card
        </div>
      )}
    </div>
  )
}

export default ColumnHeader