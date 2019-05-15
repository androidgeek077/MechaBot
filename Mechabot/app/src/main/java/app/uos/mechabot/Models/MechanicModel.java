package app.uos.mechabot.Models;


public class MechanicModel {

    String phone;
    String name;
    String email;
    String password;
    String education;

    public MechanicModel(String name, String email, String password, String education, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.education = education;
        this.phone = phone;
    }

    public MechanicModel(String name, String email,  String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public MechanicModel() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}