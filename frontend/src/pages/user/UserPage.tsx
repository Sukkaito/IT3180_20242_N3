import { useState } from "react";
import UserNavbar from "../../components/UserNavbar";

interface SummaryData {
  currentLoans: number;
  unpaidFines: number;
  pendingRequests: number;
}

export default function UserPage() {
  // Comment useState và useEffect liên quan fetch đi
  // const [summary, setSummary] = useState<SummaryData | null>(null);
  // const [loading, setLoading] = useState(true);
  // const [error, setError] = useState(false);

  // Comment useEffect đi
  /*
  useEffect(() => {
    const fetchSummary = async () => {
      try {
        const response = await fetch("/api/user/summary");

        const contentType = response.headers.get("content-type");
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        if (!contentType || !contentType.includes("application/json")) {
          const text = await response.text();
          console.error("Expected JSON, received:", text);
          throw new Error("Invalid response format");
        }

        const data = await response.json();
        setSummary(data);
      } catch (error) {
        console.error("Error fetching summary:", error);
        setError(true);
      } finally {
        setLoading(false);
      }
    };

    fetchSummary();
  }, []);
  */

  return (
    <>
      <title>User Dashboard</title>
      <UserNavbar selected="home" />
      <div className="p-6 bg-gray-100 min-h-screen">
        <h1 className="text-3xl font-bold mb-6 text-blue-700">
          Welcome to Your Dashboard
        </h1>

        {/* Tạm thời bỏ phần hiển thị dữ liệu */}
        <p>Summary data loading is disabled temporarily.</p>

        {/* Nếu muốn, bạn có thể hiển thị một dữ liệu mặc định */}
        {/* 
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div className="bg-white p-6 rounded shadow text-center">
            <p className="text-4xl font-bold text-indigo-600">0</p>
            <p className="mt-2 text-lg font-medium">Books Borrowed</p>
          </div>
          <div className="bg-white p-6 rounded shadow text-center">
            <p className="text-4xl font-bold text-red-600">0đ</p>
            <p className="mt-2 text-lg font-medium">Unpaid Fines</p>
          </div>
          <div className="bg-white p-6 rounded shadow text-center">
            <p className="text-4xl font-bold text-yellow-600">0</p>
            <p className="mt-2 text-lg font-medium">Pending Requests</p>
          </div>
        </div>
        */}
      </div>
    </>
  );
}
