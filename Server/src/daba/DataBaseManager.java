package daba;

import models.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.sql.*;

public class DataBaseManager {
    private Connection connection;
    private String url = System.getenv("URL_DB");
    private Properties properties = new Properties();

    public DataBaseManager(){
    }

    public void connectDataBase(String url, Properties properties){
        try {
            connection = DriverManager.getConnection(url, properties);
            System.err.println("Successfully connected to the database\n");
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
            System.out.println("Error sending request drop ID_SEQ " + e.getMessage());
        }
    }

    public void dropEnumHairColor(){
        try{
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "DROP TYPE HairColor";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e){
            System.out.println("Error sending request drop HairColor " + e.getMessage());
        }
    }

    public void dropEnumFormOfEducation(){
        try{
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "DROP TYPE FormOfEducation;";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e){
            System.out.println("Error sending request drop FormOfEducation " + e.getMessage());
        }
    }

    public void dropEnumEyesColor(){
        try{
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "DROP TYPE EyesColor;";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e){
            System.out.println("Error sending request drop EyesColor " + e.getMessage());
        }
    }

    public void dropEnumCountry(){
        try{
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "DROP TYPE Country;";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e){
            System.out.println("Error sending request drop Country " + e.getMessage());
        }
    }

    public void createEnumHairColor(){
        try{
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "CREATE TYPE HairColor AS ENUM ('RED', 'BLACK', 'BLUE', 'BROWN');";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e){
            System.out.println("Error sending request HairColor " + e.getMessage());
        }
    }

    public void createEnumEyesColor(){
        try{
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "CREATE TYPE EyesColor AS ENUM ('RED', 'BLACK', 'BLUE', 'WHITE');";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e){
            System.out.println("Error sending request EyesColor " + e.getMessage());
        }
    }

    public void createEnumFormOfEducation(){
        try{
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "CREATE TYPE FormOfEducation AS ENUM ('DISTANCE_EDUCATION', 'FULL_TIME_EDUCATION', 'EVENING_CLASSES');";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e){
            System.out.println("Error sending request FormOfEducation " + e.getMessage());
        }
    }

    public void createEnumCountry(){
        try{
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "CREATE TYPE Country AS ENUM ('GERMANY', 'SPAIN', 'INDIA', 'THAILAND');";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e){
            System.out.println("Error sending request Country " + e.getMessage());
        }
    }

    public void createTableCoordinates(){
        try{
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "CREATE TABLE IF NOT EXISTS Coordinates (" +
                    "    coordinatesId SERIAL PRIMARY KEY," +
                    "    coordinatesX FLOAT NOT NULL," +
                    "    coordinatesY INT NOT NULL" +
                    ");";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e){
            System.out.println("Error sending request Coordinates " + e.getMessage());
        }
    }

    public void createTableLocation(){
        try{
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "CREATE TABLE IF NOT EXISTS Location (" +
                    "    locationId SERIAL PRIMARY KEY," +
                    "    locationX BIGINT NOT NULL," +
                    "    locationY FLOAT NOT NULL," +
                    "    locationName TEXT NOT NULL" +
                    ");";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e){
            System.out.println("Error sending request Location " + e.getMessage());
        }
    }

    public void createTablePerson(){
//        dropEnumCountry();
//        dropEnumEyesColor();
//        dropEnumHairColor();
//        createEnumCountry();
//        createEnumHairColor();
//        createEnumEyesColor();
        createTableLocation();
        try{
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "CREATE TABLE IF NOT EXISTS Person (" +
                    "    adminName TEXT PRIMARY KEY," +
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
            System.out.println("Error sending request Person " + e.getMessage());
        }
    }

    public void createTableStudyGroup(){
//        dropEnumFormOfEducation();
//        createEnumFormOfEducation();
        createTableCoordinates();
        createTablePerson();
        createSeqId();
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "CREATE TABLE IF NOT EXISTS StudyGroup (" +
                    "    groupId BIGINT PRIMARY KEY DEFAULT nextval('ID_SEQ')," +
                    "    userName TEXT NOT NULL," +
                    "    groupName TEXT NOT NULL," +
                    "    coordinatesId BIGINT NOT NULL," +
                    "    creationDate TIMESTAMP NOT NULL," +
                    "    studentsCount INT," +
                    "    expelledStudents BIGINT," +
                    "    transferredStudents INT NOT NULL," +
                    "    formOfEducation FormOfEducation NOT NULL," +
                    "    groupAdminName TEXT NOT NULL," +
                    "    FOREIGN KEY (coordinatesId) REFERENCES Coordinates(coordinatesId)," +
                    "    FOREIGN KEY (groupAdminName) REFERENCES Person(adminName)," +
                    "    FOREIGN KEY (userName) REFERENCES Users(userName)" +
                    ");";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error sending request StudyGroup " + e.getMessage());
        }
    }
    public void createTableUser(){
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "CREATE TABLE IF NOT EXISTS Users " +
                    "(userName TEXT PRIMARY KEY, " +
                    " password TEXT);";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error sending request Users " + e.getMessage());
        }
    }

    public boolean checkUser(String userName) throws IOException {
        properties.load(new FileInputStream(System.getenv("PROP")));
        connectDataBase(url, properties);
        boolean exists = false;
        String sql = "SELECT COUNT(*) AS count FROM users WHERE userName = ?";
        ResultSet resultSet;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, userName);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                if (count > 0) {
                    exists = true;
                }
            }
            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Error with sql(");
        }
        return exists;
    }

    public boolean checkPassword(String userName, String passwd) throws IOException {
        properties.load(new FileInputStream(System.getenv("PROP")));
        connectDataBase(url, properties);
        String sql = "SELECT password FROM Users WHERE userName = ?";
        try (PreparedStatement prepareStatement = connection.prepareStatement(sql)){
            prepareStatement.setString(1, userName);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("password");
                String hashedInputPassword = PasswordHasher.hashingPassword(passwd);
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

    public void registerUser(String userName, String passwd) throws IOException {
        properties.load(new FileInputStream(System.getenv("PROP")));
        connectDataBase(url, properties);
        String sql = "INSERT INTO users (userName, password) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, PasswordHasher.hashingPassword(passwd));
            preparedStatement.executeUpdate();
            System.out.println("User added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding user registration" + e.getMessage());
        }
    }

    public LinkedHashMap<Long, StudyGroup> readFromDataBase() throws IOException {
//        properties.load(new FileInputStream(System.getenv("PROP")));
//        connectDataBase(url, properties); // Подключаемся к базе данных
//        LinkedHashMap<Long, StudyGroup> studyGroupLinkedHashMap = new LinkedHashMap<>();
//        String sql = "SELECT * FROM StudyGroup"; // Запрос для выборки данных из таблицы StudyGroup
//        try (Statement statement = connection.createStatement();
//             ResultSet resultSet = statement.executeQuery(sql)) {
//            // Перебираем результаты запроса
//            while (resultSet.next()) {
//                StudyGroup studyGroup = new StudyGroup();
//                // Заполняем объект StudyGroup данными из результата запроса
//                studyGroup.setGroupId(resultSet.getLong("groupId"));
//                studyGroup.setUserName(resultSet.getString("userName"));
//                studyGroup.setGroupName(resultSet.getString("groupName"));
//                studyGroup.setCreationDate(resultSet.getTimestamp("creationDate").toLocalDateTime());
//                studyGroup.setStudentsCount(resultSet.getInt("studentsCount"));
//                studyGroup.setExpelledStudents(resultSet.getLong("expelledStudents"));
//                studyGroup.setTransferredStudents(resultSet.getInt("transferredStudents"));
//                studyGroup.setFormOfEducation(FormOfEducation.valueOf(resultSet.getString("formOfEducation")));
//                studyGroup.setGroupName(resultSet.getString("groupAdminName"));
//
//                // Добавляем объект StudyGroup в LinkedHashMap
//                studyGroupLinkedHashMap.put(studyGroup.getGroupId(), studyGroup);
//            }
//        } catch (SQLException e) {
//            System.out.println("Error reading from database: " + e.getMessage());
//        }
//        return studyGroupLinkedHashMap;

//        groupId, userName, groupName, coordinatesId, studentsCount, expelledStudents, transferredStudents, formOfEducation, groupAdminName

        properties.load(new FileInputStream(System.getenv("PROP")));
        connectDataBase(url, properties);
        LinkedHashMap<Long, StudyGroup> studyGroupLinkedHashMap = new LinkedHashMap<>();
        String sql = "SELECT * FROM StudyGroup JOIN Coordinates ON StudyGroup.coordinatesId = Coordinates.coordinatesId JOIN Person ON StudyGroup.groupAdminName = Person.adminName JOIN Location ON Person.locationId = Location.locationId";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                StudyGroup studyGroup = new StudyGroup();
                Coordinates coordinates = new Coordinates();
                Location location = new Location();
                Person person = new Person();
                studyGroup.setGroupId(rs.getLong("groupId"));
                studyGroup.setUserName(rs.getString("userName"));
                studyGroup.setGroupName(rs.getString("groupName"));
                studyGroup.setCreationDate(rs.getDate("creationDate").toLocalDate().atStartOfDay());
                coordinates.setX(rs.getFloat("coordinatesX"));
                coordinates.setY(rs.getInt("coordinatesY"));
                studyGroup.setCoordinates(coordinates);
                studyGroup.setStudentsCount(rs.getInt("studentsCount"));
                studyGroup.setExpelledStudents(rs.getLong("expelledStudents"));
                studyGroup.setTransferredStudents(rs.getInt("transferredStudents"));
                studyGroup.setFormOfEducation(FormOfEducation.valueOf(rs.getString("formOfEducation")));
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studyGroupLinkedHashMap;
    }

    public void insertIntoCoordinates(float coordinatesX, int coordinatesY) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO Coordinates (coordinatesX, coordinatesY) " +
                            "VALUES (?, ?)"
            );
            preparedStatement.setFloat(1, coordinatesX);
            preparedStatement.setInt(2, coordinatesY);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Data inserted successfully into Coordinates table.");
        } catch (SQLException e) {
            System.out.println("Error inserting data into Coordinates table: " + e.getMessage());
        }
    }

    public void insertIntoLocation(long locationX, float locationY, String locationName) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO Location (locationX, locationY, locationName) " +
                            "VALUES (?, ?, ?)"
            );
            preparedStatement.setLong(1, locationX);
            preparedStatement.setFloat(2, locationY);
            preparedStatement.setString(3, locationName);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Data inserted successfully into Location table.");
        } catch (SQLException e) {
            System.out.println("Error inserting data into Location table: " + e.getMessage());
        }
    }

    public void insertIntoPerson(String adminName, long height, EyesColor eyeColor, HairColor hairColor, Country nationality, long locationId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO Person (adminName, height, eyeColor, hairColor, nationality, locationId) " +
                            "VALUES (?, ?, ?, ?, ?, ?)"
            );
            preparedStatement.setString(1, adminName);
            preparedStatement.setLong(2, height);
            preparedStatement.setString(3, eyeColor.name()); // Enum EyesColor преобразуется в строку
            preparedStatement.setString(4, hairColor.name()); // Enum HairColor преобразуется в строку
            preparedStatement.setString(5, nationality.name()); // Enum Country преобразуется в строку
            preparedStatement.setLong(6, locationId);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Data inserted successfully into Person table.");
        } catch (SQLException e) {
            System.out.println("Error inserting data into Person table: " + e.getMessage());
        }
    }

    public void insertIntoStudyGroup(String userName, String groupName, long coordinatesId, Timestamp creationDate, int studentsCount, long expelledStudents, int transferredStudents, FormOfEducation formOfEducation, long groupAdminName) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO StudyGroup (userName, groupName, coordinatesId, creationDate, studentsCount, expelledStudents, transferredStudents, formOfEducation, groupAdminName) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, groupName);
            preparedStatement.setLong(3, coordinatesId);
            preparedStatement.setTimestamp(4, creationDate);
            preparedStatement.setInt(5, studentsCount);
            preparedStatement.setLong(6, expelledStudents);
            preparedStatement.setInt(7, transferredStudents);
            preparedStatement.setString(8, formOfEducation.name()); // Enum FormOfEducation преобразуется в строку
            preparedStatement.setLong(9, groupAdminName);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Data inserted successfully into StudyGroup table.");
        } catch (SQLException e) {
            System.out.println("Error inserting data into StudyGroup table: " + e.getMessage());
        }
    }
}
