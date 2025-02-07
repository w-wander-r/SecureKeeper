// enable after ..

// package SecureKeeper.models;

// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.Table;

// @Entity
// @Table(name = "notes")
// public class NoteModel {

//     @Id
//     @GeneratedValue(strategy = GenerationType.AUTO)
//     private Long id;

//     @ManyToOne
//     @JoinColumn(name = "folder_id", nullable = false)
//     private FolderModel folder;

//     private String title;
//     private String email;
//     private String username;
//     private String password;
    
//     public NoteModel() {
//     }
//     public NoteModel(Long id, FolderModel folder, String title, String email, String username, String password) {
//         this.id = id;
//         this.folder = folder;
//         this.title = title;
//         this.email = email;
//         this.username = username;
//         this.password = password;
//     }
//     public Long getId() {
//         return id;
//     }
//     public FolderModel getFolder() {
//         return folder;
//     }
//     public String getTitle() {
//         return title;
//     }
//     public String getEmail() {
//         return email;
//     }
//     public String getUsername() {
//         return username;
//     }
//     public String getPassword() {
//         return password;
//     }
// }
