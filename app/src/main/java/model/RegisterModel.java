package model;

public class RegisterModel {
    String emailId;
    String phoneNumber;
    String name;
    String password;

    public RegisterModel(String emailId,String name,String phoneNumber,String password)
    {
        this.emailId=emailId;
        this.phoneNumber=phoneNumber;
        this.name=name;
        this.password=password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmailId() {
        return emailId;
    }

}
