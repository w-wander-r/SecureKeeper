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
// @Table(name = "folders")
// public class FolderModel {

//     @Id
//     @GeneratedValue(strategy = GenerationType.AUTO)
//     private Long id;

//     @ManyToOne
//     @JoinColumn(name = "user_id", nullable = false)
//     private UsersModel user;
//     private String name;

//     public FolderModel() {
//     }
//     public FolderModel(Long id, UsersModel user, String name) {
//         this.id = id;
//         this.user = user;
//         this.name = name;
//     }
//     public Long getId() {
//         return id;
//     }
//     public void setId(Long id) {
//         this.id = id;
//     }
//     public UsersModel getUser() {
//         return user;
//     }
//     public void setUser(UsersModel user) {
//         this.user = user;
//     }
//     public String getName() {
//         return name;
//     }
//     public void setName(String name) {
//         this.name = name;
//     }
// }