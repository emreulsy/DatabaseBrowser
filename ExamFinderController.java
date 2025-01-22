package com.example.db1;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.sql.*;

public class ExamFinderController {

    @FXML private TextField sssnField;
    @FXML private Label resultLabel;
    @FXML private TextField courseIdField;
    @FXML private TextField courseNameField;
    @FXML private TextField ectsField;
    @FXML private TextField numHoursField;


    private static final String URL = "jdbc:mysql://localhost:3306/java";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    @FXML
    private void saveCourse() {
        try {

            if (courseIdField.getText().isEmpty() ||
                    courseNameField.getText().isEmpty() ||
                    ectsField.getText().isEmpty() ||
                    numHoursField.getText().isEmpty()) {
                resultLabel.setText("Lütfen tüm alanları doldurun!");
                return;
            }


            try (Connection conn = getConnection()) {
                String sql = "INSERT INTO course (courseCode, courseName, ects, numHours) VALUES (?, ?, ?, ?)";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, courseIdField.getText());
                    pstmt.setString(2, courseNameField.getText());
                    pstmt.setInt(3, Integer.parseInt(ectsField.getText()));
                    pstmt.setInt(4, Integer.parseInt(numHoursField.getText()));

                    pstmt.executeUpdate();
                }
            }

            resultLabel.setText("Ders başarıyla kaydedildi!");
            clearFields();

        } catch (NumberFormatException e) {
            resultLabel.setText("ECTS ve Ders Saati sayısal değer olmalıdır!");
        } catch (SQLException e) {
            resultLabel.setText("Veritabanı hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void searchExam() {
        String sssn = sssnField.getText();

        try (Connection conn = getConnection()) {
            String query = "SELECT e.examname, c.courseCode, c.courseName, c.ects, c.numHours " +
                    "FROM studenttakingexam e " +
                    "JOIN course c ON e.courseCode = c.courseCode " +
                    "WHERE e.sssn = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, sssn);

                try (ResultSet rs = pstmt.executeQuery()) {
                    StringBuilder result = new StringBuilder();
                    boolean found = false;

                    while (rs.next()) {
                        found = true;
                        courseIdField.setText(rs.getString("courseCode"));
                        courseNameField.setText(rs.getString("courseName"));
                        ectsField.setText(String.valueOf(rs.getInt("ects")));
                        numHoursField.setText(String.valueOf(rs.getInt("numHours")));

                        result.append(rs.getString("examname"))
                                .append(" (")
                                .append(rs.getString("courseCode"))
                                .append(")\n");
                    }

                    if (found) {
                        resultLabel.setText("Sınavlar:\n" + result.toString());
                    } else {
                        resultLabel.setText("Bu SSSN için sınav bulunamadı");
                        clearFields();
                    }
                }
            }
        } catch (SQLException e) {
            resultLabel.setText("Veritabanı hatası: " + e.getMessage());
            e.printStackTrace();
            clearFields();
        }
    }

    @FXML
    private void clearFields() {
        courseIdField.clear();
        courseNameField.clear();
        ectsField.clear();
        numHoursField.clear();
    }
}