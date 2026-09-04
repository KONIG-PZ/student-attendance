"""
Student Attendance QR Scanner
Reads student QR codes via webcam and calls the Spring Boot
attendance API to record time-in / time-out.
"""

import time
import cv2
import requests
from pyzbar.pyzbar import decode

# ---- CONFIG ----
API_BASE_URL = "http://localhost:8086"
LOGIN_URL = f"{API_BASE_URL}/api/auth/login"
SCAN_URL = f"{API_BASE_URL}/api/attendance/scan"

# Hardcoded service credentials for the scanner station.
# In production, move these to environment variables instead of
# leaving them in source code.
SCANNER_USERNAME = "it.admin"
SCANNER_PASSWORD = "admin123"

# Seconds to wait before the same studentNumber can be scanned again,
# so holding a QR code in front of the camera doesn't fire dozens
# of duplicate requests per second.
SCAN_COOLDOWN_SECONDS = 5


def login():
    """Logs in once and returns the JWT token string."""
    response = requests.post(
        LOGIN_URL,
        json={"username": SCANNER_USERNAME, "password": SCANNER_PASSWORD},
    )
    response.raise_for_status()
    token = response.json()["token"]
    print("Logged in, token acquired.")
    return token


def send_scan(token, student_number):
    """Calls POST /api/attendance/scan for the given studentNumber."""
    headers = {"Authorization": f"Bearer {token}"}
    response = requests.post(
        SCAN_URL,
        params={"studentNumber": student_number},
        headers=headers,
    )

    if response.status_code == 200:
        data = response.json()
        if data.get("timeOut"):
            print(f"[TIME OUT] {data['studentName']} ({student_number}) at {data['timeOut']}")
        else:
            print(f"[TIME IN]  {data['studentName']} ({student_number}) at {data['timeIn']}")
    elif response.status_code == 401:
        print("Token expired or invalid — re-login required.")
        return "TOKEN_EXPIRED"
    else:
        print(f"[REJECTED] {student_number} — {response.status_code}: {response.text}")

    return None


def main():
    token = login()

    # Tracks last successful scan time per student, for the cooldown.
    last_scanned = {}

    cap = cv2.VideoCapture(0)
    if not cap.isOpened():
        print("Could not open webcam.")
        return

    print("Scanner running. Press 'q' to quit.")

    while True:
        ret, frame = cap.read()
        if not ret:
            print("Failed to read from webcam.")
            break

        decoded_objects = decode(frame)

        for obj in decoded_objects:
            student_number = obj.data.decode("utf-8").strip()

            # Draw a box around the detected QR code for visual feedback.
            points = obj.polygon
            if len(points) == 4:
                pts = [(p.x, p.y) for p in points]
                for i in range(4):
                    cv2.line(frame, pts[i], pts[(i + 1) % 4], (0, 255, 0), 2)

            now = time.time()
            last_time = last_scanned.get(student_number, 0)

            if now - last_time >= SCAN_COOLDOWN_SECONDS:
                result = send_scan(token, student_number)

                if result == "TOKEN_EXPIRED":
                    token = login()
                    send_scan(token, student_number)

                last_scanned[student_number] = now

        cv2.imshow("QR Scanner - Student Attendance", frame)

        if cv2.waitKey(1) & 0xFF == ord("q"):
            break

    cap.release()
    cv2.destroyAllWindows()


if __name__ == "__main__":
    main()