import { useEffect, useState } from 'react'
import axios from 'axios'
import Board from '@lourenci/react-kanban'
import '@lourenci/react-kanban/dist/styles.css'
import './App.css'
import ColumnHeader from './components/ColumnHeader'
import ColumnAdder from './components/ColumnAdder'
import ColumnInit from './components/ColumnInit'

function App() {
  const [columns, setColumns] = useState([])
  const [isLoading, setIsLoading] = useState(true)

  const fetchBoard = () => {
    setIsLoading(true)
    axios.get('http://localhost:8080/boardColumn')
      .then(res => {
        setIsLoading(false)
        setColumns(res.data)
      })
      .catch(err => {
        setIsLoading(false)
        console.error(err)
      })
  }

  useEffect(() => {
    fetchBoard()
  }, [])

  const handleCardDrag = (_board, card, _source, destination) => {
    axios.put(`http://localhost:8080/boardTasks/${card.id}`, {
      ...card,
      boardColumnId: destination.toColumnId
    })
      .then(() => {
        fetchBoard()
      })
      .catch(err => console.error(err))
  }

  const handleColumnDrag = (_board, card, source, destination) => {
    axios.post(`http://localhost:8080/boardColumnPos`, {
      "fromId": columns[source.fromPosition].id,
      "toId": columns[destination.toPosition].id
    })
    .then(() => {
      fetchBoard()
    })
    .catch(err => console.error(err))
  }

  const handleNewCard = (board, column, card) => {
    console.log(board, column, card)
  }

  const handleRemoveCard = (card) => {
    axios.delete(`http://localhost:8080/boardTasks/${card.id}`)
      .then(() => {
        fetchBoard()
      })
      .catch(err => console.error(err))
  }

  const handleRemoveColumn = (_board, column) => {
    axios.delete(`http://localhost:8080/boardColumn/${column.id}`)
      .then(() => {
        fetchBoard()
      })
      .catch(err => console.error(err))
  }

  return (
    <div className="App">
      <header className='app__header'>
        Simple kanban
      </header>
      {columns.length ? (
        <Board
          initialBoard={{ columns }}
          // children={{ columns }}
          allowAddCard
          allowRemoveCard
          allowAddColumn
          allowRemoveColumn
          onColumnNew={(board, column) => console.log(board, column)}
          renderColumnAdder={({ addColumn }) => (
            <ColumnAdder
              addColumn={addColumn}
              fetchBoard={fetchBoard}
              columns={columns}
            />
          )}
          renderColumnHeader={(column, { removeColumn, addCard }) => (
            <ColumnHeader column={column} removeColumn={removeColumn} addCard={addCard} />
          )}
          onColumnRemove={(board, column) => handleRemoveColumn(board, column)}
          onCardNew={(board, column, card) => handleNewCard(board, column, card)}
          onCardRemove={(_board, _column, card) => handleRemoveCard(card)}
          onCardDragEnd={(board, card, source, destination) => handleCardDrag(board, card, source, destination)}
          onColumnDragEnd={(board, card, source, destination) => handleColumnDrag(board, card, source, destination)}
        />
      ) : (
        <ColumnInit setColumns={setColumns} />
      )}
    </div>
  );
}

export default App;
