import React, { useState } from 'react';
import { noteAPI } from '../services/api';
import './Dashboard.css';

const NoteList = ({ notes, onDeleteNote, onRefresh }) => {
  const [viewingNote, setViewingNote] = useState(null);
  const [noteDetails, setNoteDetails] = useState(null);
  const [copySuccess, setCopySuccess] = useState('');

  const handleViewNote = async (noteId) => {
    try {
      const details = await noteAPI.getById(noteId);
      setNoteDetails(details);
      setViewingNote(noteId);
    } catch (err) {
      alert('Failed to load note details');
    }
  };

  const handleCopyPassword = (password) => {
    navigator.clipboard.writeText(password)
      .then(() => {
        setCopySuccess('Password copied!');
        setTimeout(() => setCopySuccess(''), 2000);
      })
      .catch(() => {
        setCopySuccess('Failed to copy');
      });
  };

  const handleCloseDetails = () => {
    setViewingNote(null);
    setNoteDetails(null);
  };

  if (notes.length === 0) {
    return (
      <div className="empty-notes">
        <p>No notes in this folder yet. Create your first note!</p>
      </div>
    );
  }

  return (
    <div className="note-list">
      {copySuccess && <div className="copy-success">{copySuccess}</div>}
      
      <div className="note-grid">
        {notes.map((note) => (
          <div key={note.id} className="note-card">
            <div className="note-header">
              <h4>{note.title}</h4>
              <button
                onClick={() => onDeleteNote(note.id)}
                className="delete-note-btn"
                title="Delete note"
              >
                ×
              </button>
            </div>
            <div className="note-body">
              <p><strong>Username:</strong> {note.username || 'N/A'}</p>
              <p><strong>Email:</strong> {note.email || 'N/A'}</p>
            </div>
            <div className="note-actions">
              <button
                onClick={() => handleViewNote(note.id)}
                className="view-btn"
              >
                View Details
              </button>
            </div>
          </div>
        ))}
      </div>

      {viewingNote && noteDetails && (
        <div className="note-modal-overlay" onClick={handleCloseDetails}>
          <div className="note-modal" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h3>{noteDetails.title}</h3>
              <button onClick={handleCloseDetails} className="close-modal">×</button>
            </div>
            <div className="modal-body">
              <div className="detail-row">
                <strong>Username:</strong>
                <span>{noteDetails.username || 'N/A'}</span>
              </div>
              <div className="detail-row">
                <strong>Email:</strong>
                <span>{noteDetails.email || 'N/A'}</span>
              </div>
              <div className="detail-row">
                <strong>Password:</strong>
                <div className="password-display">
                  <span className="password-masked">{'•'.repeat(noteDetails.password?.length || 8)}</span>
                  <button
                    onClick={() => handleCopyPassword(noteDetails.password)}
                    className="copy-btn"
                  >
                    📋 Copy
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default NoteList;