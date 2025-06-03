// Component dùng để hiển thị biểu đồ số lượt truy cập website
import React, { useEffect, useState } from "react";
import axios from "axios";
import {
    LineChart, Line, XAxis, YAxis, Tooltip, CartesianGrid, ResponsiveContainer
} from "recharts";

// Time range options
const timeRangeOptions = [
    { label: "Last 10 minutes", value: { from: "-10mins", until: "now" } },
    { label: "Last 30 minutes", value: { from: "-30mins", until: "now" } },
    { label: "Last hour", value: { from: "-1hours", until: "now" } },
    { label: "Last 6 hours", value: { from: "-6hours", until: "now" } },
    { label: "Last 24 hours", value: { from: "-24hours", until: "now" } },
];

// Component dùng để hiển thị biểu đồ số lượt truy cập website
const VisitChart = () => {
    const [chartData, setChartData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [timeRange, setTimeRange] = useState(timeRangeOptions[0].value);

    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);
                const response = await axios.get('http://localhost:81/render', {
                    params: {
                        from: timeRange.from,
                        until: timeRange.until,
                        target: 'stats_counts.response.200',
                        format: 'json'
                    }
                });
                
                // Transform the data into a format that recharts can use
                const formattedData = response.data[0]?.datapoints.map(point => ({
                    timestamp: new Date(point[1] * 1000).toLocaleTimeString(),
                    value: point[0] || 0
                })) || [];
                
                setChartData(formattedData);
                setError(null);
            } catch (err) {
                console.error('Error fetching chart data:', err);
                setError('Failed to load chart data');
            } finally {
                setLoading(false);
            }
        };
        
        fetchData();
        
        // Set up interval to refresh data every 60 seconds
        const intervalId = setInterval(fetchData, 60000);
        
        // Clean up interval on component unmount
        return () => clearInterval(intervalId);
    }, [timeRange]); // Re-fetch when timeRange changes

    const handleTimeRangeChange = (e) => {
        const selectedOption = timeRangeOptions.find(option => 
            option.value.from === e.target.value.split("|")[0] && 
            option.value.until === e.target.value.split("|")[1]
        );
        setTimeRange(selectedOption.value);
    };

    return (
        <div className="visit-chart">
            <div className="chart-header">
                <div className="time-range-selector">
                    <label className="font-semibold text-purple-700 mb-4"htmlFor="timeRange">Time Range: </label>
                    
                    <select 
                        id="timeRange" 
                        value={`${timeRange.from}|${timeRange.until}`}
                        onChange={handleTimeRangeChange}
                    >
                        {timeRangeOptions.map((option, index) => (
                            <option 
                                key={index} 
                                value={`${option.value.from}|${option.value.until}`}
                            >
                                {option.label}
                            </option>
                        ))}
                    </select>
                </div>
            </div>
            {loading && <p>Loading chart data...</p>}
            {error && <p className="error">{error}</p>}
            {!loading && !error && (
                <ResponsiveContainer width="100%" height={300}>
                    <LineChart data={chartData} margin={{ top: 5, right: 30, left: 20, bottom: 5 }}>
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis dataKey="timestamp" />
                        <YAxis />
                        <Tooltip />
                        <Line type="monotone" dataKey="value" stroke="#8884d8" activeDot={{ r: 8 }} />
                    </LineChart>
                </ResponsiveContainer>
            )}
        </div>
    );
};

export default VisitChart;
