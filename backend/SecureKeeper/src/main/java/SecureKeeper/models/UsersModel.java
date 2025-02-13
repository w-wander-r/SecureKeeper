package SecureKeeper.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UsersModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    private String username;
    private String password;

    /* 

     * First approach: not using @OneToMany to keep simplicity
     *  and could be better performance as folders not loading
     * but there some drawbacks ->
     *  -> Indirect Access, which can cause lower performance
     *  -> Lack Of Cascading - TODO: test this case(deleting user)
     
    */

    /* 
      
     * Second approach: use @OneToMany
     *  Most likely its best approach for now
      
    */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Folder> folders;

    // Constructors
    public UsersModel() {}

    public UsersModel(Long id, String username, String password, List<Folder> folders) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.folders = folders;
    }

    // Getters and Setters
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public List<Folder> getFolders() {return folders;}
    public void setFolders(List<Folder> folders) {this.folders = folders;}

    @Override
    public String toString() {
        return "UsersModel [id=" + id + ", username=" + username + ", password=" + password + "]";
    }
}
