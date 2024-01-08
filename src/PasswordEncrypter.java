import java.util.Base64;

public class PasswordEncrypter {

    public static String encrypt(String plaintext, int key) {
        StringBuilder encryptedText = new StringBuilder();

        // Convert the integer key to a string
        String keyString = String.valueOf(key);

        for (int i = 0; i < plaintext.length(); i++) {
            char plainChar = plaintext.charAt(i);
            char keyChar = keyString.charAt(i % keyString.length());

            // XOR operation
            char encryptedChar = (char) (plainChar ^ keyChar);

            // Convert to Base64
            String base64Char = Base64.getEncoder().encodeToString(String.valueOf(encryptedChar).getBytes());
            encryptedText.append(base64Char);
        }

        return encryptedText.toString();
    }

    public static String decrypt(String encryptedText, int key) {
        StringBuilder decryptedText = new StringBuilder();

        // Convert the integer key to a string
        String keyString = String.valueOf(key);

        // Iterate through each base64 character in the encrypted text
        for (int i = 0; i < encryptedText.length(); i += 4) {
            String base64Char = encryptedText.substring(i, i + 4);
            byte[] decodedBytes = Base64.getDecoder().decode(base64Char);
            char decryptedChar = (char) (decodedBytes[0] ^ keyString.charAt((i / 4) % keyString.length()));
            decryptedText.append(decryptedChar);
        }

        return decryptedText.toString();
    }

    public static void main(String[] args) {
        String[] passwords = {
                "mert2002", "asrin2003", "111elif", "222eralp", "3333gulsum",
                "4444feyza", "55hasan", "66mete", "1ramazan", "mert2002", "mert2002"
        };

        int[] keys = {25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35};

        for (int i = 0; i < passwords.length; i++) {
            String encryptedPassword = encrypt(passwords[i], keys[i]);
            System.out.println("Original: " + passwords[i]);
            System.out.println("Encrypted: " + encryptedPassword);
            System.out.println("Decrypted: " + decrypt("Aw==Bg==Aw==Ug==Xg==Xg==VA==", keys[i]));
            System.out.println();
        }
    }
}
