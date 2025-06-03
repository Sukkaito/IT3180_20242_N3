// Trang quản lý Loans 
import { useState, useEffect } from "react";
import AdminNavbar from "../../components/AdminNavbar";
import { BookLoan, LoanStatus } from "../../data/bookLoans";
import { STORAGE_KEY_PREFIX } from "../../services/baseService";
import { BookLoanService } from "../../services/bookLoanService";

// Local storage key
const BOOK_LOANS_STORAGE_KEY = `${STORAGE_KEY_PREFIX}loans`;

export default function LoansManage() {
    // State để lưu giá trị tìm kiếm
    const [search, setSearch] = useState("");
    // State để lưu trạng thái lọc (filter status)
    const [filterStatus, setFilterStatus] = useState<string>("");
    // State để lưu ID của khoản vay (loan) đang chỉnh sửa tiền phạt
    const [editingFineLoanId, setEditingFineLoanId] = useState<string | null>(null);
    // State để lưu số tiền phạt đang nhập
    const [fineAmount, setFineAmount] = useState<string>("");
    // State to store loans
    const [loans, setLoans] = useState<BookLoan[]>([]);
    // Loading state
    const [loading, setLoading] = useState(true);

    // Load loans on component mount
    useEffect(() => {
        const loadLoans = async () => {
            try {
                setLoading(true);
                const loanData = await BookLoanService.getAll();
                setLoans(loanData);
            } catch (error) {
                console.error("Error loading loans:", error);
            } finally {
                setLoading(false);
            }
        };
        
        loadLoans();
    }, []);

    // Lọc danh sách loans dựa trên giá trị tìm kiếm và trạng thái lọc
    const filteredLoans = loans.filter((loan) => {
        const lowerSearch = search.toLowerCase(); // chuyển về chữ thường để so sánh
        // Kiểm tra có khớp tìm kiếm username, bookTitle hoặc bookCopyId không
        const matchesSearch =
            loan.userUserName.toLowerCase().includes(lowerSearch) ||
            loan.bookCopyOriginalBookTitle.toLowerCase().includes(lowerSearch) ||
            String(loan.bookCopyId).includes(lowerSearch);
        // Kiểm tra trạng thái có khớp với filterStatus (nếu filterStatus không rỗng)
        const matchesStatus = filterStatus ? loan.status === filterStatus : true;
        return matchesSearch && matchesStatus; // chỉ giữ những phần tử khớp cả 2 điều kiện
    });

    // Hàm trả về màu chữ CSS cho trạng thái của khoản vay
    const getStatusColor = (status: LoanStatus) => {
        switch (status) {
            case "BORROWED":
                return "text-green-600 font-semibold";
            case "REQUEST_BORROWING":
                return "text-blue-600 font-semibold";
            case "REQUEST_RETURNING":
                return "text-yellow-600 font-semibold";
            case "RETURNED":
                return "text-gray-600 font-semibold";
            case "REJECTED":
                return "text-red-600 font-semibold";
            case "NONRETURNABLE":
                return "text-purple-600 font-semibold";
            default:
                return "";
        }
    };

    // Xử lý lưu tiền phạt
    const handleSaveFine = async (loan: BookLoan) => {
        try {
            const fineAmountNum = parseFloat(fineAmount);
            if (isNaN(fineAmountNum) || fineAmountNum < 0) {
                alert("Please enter a valid fine amount");
                return;
            }
            
            await BookLoanService.addFine({bookLoanId: loan.id,
                amount: fineAmountNum, 
                username: loan.userUserName,
                description: `Fine for loan ${loan.id}`});
            
            // Refresh loans list
            const updatedLoans = await BookLoanService.getAll();
            setLoans(updatedLoans);
            
            // Close the fine editing form
            setEditingFineLoanId(null);
            setFineAmount("");
        } catch (error) {
            console.error("Error adding fine:", error);
            alert("Failed to add fine. Please try again.");
        }
    };

    // Hủy chỉnh sửa tiền phạt, reset các state liên quan
    const handleCancelFine = () => {
        setEditingFineLoanId(null);
        setFineAmount("");
    };

    // Show loading indicator while data is being fetched
    if (loading) {
        return (
            <>
                <AdminNavbar selected="loans" />
                <div className="min-h-screen bg-purple-50 p-6">
                    <div className="text-center py-10">
                        <p className="text-purple-600">Loading loans data...</p>
                    </div>
                </div>
            </>
        );
    }

    return (
        <>
            {/* Tiêu đề trang */}
            <title>Manage Book Loans</title>
            {/* Thanh navbar của admin, highlight mục "loans" */}
            <AdminNavbar selected="loans" />

            {/* Container chính */}
            <div className="min-h-screen bg-purple-50">
                <div className="p-4">
                    {/* Tiêu đề chính */}
                    <h2 className="text-2xl font-semibold text-purple-700 mb-2">
                        Manage Book Loans
                    </h2>
                    {/* Mô tả chức năng */}
                    <p className="text-gray-700 mb-4">
                        View, search and filter book loans from the system.
                    </p>

                    {/* Khung tìm kiếm và lọc trạng thái */}
                    <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between mb-4 gap-2">
                        {/* Input tìm kiếm */}
                        <input
                            type="text"
                            placeholder="Search by username, Book Title or Copy ID..."
                            className="border border-purple-300 rounded px-3 py-2 w-full sm:w-1/3"
                            value={search}
                            onChange={(e) => setSearch(e.target.value)}
                        />

                        {/* Select lọc theo trạng thái */}
                        <select
                            className="border border-purple-300 rounded px-3 py-2 w-full sm:w-1/5"
                            value={filterStatus}
                            onChange={(e) => setFilterStatus(e.target.value)}
                        >
                            <option value="">All Statuses</option>
                            {[
                                "REJECTED", "REQUEST_BORROWING", "BORROWED", "REQUEST_RETURNING", "RETURNED","NONRETURNABLE"
                            ].map((status) => (
                                <option key={status} value={status}>
                                    {status}
                                </option>
                            ))}
                        </select>
                    </div>
                </div>

                {/* Bảng danh sách các khoản vay */}
                <div className="px-4 pb-10">
                    <div className="overflow-x-auto bg-white shadow-lg rounded-2xl">
                        <table className="min-w-full text-center">
                            <thead className="bg-purple-200 text-purple-900 rounded-t-2xl">
                                <tr>
                                    {/* Tiêu đề cột */}
                                    {[
                                        "Username", "Book Title", "Book Copy ID", "Loan Date", "Due Date",
                                        "Actual Return Date", "Loaned At", "Updated At", "Status", "Action"
                                    ].map((heading) => (
                                        <th key={heading} className="py-2 px-3 text-sm font-semibold">
                                            {heading}
                                        </th>
                                    ))}
                                </tr>
                            </thead>
                            <tbody>
                                {/* Nếu không có khoản vay nào khớp điều kiện lọc */}
                                {filteredLoans.length === 0 ? (
                                    <tr>
                                        <td colSpan={10} className="py-6 text-gray-500">
                                            No loans found.
                                        </td>
                                    </tr>
                                ) : (
                                    filteredLoans.flatMap((loan, index) => {
                                        // Kiểm tra xem khoản vay này có đang chỉnh sửa tiền phạt không
                                        const isEditingFine = loan.id === editingFineLoanId;

                                        return [
                                            <tr
                                                key={loan.id}
                                                className={`${index % 2 === 0 ? "bg-white" : "bg-purple-50"
                                                    } border-b hover:bg-purple-100 transition`}
                                            >
                                                {/* Dữ liệu các cột */}
                                                <td className="py-2 px-3">{loan.userUserName}</td>
                                                <td className="py-2 px-3">{loan.bookCopyOriginalBookTitle}</td>
                                                <td className="py-2 px-3">{loan.bookCopyId}</td>
                                                <td className="py-2 px-3 text-sm text-gray-600">
                                                    {new Date(loan.loanDate).toLocaleDateString()}
                                                </td>
                                                <td className="py-2 px-3 text-sm text-gray-600">
                                                    {new Date(loan.dueDate).toLocaleDateString()}
                                                </td>
                                                <td className="py-2 px-3 text-sm text-gray-600">
                                                    {loan.actualReturnDate
                                                        ? new Date(loan.actualReturnDate).toLocaleDateString()
                                                        : "—"}
                                                </td>
                                                <td className="py-2 px-3 text-sm text-gray-600">
                                                    {new Date(loan.loanedAt).toLocaleString()}
                                                </td>
                                                <td className="py-2 px-3 text-sm text-gray-600">
                                                    {new Date(loan.updatedAt).toLocaleString()}
                                                </td>
                                                <td className="py-2 px-3">
                                                    {/* Hiển thị trạng thái với màu nền theo status */}
                                                    <span
                                                        className={`px-2 py-1 rounded-full text-xs font-semibold ${getStatusColor(loan.status as LoanStatus)}`}
                                                    >
                                                        {loan.status}
                                                    </span>
                                                </td>
                                                <td className="py-2 px-3 space-x-1">
                                                    {/* Nút chỉnh sửa tiền phạt */}
                                                    <button
                                                        className="bg-red-500 hover:bg-red-600 text-white px-2 py-1 text-sm rounded shadow"
                                                        onClick={() => {
                                                            if (editingFineLoanId === loan.id) {
                                                                setEditingFineLoanId(null);
                                                                setFineAmount("");
                                                            } else {
                                                                setEditingFineLoanId(loan.id);
                                                                setFineAmount("");
                                                            }
                                                        }}
                                                    >
                                                        💸
                                                    </button>
                                                </td>
                                            </tr>,
                                            // Nếu đang chỉnh sửa tiền phạt, hiển thị thêm một hàng để nhập tiền phạt
                                            isEditingFine && (
                                                <tr key={`${loan.id}-fine-edit`} className={`${index % 2 === 0 ? "bg-purple-50" : "bg-white"}`}>
                                                    <td colSpan={10} className="py-2 px-3 text-left">
                                                        <div className="flex items-center space-x-2">
                                                            {/* Input nhập tiền phạt */}
                                                            <input
                                                                type="number"
                                                                min={0}
                                                                placeholder="Enter fine amount"
                                                                className="border border-purple-300 rounded px-2 py-1 w-40"
                                                                value={fineAmount}
                                                                onChange={(e) => setFineAmount(e.target.value)}
                                                                autoFocus
                                                            />
                                                            {/* Nút lưu tiền phạt */}
                                                            <button
                                                                className="bg-green-500 text-white px-3 py-1 rounded hover:bg-green-600"
                                                                onClick={() => handleSaveFine(loan)}
                                                            >
                                                                Save
                                                            </button>
                                                            {/* Nút hủy chỉnh sửa */}
                                                            <button
                                                                className="bg-gray-400 text-white px-3 py-1 rounded hover:bg-gray-500"
                                                                onClick={handleCancelFine}
                                                            >
                                                                Cancel
                                                            </button>
                                                        </div>
                                                    </td>
                                                </tr>
                                            ),
                                        ];
                                    })
                                )}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </>
    );
}
