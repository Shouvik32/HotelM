import { useEffect } from 'react'
import { useAppDispatch, useAppSelector } from './useReduxHooks'
import { fetchBookings } from '../slices/bookingSlice'

export const useBookings = () => {
  const dispatch = useAppDispatch()
  const bookings = useAppSelector(state => state.booking.items)
  const status = useAppSelector(state => state.booking.status)
  const error = useAppSelector(state => state.booking.error)

  useEffect(() => {
    if (status === 'idle') {
      dispatch(fetchBookings())
    }
  }, [status, dispatch])

  return { bookings, status, error }
}

export default useBookings