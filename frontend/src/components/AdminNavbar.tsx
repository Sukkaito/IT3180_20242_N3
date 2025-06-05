import { Link } from "react-router-dom";
import { useState, useEffect, useCallback } from "react";

interface NavItem {
    name: string;
    path: string;
    key: string;
}

const navItems: NavItem[] = [
    { name: "Users", path: "/admin/users", key: "users" },
    { name: "Loans", path: "/admin/loans", key: "loans" },
    { name: "Requests", path: "/admin/requests", key: "requests" },
    { name: "Fines", path: "/admin/fines", key: "fines" },
    { name: "Expenses", path: "/admin/expense", key: "expense" },
    { name: "Infrastructure", path: "/admin/status", key: "status" },
    { name: "Profile", path: "/admin/profile", key: "profile" },
    { name: "Logout", path: "/logout", key: "logout" }
];

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

                        <Link key="Dashboard" to="/admin" className={getNavLinkClass("dashboard")}>
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

                        {navItems.map((item) => (
                            <Link key={item.key} to={item.path} className={getNavLinkClass(item.key)}>
                                {item.name}
                            </Link>
                        ))}
                    </div>
                </div>
            </nav>
        </>
    );
}