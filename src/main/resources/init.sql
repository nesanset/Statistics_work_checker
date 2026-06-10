CREATE TABLE IF NOT EXISTS app_users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(60) NOT NULL UNIQUE,
    password VARCHAR(80) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS control_works (
    id SERIAL PRIMARY KEY,
    created_by_user_id INTEGER NOT NULL,
    title VARCHAR(150) NOT NULL,
    passing_score NUMERIC(5,2) NOT NULL CHECK (passing_score >= 0),
    student_list_file_name VARCHAR(180),
    variants_root_path VARCHAR(300),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_control_works_user FOREIGN KEY (created_by_user_id) REFERENCES app_users (id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS student_groups (
    id SERIAL PRIMARY KEY,
    control_work_id INTEGER NOT NULL,
    name VARCHAR(30) NOT NULL,
    CONSTRAINT uq_student_groups_name UNIQUE (control_work_id, name),
    CONSTRAINT fk_student_groups_work FOREIGN KEY (control_work_id) REFERENCES control_works (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS variants (
    id SERIAL PRIMARY KEY,
    control_work_id INTEGER NOT NULL,
    code VARCHAR(30) NOT NULL,
    source_file_name VARCHAR(180),
    CONSTRAINT uq_variants_code UNIQUE (control_work_id, code),
    CONSTRAINT fk_variants_work FOREIGN KEY (control_work_id) REFERENCES control_works (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS students (
    id SERIAL PRIMARY KEY,
    group_id INTEGER NOT NULL,
    variant_id INTEGER NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    check_status VARCHAR(20) NOT NULL,
    CONSTRAINT uq_students_group_full_name UNIQUE (group_id, full_name),
    CONSTRAINT chk_students_status CHECK (check_status IN ('NOT_CHECKED', 'IN_PROGRESS', 'CHECKED', 'MISSING_WORK')),
    CONSTRAINT fk_students_group FOREIGN KEY (group_id) REFERENCES student_groups (id) ON DELETE CASCADE,
    CONSTRAINT fk_students_variant FOREIGN KEY (variant_id) REFERENCES variants (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS assignments (
    id SERIAL PRIMARY KEY,
    variant_id INTEGER NOT NULL,
    number INTEGER NOT NULL,
    text TEXT NOT NULL,
    max_score NUMERIC(5,2) NOT NULL CHECK (max_score > 0),
    CONSTRAINT uq_assignments_number UNIQUE (variant_id, number),
    CONSTRAINT fk_assignments_variant FOREIGN KEY (variant_id) REFERENCES variants (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS grades (
    id SERIAL PRIMARY KEY,
    student_id INTEGER NOT NULL,
    assignment_id INTEGER NOT NULL,
    score NUMERIC(5,2) CHECK (score >= 0),
    comment_template VARCHAR(40),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_grades_student_assignment UNIQUE (student_id, assignment_id),
    CONSTRAINT chk_grades_comment_template CHECK (comment_template IS NULL OR comment_template IN ('NO_COMMENT', 'TASK_NOT_COMPLETED', 'COMPLETED_CORRECTLY')),
    CONSTRAINT fk_grades_student FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE,
    CONSTRAINT fk_grades_assignment FOREIGN KEY (assignment_id) REFERENCES assignments (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_control_works_user ON control_works (created_by_user_id);
CREATE INDEX IF NOT EXISTS idx_student_groups_work ON student_groups (control_work_id);
CREATE INDEX IF NOT EXISTS idx_variants_work ON variants (control_work_id);
CREATE INDEX IF NOT EXISTS idx_students_group ON students (group_id);
CREATE INDEX IF NOT EXISTS idx_students_variant ON students (variant_id);
CREATE INDEX IF NOT EXISTS idx_assignments_variant ON assignments (variant_id);
CREATE INDEX IF NOT EXISTS idx_grades_student ON grades (student_id);
CREATE INDEX IF NOT EXISTS idx_grades_assignment ON grades (assignment_id);

INSERT INTO app_users (username, password)
VALUES ('teacher', 'cde383eee8ee7a4400adf7a15f716f179a2eb97646b37e089eb8d6d04e663416')
ON CONFLICT (username) DO NOTHING;
