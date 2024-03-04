package Models;

import java.time.LocalDateTime;

public class Users {

    private int userID;
    private String firstName;
    private String lastName;
    private String password;
    private String emailAddress;
    private String role;
    private String accountStatus;
    private LocalDateTime dateCreated;
    private LocalDateTime lastLogin;

    // Empty constructor
    public Users() {
    }

    // Constructor with all attributes
    public Users(int userID, String firstName, String lastName, String password, String emailAddress, String role, String accountStatus, LocalDateTime dateCreated, LocalDateTime lastLogin) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.emailAddress = emailAddress;
        this.role = role;
        this.accountStatus = accountStatus;
        this.dateCreated = dateCreated;
        this.lastLogin = lastLogin;
    }

    public Users(int userID,String firstname, String lastname, String email, String role, String status, LocalDateTime dateCreated, LocalDateTime lastLogin) {
        this.userID = userID;
        this.firstName = firstname;
        this.lastName = lastname;
        this.emailAddress = email;
        this.role = role;
        this.accountStatus = status;
        this.dateCreated = dateCreated;
        this.lastLogin = lastLogin;
    }

    public Users(int userID,String firstname, String lastname, String email, String status) {
        this.userID = userID;
        this.firstName = firstname;
        this.lastName = lastname;
        this.emailAddress = email;
        this.accountStatus = status;
    }



    // Getters and setters for all attributes

    public int getUserID() {
        return userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userID=" + userID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", role='" + role + '\'' +
                ", accountStatus='" + accountStatus + '\'' +
                ", dateCreated=" + dateCreated +
                ", lastLogin=" + lastLogin +
                '}';
    }
}
