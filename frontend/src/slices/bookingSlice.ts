import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit'
import http from '../api/http'

export interface Booking {
  id: number
  guestName: string
  roomNumber: string
  checkIn: string
  checkOut: string
}

interface BookingState {
  items: Booking[]
  status: 'idle' | 'loading' | 'succeeded' | 'failed'
  error: string | null
}

const initialState: BookingState = {
  items: [],
  status: 'idle',
  error: null
}

export const fetchBookings = createAsyncThunk('booking/fetchBookings', async () => {
  const response = await http.get<Booking[]>('/api/bookings')
  return response.data
})

const bookingSlice = createSlice({
  name: 'booking',
  initialState,
  reducers: {},
  extraReducers: builder => {
    builder
      .addCase(fetchBookings.pending, state => {
        state.status = 'loading'
        state.error = null
      })
      .addCase(fetchBookings.fulfilled, (state, action: PayloadAction<Booking[]>) => {
        state.status = 'succeeded'
        state.items = action.payload
      })
      .addCase(fetchBookings.rejected, (state, action) => {
        state.status = 'failed'
        state.error = action.error.message || 'Failed to fetch bookings'
      })
  }
})

export default bookingSlice.reducer