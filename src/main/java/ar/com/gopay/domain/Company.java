package ar.com.gopay.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "companies")
public class Company extends User {

    @NotNull
    @NotBlank(message = "{username.notblank}")
    @Size(min = 6, max = 15)
    private String username;

    @NotBlank
    @Size(min = 6, max = 50)
    private String name;

    @JsonIgnore
    private String logo;

    public Company() { }

    public Company(String username, String email, String password, String name, String logo) {
        super(email, password);
        this.username = username;
        this.name = name;
        this.logo = logo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return "Company{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", id=" + id +
                ", email='" + email + '\'' +
                '}';
    }
}
