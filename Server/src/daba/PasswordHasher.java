package daba;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {
    public String hashingPassword(String password) {
        try {
            // Получаем экземпляр MessageDigest с алгоритмом SHA-384
            MessageDigest digest = MessageDigest.getInstance("SHA-384");
            // Обновляем дайджест данными пароля
            byte[] hashedBytes = digest.digest(password.getBytes());
            // Преобразуем массив байт в шестнадцатеричную строку
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // Обработка ошибки, если алгоритм не найден
            throw new RuntimeException("SHA-384 algorithm not found", e);
        }
    }
}
