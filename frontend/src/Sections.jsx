import { useEffect, useState } from "react";
import Layout from "./Layout";

const API_BASE_URL = "http://localhost:8086";
const SECTIONS_URL = `${API_BASE_URL}/api/sections`;

function Sections() {
  const [sections, setSections] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [formData, setFormData] = useState({
    sectionName: "",
    course: "",
    yearLevel: "",
    academicYear: "",
    active: true,
  });

  const [creating, setCreating] = useState(false);
  const [createError, setCreateError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

  const [editingSection, setEditingSection] = useState(null);
  const [editFormData, setEditFormData] = useState({
    sectionName: "",
    course: "",
    yearLevel: "",
    academicYear: "",
    active: true,
  });

  const [updating, setUpdating] = useState(false);
  const [editError, setEditError] = useState("");

  const token = localStorage.getItem("token");

  async function fetchSections() {
    try {
      setLoading(true);
      setError("");

      const response = await fetch(SECTIONS_URL, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error(`Failed to load sections (${response.status})`);
      }

      const data = await response.json();
      setSections(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    fetchSections();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  function handleChange(event) {
    const { name, value, type, checked } = event.target;

    setFormData((previous) => ({
      ...previous,
      [name]: type === "checkbox" ? checked : value,
    }));
  }

  async function handleSubmit(event) {
    event.preventDefault();

    setCreating(true);
    setCreateError("");
    setSuccessMessage("");

    const newSection = {
      academicYear: formData.academicYear,
      active: formData.active,
      course: formData.course,
      sectionName: formData.sectionName,
      yearLevel: formData.yearLevel,
    };

    try {
      const response = await fetch(SECTIONS_URL, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(newSection),
      });

      if (!response.ok) {
        const message = await response.text();

        throw new Error(
          message || `Failed to create section (${response.status})`
        );
      }

      setSuccessMessage("Section created successfully.");

      setFormData({
        sectionName: "",
        course: "",
        yearLevel: "",
        academicYear: "",
        active: true,
      });

      await fetchSections();
    } catch (err) {
      setCreateError(err.message);
    } finally {
      setCreating(false);
    }
  }

  function handleEdit(section) {
    setEditingSection(section);
    setEditError("");
    setSuccessMessage("");

    setEditFormData({
      sectionName: section.sectionName,
      course: section.course,
      yearLevel: section.yearLevel,
      academicYear: section.academicYear,
      active: section.active,
    });
  }

  function handleEditChange(event) {
    const { name, value, type, checked } = event.target;

    setEditFormData((previous) => ({
      ...previous,
      [name]: type === "checkbox" ? checked : value,
    }));
  }

  function handleCancelEdit() {
    setEditingSection(null);
    setEditError("");
  }

  async function handleUpdate(event) {
    event.preventDefault();

    if (!editingSection) {
      return;
    }

    setUpdating(true);
    setEditError("");
    setSuccessMessage("");

    const updatedSection = {
      academicYear: editFormData.academicYear,
      active: editFormData.active,
      course: editFormData.course,
      id: editingSection.id,
      sectionName: editFormData.sectionName,
      yearLevel: editFormData.yearLevel,
    };

    try {
      const response = await fetch(
        `${SECTIONS_URL}/${editingSection.id}`,
        {
          method: "PUT",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
          body: JSON.stringify(updatedSection),
        }
      );

      if (!response.ok) {
        const message = await response.text();

        throw new Error(
          message || `Failed to update section (${response.status})`
        );
      }

      setSuccessMessage("Section updated successfully.");
      setEditingSection(null);

      await fetchSections();
    } catch (err) {
      setEditError(err.message);
    } finally {
      setUpdating(false);
    }
  }

  return (
    <Layout>
      <div className="max-w-6xl mx-auto">
        <div className="mb-6">
          <h1 className="text-2xl font-semibold text-gray-800">
            Section Management
          </h1>

          <p className="mt-1 text-sm text-gray-500">
            Create, view, and edit sections registered in the attendance
            system.
          </p>
        </div>

        {successMessage && (
          <div className="mb-4 rounded-md border border-green-200 bg-green-50 p-3">
            <p className="text-sm text-green-700">{successMessage}</p>
          </div>
        )}

        {/* CREATE SECTION */}
        <div className="mb-8 rounded-lg border border-gray-200 bg-white p-6">
          <h2 className="mb-4 text-lg font-semibold text-gray-800">
            Create Section
          </h2>

          {createError && (
            <div className="mb-4 rounded-md border border-red-200 bg-red-50 p-3">
              <p className="text-sm text-red-700">{createError}</p>
            </div>
          )}

          <form onSubmit={handleSubmit}>
            <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
              <div>
                <label className="mb-1 block text-sm font-medium text-gray-700">
                  Section Name
                </label>

                <input
                  name="sectionName"
                  type="text"
                  value={formData.sectionName}
                  onChange={handleChange}
                  placeholder="BSCS 3A"
                  required
                  className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm"
                />
              </div>

              <div>
                <label className="mb-1 block text-sm font-medium text-gray-700">
                  Course
                </label>

                <input
                  name="course"
                  type="text"
                  value={formData.course}
                  onChange={handleChange}
                  placeholder="BS Computer Science"
                  required
                  className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm"
                />
              </div>

              <div>
                <label className="mb-1 block text-sm font-medium text-gray-700">
                  Year Level
                </label>

                <input
                  name="yearLevel"
                  type="text"
                  value={formData.yearLevel}
                  onChange={handleChange}
                  placeholder="3"
                  required
                  className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm"
                />
              </div>

              <div>
                <label className="mb-1 block text-sm font-medium text-gray-700">
                  Academic Year
                </label>

                <input
                  name="academicYear"
                  type="text"
                  value={formData.academicYear}
                  onChange={handleChange}
                  placeholder="2026-2027"
                  required
                  className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm"
                />
              </div>
            </div>

            <div className="mt-4 flex items-center">
              <input
                id="active"
                name="active"
                type="checkbox"
                checked={formData.active}
                onChange={handleChange}
                className="h-4 w-4"
              />

              <label htmlFor="active" className="ml-2 text-sm text-gray-700">
                Active
              </label>
            </div>

            <button
              type="submit"
              disabled={creating}
              className="mt-5 rounded-md bg-gray-800 px-4 py-2 text-sm font-medium text-white hover:bg-gray-700 disabled:opacity-50"
            >
              {creating ? "Creating..." : "Create Section"}
            </button>
          </form>
        </div>

        {/* EDIT SECTION */}
        {editingSection && (
          <div className="mb-8 rounded-lg border border-blue-200 bg-blue-50 p-6">
            <h2 className="mb-4 text-lg font-semibold text-gray-800">
              Edit Section #{editingSection.id}
            </h2>

            {editError && (
              <div className="mb-4 rounded-md border border-red-200 bg-red-50 p-3">
                <p className="text-sm text-red-700">{editError}</p>
              </div>
            )}

            <form onSubmit={handleUpdate}>
              <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
                <div>
                  <label className="mb-1 block text-sm font-medium text-gray-700">
                    Section Name
                  </label>

                  <input
                    name="sectionName"
                    type="text"
                    value={editFormData.sectionName}
                    onChange={handleEditChange}
                    required
                    className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm"
                  />
                </div>

                <div>
                  <label className="mb-1 block text-sm font-medium text-gray-700">
                    Course
                  </label>

                  <input
                    name="course"
                    type="text"
                    value={editFormData.course}
                    onChange={handleEditChange}
                    required
                    className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm"
                  />
                </div>

                <div>
                  <label className="mb-1 block text-sm font-medium text-gray-700">
                    Year Level
                  </label>

                  <input
                    name="yearLevel"
                    type="text"
                    value={editFormData.yearLevel}
                    onChange={handleEditChange}
                    required
                    className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm"
                  />
                </div>

                <div>
                  <label className="mb-1 block text-sm font-medium text-gray-700">
                    Academic Year
                  </label>

                  <input
                    name="academicYear"
                    type="text"
                    value={editFormData.academicYear}
                    onChange={handleEditChange}
                    required
                    className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm"
                  />
                </div>
              </div>

              <div className="mt-4 flex items-center">
                <input
                  id="editActive"
                  name="active"
                  type="checkbox"
                  checked={editFormData.active}
                  onChange={handleEditChange}
                  className="h-4 w-4"
                />

                <label
                  htmlFor="editActive"
                  className="ml-2 text-sm text-gray-700"
                >
                  Active
                </label>
              </div>

              <div className="mt-5 flex gap-2">
                <button
                  type="submit"
                  disabled={updating}
                  className="rounded-md bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700 disabled:opacity-50"
                >
                  {updating ? "Saving..." : "Save Changes"}
                </button>

                <button
                  type="button"
                  onClick={handleCancelEdit}
                  className="rounded-md bg-gray-200 px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-300"
                >
                  Cancel
                </button>
              </div>
            </form>
          </div>
        )}

        {/* SECTION LIST */}
        <h2 className="mb-4 text-lg font-semibold text-gray-800">
          Sections
        </h2>

        {loading && <p className="text-gray-500">Loading sections...</p>}

        {error && (
          <div className="mb-4 rounded-md border border-red-200 bg-red-50 p-3">
            <p className="text-sm text-red-700">{error}</p>
          </div>
        )}

        {!loading && !error && sections.length === 0 && (
          <div className="rounded-lg border border-gray-200 bg-white p-6">
            <p className="text-gray-500">No sections found.</p>
          </div>
        )}

        {!loading && !error && sections.length > 0 && (
          <div className="overflow-x-auto rounded-lg border border-gray-200 bg-white">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-4 py-3 text-left text-xs font-medium uppercase text-gray-500">
                    ID
                  </th>
                  <th className="px-4 py-3 text-left text-xs font-medium uppercase text-gray-500">
                    Section
                  </th>
                  <th className="px-4 py-3 text-left text-xs font-medium uppercase text-gray-500">
                    Course
                  </th>
                  <th className="px-4 py-3 text-left text-xs font-medium uppercase text-gray-500">
                    Year Level
                  </th>
                  <th className="px-4 py-3 text-left text-xs font-medium uppercase text-gray-500">
                    Academic Year
                  </th>
                  <th className="px-4 py-3 text-left text-xs font-medium uppercase text-gray-500">
                    Status
                  </th>
                  <th className="px-4 py-3 text-left text-xs font-medium uppercase text-gray-500">
                    Actions
                  </th>
                </tr>
              </thead>

              <tbody className="divide-y divide-gray-200">
                {sections.map((section) => (
                  <tr key={section.id} className="hover:bg-gray-50">
                    <td className="px-4 py-3 text-sm text-gray-600">
                      {section.id}
                    </td>

                    <td className="px-4 py-3 text-sm font-medium text-gray-800">
                      {section.sectionName}
                    </td>

                    <td className="px-4 py-3 text-sm text-gray-600">
                      {section.course}
                    </td>

                    <td className="px-4 py-3 text-sm text-gray-600">
                      {section.yearLevel}
                    </td>

                    <td className="px-4 py-3 text-sm text-gray-600">
                      {section.academicYear}
                    </td>

                    <td className="px-4 py-3">
                      <span
                        className={`inline-flex rounded-full px-2.5 py-1 text-xs font-medium ${
                          section.active
                            ? "bg-green-100 text-green-700"
                            : "bg-gray-100 text-gray-600"
                        }`}
                      >
                        {section.active ? "Active" : "Inactive"}
                      </span>
                    </td>

                    <td className="px-4 py-3">
                      <button
                        type="button"
                        onClick={() => handleEdit(section)}
                        className="rounded-md bg-blue-100 px-3 py-1.5 text-sm font-medium text-blue-700 hover:bg-blue-200"
                      >
                        Edit
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </Layout>
  );
}

export default Sections;