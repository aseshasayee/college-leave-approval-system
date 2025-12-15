CREATE TYPE user_role AS ENUM (
    'STUDENT',
    'FACULTY_ADVISOR',
    'YEAR_COORDINATOR',
    'HOD'
);

CREATE TYPE leave_type AS ENUM (
    'OD',
    'ML'
);

CREATE TYPE approval_decision AS ENUM (
    'PENDING',
    'APPROVED',
    'REJECTED',
    'QUERY'
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    role user_role NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE students (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL,
    roll_no VARCHAR(20) UNIQUE NOT NULL,
    department VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    section VARCHAR(10) NOT NULL,
    semester INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE leave_requests (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    type leave_type NOT NULL,
    document_path TEXT NOT NULL,
    status approval_decision DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);

CREATE TABLE approval_steps (
    id BIGSERIAL PRIMARY KEY,
    request_id BIGINT NOT NULL,
    approver_id BIGINT NOT NULL,
    approver_role user_role NOT NULL,
    decision approval_decision DEFAULT 'PENDING',
    remarks TEXT,
    step_order INT NOT NULL,
    action_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (request_id) REFERENCES leave_requests(id) ON DELETE CASCADE,
    FOREIGN KEY (approver_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE document_signatures (
    id BIGSERIAL PRIMARY KEY,
    approval_step_id BIGINT NOT NULL,
    signed_by BIGINT NOT NULL,
    signature BYTEA NOT NULL,
    signed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (approval_step_id) REFERENCES approval_steps(id) ON DELETE CASCADE,
    FOREIGN KEY (signed_by) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE queries (
    id BIGSERIAL PRIMARY KEY,
    request_id BIGINT NOT NULL,
    raised_by BIGINT NOT NULL,
    message TEXT NOT NULL,
    resolved BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (request_id) REFERENCES leave_requests(id) ON DELETE CASCADE,
    FOREIGN KEY (raised_by) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_students_user ON students(user_id);
CREATE INDEX idx_leave_student ON leave_requests(student_id);
CREATE INDEX idx_approval_request ON approval_steps(request_id);
CREATE INDEX idx_approval_approver ON approval_steps(approver_id);
CREATE INDEX idx_query_request ON queries(request_id);


