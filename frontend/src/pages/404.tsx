
export default function NotFound() {
  return (
    <>
      <title>404 - Page Not Found</title>
      <div className="flex flex-col items-center justify-center h-screen">
        <h1 className="text-4xl font-bold text-blue-700">404 - Page Not Found</h1>
        <p className="mt-4 text-lg">The page you are looking for does not exist.</p>
      </div>
    </>
  );
}