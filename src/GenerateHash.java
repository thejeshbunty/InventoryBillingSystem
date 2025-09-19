import org.mindrot.jbcrypt.BCrypt;

public class GenerateHash {
    public static void main(String[] args) {
        String adminPassword = "bunty";
        String cashierPassword = "1234";

        String adminHash = BCrypt.hashpw(adminPassword, BCrypt.gensalt());
        String cashierHash = BCrypt.hashpw(cashierPassword, BCrypt.gensalt());

        System.out.println("Admin hash: " + adminHash);
        System.out.println("Cashier hash: " + cashierHash);
    }
}
