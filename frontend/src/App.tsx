import { Link } from 'react-router-dom'

function App() {

  return (
    <>
      <div className="min-h-screen bg-gradient-to-br from-blue-100 via-blue-200 to-blue-300 flex flex-col items-center justify-center p-6">
        <h1 className="text-4xl font-bold mb-10 text-center text-blue-800 drop-shadow-lg">Welcome to the Library</h1>
        <div className="flex space-x-6">
          <Link to="/" className="px-6 py-3 bg-blue-600 text-white rounded-2xl shadow-lg hover:bg-blue-700 transition duration-300">
            Home
          </Link>
          <Link to="/login" className="px-6 py-3 bg-blue-500 text-white rounded-2xl shadow-lg hover:bg-blue-600 transition duration-300">
            Login
          </Link>
          <Link to="/register" className="px-6 py-3 bg-blue-400 text-white rounded-2xl shadow-lg hover:bg-blue-500 transition duration-300">
            Register
          </Link>
        </div>
      </div>
    </>
  )
}

export default App
