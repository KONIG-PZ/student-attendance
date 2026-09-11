import { useEffect, useState } from "react";
import Layout from "./Layout";

const API_BASE_URL = "http://localhost:8086";
const MY_SECTIONS_URL = `${API_BASE_URL}/api/teacher/my-sections`;

function MySections() {
  const [sections, setSections] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);
  const token = localStorage.getItem("token");

  useEffect(() => {
    async function fetchSections() {
      try {
        const response = await fetch(MY_SECTIONS_URL, {
          headers: { Authorization: `Bearer ${token}` },
        });

        if (!response.ok) {
          setError(`Failed to load sections (${response.status})`);
          setLoading(false);
          return;
        }

        const data = await response.json();
        setSections(data);
      } catch (err) {
        setError(`Request failed: ${err}`);
      } finally {
        setLoading(false);
      }
    }

    fetchSections();
  }, [token]);

  return (
    <Layout>
      <h1 className="text-xl font-semibold text-gray-800 mb-4">My Sections</h1>

      {loading && <p className="text-sm text-gray-500">Loading sections...</p>}
      {error && <p className="text-sm text-red-600">{error}</p>}

      {!loading && !error && sections.length === 0 && (
        <p className="text-sm text-gray-500">No sections assigned yet.</p>
      )}

      {!loading && !error && sections.length > 0 && (
        <div className="grid gap-3 max-w-md">
          {sections.map((section) => (
            <div
              key={section.id}
              className="bg-white rounded-lg border border-gray-200 p-4"
            >
              <p className="font-medium text-gray-800">{section.sectionName}</p>
              <p className="text-sm text-gray-500">
                {section.course} — {section.yearLevel} — {section.academicYear}
              </p>
            </div>
          ))}
        </div>
      )}
    </Layout>
  );
}

export default MySections;