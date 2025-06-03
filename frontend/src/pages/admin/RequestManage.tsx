// Trang quản lý Requests 
import { useState, useEffect } from "react";
import AdminNavbar from "../../components/AdminNavbar";
import {
    BookRequest,
    BookRequestStatusEnum,
    BookRequestTypeEnum,
} from "../../data/bookRequests";
import { BookRequestService } from "../../services/bookRequestService";

export default function RequestManage() {
    // State để lưu giá trị tìm kiếm theo username hoặc Book Loan ID
    const [search, setSearch] = useState("");
    // State để lưu giá trị lọc theo trạng thái yêu cầu (Accepted, Denied, Pending,...)
    const [filterStatus, setFilterStatus] = useState<string>("");
    // State để lưu giá trị lọc theo loại yêu cầu (Borrowing hoặc Returning)
    const [filterType, setFilterType] = useState<string>("");
    // State to store requests
    const [requests, setRequests] = useState<BookRequest[]>([]);
    // Loading state
    const [loading, setLoading] = useState(true);

    // Load requests on component mount
    useEffect(() => {
        const loadRequests = async () => {
            try {
                setLoading(true);
                const data = await BookRequestService.getAll();
                
                // Process the data to replace null values with "Unknown"
                const processedData = data.map(request => ({
                    ...request,
                    username: request.username || "Unknown",
                    bookLoanId: request.bookLoanId || "Unknown",
                    status: request.status || "Unknown",
                    type: request.type || "Unknown",
                    createdAt: request.createdAt || new Date().toISOString(),
                    updatedAt: request.updatedAt || new Date().toISOString()
                }));
                
                setRequests(processedData);
            } catch (error) {
                console.error("Error loading requests:", error);
            } finally {
                setLoading(false);
            }
        };
        
        loadRequests();
    }, []);



    // Lọc requests dựa trên giá trị tìm kiếm, trạng thái và loại yêu cầu
    const filteredRequests = requests.filter((req) => {
        // Chuyển search thành chữ thường để so sánh không phân biệt hoa thường
        const lowerSearch = search.toLowerCase();
        // Kiểm tra xem username hoặc bookLoanId có chứa chuỗi tìm kiếm không
        const matchesSearch =
            req.username.toLowerCase().includes(lowerSearch) ||
            String(req.bookLoanId).includes(lowerSearch);
        // Kiểm tra trạng thái nếu có bộ lọc, nếu không thì mặc định là true
        const matchesStatus = filterStatus ? req.status === filterStatus : true;
        // Kiểm tra loại yêu cầu nếu có bộ lọc, nếu không thì mặc định là true
        const matchesType = filterType ? req.type === filterType : true;
        // Trả về true nếu thỏa cả 3 điều kiện
        return matchesSearch && matchesStatus && matchesType;
    });

    // Hàm trả về class màu sắc tương ứng với từng trạng thái của yêu cầu
    const getStatusColor = (status: string) => {
        switch (status) {
            case "ACCEPTED":
                return "text-green-600 font-semibold"; // Màu xanh lá cho trạng thái đã chấp nhận
            case "DENIED":
                return "text-red-600 font-semibold"; // Màu đỏ cho trạng thái bị từ chối
            case "PENDING":
                return "text-blue-700 font-semibold"; // Màu xanh dương cho trạng thái đang chờ xử lý
            default:
                return ""; // Không màu cho trạng thái khác hoặc không xác định
        }
    };

    // Hàm trả về class style cho loại yêu cầu (mượn hoặc trả)
    const getTypeColor = (type: string) => {
        switch (type) {
            case "BORROWING":
                return "bg-blue-100 text-blue-600 px-2 py-1 rounded-full text-sm font-medium"; // Kiểu nút màu xanh dương nhạt cho yêu cầu mượn
            case "RETURNING":
                return "bg-yellow-100 text-yellow-600 px-2 py-1 rounded-full text-sm font-medium"; // Kiểu nút màu vàng nhạt cho yêu cầu trả
            default:
                return ""; // Không style cho loại không xác định
        }
    };

    // Handle request processing (approve or reject)
    const handleProcessRequest = async (requestId: string, approve: boolean) => {
        try {
            await BookRequestService.processRequest(requestId, approve);
            
            // Refresh the requests list
            const updatedRequests = await BookRequestService.getAll();
            setRequests(updatedRequests);
        } catch (error) {
            console.error("Error processing request:", error);
            alert("Failed to process the request. Please try again.");
        }
    };

    // Show loading indicator while data is being fetched
    if (loading) {
        return (
            <>
                <AdminNavbar selected="requests" />
                <div className="min-h-screen bg-purple-50 p-6">
                    <div className="text-center py-10">
                        <p className="text-purple-600">Loading requests data...</p>
                    </div>
                </div>
            </>
        );
    }

    return (
        <>
            {/* Đặt title cho trang */}
            <title>Manage Book Requests</title>
            {/* Thanh điều hướng admin với mục "requests" được chọn */}
            <AdminNavbar selected="requests" />

            {/* Container chính của trang với nền tím nhạt */}
            <div className="min-h-screen bg-purple-50">
                <div className="p-4">
                    {/* Tiêu đề trang */}
                    <h2 className="text-2xl font-semibold text-purple-700 mb-2">
                        Manage Book Requests
                    </h2>
                    {/* Mô tả ngắn gọn về trang */}
                    <p className="text-gray-700 mb-4">
                        View, search and filter book requests from the system.
                    </p>

                    {/* Khu vực tìm kiếm và bộ lọc */}
                    <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between mb-4 gap-2">
                        {/* Input tìm kiếm theo username hoặc Book Loan ID */}
                        <input
                            type="text"
                            placeholder="Search by username or Book Loan ID..."
                            className="border border-purple-300 rounded px-3 py-2 w-full sm:w-1/3"
                            value={search}
                            onChange={(e) => setSearch(e.target.value)} // Cập nhật state khi nhập tìm kiếm
                        />

                        {/* Select để lọc theo trạng thái */}
                        <select
                            className="border border-purple-300 rounded px-3 py-2 w-full sm:w-1/5"
                            value={filterStatus}
                            onChange={(e) => setFilterStatus(e.target.value)} // Cập nhật state khi chọn trạng thái lọc
                        >
                            <option value="">All Statuses</option>
                            {/* Tạo option cho từng trạng thái trong enum */}
                            {Object.values(BookRequestStatusEnum).map((status) => (
                                <option key={status} value={status}>
                                    {status}
                                </option>
                            ))}
                        </select>

                        {/* Select để lọc theo loại yêu cầu */}
                        <select
                            className="border border-purple-300 rounded px-3 py-2 w-full sm:w-1/5"
                            value={filterType}
                            onChange={(e) => setFilterType(e.target.value)} // Cập nhật state khi chọn loại lọc
                        >
                            <option value="">All Types</option>
                            {/* Tạo option cho từng loại trong enum */}
                            {Object.values(BookRequestTypeEnum).map((type) => (
                                <option key={type} value={type}>
                                    {type}
                                </option>
                            ))}
                        </select>
                    </div>
                </div>

                {/* Bảng hiển thị danh sách yêu cầu */}
                <div className="px-4 pb-10">
                    <div className="overflow-x-auto bg-white shadow-lg rounded-2xl">
                        <table className="min-w-full text-center">
                            <thead className="bg-purple-100 text-purple-700">
                                <tr>
                                    {/* Các cột của bảng */}
                                    <th className="py-3 px-4">Username</th>
                                    <th className="py-3 px-4">Book Loan ID</th>
                                    <th className="py-3 px-4">Created At</th>
                                    <th className="py-3 px-4">Updated At</th>
                                    <th className="py-3 px-4">Type</th>
                                    <th className="py-3 px-4">Status</th>
                                    <th className="py-3 px-4">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                {/* Nếu không có yêu cầu nào khớp với bộ lọc, hiển thị thông báo */}
                                {filteredRequests.length === 0 && (
                                    <tr>
                                        <td colSpan={7} className="py-6 text-gray-500">
                                            No requests found.
                                        </td>
                                    </tr>
                                )}
                                {/* Lặp qua từng yêu cầu đã lọc và tạo hàng hiển thị */}
                                {filteredRequests.map((req) => (
                                    <tr key={req.id} className="border-t hover:bg-purple-50 transition">
                                        <td className="py-3 px-4">{req.username}</td>
                                        <td className="py-3 px-4">{req.bookLoanId}</td>
                                        {/* Hiển thị ngày tạo, định dạng ngày tháng giờ */}
                                        <td className="py-3 px-4 text-sm text-gray-600">
                                            {new Date(req.createdAt).toLocaleString()}
                                        </td>
                                        {/* Hiển thị ngày cập nhật cuối cùng */}
                                        <td className="py-3 px-4 text-sm text-gray-600">
                                            {new Date(req.updatedAt).toLocaleString()}
                                        </td>
                                        {/* Hiển thị loại yêu cầu với style màu */}
                                        <td className="py-3 px-4">
                                            <span className={getTypeColor(req.type)}>{req.type}</span>
                                        </td>
                                        {/* Hiển thị trạng thái yêu cầu với màu sắc */}
                                        <td className="py-3 px-4">
                                            <span className={getStatusColor(req.status)}>{req.status}</span>
                                        </td>
                                        {/* Phần hành động cho yêu cầu */}
                                        <td className="py-3 px-4 space-x-2">
                                            {/* Nếu trạng thái là PENDING, hiển thị nút Accept và Deny */}
                                            {req.status === "PENDING" ? (
                                                <>
                                                    <button 
                                                        className="bg-green-500 text-white px-3 py-1 rounded hover:bg-green-600"
                                                        onClick={() => handleProcessRequest(req.id, true)}
                                                    >
                                                        Accept
                                                    </button>
                                                    <button 
                                                        className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"
                                                        onClick={() => handleProcessRequest(req.id, false)}
                                                    >
                                                        Deny
                                                    </button>
                                                </>
                                            ) : (
                                                // Nếu không phải trạng thái chờ xử lý thì hiển thị dấu gạch ngang
                                                <span className="text-gray-400">—</span>
                                            )}
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </>
    );
}
