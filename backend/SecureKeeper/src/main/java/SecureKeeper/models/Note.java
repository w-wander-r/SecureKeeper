package SecureKeeper.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// Each note belong to user folder
@Entity
@Table(name = "notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "note_id")
    private Long id;

    @Column(nullable = false)
    private String title;
    
    private String username;
    
    private String email;
    
    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "folder_id", nullable = false)
    private Folder folder;

    // Constructors
    public Note() {}

    public Note(Long id, String title, String username, String email, String password, Folder folder) {
        this.id = id;
        this.title = title;
        this.username = username;
        this.email = email;
        this.password = password;
        this.folder = folder;
    }
    
    //  Getters and Setters
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public Folder getFolder() {return folder;}
    public void setFolder(Folder folder) {this.folder = folder;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
}