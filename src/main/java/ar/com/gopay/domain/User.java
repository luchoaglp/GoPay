package ar.com.gopay.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "username" })/*,
        @UniqueConstraint(columnNames = { "email" })*/
})
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected Long id;

    @NotNull
    @NotBlank(message = "{username.notblank}")
    @Size(min = 6, max = 15)
    protected String username;

    //@NaturalId
    @NotNull
    @NotBlank
    @Size(min = 6, max = 50)
    @Email
    protected String email;

    @JsonIgnore
    @NotNull
    @NotBlank(message = "{password.notblank}")
    @Size(max = 100)
    protected String password;

    @JsonProperty("created_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT-03:00")
    @CreatedDate
    protected Date createdDate;

    @JsonProperty("last_modified_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT-03:00")
    @LastModifiedDate
    protected Date lastModifiedDate;

    public User() { }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdDate = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}