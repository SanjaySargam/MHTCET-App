package Models;

public class ProfileModel {
    private String name;
    private String email;
    private String phone;
    private int bookmarkCount;


    public ProfileModel(String name, String email,String phone,int bookmarkCount) {
        this.name = name;
        this.email = email;
        this.phone=phone;
        this.bookmarkCount=bookmarkCount;


    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public int getBookmarkCount() {
        return bookmarkCount;
    }

    public void setBookmarkCount(int bookmarkCount) {
        this.bookmarkCount = bookmarkCount;
    }
}
