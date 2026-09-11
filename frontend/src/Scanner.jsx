  if (!token) {
    return (
      <Layout>
        <p>You must be logged in to use the scanner.</p>
      </Layout>
    );
  }

  return (
    <Layout>
      <div className="max-w-md">
        <h1 className="text-xl font-semibold text-gray-800 mb-4">
          QR Scanner
        </h1>
        <div
          id="qr-reader"
          className="rounded-lg overflow-hidden border border-gray-200 mb-4"
        ></div>
        <h2 className="text-sm font-medium text-gray-600 mb-2">Log</h2>
        <ul className="text-sm space-y-1">
          {log.map((entry, i) => (
            <li key={i} className="bg-gray-100 rounded px-3 py-2 text-gray-700">
              {entry}
            </li>
          ))}
        </ul>
      </div>
    </Layout>
  );