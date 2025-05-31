// Trang Login 
import { useState } from "react";
import { Link } from "react-router-dom";

export default function LoginPage() {
  // State lưu role hiện tại, mặc định là "user"
  const [role, setRole] = useState<"user" | "admin">("user");

  // Kiểm tra role hiện tại có phải là admin không
  const isAdmin = role === "admin";

  return (
    // Wrapper chính với nền gradient khác nhau tùy theo role
    <div className={`min-h-screen flex items-center justify-center ${isAdmin
      ? "bg-gradient-to-br from-purple-200 via-purple-300 to-purple-400"
      : "bg-gradient-to-br from-blue-100 via-blue-200 to-blue-300"
      } p-4`}>
      {/* Container form với background trắng, bo góc và bóng */}
      <div className="bg-white pt-20 pb-8 px-8 rounded-2xl shadow-2xl w-full max-w-md relative overflow-hidden">

        {/* Tab chuyển đổi giữa User và Admin ở phía trên cùng, dùng button */}
        <div className="absolute top-0 left-0 right-0 flex rounded-t-2xl overflow-hidden">
          <button
            onClick={() => setRole("user")} // Chọn role user khi click
            className={`w-1/2 py-3 text-sm font-semibold transition duration-300 ${!isAdmin
              ? "bg-blue-500 text-white" // Tab User active có nền xanh dương
              : "bg-white text-blue-600 hover:bg-blue-100" // Tab User inactive
              }`}
          >
            User
          </button>
          <button
            onClick={() => setRole("admin")} // Chọn role admin khi click
            className={`w-1/2 py-3 text-sm font-semibold transition duration-300 ${isAdmin
              ? "bg-purple-600 text-white" // Tab Admin active có nền tím đậm
              : "bg-white text-purple-600 hover:bg-purple-100" // Tab Admin inactive
              }`}
          >
            Admin
          </button>
        </div>

        {/* Tiêu đề của trang, thay đổi text và màu theo role */}
        <h2 className={`text-3xl font-bold text-center mb-6 ${isAdmin ? "text-purple-700" : "text-blue-700"
          }`}>
          {isAdmin ? "Admin Portal Login" : "Login to Library"}
        </h2>

        {/* Form đăng nhập với 2 input email và password */}
        <form className="flex flex-col space-y-4">
          <div>
            {/* Label cho trường email */}
            <label className="block mb-1 text-gray-700 font-medium" htmlFor="email">
              Email
            </label>
            {/* Input email với placeholder và viền focus thay đổi màu theo role */}
            <input
              id="email"
              type="email"
              placeholder={isAdmin ? "Enter your admin email" : "Enter your email"}
              className={`w-full px-4 py-2 rounded-lg border border-gray-300 focus:ring-2 ${isAdmin ? "focus:ring-purple-400" : "focus:ring-blue-400"
                } focus:outline-none`}
              required
            />
          </div>

          <div>
            {/* Label cho trường password */}
            <label className="block mb-1 text-gray-700 font-medium" htmlFor="password">
              Password
            </label>
            {/* Input password với placeholder và viền focus thay đổi màu theo role */}
            <input
              id="password"
              type="password"
              placeholder="Enter your password"
              className={`w-full px-4 py-2 rounded-lg border border-gray-300 focus:ring-2 ${isAdmin ? "focus:ring-purple-400" : "focus:ring-blue-400"
                } focus:outline-none`}
              required
            />
          </div>

          {/* Nút submit với màu nền và hiệu ứng hover thay đổi theo role */}
          <button
            type="submit"
            className={`w-full py-2 rounded-lg transition duration-300 font-semibold ${isAdmin
              ? "bg-purple-600 hover:bg-purple-700 text-white"
              : "bg-blue-600 hover:bg-blue-700 text-white"
              }`}
          >
            {isAdmin ? "Login as Admin" : "Login as User"}
          </button>
        </form>

        {/* Nếu role là user thì hiển thị link đăng ký */}
        {!isAdmin && (
          <p className="mt-6 text-center text-gray-600">
            Don't have an account?{" "}
            {/* Link chuyển hướng sang trang đăng ký */}
            <Link to="/register" className="text-blue-600 hover:underline font-semibold">
              Register
            </Link>
          </p>
        )}
      </div>
    </div>
  );
}
