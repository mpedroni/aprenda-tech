CREATE TABLE enrollments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    course_id INT NOT NULL,
    enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE,

    FOREIGN KEY (course_id) REFERENCES courses(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX idx_enrollments_user_id ON enrollments (user_id);
CREATE INDEX idx_enrollments_course_id ON enrollments (course_id);