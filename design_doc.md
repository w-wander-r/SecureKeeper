
# Project Name: SecureKeeper

## Introduction
SecureKeeper is a password management platform designed to solve the problem of storing and organizing passwords securely. Instead of storing passwords in unsecured files, users can create folders and notes to manage their credentials in an encrypted and user-friendly way.

## Goals
- Provide a **secure platform** for users to store and manage their passwords.
- Simplify users lives by allowing them to organize credentials into **folders** and **notes**.
- Ensure all data is **encrypted** and accessible only with a user-defined PIN.

## Scope
### In Scope
- User authentication (login, registration).
- Creation and management of folders (e.g., "Study" for study-related credentials).
- Creation and management of notes (storing usernames, emails, passwords, etc.).
- Encryption of all sensitive data in the database.
- Access to encrypted data only after entering a user-defined PIN.

### Out of Scope
- Password sharing between users.
- Two-factor authentication (for now).
- Mobile app or desktop client (web-only for now).

## Audience
- **Primary Users**: Regular users who need a secure way to manage their passwords.
- **Stakeholders**: Developers, security experts, and potential future users.

## Features
1. **User Authentication**:
   - Secure login and registration.
2. **Folder Management**:
   - Create, edit, and delete folders (e.g., "Work", "Study", "Personal").
3. **Note Management**:
   - Add, edit, and delete notes within folders.
   - Store credentials like usernames, emails, and passwords.
4. **Data Encryption**:
   - All sensitive data in notes is stored as encrypted values in the database.

## Technical Details
### Tech Stack
- **Backend**: Java with Spring (Spring Boot, Spring Security).
- **Database**: MySQL.

### Architecture
- **Backend**: RESTful API for handling user authentication, folder/note management, and encryption/decryption.
- **Database**: MySQL database to store user data, folders, and encrypted notes.
- **Security**: Spring Security for authentication and authorization.

## Timeline
- No strict deadlines for now. Focus on building a secure and functional product.

## Potential Risks
1. **Data Leaks**:
   - Mitigation: Use strong encryption algorithms and secure key management practices.
   - Regularly audit the codebase for vulnerabilities.
2. **PIN Security**:
   - Mitigation: Ensure the PIN is securely stored and hashed (if used for authentication).
3. **Scalability**:
   - Mitigation: Design the system to handle future growth (e.g., optimize database queries, use caching).

## Testing
<!-- todo -->

## Deliverables
- A fully functional web application for password management.
- Secure backend API with user authentication and data encryption.
- User sensitive data such passwords, emails, usernames securely stored
<!-- - Documentation for developers and users. -->

---

## Example Workflow
1. **User Registration**:
   - User signs up with an email and password.
2. **Folder Creation**:
   - User creates a folder (e.g., "Study") to organize credentials.
3. **Note Creation**:
   - User adds a note to the folder with credentials (e.g., username, email, password).
4. **Data Encryption**:
   - The note's data is encrypted and stored in the database.
5. **Accessing Data**:
   - User enters their PIN to decrypt and view the note's contents.
