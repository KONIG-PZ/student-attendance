import Layout from "./Layout";

function Home() {
  const username = localStorage.getItem("username");
  const role = localStorage.getItem("role");

  return (
    <Layout>
      <div className="bg-white rounded-lg border border-gray-200 p-6 max-w-md">
        <h1 className="text-xl font-semibold text-gray-800 mb-2">
          Welcome, {username}
        </h1>
        <p className="text-sm text-gray-500">Role: {role}</p>
      </div>
    </Layout>
  );
}

export default Home;