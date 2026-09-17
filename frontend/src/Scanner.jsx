import { useEffect, useRef, useState } from "react";
import { Html5Qrcode } from "html5-qrcode";
import Layout from "./Layout";

const API_BASE_URL = "http://localhost:8086";
const SCAN_URL = `${API_BASE_URL}/api/attendance/scan`;

const SCAN_COOLDOWN_MS = 5000;

function Scanner() {
  const [log, setLog] = useState([]);
  const scannerRef = useRef(null);
  const lastScannedRef = useRef({});
  const token = localStorage.getItem("token");

  useEffect(() => {
    if (!token) return;

    const html5QrCode = new Html5Qrcode("qr-reader");
    scannerRef.current = html5QrCode;

    html5QrCode
      .start(
        { facingMode: "environment" },
        { fps: 10, qrbox: 250 },
        (decodedText) => handleScan(decodedText),
        () => {
          // Called continuously while no QR code is found — ignore.
        }
      )
      .catch((err) => addLog(`Camera error: ${err}`));

    return () => {
      try {
        html5QrCode
          .stop()
          .then(() => html5QrCode.clear())
          .catch(() => {});
      } catch (err) {
        // stop() can throw synchronously if the scanner never
        // fully started — safe to ignore during cleanup.
      }
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [token]);

  function addLog(message) {
    setLog((prev) => [...prev, `${new Date().toLocaleTimeString()} — ${message}`]);
  }

  async function handleScan(studentNumber) {
    const now = Date.now();
    const lastTime = lastScannedRef.current[studentNumber] || 0;

    if (now - lastTime < SCAN_COOLDOWN_MS) {
      return;
    }
    lastScannedRef.current[studentNumber] = now;

    try {
      const response = await fetch(
        `${SCAN_URL}?studentNumber=${encodeURIComponent(studentNumber)}`,
        {
          method: "POST",
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      const data = await response.json().catch(() => null);

      if (response.ok) {
        if (data.timeOut) {
          addLog(`[TIME OUT] ${data.studentName} (${studentNumber}) at ${data.timeOut}`);
        } else {
          addLog(`[TIME IN] ${data.studentName} (${studentNumber}) at ${data.timeIn}`);
        }
      } else {
        addLog(`[REJECTED] ${studentNumber} — ${response.status}: ${data}`);
      }
    } catch (err) {
      addLog(`Request failed: ${err}`);
    }
  }

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
}

export default Scanner;