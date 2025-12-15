# Leave & OD Approval System - API Documentation

## Base URL
```
http://localhost:8080/api
```

## Configuration Required
Before running, update `application.properties` with your Supabase credentials:
```properties
supabase.storage.url=https://[your-project].supabase.co/storage/v1
supabase.storage.bucket=images
supabase.storage.anon-key=[your-anon-key]
```

---

## Authentication Endpoints

### 10. Register New User
**Endpoint:** `POST /api/auth/register`

**Description:** Register a new user (Student, Faculty Advisor, Year Coordinator, or HOD)

**Request Body (Authority):**
```json
{
  "name": "Dr. John Smith",
  "email": "john.smith@srmasd.edu.in",
  "role": "FACULTY_ADVISOR"
}
```

**Request Body (Student):**
```json
{
  "name": "Jane Doe",
  "email": "jane.doe@srmasd.edu.in",
  "role": "STUDENT",
  "rollNo": "RA2111003010001",
  "department": "CSE",
  "year": 2,
  "section": "A",
  "semester": 4
}
```

**Role values:** `STUDENT`, `FACULTY_ADVISOR`, `YEAR_COORDINATOR`, `HOD`

**Response:**
```json
{
  "userId": 1,
  "name": "Jane Doe",
  "email": "jane.doe@srmasd.edu.in",
  "role": "STUDENT",
  "studentId": 1,
  "rollNo": "RA2111003010001",
  "department": "CSE",
  "year": 2,
  "section": "A",
  "semester": 4
}
```

**Note:** Email must end with `@srmasd.edu.in`

---

### 11. Login User
**Endpoint:** `POST /api/auth/login`

**Description:** Login with institutional email (simple prototype authentication)

**Request Body:**
```json
{
  "email": "jane.doe@srmasd.edu.in"
}
```

**Response:** Same as registration response

**Example:**
```javascript
fetch('http://localhost:8080/api/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ email: 'jane.doe@srmasd.edu.in' })
})
.then(response => response.json())
.then(data => {
  console.log('User ID:', data.userId);
  console.log('Role:', data.role);
  if (data.studentId) {
    console.log('Student ID:', data.studentId);
  }
});
```

---

### 12. Get User Details by ID
**Endpoint:** `GET /api/auth/user/{userId}`

**Description:** Get user information by user ID

**Example:**
```javascript
fetch('http://localhost:8080/api/auth/user/1')
  .then(response => response.json())
  .then(data => console.log(data));
```

**Response:** Same as login/register response

---

## Student Endpoints

### 1. Submit Leave Request with Image Upload
**Endpoint:** `POST /api/student/{studentId}/leave-request/upload`

**Description:** Student uploads an image of their leave/OD letter

**Parameters:**
- `studentId` (path parameter) - ID of the student
- `file` (multipart form data) - Image file (JPG, PNG, PDF)
- `type` (form parameter) - Leave type: `OD` or `ML`

**Example using cURL:**
```bash
curl -X POST http://localhost:8080/api/student/1/leave-request/upload \
  -F "file=@/path/to/leave-letter.jpg" \
  -F "type=OD"
```

**Example using JavaScript (fetch):**
```javascript
const formData = new FormData();
formData.append('file', fileInput.files[0]);
formData.append('type', 'OD');

fetch('http://localhost:8080/api/student/1/leave-request/upload', {
  method: 'POST',
  body: formData
})
.then(response => response.json())
.then(data => console.log(data));
```

**Response:**
```json
{
  "requestId": 1,
  "type": "OD",
  "status": "PENDING",
  "documentPath": "https://[project].supabase.co/storage/v1/object/public/images/uuid_filename.jpg",
  "createdAt": "2025-12-15T10:30:00",
  "approvalSteps": [
    {
      "approverName": "Dr. Smith",
      "approverRole": "FACULTY_ADVISOR",
      "decision": "PENDING",
      "remarks": null,
      "actionTime": null,
      "stepOrder": 1
    },
    {
      "approverName": "Dr. Johnson",
      "approverRole": "YEAR_COORDINATOR",
      "decision": "PENDING",
      "remarks": null,
      "actionTime": null,
      "stepOrder": 2
    },
    {
      "approverName": "Dr. Williams",
      "approverRole": "HOD",
      "decision": "PENDING",
      "remarks": null,
      "actionTime": null,
      "stepOrder": 3
    }
  ],
  "queries": []
}
```

---

### 2. Submit Leave Request with Text Message
**Endpoint:** `POST /api/student/{studentId}/leave-request/message`

**Description:** Student types their leave request in a fixed format

**Parameters:**
- `studentId` (path parameter) - ID of the student

**Request Body:**
```json
{
  "type": "ML",
  "message": "I am suffering from fever and unable to attend classes. Requesting medical leave for 3 days from 16th Dec to 18th Dec."
}
```

**Example using JavaScript:**
```javascript
fetch('http://localhost:8080/api/student/1/leave-request/message', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    type: 'ML',
    message: 'Leave request message...'
  })
})
.then(response => response.json())
.then(data => console.log(data));
```

**Response:** Same as endpoint #1

---

### 3. Get All Student Requests
**Endpoint:** `GET /api/student/{studentId}/leave-requests`

**Description:** Get all leave requests submitted by a student

**Parameters:**
- `studentId` (path parameter) - ID of the student

**Example:**
```javascript
fetch('http://localhost:8080/api/student/1/leave-requests')
  .then(response => response.json())
  .then(data => console.log(data));
```

**Response:**
```json
[
  {
    "requestId": 1,
    "type": "OD",
    "status": "APPROVED",
    "documentPath": "uploads/documents/...",
    "createdAt": "2025-12-15T10:30:00",
    "approvalSteps": [...],
    "queries": []
  },
  {
    "requestId": 2,
    "type": "ML",
    "status": "PENDING",
    "documentPath": "uploads/documents/...",
    "createdAt": "2025-12-14T09:15:00",
    "approvalSteps": [...],
    "queries": []
  }
]
```

---

### 4. Get Real-Time Request Status
**Endpoint:** `GET /api/student/request/{requestId}/status`

**Description:** Get detailed real-time status of a specific request

**Parameters:**
- `requestId` (path parameter) - ID of the request

**Example:**
```javascript
// Poll every 5 seconds for real-time updates
setInterval(() => {
  fetch('http://localhost:8080/api/student/request/1/status')
    .then(response => response.json())
    .then(data => {
      updateUI(data); // Update your UI with new status
    });
}, 5000);
```

**Response:**
```json
{
  "requestId": 1,
  "type": "OD",
  "status": "PENDING",
  "documentPath": "uploads/documents/...",
  "createdAt": "2025-12-15T10:30:00",
  "approvalSteps": [
    {
      "approverName": "Dr. Smith",
      "approverRole": "FACULTY_ADVISOR",
      "decision": "APPROVED",
      "remarks": "Approved",
      "actionTime": "2025-12-15T11:00:00",
      "stepOrder": 1
    },
    {
      "approverName": "Dr. Johnson",
      "approverRole": "YEAR_COORDINATOR",
      "decision": "PENDING",
      "remarks": null,
      "actionTime": null,
      "stepOrder": 2
    },
    {
      "approverName": "Dr. Williams",
      "approverRole": "HOD",
      "decision": "PENDING",
      "remarks": null,
      "actionTime": null,
      "stepOrder": 3
    }
  ],
  "queries": []
}
```

---

### 5. Resolve Query
**Endpoint:** `POST /api/student/query/{queryId}/resolve`

**Description:** Mark a query as resolved (after student responds)

**Parameters:**
- `queryId` (path parameter) - ID of the query

**Example:**
```javascript
fetch('http://localhost:8080/api/student/query/1/resolve', {
  method: 'POST'
})
.then(response => response.json())
.then(data => console.log(data));
```

**Response:**
```json
"Query resolved successfully"
```

---

## Authority Endpoints (Faculty Advisor, Year Coordinator, HOD)

### 6. Get Pending Requests for Authority
**Endpoint:** `GET /api/authority/{approverId}/pending-requests`

**Description:** Get all requests pending approval for a specific authority

**Parameters:**
- `approverId` (path parameter) - ID of the authority user

**Example:**
```javascript
fetch('http://localhost:8080/api/authority/2/pending-requests')
  .then(response => response.json())
  .then(data => console.log(data));
```

**Response:**
```json
[
  {
    "id": 1,
    "student": {
      "id": 1,
      "rollNo": "RA2111003010001",
      "department": "CSE",
      "year": 2,
      "section": "A"
    },
    "type": "OD",
    "documentPath": "uploads/documents/...",
    "status": "PENDING",
    "createdAt": "2025-12-15T10:30:00"
  }
]
```

---

### 7. Get Request Details
**Endpoint:** `GET /api/authority/request/{requestId}/details`

**Description:** Get full details of a request including approval chain and queries

**Parameters:**
- `requestId` (path parameter) - ID of the request

**Response:** Same as endpoint #4

---

### 8. Approve or Reject Request
**Endpoint:** `POST /api/authority/{approverId}/request/{requestId}/approve`

**Description:** Authority approves or rejects a request

**Parameters:**
- `approverId` (path parameter) - ID of the authority
- `requestId` (path parameter) - ID of the request

**Request Body:**
```json
{
  "decision": "APPROVED",
  "remarks": "Approved for valid reason"
}
```

**Decision values:** `APPROVED`, `REJECTED`

**Example:**
```javascript
fetch('http://localhost:8080/api/authority/2/request/1/approve', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    decision: 'APPROVED',
    remarks: 'Approved'
  })
})
.then(response => response.json())
.then(data => console.log(data));
```

**Response:**
```json
"Request processed successfully"
```

---

### 9. Raise Query
**Endpoint:** `POST /api/authority/{approverId}/request/{requestId}/query`

**Description:** Authority raises a query on a request

**Parameters:**
- `approverId` (path parameter) - ID of the authority
- `requestId` (path parameter) - ID of the request

**Request Body:**
```json
{
  "message": "Please provide medical certificate for verification"
}
```

**Example:**
```javascript
fetch('http://localhost:8080/api/authority/2/request/1/query', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    message: 'Please clarify the dates'
  })
})
.then(response => response.json())
.then(data => console.log(data));
```

**Response:**
```json
"Query raised successfully"
```

---

## Enums Reference

### LeaveType
- `OD` - On Duty
- `ML` - Medical Leave

### ApprovalDecision
- `PENDING` - Awaiting approval
- `APPROVED` - Approved by authority
- `REJECTED` - Rejected by authority
- `QUERY` - Query raised

### UserRole
- `STUDENT`
- `FACULTY_ADVISOR`
- `YEAR_COORDINATOR`
- `HOD`

---

## Workflow Summary

1. **Student submits request**
   - Uploads image to Supabase Storage OR types message
   - Document URL stored in database
   - Approval chain created: FA → YC → HOD

2. **FA receives notification**
   - Reviews document from Supabase URL
   - Can approve/reject/query

3. **If FA approves**
   - System downloads document from Supabase
   - Generates SHA-256 hash
   - FA's private key signs the hash
   - Digital signature stored in database
   - Request moves to Year Coordinator

4. **YC receives notification**
   - Reviews document and previous signatures
   - Can approve/reject/query

5. **If YC approves**
   - Same signature process
   - Request moves to HOD

6. **HOD final approval**
   - Reviews entire approval chain
   - Approves with final signature
   - Request status changes to APPROVED

7. **Student sees real-time status**
   - Poll status endpoint for updates
   - See all approval steps with signatures

---

## Security Features

- **Digital Signatures:** Each approval generates a cryptographic signature
- **Document Hash:** SHA-256 hash prevents tampering
- **Approval Chain:** Enforced hierarchy, no skipping levels
- **Query System:** Controlled communication flow

---

## File Storage & Digital Signatures

### Supabase Storage
All documents are stored in Supabase Storage bucket `images`:
```
https://[your-project].supabase.co/storage/v1/object/public/images/{filename}
```

### How Digital Signatures Work

1. **Document Upload:**
   - Image uploaded to Supabase Storage bucket "images"
   - Returns public URL (stored in `documentPath`)

2. **Hash Generation (When Authority Approves):**
   - System downloads image from Supabase URL
   - Computes SHA-256 hash of the file bytes
   - Hash = unique fingerprint of the document

3. **Digital Signing:**
   - Authority has RSA key pair (2048-bit)
   - Private key signs the document hash
   - Signature stored in `document_signatures` table

4. **Tamper Detection:**
   - Download document from Supabase
   - Recompute SHA-256 hash
   - Verify signature with public key
   - If hashes don't match → Document tampered!

### Why This Prevents Tampering:
- Supabase URL points to exact file location
- Any modification changes the hash
- Signature verification will fail
- Approval chain is invalidated

---

## Error Responses

All endpoints return error messages in this format:
```json
"Error: [error message]"
```

Common HTTP Status Codes:
- `200` - Success
- `400` - Bad Request (invalid data)
- `404` - Not Found (resource doesn't exist)
- `500` - Internal Server Error

---

## Testing with HTML/JavaScript

Use the provided `index.html` file to test all endpoints through a web interface.

---

## Getting Started

1. **Open `auth.html`** - Register/Login page for all user types
2. **Register users:**
   - Create at least 1 Student
   - Create 1 Faculty Advisor
   - Create 1 Year Coordinator  
   - Create 1 HOD
3. **Save the User IDs** displayed after registration
4. **Open `index.html`** - Use saved IDs to test API endpoints

---

## Notes for Frontend Developer

1. **Authentication Flow:**
   - First, register users via `/api/auth/register`
   - Login via `/api/auth/login` to get user details
   - Use `userId` and `studentId` (if student) for other endpoints
   - Store user info in localStorage after login

2. **File Upload:** Use `FormData` for multipart file uploads

3. **Real-time Updates:** Poll the status endpoint every 5-10 seconds

4. **CORS:** Already enabled with `@CrossOrigin(origins = "*")`

5. **Authentication:** Simple email-based for prototype (no passwords)

6. **Email Validation:** Must end with `@srmasd.edu.in`

7. **Error Handling:** Always check response status and handle errors

8. **File Size:** Maximum 10MB per file

9. **Supported Formats:** JPG, PNG, PDF for documents

10. **Document URLs:** All document paths are Supabase Storage URLs

11. **Display Images:** Use the `documentPath` URL directly in `<img>` tags

12. **Supabase Config:** Make sure backend has correct Supabase credentials

---

## Future Enhancements

- WebSocket for real-time notifications
- Email notifications
- Authentication & Authorization (JWT)
- Document preview
- Signature verification endpoint
- Audit trail endpoint
