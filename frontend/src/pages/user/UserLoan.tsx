/*import { useEffect, useState } from "react";
import UserNavbar from "../../components/UserNavbar";

interface Loan {
  id: number;
  title: string;
  author: string;
  borrowedDate: string;
  dueDate: string;
}

export default function UserLoan() {
  const [loans, setLoans] = useState<Loan[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchLoans = async () => {
      setLoading(true);
      try {
        const response = await fetch("/api/user/loans");

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data: Loan[] = await response.json();
        setLoans(data);
      } catch (error) {
        console.error("Error fetching loans:", error);
        setLoans([]);  // Nếu lỗi thì set rỗng hoặc xử lý khác nếu muốn
      } finally {
        setLoading(false);
      }
    };

    fetchLoans();
  }, []);

  const isOverdue = (dueDate: string) => {
    return new Date(dueDate) < new Date();
  };

  return (
    <>
      <title>My Loans</title>
      <UserNavbar selected="loan" />

      <div className="p-6 bg-gray-100 min-h-screen">
        <h1 className="text-2xl font-bold mb-4 text-blue-700">Books You Borrowed</h1>

        {loading ? (
          <p>Loading...</p>
        ) : loans.length === 0 ? (
          <p>No books currently borrowed.</p>
        ) : (
          <ul className="space-y-4">
            {loans.map((loan) => (
              <li key={loan.id} className="bg-white p-4 rounded shadow">
                <h2 className="text-lg font-semibold">{loan.title}</h2>
                <p className="text-gray-700">Author: {loan.author}</p>
                <p className="text-gray-500">Borrowed: {loan.borrowedDate}</p>
                <p
                  className={`${
                    isOverdue(loan.dueDate) ? "text-red-600 font-bold" : "text-gray-600"
                  }`}
                >
                  Due: {loan.dueDate} {isOverdue(loan.dueDate) ? "(Overdue!)" : ""}
                </p>
              </li>
            ))}
          </ul>
        )}
      </div>
    </>
  );
}
*/

import { useState } from "react";
import UserNavbar from "../../components/UserNavbar";

interface Loan {
  id: number;
  title: string;
  author: string;
  borrowedDate: string;
  dueDate: string;
}

export default function UserLoan() {
  // Dữ liệu mẫu tạm thời, thay cho fetch API
  const [loans] = useState<Loan[]>([
    {
      id: 1,
      title: "Learn React",
      author: "Author A",
      borrowedDate: "2025-05-01",
      dueDate: "2025-06-01",
    },
    {
      id: 2,
      title: "Mastering TypeScript",
      author: "Author B",
      borrowedDate: "2025-05-15",
      dueDate: "2025-06-15",
    },
  ]);

  const isOverdue = (dueDate: string) => {
    return new Date(dueDate) < new Date();
  };

  return (
    <>
      <title>My Loans</title>
      <UserNavbar selected="loan" />

      <div className="p-6 bg-gray-100 min-h-screen">
        <h1 className="text-2xl font-bold mb-4 text-blue-700">Books You Borrowed</h1>

        {loans.length === 0 ? (
          <p>No books currently borrowed.</p>
        ) : (
          <ul className="space-y-4">
            {loans.map((loan) => (
              <li key={loan.id} className="bg-white p-4 rounded shadow">
                <h2 className="text-lg font-semibold">{loan.title}</h2>
                <p className="text-gray-700">Author: {loan.author}</p>
                <p className="text-gray-500">Borrowed: {loan.borrowedDate}</p>
                <p
                  className={`${
                    isOverdue(loan.dueDate) ? "text-red-600 font-bold" : "text-gray-600"
                  }`}
                >
                  Due: {loan.dueDate} {isOverdue(loan.dueDate) ? "(Overdue!)" : ""}
                </p>
              </li>
            ))}
          </ul>
        )}
      </div>
    </>
  );
}
