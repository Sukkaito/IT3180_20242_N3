// Component dùng để hiển thị biểu đồ số lượt truy cập website
import React, { useEffect, useState } from "react";
import axios from "axios";
import {
    LineChart, Line, XAxis, YAxis, Tooltip, CartesianGrid, ResponsiveContainer
} from "recharts";

// Component dùng để hiển thị biểu đồ số lượt truy cập website
const VisitChart = () => {
    // State lưu dữ liệu lượt truy cập (dạng mảng các đối tượng {date, views})
    const [data, setData] = useState([]);

    // useEffect chạy 1 lần sau khi component mount
    useEffect(() => {
        // Gửi request đến API lấy dữ liệu lượt truy cập
        axios.get("http://localhost:8080/api/visits") // Thay URL nếu dùng proxy
            .then(res => {
                // Format lại dữ liệu từ API về dạng { date, views }
                const formatted = res.data.map(item => ({
                    date: item.visitDate,    // ngày truy cập
                    views: item.viewCount    // số lượt xem
                }));
                // Lưu dữ liệu vào state
                setData(formatted);
            });
    }, []);

    return (
        <div className="p-4 bg-white rounded-2xl shadow-md">
            {/* Tiêu đề biểu đồ */}
            <h2 className="text-lg font-semibold mb-4">Website Visits Count</h2>

            {/* ResponsiveContainer giúp biểu đồ tự co giãn theo kích thước */}
            <ResponsiveContainer width="100%" height={300}>
                <LineChart data={data}>
                    {/* Lưới biểu đồ */}
                    <CartesianGrid strokeDasharray="3 3" />

                    {/* Trục X biểu diễn ngày */}
                    <XAxis dataKey="date" />

                    {/* Trục Y biểu diễn số lượt xem */}
                    <YAxis />

                    {/* Tooltip hiện thông tin chi tiết khi di chuột qua */}
                    <Tooltip />

                    {/* Vẽ đường biểu diễn dữ liệu */}
                    <Line
                        type="monotone"        // kiểu đường cong
                        dataKey="views"        // trường dữ liệu được vẽ
                        stroke="#8884d8"       // màu đường
                        strokeWidth={3}        // độ dày đường
                    />
                </LineChart>
            </ResponsiveContainer>
        </div>
    );
};

export default VisitChart;
