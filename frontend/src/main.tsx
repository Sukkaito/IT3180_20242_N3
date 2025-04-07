import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter, Routes, Route, Outlet, Navigate } from 'react-router-dom'
import './index.css'

import App from './App.tsx'
import Login from './pages/auth/Login.tsx'
import Register from './pages/auth/Register.tsx'
import UserPage from './pages/user/UserPage.tsx'
import UserProfile from './pages/user/UserProfile.tsx'
import UserBookSearch from './pages/user/UserBookSearch.tsx'
import UserLoan from './pages/user/UserLoan.tsx'
import UserRequest from './pages/user/UserRequest.tsx'
import UserFine from './pages/user/UserFine.tsx'
import AdminDashboard from './pages/admin/AdminDashboard.tsx'
import NotFound from './pages/404.tsx'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<App />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        // Todo: Add authentication and authorization wrappers
        <Route path="/admin" element={<div><Outlet /></div>} >
          <Route index element={<div>Admin Home</div>} />
          <Route path="dashboard" element={<div>Admin Dashboard</div>} />
        </Route>

        <Route path="/user" element={<div><Outlet /></div>} >
          <Route index element={<Navigate to="home" replace />} />
          <Route path="home" element={<UserPage />} />
          <Route path="search" element={<UserBookSearch />} />
          <Route path="loaned" element={<UserLoan />} />
          <Route path="requests" element={<UserRequest />} />
          <Route path="fines" element={<UserFine />} />
          <Route path="profile" element={<UserProfile />} />
        </Route>

        <Route path="/admin" element={<div><Outlet /></div>} >
          <Route index element={<Navigate to="dashboard" replace />} />
          <Route path="dashboard" element={<AdminDashboard />} />
        </Route>

        <Route path="*" element={<NotFound />} />
      </Routes>
    </BrowserRouter>
  </StrictMode>
)
