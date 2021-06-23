package tech.codingclub.helix.entity;

/**
 * Created by hackme on 2/7/18.
 */
public class Member extends MemberBase {

    public String name;
    public String email;
    public String role;
    public String password;
    public String image;
    public String token;
    public Boolean is_verified;

    public boolean isIs_followed() {
        return is_followed;
    }

    public boolean is_followed;

    public Member(boolean is_followed) {
        this.is_followed = is_followed;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public String getImage() {
        return image;
    }

    public String getToken() {
        return token;
    }

    public Boolean getIs_verified() {
        return is_verified;
    }
    public Long getId()
    {
        return id;
    }

    public Member(String name, String email, String role, String password, String image, String token, Boolean is_verified) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.password = password;
        this.image = image;
        this.token = token;
        this.is_verified = is_verified;
    }
    public Member(){

    }
}
