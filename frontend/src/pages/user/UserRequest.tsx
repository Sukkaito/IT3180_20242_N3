
import { useEffect, useState } from "react";
import UserNavbar from "../../components/UserNavbar";

interface Request {
  id: number;
  title: string;
  requestedDate: string;
  status: "pending" | "approved" | "rejected";
}

export default function UserRequest() {
  const [requests, setRequests] = useState<Request[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchRequests = async () => {
      setLoading(true);
      try {
        const response = await fetch("/api/user/requests");

        // Không throw lỗi nữa, chỉ check status để xử lý phù hợp
        if (!response.ok) {
          // Có thể log lỗi nhưng không throw
          console.warn(`HTTP error! status: ${response.status}`);
          setRequests([]);
          return;
        }

        const text = await response.text();

        if (!text) {
          setRequests([]);
        } else {
          const data = JSON.parse(text);
          setRequests(data);
        }
      } catch {
        // Ở đây cũng không log lỗi nữa, chỉ set rỗng
        setRequests([]);
      } finally {
        setLoading(false);
      }
    };

    fetchRequests();
  }, []);

  const statusLabel = {
    pending: "Pending",
    approved: "Approved",
    rejected: "Rejected",
  };

  const statusColor = {
    pending: "text-yellow-600",
    approved: "text-green-600",
    rejected: "text-red-600",
  };

  return (
    <>
      <title>My Requests</title>
      <UserNavbar selected="request" />
      <div className="p-6 bg-gray-100 min-h-screen">
        <h1 className="text-2xl font-bold mb-4 text-blue-700">My Book Requests</h1>

        {loading ? (
          <p>Loading...</p>
        ) : requests.length === 0 ? (
          <p>You haven't requested any books yet.</p>
        ) : (
          <ul className="space-y-4">
            {requests.map((req) => (
              <li key={req.id} className="bg-white p-4 rounded shadow">
                <h2 className="text-lg font-semibold">{req.title}</h2>
                <p className="text-gray-600">Requested on: {req.requestedDate}</p>
                <p className={`${statusColor[req.status]} font-semibold`}>
                  Status: {statusLabel[req.status]}
                </p>
              </li>
            ))}
          </ul>
        )}
      </div>
    </>
  );
}
