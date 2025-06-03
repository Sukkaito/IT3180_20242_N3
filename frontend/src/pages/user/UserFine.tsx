import { useEffect, useState } from "react";
import UserNavbar from "../../components/UserNavbar";

interface Fine {
  id: number;
  reason: string;
  amount: number;
  paid: boolean;
}

export default function UserFine() {
  const [fines, setFines] = useState<Fine[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchFines = async () => {
      setLoading(true);
      setError(null);  // reset lỗi trước khi fetch
      try {
        const response = await fetch("/api/user/fines");

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const text = await response.text();

        if (!text) {
          // response body rỗng, set mảng rỗng
          setFines([]);
        } else {
          const data = JSON.parse(text);
          setFines(data);
        }
      } catch (error: any) {
        console.error("Error fetching fines:", error);
        setError(error.message || "Unknown error");
        setFines([]);  // lỗi thì set mảng rỗng tạm thời
      } finally {
        setLoading(false);
      }
    };

    fetchFines();
  }, []);

  const totalFine = fines.reduce((sum, fine) => (fine.paid ? sum : sum + fine.amount), 0);

  return (
    <>
      <title>My Fines</title>
      <UserNavbar selected="fine" />

      <div className="p-6 bg-gray-100 min-h-screen">
        <h1 className="text-2xl font-bold mb-4 text-blue-700">Your Fines</h1>

        {loading ? (
          <p>Loading...</p>
        ) : error ? (
          <p className="text-red-600 font-semibold">Error: {error}</p>
        ) : fines.length === 0 ? (
          <p>You have no fines. 🎉</p>
        ) : (
          <>
            <ul className="space-y-4">
              {fines.map((fine) => (
                <li
                  key={fine.id}
                  className={`p-4 rounded shadow ${
                    fine.paid ? "bg-green-100" : "bg-red-100"
                  }`}
                >
                  <p className="font-medium">{fine.reason}</p>
                  <p>Amount: {fine.amount.toLocaleString()} VND</p>
                  <p>Status: {fine.paid ? "Paid" : "Unpaid"}</p>
                </li>
              ))}
            </ul>

            <div className="mt-6 text-lg font-semibold">
              Total unpaid:{" "}
              <span className="text-red-600">{totalFine.toLocaleString()} VND</span>
            </div>
          </>
        )}
      </div>
    </>
  );
}
