// Dashboard cho Admin 
import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import AdminNavbar from "../../components/AdminNavbar";
import VisitChart from "../../components/VisitChart";
import { DashboardService, MetricsData } from "../../services/dashboardService";

export default function AdminDashboard() {
  // State for metrics data
  const [metrics, setMetrics] = useState<MetricsData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Fetch metrics on component mount
  useEffect(() => {
    const fetchMetrics = async () => {
      try {
        setLoading(true);
        const data = await DashboardService.getMetrics();
        setMetrics(data);
        setError(null);
      } catch (err) {
        console.error('Error fetching dashboard metrics:', err);
        setError('Failed to load dashboard metrics');
      } finally {
        setLoading(false);
      }
    };

    fetchMetrics();
  }, []);

  // Show loading indicator
  if (loading) {
    return (
      <>
        <AdminNavbar selected="dashboard" />
        <div className="min-h-screen bg-purple-50 p-6">
          <div className="text-center py-10">
            <p className="text-purple-600">Loading dashboard data...</p>
          </div>
        </div>
      </>
    );
  }

  // Show error message if there was a problem
  if (error) {
    return (
      <>
        <AdminNavbar selected="dashboard" />
        <div className="min-h-screen bg-purple-50 p-6">
          <div className="text-center py-10">
            <p className="text-red-600">{error}</p>
            <button 
              className="mt-4 bg-purple-600 text-white py-2 px-4 rounded hover:bg-purple-700"
              onClick={() => window.location.reload()}
            >
              Retry
            </button>
          </div>
        </div>
      </>
    );
  }

  return (
    <>
      {/* Đặt title cho trang admin dashboard */}
      <title>Admin Dashboard</title>

      {/* Thanh navbar với item được chọn là "dashboard" */}
      <AdminNavbar selected="dashboard" />

      <div className="min-h-screen bg-purple-50">
        <div className="p-4">
          {/* Tiêu đề chính */}
          <h2 className="text-2xl font-semibold text-purple-700 mb-4">Welcome back, Admin!</h2>

          {/* Mô tả ngắn về chức năng quản lý */}
          <p className="text-gray-700">Manage books, categories, authors and publishers.</p>

          {/* 
            Section 1: Các link quản lý các thực thể chính
            Sử dụng grid để bố trí 4 ô (books, categories, authors, publishers)
          */}
          <div className="mt-6 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
            {/* Link đến trang quản lý sách */}
            <Link to="/admin/manage/books" className="bg-white shadow-md rounded-lg p-6">
              <h3 className="text-lg font-semibold text-purple-600 mb-2">Books</h3>
              {/* Số lượng sách từ API */}
              <p className="text-3xl font-bold text-purple-800">
                {metrics?.books.toLocaleString() || "0"}
              </p>
            </Link>

            {/* Link đến trang quản lý danh mục */}
            <Link to="/admin/manage/categories" className="bg-white shadow-md rounded-lg p-6">
              <h3 className="text-lg font-semibold text-purple-600 mb-2">Categories</h3>
              <p className="text-3xl font-bold text-purple-800">
                {metrics?.categories.toLocaleString() || "0"}
              </p>
            </Link>

            {/* Link đến trang quản lý tác giả */}
            <Link to="/admin/manage/authors" className="bg-white shadow-md rounded-lg p-6">
              <h3 className="text-lg font-semibold text-purple-600 mb-2">Authors</h3>
              <p className="text-3xl font-bold text-purple-800">
                {metrics?.authors.toLocaleString() || "0"}
              </p>
            </Link>

            {/* Link đến trang quản lý nhà xuất bản */}
            <Link to="/admin/manage/publishers" className="bg-white shadow-md rounded-lg p-6">
              <h3 className="text-lg font-semibold text-purple-600 mb-2">Publishers</h3>
              <p className="text-3xl font-bold text-purple-800">
                {metrics?.publishers.toLocaleString() || "0"}
              </p>
            </Link>
          </div>

          {/* Mô tả thêm về các quản lý khác */}
          <p className="mt-6 text-gray-700">Manage users, loans and requests.</p>

          {/* 
            Section 2: Các link quản lý người dùng, mượn sách, yêu cầu
            Grid 3 cột
          */}
          <div className="mt-10 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
            {/* Link đến trang quản lý người dùng */}
            <Link to="/admin/users" className="bg-white shadow-md rounded-lg p-6">
              <h3 className="text-lg font-semibold text-purple-600 mb-2">Users</h3>
              <p className="text-3xl font-bold text-purple-800">
                {metrics?.users.toLocaleString() || "0"}
              </p>
            </Link>

            {/* Link đến trang quản lý các phiếu mượn */}
            <Link to="/admin/loans" className="bg-white shadow-md rounded-lg p-6">
              <h3 className="text-lg font-semibold text-purple-600 mb-2">Loans</h3>
              <p className="text-3xl font-bold text-purple-800">
                {metrics?.loans.toLocaleString() || "0"}
              </p>
            </Link>

            {/* Link đến trang quản lý các yêu cầu */}
            <Link to="/admin/requests" className="bg-white shadow-md rounded-lg p-6">
              <h3 className="text-lg font-semibold text-purple-600 mb-2">Requests</h3>
              <p className="text-3xl font-bold text-purple-800">
                {metrics?.requests.toLocaleString() || "0"}
              </p>
            </Link>
          </div>

          {/* 
            Section 3: Biểu đồ lượt truy cập website 
            Tiêu đề + component biểu đồ
          */}
          <div className="mt-10">
            <h2 className="text-2xl font-semibold text-purple-700 mb-4">Website Visits</h2>
            <VisitChart />
          </div>

          {/* 
            Section 4: Trending Titles và Trending Categories
            2 bảng song song (grid 2 cột)
          */}
          <h2 className="mt-10 text-2xl font-semibold text-purple-700 mb-4">Trending</h2>

          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            {/* Bảng Trending Titles */}
            <div className="bg-white shadow-md rounded-2xl p-4">
              <h3 className="text-xl font-semibold text-purple-700 mb-4">Trending Titles</h3>
              <div className="overflow-x-auto">
                <table className="min-w-full table-auto border border-gray-200">
                  <thead className="bg-purple-100 text-purple-800">
                    <tr>
                      <th className="px-4 py-2 border">Top</th>
                      <th className="px-4 py-2 border">Title</th>
                      <th className="px-4 py-2 border">Views</th>
                    </tr>
                  </thead>
                  <tbody>
                    {/* Tạo 5 dòng giả lập dữ liệu trending titles */}
                    {[...Array(5)].map((_, i) => (
                      <tr key={i} className="text-center border-t">
                        <td className="px-4 py-2 border">{i + 1}</td>
                        <td className="px-4 py-2 border">Title {i + 1}</td>
                        <td className="px-4 py-2 border text-green-600 font-medium">{(1000 - i * 100).toLocaleString()}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>

            {/* Bảng Trending Categories */}
            <div className="bg-white shadow-md rounded-2xl p-4">
              <h3 className="text-xl font-semibold text-purple-700 mb-4">Trending Categories</h3>
              <div className="overflow-x-auto">
                <table className="min-w-full table-auto border border-gray-200">
                  <thead className="bg-purple-100 text-purple-800">
                    <tr>
                      <th className="px-4 py-2 border">Top</th>
                      <th className="px-4 py-2 border">Name</th>
                      <th className="px-4 py-2 border">View</th>
                    </tr>
                  </thead>
                  <tbody>
                    {/* Tạo 5 dòng giả lập dữ liệu trending categories */}
                    {[...Array(5)].map((_, i) => (
                      <tr key={i} className="text-center border-t">
                        <td className="px-4 py-2 border">{i + 1}</td>
                        <td className="px-4 py-2 border">Category {i + 1}</td>
                        <td className="px-4 py-2 border text-green-600 font-medium">{(500 - i * 50).toLocaleString()}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
