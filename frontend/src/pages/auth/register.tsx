// Trang Register 
import { Link } from "react-router-dom";

export default function RegisterPage() {
  return (
    // Container chính căn giữa trang, nền gradient xanh dương nhẹ nhàng
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-100 via-blue-200 to-blue-300 p-4">
      {/* Box form đăng ký với background trắng, bo góc, shadow */}
      <div className="bg-white pt-12 pb-8 px-8 rounded-2xl shadow-2xl w-full max-w-md">

        {/* Tiêu đề trang đăng ký */}
        <h2 className="text-3xl font-bold text-center text-blue-700 mb-6">
          Create an Account
        </h2>

        {/* Form đăng ký gồm các input nhập thông tin user */}
        <form className="flex flex-col space-y-4">
          {/* Nhập tên */}
          <div>
            <label className="block mb-1 text-gray-700 font-medium" htmlFor="name">
              Name
            </label>
            <input
              id="name"
              type="text"
              placeholder="Enter your name"
              className="w-full px-4 py-2 rounded-lg border border-gray-300 focus:ring-2 focus:ring-blue-400 focus:outline-none"
              required
            />
          </div>

          {/* Nhập tên đăng nhập (username) */}
          <div>
            <label className="block mb-1 text-gray-700 font-medium" htmlFor="usernameusername">
              Username
            </label>
            <input
              id="username"
              type="username"
              placeholder="Enter your username"
              className="w-full px-4 py-2 rounded-lg border border-gray-300 focus:ring-2 focus:ring-blue-400 focus:outline-none"
              required
            />
          </div>

          {/* Nhập email */}
          <div>
            <label className="block mb-1 text-gray-700 font-medium" htmlFor="email">
              Email
            </label>
            <input
              id="email"
              type="email"
              placeholder="Enter your email"
              className="w-full px-4 py-2 rounded-lg border border-gray-300 focus:ring-2 focus:ring-blue-400 focus:outline-none"
              required
            />
          </div>

          {/* Nhập mật khẩu */}
          <div>
            <label className="block mb-1 text-gray-700 font-medium" htmlFor="password">
              Password
            </label>
            <input
              id="password"
              type="password"
              placeholder="Create a password"
              className="w-full px-4 py-2 rounded-lg border border-gray-300 focus:ring-2 focus:ring-blue-400 focus:outline-none"
              required
            />
          </div>

          {/* Xác nhận mật khẩu */}
          <div>
            <label className="block mb-1 text-gray-700 font-medium" htmlFor="confirmPassword">
              Confirm Password
            </label>
            <input
              id="confirmPassword"
              type="password"
              placeholder="Confirm your password"
              className="w-full px-4 py-2 rounded-lg border border-gray-300 focus:ring-2 focus:ring-blue-400 focus:outline-none"
              required
            />
          </div>

          {/* Nút submit đăng ký */}
          <button
            type="submit"
            className="w-full py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition duration-300 font-semibold"
          >
            Register
          </button>
        </form>

        {/* Link chuyển về trang đăng nhập nếu đã có tài khoản */}
        <p className="mt-6 text-center text-gray-600">
          Already have an account?{" "}
          <Link to="/login" className="text-blue-600 hover:underline font-semibold">
            Login
          </Link>
        </p>
      </div>
    </div>
  );
}
