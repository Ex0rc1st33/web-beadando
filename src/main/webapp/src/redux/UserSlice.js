import {createSlice} from '@reduxjs/toolkit'

export const userSlice = createSlice({
    name: "user",
    initialState: {
        token: ""
    },
    reducers: {
        load: (state, action) => {
            state.token = action.payload
        },
        unload: (state) => {
            state.name = ""
            state.token = ""
        },
    },
})

// Action creators are generated for each case reducer function
export const {load, unload} = userSlice.actions

export const userToken = state => state.user.token;

export default userSlice.reducer