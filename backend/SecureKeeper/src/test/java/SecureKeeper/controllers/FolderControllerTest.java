package SecureKeeper.controllers;

import SecureKeeper.exceptions.AccessDeniedException;
import SecureKeeper.exceptions.ResourceNotFoundException;
import SecureKeeper.models.Folder;
import SecureKeeper.models.FolderCreationRequest;
import SecureKeeper.models.FolderDTO;
import SecureKeeper.models.FolderUpdateDTO;
import SecureKeeper.models.User;
import SecureKeeper.repo.FolderRepo;
import SecureKeeper.repo.UserRepo;
import SecureKeeper.service.FolderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FolderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FolderService folderService;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private FolderRepo folderRepo;

    private User testUser;
    private Folder testFolder;
    private String authUsername = "testuser";

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername(authUsername);
        testUser.setPassword("password");

        testFolder = new Folder();
        testFolder.setId(1L);
        testFolder.setName("Test Folder");
        testFolder.setUser(testUser);

        // Mock authentication
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(authUsername)
                .password("password")
                .authorities("USER")
                .build();

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepo.findByUsername(authUsername)).thenReturn(Optional.of(testUser));
    }

    @Test
    void getFolderById_FolderNotFound_ReturnsNotFound() throws Exception {
        // Arrange
        when(folderRepo.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/folders/999"))
                .andDo(print())
                .andExpect(status().isNotFound()) // HTTP 404
                .andExpect(jsonPath("$.message").value("Folder with ID 999 not found"));
    }

    @Test
    void getFolderById_UnauthorizedAccess_ReturnsForbidden() throws Exception {
        // Arrange
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("otheruser");

        Folder otherFolder = new Folder();
        otherFolder.setId(1L);
        otherFolder.setName("Other Folder");
        otherFolder.setUser(otherUser);

        when(folderRepo.findById(1L)).thenReturn(Optional.of(otherFolder));

        // Act & Assert
        mockMvc.perform(get("/api/folders/1"))
                .andDo(print())
                .andExpect(status().isForbidden()) // HTTP 403
                .andExpect(jsonPath("$.message").value("You are not allowed to access this folder"));
    }

    @Test
    void deleteFolder_Success_ReturnsNoContent() throws Exception {
        // Arrange
        when(folderRepo.findById(1L)).thenReturn(Optional.of(testFolder));

        // Act & Assert
        mockMvc.perform(delete("/api/folders/1"))
                .andDo(print())
                .andExpect(status().isNoContent()); // HTTP 204
    }

    @Test
    void createFolder_ValidRequest_ReturnsCreatedFolder() throws Exception {
        // Arrange
        FolderCreationRequest request = new FolderCreationRequest("New Folder");
        Folder savedFolder = new Folder("New Folder", testUser);
        savedFolder.setId(2L);

        when(folderService.createFolder(any(Folder.class))).thenReturn(savedFolder);

        // Act & Assert
        mockMvc.perform(post("/api/folders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("New Folder"));
    }

    @Test
    void updateFolder_Success_ReturnsUpdatedFolder() throws Exception {
        // Arrange
        FolderUpdateDTO updateDTO = new FolderUpdateDTO("Updated Name");
        Folder updatedFolder = new Folder("Updated Name", testUser);
        updatedFolder.setId(1L);

        when(folderRepo.findById(1L)).thenReturn(Optional.of(testFolder));
        when(folderService.createFolder(any(Folder.class))).thenReturn(updatedFolder);

        // Act & Assert
        mockMvc.perform(patch("/api/folders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void getAllFoldersByUser_WrongUserId_ReturnsForbidden() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/folders/user/999"))
                .andDo(print())
                .andExpect(status().isForbidden()); // HTTP 403
    }

    @Test
    void getAllFoldersByUser_ValidUserId_ReturnsFolders() throws Exception {
        // Arrange
        List<Folder> folders = Arrays.asList(
                new Folder("Folder 1", testUser),
                new Folder("Folder 2", testUser)
        );
        folders.get(0).setId(1L);
        folders.get(1).setId(2L);

        when(folderService.getAllFoldersByUser(testUser)).thenReturn(folders);

        // Act & Assert
        mockMvc.perform(get("/api/folders/user/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}