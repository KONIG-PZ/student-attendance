import { Link, useNavigate } from "react-router-dom";

function Layout({ children }) {
  const navigate = useNavigate();
  const username = localStorage.getItem("username");
  const role = localStorage.getItem("role");

  function handleLogout() {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    localStorage.removeItem("role");
    navigate("/login");
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <nav className="bg-white border-b border-gray-200 px-6 py-4 flex items-center justify-between">
        <div className="flex items-center gap-6">
          <span className="font-semibold text-gray-800">Student Attendance</span>
          <Link to="/" className="text-sm text-gray-600 hover:text-gray-900">
            Home
          </Link>
          <Link to="/scanner" className="text-sm text-gray-600 hover:text-gray-900">
            Scanner
          </Link>
          {role === "TEACHER" && (
            <Link to="/my-sections" className="text-sm text-gray-600 hover:text-gray-900">
              My Sections
            </Link>
          )}
          {role === "SUPER_ADMIN" && (
            <Link to="/admin-overview" className="text-sm text-gray-600 hover:text-gray-900">
              Overview
            </Link>
          )}
        </div>
        <div className="flex items-center gap-4">
          <span className="text-sm text-gray-500">
            {username} ({role})
          </span>
          <button
            onClick={handleLogout}
            className="text-sm bg-gray-100 hover:bg-gray-200 text-gray-700 px-3 py-1.5 rounded-md"
          >
            Log Out
          </button>
        </div>
      </nav>
      <main className="p-6">{children}</main>
    </div>
  );
}

export default Layout;