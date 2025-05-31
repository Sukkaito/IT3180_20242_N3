import { useCallback, useEffect, useState } from "react";
import { Link } from "react-router-dom";

export default function UserNavbar({ selected = "home" }: { selected?: string }) {
  const [showNavbar, setShowNavbar] = useState(true);
  const [lastScrollY, setLastScrollY] = useState(0);

  const handleScroll = useCallback(() => {
    const currentScrollY = window.scrollY;
    setShowNavbar(currentScrollY < lastScrollY || currentScrollY < 10);
    setLastScrollY(currentScrollY);
  }, [lastScrollY]);

  useEffect(() => {
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [handleScroll, lastScrollY]);

  const getNavLinkClass = (tab: string) =>
    `px-4 py-2 rounded-lg transition duration-200 ${
      selected === tab
        ? "bg-blue-500 text-white"
        : "bg-blue-100 text-blue-700 hover:bg-blue-200"
    }`;
  return (  
    <>
      {/* Spacer to prevent content from being hidden behind the navbar */}
      <div className="h-16" />

      <nav
        className={`fixed top-0 left-0 w-full bg-white shadow-md transition-transform duration-300 z-50 ${
          showNavbar ? "translate-y-0" : "-translate-y-full"
        }`}
      >
        <div className="max-w-7xl mx-auto px-4 py-3 flex justify-between items-center">
          <h1 className="text-xl font-bold text-blue-700">Library Portal</h1>
          <div className="flex space-x-3">
          <Link to="/user" className={getNavLinkClass("home")}>
              Home
            </Link>
            <Link to="/user/search" className={getNavLinkClass("search")}>
              Search
            </Link>
            <Link to="/user/loaned" className={getNavLinkClass("loan")}>
              Loaned
            </Link>
            <Link to="/user/requests" className={getNavLinkClass("request")}>
              Requests
            </Link>
            <Link to="/user/fines" className={getNavLinkClass("fine")}>
              Fines
            </Link>
            <Link to="/user/profile" className={getNavLinkClass("profile")}>
              Profile
            </Link>
          </div>
        </div>
      </nav>
    </>
  );
}
