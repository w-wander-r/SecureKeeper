import React, { useState } from 'react';
import { folderAPI } from '../services/api';
import './Dashboard.css';

const FolderList = ({ folders, selectedFolder, onSelectFolder }) => {
  const [editingFolder, setEditingFolder] = useState(null);
  const [editName, setEditName] = useState('');

  const handleDeleteFolder = async (folderId, e) => {
    e.stopPropagation();
    if (window.confirm('Are you sure you want to delete this folder? All notes in it will be deleted.')) {
      try {
        await folderAPI.delete(folderId);
        onSelectFolder(null);
      } catch (err) {
        alert('Failed to delete folder');
      }
    }
  };

  const handleUpdateFolder = async (folderId, e) => {
    e.stopPropagation();
    if (editName.trim()) {
      try {
        await folderAPI.update(folderId, editName);
        setEditingFolder(null);
        setEditName('');
        // Refresh the folder list by selecting the folder again
        onSelectFolder(folderId);
      } catch (err) {
        alert('Failed to update folder');
      }
    }
  };

  const startEditing = (folder, e) => {
    e.stopPropagation();
    setEditingFolder(folder.id);
    setEditName(folder.name);
  };

  return (
    <div className="folder-list">
      {folders.length === 0 ? (
        <p className="empty-message">No folders yet. Create one to get started!</p>
      ) : (
        folders.map((folder) => (
          <div
            key={folder.id}
            className={`folder-item ${selectedFolder === folder.id ? 'selected' : ''}`}
            onClick={() => onSelectFolder(folder.id)}
          >
            {editingFolder === folder.id ? (
              <div className="folder-edit" onClick={(e) => e.stopPropagation()}>
                <input
                  type="text"
                  value={editName}
                  onChange={(e) => setEditName(e.target.value)}
                  onKeyPress={(e) => e.key === 'Enter' && handleUpdateFolder(folder.id, e)}
                  className="folder-edit-input"
                  autoFocus
                />
                <button onClick={(e) => handleUpdateFolder(folder.id, e)} className="icon-btn">
                  ✓
                </button>
                <button onClick={() => setEditingFolder(null)} className="icon-btn cancel">
                  ✕
                </button>
              </div>
            ) : (
              <>
                <div className="folder-info">
                  <span className="folder-name">{folder.name}</span>
                </div>
                <div className="folder-actions">
                  <button onClick={(e) => startEditing(folder, e)} className="icon-btn edit">
                    ✏️
                  </button>
                  <button onClick={(e) => handleDeleteFolder(folder.id, e)} className="icon-btn delete">
                    🗑️
                  </button>
                </div>
              </>
            )}
          </div>
        ))
      )}
    </div>
  );
};

export default FolderList;