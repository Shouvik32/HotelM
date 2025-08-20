import React from 'react'
import useBookings from '../hooks/useBookings'
import { withLoading } from '../hocs/withLoading'

const BookingTable: React.FC<{ bookings: any[] }> = ({ bookings }) => {
  if (bookings.length === 0) return <div>No bookings found.</div>
  return (
    <table style={{ width: '100%', borderCollapse: 'collapse' }}>
      <thead>
        <tr>
          <th>Guest</th>
          <th>Room</th>
          <th>Check In</th>
          <th>Check Out</th>
        </tr>
      </thead>
      <tbody>
        {bookings.map(b => (
          <tr key={b.id}>
            <td>{b.guestName}</td>
            <td>{b.roomNumber}</td>
            <td>{b.checkIn}</td>
            <td>{b.checkOut}</td>
          </tr>
        ))}
      </tbody>
    </table>
  )
}

const BookingTableWithLoading = withLoading(BookingTable)

const BookingList: React.FC = () => {
  const { bookings, status } = useBookings()
  return <BookingTableWithLoading loading={status === 'loading'} bookings={bookings} />
}

export default BookingList