package bg.tu_varna.sit.threadrunnabletasks;

import java.util.regex.Pattern;

public class TaskOneLoginValidator {


    public  boolean emailValidate(String email) {
        String emailValidate = "^.+@.{2,}\\..{2,}$";
        Pattern pattern = Pattern.compile(emailValidate);
        return pattern.matcher(email).matches();
    }

    public boolean passwordValidate(String password) {
        String passwordValidate = "^(.{2}[0-9].{2}[0-9][A-Z][!@#$%&*]){2}$";
        Pattern pattern = Pattern.compile(passwordValidate);
        return pattern.matcher(password).matches();
    }

}
