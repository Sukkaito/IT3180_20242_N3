export interface StatusLog {
  id: number;
  component: "server" | "database";
  status: string;
  timestamp: string;
  message: string;
}
