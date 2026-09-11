import { useEffect, useState } from "react";
import Layout from "./Layout";

const API_BASE_URL = "http://localhost:8086";
const OVERVIEW_URL = `${API_BASE_URL}/api/admin/overview`;

function AdminOverview() {
  const [overview, setOverview] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);
  const token = localStorage.getItem("token");

  useEffect(() => {
    async function fetchOverview() {
      try {
        const response = await fetch(OVERVIEW_URL, {
          headers: { Authorization: `Bearer ${token}` },
        });

        if (!response.ok) {
          setError(`Failed to load overview (${response.status})`);
          setLoading(false);
          return;
        }

        const data = await response.json();
        setOverview(data);
      } catch (err) {
        setError(`Request failed: ${err}`);
      } finally {
        setLoading(false);
      }
    }

    fetchOverview();
  }, [token]);

  return (
    <Layout>
      <h1 className="text-xl font-semibold text-gray-800 mb-4">System Overview</h1>

      {loading && <p className="text-sm text-gray-500">Loading overview...</p>}
      {error && <p className="text-sm text-red-600">{error}</p>}

      {!loading && !error && overview && (
        <div className="grid grid-cols-2 sm:grid-cols-3 gap-3 max-w-2xl">
          {[
            ["Total Students", overview.totalStudents],
            ["Active Students", overview.activeStudents],
            ["Inactive Students", overview.inactiveStudents],
            ["Total Teachers", overview.totalTeachers],
            ["Total Sections", overview.totalSections],
            ["Active Sections", overview.activeSections],
            ["Inactive Sections", overview.inactiveSections],
          ].map(([label, value]) => (
            <div
              key={label}
              className="bg-white rounded-lg border border-gray-200 p-4"
            >
              <p className="text-2xl font-semibold text-gray-800">{value}</p>
              <p className="text-xs text-gray-500 mt-1">{label}</p>
            </div>
          ))}
        </div>
      )}
    </Layout>
  );
}

export default AdminOverview;