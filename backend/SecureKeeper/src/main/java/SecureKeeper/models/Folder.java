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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


// Each user from `UsersModel` class can create multiple folders
@Entity
@Table(name = "folders")
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folder_id")
    private Long id;

    private String name;

    // @ManyToOne linking user with his folders
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    // @OneToMany linking individual folder with notes
    // @JsonIgnore is solving circular reference
    // TODO change CascadeType.All to CascadeType.REMOVE
    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Note> notes;

    // Constructors
    public Folder() {}
    
    public Folder(String name, User user) {
        this.name = name;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public User getUser () {return user;}
    public void setUser (User user) {this.user = user;}

    public List<Note> getNotes() {return notes;}
    public void setNotes(List<Note> notes) {this.notes = notes;}
}