package forms;

import java.util.Objects;

public class LoginForm {
    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginForm)) return false;
        LoginForm loginForm = (LoginForm) o;
        return name.equals(loginForm.name) &&
                password.equals(loginForm.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password);
    }
}
