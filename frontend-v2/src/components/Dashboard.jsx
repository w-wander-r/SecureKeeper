import React, { useState, useEffect } from 'react';
import { authAPI, folderAPI, noteAPI, isAuthenticated } from '../services/api';
import FolderList from './FolderList';
import NoteList from './NoteList';
import NoteForm from './NoteForm';
import './Dashboard.css';

const Dashboard = () => {
  const [folders, setFolders] = useState([]);
  const [selectedFolder, setSelectedFolder] = useState(null);
  const [notes, setNotes] = useState([]);
  const [showNoteForm, setShowNoteForm] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    if (isAuthenticated()) {
      loadFolders();
    } else {
      window.location.href = '/login';
    }
  }, []);

  const loadFolders = async () => {
    try {
      setLoading(true);
      const data = await folderAPI.getAll();
      setFolders(data);
    } catch (err) {
      setError('Failed to load folders');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const loadNotes = async (folderId) => {
    try {
      setLoading(true);
      const data = await noteAPI.getAllByFolder(folderId);
      setNotes(data);
      setSelectedFolder(folderId);
    } catch (err) {
      setError('Failed to load notes');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateFolder = async (name) => {
    try {
      await folderAPI.create(name);
      await loadFolders();
    } catch (err) {
      setError('Failed to create folder');
      console.error(err);
    }
  };

  const handleCreateNote = async (noteData) => {
    if (!selectedFolder) {
      setError('Please select a folder first');
      return;
    }

    try {
      await noteAPI.create({
        ...noteData,
        folderId: selectedFolder,
      });
      await loadNotes(selectedFolder);
      setShowNoteForm(false);
    } catch (err) {
      setError('Failed to create note');
      console.error(err);
    }
  };

  const handleDeleteNote = async (noteId) => {
    try {
      await noteAPI.delete(noteId);
      await loadNotes(selectedFolder);
    } catch (err) {
      setError('Failed to delete note');
      console.error(err);
    }
  };

  const handleLogout = () => {
    authAPI.logout();
    window.location.href = '/login';
  };

  if (loading && folders.length === 0) {
    return (
      <div className="loading-container">
        <div className="loading-spinner"></div>
        <p>Loading...</p>
      </div>
    );
  }

  return (
    <div className="dashboard">
      <header className="dashboard-header">
        <h1>SecureKeeper</h1>
        <button onClick={handleLogout} className="logout-btn">
          Logout
        </button>
      </header>

      {error && <div className="error-banner">{error}</div>}

      <div className="dashboard-content">
        <div className="sidebar">
          <div className="sidebar-section">
            <h3>Folders</h3>
            <button 
              onClick={() => {
                const name = prompt('Enter folder name:');
                if (name) handleCreateFolder(name);
              }}
              className="add-btn"
            >
              + New Folder
            </button>
          </div>
          
          <FolderList
            folders={folders}
            selectedFolder={selectedFolder}
            onSelectFolder={loadNotes}
          />
        </div>

        <div className="main-content">
          {selectedFolder ? (
            <>
              <div className="content-header">
                <h2>Notes in Folder</h2>
                <button
                  onClick={() => setShowNoteForm(true)}
                  className="add-btn"
                >
                  + New Note
                </button>
              </div>

              {showNoteForm ? (
                <NoteForm
                  onSubmit={handleCreateNote}
                  onCancel={() => setShowNoteForm(false)}
                />
              ) : (
                <NoteList
                  notes={notes}
                  onDeleteNote={handleDeleteNote}
                  onRefresh={() => loadNotes(selectedFolder)}
                />
              )}
            </>
          ) : (
            <div className="empty-state">
              <h3>Welcome to SecureKeeper!</h3>
              <p>Select a folder to view or create notes, or create a new folder to get started.</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;