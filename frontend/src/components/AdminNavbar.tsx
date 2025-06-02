import { Link } from "react-router-dom";
import { useState, useEffect, useCallback } from "react";

export default function AdminNavbar({ selected = "dashboard" }: { selected?: string }) {
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
        `px-4 py-2 rounded-lg transition duration-200 ${selected === tab
            ? "bg-purple-500 text-white"
            : "bg-purple-100 text-purple-700 hover:bg-purple-200"
        }`;

    return (
        <>
            <div className="h-16" />

            <nav
                className={`fixed top-0 left-0 w-full bg-white shadow-md transition-transform duration-300 z-50 ${showNavbar ? "translate-y-0" : "-translate-y-full"
                    }`}
            >
                <div className="max-w-7xl mx-auto px-4 py-3 flex justify-between items-center">
                    <h1 className="text-xl font-bold text-purple-700">Admin Portal</h1>
                    <div className="flex space-x-3 items-center">
                        <Link to="/admin" className={getNavLinkClass("dashboard")}>
                            Dashboard
                        </Link>

                        {/* Manage dropdown */}
                        <div className="relative group">
                            <div className={`${getNavLinkClass("manage")} flex items-center gap-1 cursor-pointer`}>
                                Manage
                            </div>
                            <div className="absolute top-full left-0 hidden group-hover:flex flex-col bg-white shadow-md border rounded-lg w-36">
                                <Link to="/admin/manage/books" className="px-4 py-2 hover:bg-purple-100 text-left text-gray-700">
                                    Books
                                </Link>
                                <Link to="/admin/manage/categories" className="px-4 py-2 hover:bg-purple-100 text-left text-gray-700">
                                    Categories
                                </Link>
                                <Link to="/admin/manage/authors" className="px-4 py-2 hover:bg-purple-100 text-left text-gray-700">
                                    Authors
                                </Link>
                                <Link to="/admin/manage/publishers" className="px-4 py-2 hover:bg-purple-100 text-left text-gray-700">
                                    Publishers
                                </Link>
                            </div>
                        </div>

                        <Link to="/admin/users" className={getNavLinkClass("users")}>
                            Users
                        </Link>
                        <Link to="/admin/loans" className={getNavLinkClass("loans")}>
                            Loans
                        </Link>
                        <Link to="/admin/requests" className={getNavLinkClass("requests")}>
                            Requests
                        </Link>
                        <Link to="/admin/fines" className={getNavLinkClass("fines")}>
                            Fines
                        </Link>
                        <Link to="/admin/expense" className={getNavLinkClass("expense")}>
                            Expenses
                        </Link>
                        <Link to="/admin/infrastructure" className={getNavLinkClass("infrastructure")}>
                            Infrastructure
                        </Link>
                        <Link to="/admin/profile" className={getNavLinkClass("profile")}>
                            Profile
                        </Link>
                    </div>
                </div>
            </nav>
        </>
    );
}