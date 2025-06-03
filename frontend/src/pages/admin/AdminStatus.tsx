import { useEffect, useState } from "react"
import { usePollingData } from "../../hooks/use-fetch";
import { StatusLog } from "../../data/statusLogs";
import AdminNavbar from "../../components/AdminNavbar";

export interface AdminStatus {
  "server": string;
  "database": string;
}

export default function AdminStatus({ interval = 10000 }: { interval?: number }) {
  // Poll status data
  const { data: status, error: statusError } = usePollingData<AdminStatus>('/api/status', interval);
  
  // Poll logs data less frequently
  const { data: logs, error: logsError } = usePollingData<StatusLog[]>('/api/status/logs', interval * 5);
  
  // Derived state
  const error = statusError || logsError;

  const [serverUptimePercentage, setServerUptimePercentage] = useState<number>(100);
  const [databaseUptimePercentage, setDatabaseUptimePercentage] = useState<number>(100);
  const [serverDowntimeLogs, setServerDowntimeLogs] = useState<StatusLog[]>([]);
  const [databaseDowntimeLogs, setDatabaseDowntimeLogs] = useState<StatusLog[]>([]);

  // Calculate uptime percentages whenever logs change
  useEffect(() => {
    const serverLogs = logs?.filter(log => log.component === 'server') || [];
    const databaseLogs = logs?.filter(log => log.component === 'database') || [];

    // Filter logs to get only downtime messages
    if (serverLogs) setServerDowntimeLogs(serverLogs.filter(log => log.status !== "OK"));
    if (databaseLogs) setDatabaseDowntimeLogs(databaseLogs.filter(log => log.status !== "OK"));
    const calculateUptimePercentage = (logs: StatusLog[]): number => {
      if (logs.length === 0) return 100; // No logs, assume 100% 
      
      
      // Count-based simple calculation
      const okLogs = logs.filter(log => log.status === "OK").length;
      
      // Sort logs by timestamp
      const sortedLogs = [...logs].sort((a, b) => 
        new Date(a.timestamp).getTime() - new Date(b.timestamp).getTime()
      );
      
      // Calculate total time period covered by logs
      const firstLogTime = new Date(sortedLogs[0].timestamp).getTime();
      const lastLogTime = new Date(sortedLogs[sortedLogs.length - 1].timestamp).getTime();
      const totalTimeSpan = lastLogTime - firstLogTime;
      
      if (totalTimeSpan <= 0) {
        // Fallback to count-based calculation if timestamps are identical
        return Math.round((okLogs / logs.length) * 100);
      }
      
      // Calculate downtime periods
      let downtimeMs = 0;
      let lastDowntimeStart: number | null = null;
      
      sortedLogs.forEach((log, index) => {
        const currentTime = new Date(log.timestamp).getTime();
        
        if (log.status !== "OK" && lastDowntimeStart === null) {
          // Start of downtime period
          lastDowntimeStart = currentTime;
        } else if (log.status === "OK" && lastDowntimeStart !== null) {
          // End of downtime period
          downtimeMs += (currentTime - lastDowntimeStart);
          lastDowntimeStart = null;
        }
        
        // Handle case where the last log is a downtime status
        if (index === sortedLogs.length - 1 && lastDowntimeStart !== null) {
          downtimeMs += (currentTime - lastDowntimeStart);
        }
      });
      
      return 100 - Math.round((downtimeMs / totalTimeSpan) * 100);
    };

    if (serverLogs && serverLogs.length > 0) {
      // Use the function to set the server uptime percentage
      setServerUptimePercentage(calculateUptimePercentage(serverLogs));
    }

    if (databaseLogs && databaseLogs.length > 0) {
      // Use the function to set the database uptime percentage
      setDatabaseUptimePercentage(calculateUptimePercentage(databaseLogs));
    }
  }, [logs]);

  return (
    <>
      <title>Admin - System Status</title>
      <AdminNavbar selected="status"/>
      <div className="p-4">
        <h1 className="text-xl font-bold mb-4 text-purple-700">System Status (Last 30 days)</h1>
        {error && <p className="text-red-500">{error}</p>}
        
        {status ? (
          <div className="space-y-6">
            <ul className="space-y-2">
              {/* Uptime Percentage Bars */}
              <UptimePercentageBar 
                key = "server-status"
                componentTitle = "Server Status"
                status = { status.server }
                uptimePercentage = { serverUptimePercentage } />

              <UptimePercentageBar
                key = "database-status"
                componentTitle = "Database Status"
                status = { status.database }
                uptimePercentage = { databaseUptimePercentage } />
            </ul>

            {/* Downtime Logs */}
            <div className="space-y-4">
              <h2 className="text-lg font-semibold text-purple-700">Server Downtime Events</h2>
              < StatusLogContainer key = "server-downtime-logs" downtimeLogs={ serverDowntimeLogs }/>

              <h2 className="text-lg font-semibold text-purple-700">Database Downtime Events</h2>
              < StatusLogContainer key = "database-downtime-logs" downtimeLogs={ databaseDowntimeLogs }/>
            </div>
          </div>
        ) : (
          !error && <p>Loading status...</p>
        )}
      </div>
    </>
  );
}

function StatusLogContainer( {downtimeLogs} : {
  downtimeLogs: StatusLog[]
}) {
  return downtimeLogs.length > 0 ? (
    <div className="overflow-auto max-h-60 border rounded p-2">
      <table className="min-w-full">
        <thead>
          <tr className="bg-gray-100">
            <th className="py-2 px-3 text-left text-purple-700">Time</th>
            <th className="py-2 px-3 text-left text-purple-700">Status</th>
            <th className="py-2 px-3 text-left text-purple-700">Message</th>
          </tr>
        </thead>
        <tbody>
          {downtimeLogs.map(log => (
            <tr key={log.id} className="border-t">
              <td className="py-2 px-3">{new Date(log.timestamp).toLocaleString()}</td>
              <td className="py-2 px-3 text-red-600">{log.status}</td>
              <td className="py-2 px-3">{log.message}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  ) : (
    <p className="text-green-600">No downtime recorded</p>
  );
}

function UptimePercentageBar({ componentTitle, status, uptimePercentage}: {
  componentTitle: string;
  status: string;
  uptimePercentage: number;
}) {
  const animationName: string = `progressFill${componentTitle.replace(' ', '')}`;
  
  return (
    <li className="flex items-center">
      <strong className="w-36 text-purple-700">{`${componentTitle}:`}</strong>
      <span className={status === "OK" ? "text-green-600 font-bold" : "text-red-600 font-bold"}>
        {status}
      </span>
      <div className="ml-4 flex items-center">
        <div className="w-36 bg-gray-200 rounded-full h-4 mr-2 overflow-hidden">
          <div
            className="bg-green-600 h-4 rounded-full"
            style={{ 
              width: `${uptimePercentage}%`,
              animation: `progressFill${animationName} 1.0s ease-out`
            }}
          ></div>
        </div>
        <style>{`
          @keyframes progressFill${animationName} {
            from { width: 0; }
            to { width: ${uptimePercentage}%; }
          }
        `}</style>
        <span>{uptimePercentage}% uptime</span>
      </div>
    </li>
  );
}


