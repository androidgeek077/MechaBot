package app.uos.mechabot.Models;
public class UserModel {

    private String name;
    private String email;
    private String password;
    private String id;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
private String userid;

private Double studentlat;
private Double studentlong;

public UserModel(String userid, String name, String email, String password,  Double studentlat, Double studentlong) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userid = userid;
        this.studentlat = studentlat;
        this.studentlong = studentlong;
        }

public UserModel() {
        }

public Double getstudentlat() {
        return studentlat;
        }

public void setstudentlat(Double studentlat) {
        studentlat = studentlat;
        }

public Double getstudentlong() {
        return studentlong;
        }

public void setstudentlong(Double studentlong) {
        studentlong = studentlong;
        }


public String getpassword() {
        return password;
        }

public void setpassword(String password) {
        this.password = password;
        }



public String getname() {
        return name;
        }

public void setname(String name) {
        this.name = name;
        }

public String getemail() {
        return email;
        }

public void setemail(String email) {
        this.email = email;
        }



public String getid() {
        return id;
        }

public void setid(String id) {
        this.id = id;
        }


        }

