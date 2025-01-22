package com.example.db1;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EnrollementService {
    public static String getCourseResult(String ssn, String courseCode) {
        String query = "SELECT Grade FROM Enrollments WHERE SSN = ? AND CourseCode = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, ssn);
            statement.setString(2, courseCode);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int grade = resultSet.getInt("Grade");
                return grade >= 50 ? "Geçti" : "Kaldı";
            } else {
                return "Kayıt bulunamadı.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Hata: " + e.getMessage();
        }
    }
}