import UserNavbar from "../../components/UserNavbar";

export default function UserBookSearch() {
  return (
    <>
      <title>Search</title>
      <UserNavbar selected="search"/>

      <div className="flex flex-col items-center justify-center h-auto bg-gray-100 pt-5">
        <h1 className="text-2xl font-bold text-blue-700 mb-4">Search</h1>
        <input
          type="text"
          placeholder="Search for books, authors, etc."
          className="border border-gray-300 rounded-lg px-4 py-2 w-1/3 mb-4"
        />
        <button className="bg-blue-500 text-white rounded-lg px-4 py-2 hover:bg-blue-600">
          Search
        </button>
      </div>
    </>
  )
}
