import UserNavbar from "../../components/UserNavbar.tsx";
import books from "../../data/books.ts";
import Book from "../../components/Book.tsx";


export default function UserPage() {
  return (
    <>
      <title>Home</title>
      <UserNavbar selected="home"/>
      
      <div className="min-h-screen bg-blue-50">

        <div className="p-4">
          <h2 className="text-2xl font-semibold text-blue-700 mb-4">Welcome back!</h2>
          <p className="text-gray-700">
            Browse available books, manage your loans, requests, and fines. Use the filter to find exactly what you need.
          </p>

          {/* Book list */}
          <div className="mt-6 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
            {books.map((book) => (<Book key={book.id} book={book} />))}
          </div>

          {/* For scrolling testing purpose */}
          <p className="text-gray-700 mt-4 mb-4 text-justify leading-100">
              Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
              Lorem ipsum dolor sit amet consectetur, adipisicing elit. Maxime, dicta iste, sapiente corporis vero fugit odit totam omnis tempore aut, quia quis. Totam aut nihil soluta sint, magni velit unde.
              Lorem ipsum dolor sit, amet consectetur adipisicing elit. Deleniti in quidem earum dolorum molestiae, eligendi recusandae tempora eos eius provident dolore aperiam nihil aut modi esse similique, pariatur quia vel.
              Lorem ipsum dolor sit amet consectetur adipisicing elit adipisicing elit. Quasi, cumque. Quod, voluptatibus!
              Lorem ipsum dolor sit amet consectetur adipisicing elit. Quasi, cumque.
              Quod, voluptatibus! Lorem ipsum dolor sit amet consectetur adipisicing elit. Quasi, cumque. Quod, voluptatibus!

          </p>
        </div>
      </div>
    </>
  );
}
