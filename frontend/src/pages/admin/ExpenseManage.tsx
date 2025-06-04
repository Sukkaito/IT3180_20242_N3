// Trang quản lý chi phí (Expense) 
import { useState } from "react";
import AdminNavbar from "../../components/AdminNavbar";

// Định nghĩa kiểu dữ liệu cho một bản ghi chi phí (Expense)
type Expense = {
    month: number; // Tháng từ 1 đến 12
    unit: number; // Số đơn vị tiêu thụ (điện hoặc nước)
    unitPrice: number; // Đơn giá của mỗi đơn vị
    vat: number; // Thuế VAT tính theo phần trăm
};

// Dữ liệu giả lập chi phí theo từng năm
const dataByYear = {
    2024: {
        electricity: Array.from({ length: 12 }, (_, i) => ({
            month: i + 1, // Tháng từ 1 đến 12
            unit: Math.floor(Math.random() * 100 + 100), // Số đơn vị tiêu thụ điện (100-199)
            unitPrice: 3000, // Đơn giá tiền điện
            vat: 10, // VAT 10%
        })),
        water: Array.from({ length: 12 }, (_, i) => ({
            month: i + 1, // Tháng
            unit: Math.floor(Math.random() * 50 + 50), // Số đơn vị nước (50-99)
            unitPrice: 7000, // Đơn giá tiền nước
            vat: 5, // VAT 5%
        })),
    },
    2025: {
        electricity: Array.from({ length: 12 }, (_, i) => ({
            month: i + 1,
            unit: Math.floor(Math.random() * 100 + 120), // Số đơn vị điện năm 2025 (120-219)
            unitPrice: 3000,
            vat: 10,
        })),
        water: Array.from({ length: 12 }, (_, i) => ({
            month: i + 1,
            unit: Math.floor(Math.random() * 50 + 60), // Số đơn vị nước năm 2025 (60-109)
            unitPrice: 7000,
            vat: 5,
        })),
    },
};

export default function ExpenseManage() {
    // State lưu năm hiện tại để hiển thị dữ liệu
    const [year, setYear] = useState(2025);

    // Lấy dữ liệu tiền điện và nước của năm đang chọn
    const electricityData = dataByYear[year].electricity;
    const waterData = dataByYear[year].water;

    // Hàm tính tổng tiền cơ bản, VAT và tổng cộng cho 1 mảng dữ liệu Expense
    const calculateTotals = (data: Expense[]) => {
        // Tổng tiền chưa VAT: sum(unit * unitPrice)
        const baseTotal = data.reduce((sum, row) => sum + row.unit * row.unitPrice, 0);
        // Tổng tiền VAT: sum(unit * unitPrice * vat / 100)
        const vatTotal = data.reduce((sum, row) => sum + (row.unit * row.unitPrice * row.vat) / 100, 0);
        // Tổng tiền phải trả = baseTotal + vatTotal
        const total = baseTotal + vatTotal;
        return {
            baseTotal,
            vatTotal,
            total,
        };
    };

    return (
        <>
            <title>Manage Expenses</title>
            {/* Thanh navbar với mục hiện tại là "expense" */}
            <AdminNavbar selected="expense" />

            <div className="min-h-screen bg-purple-50 p-6 space-y-6">
                <div className="flex justify-between items-center">
                    {/* Tiêu đề trang */}
                    <h2 className="text-2xl font-bold text-purple-700">Manage Expenses</h2>

                    {/* Dropdown chọn năm để hiển thị dữ liệu */}
                    <select
                        value={year}
                        onChange={(e) => setYear(parseInt(e.target.value))}
                        className="border border-purple-300 rounded px-3 py-2"
                    >
                        {/* Lấy danh sách năm có trong dữ liệu */}
                        {Object.keys(dataByYear).map((y) => (
                            <option key={y} value={y}>
                                Year {y}
                            </option>
                        ))}
                    </select>
                </div>

                {/* Bảng hiển thị chi phí điện */}
                <div className="bg-white shadow-md rounded-2xl p-4">
                    <h3 className="text-xl font-semibold text-purple-700 mb-4">Electricity Expense</h3>
                    <div className="overflow-x-auto">
                        <table className="min-w-full table-auto border border-gray-200">
                            <thead className="bg-purple-100 text-purple-800">
                                <tr>
                                    <th className="px-4 py-2 border">Month</th>
                                    <th className="px-4 py-2 border">Unit</th>
                                    <th className="px-4 py-2 border">Unit Price</th>
                                    <th className="px-4 py-2 border">Base Amount</th>
                                    <th className="px-4 py-2 border">VAT (%)</th>
                                    <th className="px-4 py-2 border">Total</th>
                                </tr>
                            </thead>
                            <tbody>
                                {/* Lặp qua từng tháng hiển thị từng dòng chi tiết */}
                                {electricityData.map((row, i) => {
                                    const base = row.unit * row.unitPrice; // Tiền chưa VAT
                                    const total = base + (base * row.vat) / 100; // Tiền có VAT
                                    return (
                                        <tr key={i} className="text-center border-t">
                                            <td className="px-4 py-2 border">{row.month}</td>
                                            <td className="px-4 py-2 border">{row.unit}</td>
                                            <td className="px-4 py-2 border">{row.unitPrice.toLocaleString()}</td>
                                            <td className="px-4 py-2 border">{base.toLocaleString()}</td>
                                            <td className="px-4 py-2 border">{row.vat}%</td>
                                            <td className="px-4 py-2 border text-green-600 font-medium">
                                                {total.toLocaleString()}
                                            </td>
                                        </tr>
                                    );
                                })}
                                {/* Dòng tổng cộng cả năm */}
                                <tr className="text-center font-semibold bg-purple-50 border-t">
                                    <td className="px-4 py-2 border" colSpan={3}>
                                        Total
                                    </td>
                                    <td className="px-4 py-2 border">
                                        {/* Tổng tiền chưa VAT trong năm */}
                                        {calculateTotals(electricityData).baseTotal.toLocaleString()}
                                    </td>
                                    <td className="px-4 py-2 border">—</td>
                                    <td className="px-4 py-2 border text-green-700">
                                        {/* Tổng tiền cả năm có VAT */}
                                        {calculateTotals(electricityData).total.toLocaleString()}
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

                {/* Bảng hiển thị chi phí nước */}
                <div className="bg-white shadow-md rounded-2xl p-4">
                    <h3 className="text-xl font-semibold text-purple-700 mb-4">Water Expense</h3>
                    <div className="overflow-x-auto">
                        <table className="min-w-full table-auto border border-gray-200">
                            <thead className="bg-purple-100 text-purple-800">
                                <tr>
                                    <th className="px-4 py-2 border">Month</th>
                                    <th className="px-4 py-2 border">Unit</th>
                                    <th className="px-4 py-2 border">Unit Price</th>
                                    <th className="px-4 py-2 border">Base Amount</th>
                                    <th className="px-4 py-2 border">VAT (%)</th>
                                    <th className="px-4 py-2 border">Total</th>
                                </tr>
                            </thead>
                            <tbody>
                                {/* Lặp qua từng tháng chi phí nước */}
                                {waterData.map((row, i) => {
                                    const base = row.unit * row.unitPrice; // Tiền chưa VAT
                                    const total = base + (base * row.vat) / 100; // Tiền có VAT
                                    return (
                                        <tr key={i} className="text-center border-t">
                                            <td className="px-4 py-2 border">{row.month}</td>
                                            <td className="px-4 py-2 border">{row.unit}</td>
                                            <td className="px-4 py-2 border">{row.unitPrice.toLocaleString()}</td>
                                            <td className="px-4 py-2 border">{base.toLocaleString()}</td>
                                            <td className="px-4 py-2 border">{row.vat}%</td>
                                            <td className="px-4 py-2 border text-green-600 font-medium">
                                                {total.toLocaleString()}
                                            </td>
                                        </tr>
                                    );
                                })}
                                {/* Dòng tổng cộng chi phí nước trong năm */}
                                <tr className="text-center font-semibold bg-purple-50 border-t">
                                    <td className="px-4 py-2 border" colSpan={3}>
                                        Total
                                    </td>
                                    <td className="px-4 py-2 border">
                                        {/* Tổng tiền chưa VAT */}
                                        {calculateTotals(waterData).baseTotal.toLocaleString()}
                                    </td>
                                    <td className="px-4 py-2 border">—</td>
                                    <td className="px-4 py-2 border text-green-700">
                                        {/* Tổng tiền đã bao gồm VAT */}
                                        {calculateTotals(waterData).total.toLocaleString()}
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </>
    );
}
