import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Login from "./Login";
import Home from "./Home";
import Scanner from "./Scanner";
import MySections from "./MySections";
import AdminOverview from "./AdminOverview";
import Sections from "./Sections";

function isLoggedIn() {
  return !!localStorage.getItem("token");
}

function PrivateRoute({ children }) {
  return isLoggedIn() ? children : <Navigate to="/login" />;
}

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />

        <Route
          path="/"
          element={
            <PrivateRoute>
              <Home />
            </PrivateRoute>
          }
        />

        <Route
          path="/scanner"
          element={
            <PrivateRoute>
              <Scanner />
            </PrivateRoute>
          }
        />

        <Route
          path="/my-sections"
          element={
            <PrivateRoute>
              <MySections />
            </PrivateRoute>
          }
        />

        <Route
          path="/admin-overview"
          element={
            <PrivateRoute>
              <AdminOverview />
            </PrivateRoute>
          }
        />

        <Route
          path="/sections"
          element={
            <PrivateRoute>
              <Sections />
            </PrivateRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;