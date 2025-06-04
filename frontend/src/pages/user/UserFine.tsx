import UserNavbar from "../../components/UserNavbar";

interface Fine {
  id: number;
  reason: string;
  amount: number;
  paid: boolean;
}

// Dữ liệu tĩnh
const fines: Fine[] = [
  {
    id: 1,
    reason: "Late return - Clean Code",
    amount: 20000,
    paid: false,
  },
  {
    id: 2,
    reason: "Book damage - Refactoring",
    amount: 50000,
    paid: true,
  },
  {
    id: 3,
    reason: "Lost book - DSA Handbook",
    amount: 150000,
    paid: false,
  },
];

// Hàm định dạng tiền VND
const formatCurrency = (amount: number) =>
  new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND" }).format(amount);

export default function UserFine() {
  const totalFine = fines.reduce(
    (sum, fine) => (fine.paid ? sum : sum + fine.amount),
    0
  );

  return (
    <>
      <UserNavbar selected="fine" />

      <div className="p-6 bg-gray-100 min-h-screen">
        <h1 className="text-2xl font-bold mb-4 text-blue-700">Your Fines</h1>

        {fines.length === 0 ? (
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
                  <p>Amount: {formatCurrency(fine.amount)}</p>
                  <p>Status: {fine.paid ? "✅ Paid" : "❌ Unpaid"}</p>
                </li>
              ))}
            </ul>

            <div className="mt-6 text-lg font-semibold">
              Total unpaid:{" "}
              <span className="text-red-600">{formatCurrency(totalFine)}</span>
            </div>
          </>
        )}
      </div>
    </>
  );
}
