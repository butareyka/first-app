package daba;

import models.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.sql.*;

public class DataBaseManager {
    private Connection connection;
    public DataBaseManager(){
    }

    public void connectDataBase(String url, Properties properties){
        try {
            connection = DriverManager.getConnection(url, properties);
            System.out.println("Successfully connected to the database");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void initDataBase(){
        createTableUser();
        createTableStudyGroup();
    }

    public void createSeqId(){
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "CREATE SEQUENCE IF NOT EXISTS ID_SEQ START WITH 1 INCREMENT BY 1;";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error sending request 1");
        }
    }

    public void dropEnumHairColor(){
        try{
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "DROP TYPE HairColor";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void dropEnumFormOfEducation(){
        try{
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "DROP TYPE FormOfEducation;";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void dropEnumEyesColor(){
        try{
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "DROP TYPE EyesColor;";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void dropEnumCountry(){
        try{
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "DROP TYPE Country;";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void createEnumHairColor(){
        dropEnumHairColor();
        try{
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "CREATE TYPE HairColor AS ENUM ('RED', 'BLACK', 'BLUE', 'BROWN');";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void createEnumEyesColor(){
        dropEnumEyesColor();
        try{
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "CREATE TYPE EyesColor AS ENUM ('RED', 'BLACK', 'BLUE', 'WHITE');";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void createEnumFormOfEducation(){
        dropEnumFormOfEducation();
        try{
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "CREATE TYPE FormOfEducation AS ENUM ('DISTANCE_EDUCATION', 'FULL_TIME_EDUCATION', 'EVENING_CLASSES');";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void createEnumCountry(){
        dropEnumCountry();
        try{
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "CREATE TYPE Country AS ENUM ('GERMANY', 'SPAIN', 'INDIA', 'THAILAND');";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void createTableCoordinates(){
        createSeqId();
        try{
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "CREATE TABLE IF NOT EXISTS Coordinates (" +
                    "    coordinatesId BIGINT," +
                    "    coordinatesX FLOAT NOT NULL," +
                    "    coordinatesY INT NOT NULL," +
                    "    PRIMARY KEY (coordinatesId)" +
                    ");";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void createTableLocation(){
        createSeqId();
        try{
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "CREATE TABLE IF NOT EXISTS Location (" +
                    "    locationId BIGINT," +
                    "    locationX BIGINT NOT NULL," +
                    "    locationY FLOAT NOT NULL," +
                    "    locationName VARCHAR(255) NOT NULL, " +
                    "    PRIMARY KEY (locationId)" +
                    ");";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void createTablePerson(){
        createTableLocation();
        try{
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "CREATE TABLE IF NOT EXISTS Person (" +
                    "    adminId SERIAL PRIMARY KEY," +
                    "    adminName VARCHAR(255) NOT NULL," +
                    "    height BIGINT," +
                    "    eyeColor EyesColor," +
                    "    hairColor HairColor NOT NULL," +
                    "    nationality Country," +
                    "    locationId BIGINT NOT NULL," +
                    "    FOREIGN KEY (locationId) REFERENCES Location(locationId)" +
                    ");";
            statement.executeUpdate(sql);
            statement.close();;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTableStudyGroup(){
        createSeqId();
        createTableCoordinates();
        createTablePerson();
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "CREATE TABLE IF NOT EXISTS StudyGroup (" +
                    "    groupId BIGINT PRIMARY KEY DEFAULT nextval('ID_SEQ')," +
                    "    userName TEXT" +
                    "    groupName VARCHAR(255) NOT NULL," +
                    "    coordinatesId BIGINT NOT NULL," +
                    "    creationDate TIMESTAMP NOT NULL," +
                    "    studentsCount INT," +
                    "    expelledStudents BIGINT," +
                    "    transferredStudents INT NOT NULL," +
                    "    formOfEducation FormOfEducation NOT NULL," +
                    "    groupAdminName BIGINT NOT NULL," +
                    "    FOREIGN KEY (coordinatesId) REFERENCES Coordinates(coordinatesId)," +
                    "    FOREIGN KEY (groupAdminName) REFERENCES Person(adminId)" +
                    "    FOREIGN KEY (userName) REFERENCES Users(userNam)" +
                    ");";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error sending request 3");
        }
    }
    public void createTableUser(){
        try {
            System.out.println(connection);
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "CREATE TABLE IF NOT EXISTS Users " +
                    "(userName TEXT PRIMARY KEY, " +
                    " password TEXT);";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error sending request 2");
        }
    }

    public boolean checkUser(String userName){
        boolean exists = false;
        ResultSet resultSet;
        PreparedStatement preparedStatement;
        try {
            String sql = "SELECT COUNT(*) AS count FROM users WHERE userName = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                if (count > 0) {
                    exists = true;
                }
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("Error with sql(");
        }
        return exists;
    }

    public boolean checkPassword(String userName, String passwd) {
        String sql = "SELECT password FROM Users WHERE userName = ?";
        PreparedStatement prepareStatement;
        try {
            prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setString(1, userName);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("password");
                String hashedInputPassword = new PasswordHasher().hashingPassword(passwd);
                prepareStatement.close();
                resultSet.close();
                return hashedInputPassword.equals(hashedPassword);
            }
            prepareStatement.close();
            resultSet.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return false;
    }

    public void registerUser(String userName, String passwd){
        PreparedStatement preparedStatement = null;
        try {
            String sql = "INSERT INTO users (userName, password) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, passwd);
            preparedStatement.executeUpdate();
            System.out.println("User added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding user registration");
        }
        finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                System.out.println("Error with closing statement");
            }
        }
    }

    public LinkedHashMap<Long, StudyGroup> readFromDataBase(){
        PreparedStatement preparedStatement;
        LinkedHashMap<Long, StudyGroup> studyGroupLinkedHashMap = new LinkedHashMap<>();
        try {
            String sql = "SELECT groupId, groupName, coordinatesX, coordinatesY, studentsCount, expelledStudents, transferredStudents, formOfEducation, adminName, height, eyeColor, hairColor, nationality, locationName, locationX, locationY FROM StudyGroup";
            preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                StudyGroup studyGroup = new StudyGroup();
                Coordinates coordinates = new Coordinates();
                Location location = new Location();
                Person person = new Person();
                studyGroup.setGroupId(rs.getLong("groupId"));
                studyGroup.setGroupName(rs.getString("groupName"));
                coordinates.setX(rs.getFloat("coordinatesX"));
                coordinates.setX(rs.getInt("coordinatesY"));
                studyGroup.setCoordinates(coordinates);
                studyGroup.setStudentsCount(rs.getInt("studentsCount"));
                studyGroup.setExpelledStudents(rs.getLong("expelledStudents"));
                studyGroup.setTransferredStudents(rs.getInt("transferredStudents"));
                studyGroup.setFormOfEducation(FormOfEducation.valueOf(rs.getString("formOfEducation")));
                studyGroup.setUserName(rs.getString("userName"));
                person.setAdminName(rs.getString("adminName"));
                person.setHeight(rs.getLong("height"));
                person.setEyeColor(EyesColor.valueOf(rs.getString("eyeColor")));
                person.setHairColor(HairColor.valueOf(rs.getString("hairColor")));
                person.setNationality(Country.valueOf(rs.getString("nationality")));
                location.setLocationName(rs.getString("locationName"));
                location.setX(rs.getInt("locationX"));
                location.setY(rs.getDouble("locationY"));
                person.setLocation(location);
                studyGroup.setGroupAdmin(person);
                studyGroupLinkedHashMap.put(rs.getLong("groupId"), studyGroup);
            }
            rs.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studyGroupLinkedHashMap;
    }
}
