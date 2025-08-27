import React from 'react'
import BookingList from './components/BookingList'

const App: React.FC = () => {
  return (
    <div style={{ padding: 20 }}>
      <h1>HotelM - Bookings</h1>
      <BookingList />
    </div>
  )
}

export default App