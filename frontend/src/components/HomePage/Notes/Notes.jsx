import React, { useState } from 'react';
import './_notes.scss';
import axios from 'axios';
import { TipIcon, PlusIcon } from '../../icons/Icons';
import NoteModal from './NoteModal';

const Notes = ({ notes, folderId, onNoteCreated }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleCreateNote = async (noteData) => {
    try {
        console.log("Creating note with data:", {
            ...noteData,
            folderId: folderId  // Verify this is not null
        });

        const token = localStorage.getItem('token');
        const response = await axios.post('http://localhost:8090/api/notes', {
            title: noteData.title,
            username: noteData.username || "",
            email: noteData.email || "",
            password: noteData.password,
            folderId: folderId  // This must not be null
        }, {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });
        
        onNoteCreated();
    } catch (err) {
        console.error('Error creating note:', err);
        alert(err.response?.data?.message || "Failed to create note");
    }
  };

  return (
    <>
      <ul className='notes__list'>
        {notes.map((note) => (
          <li key={note.id} className='notes__list-item'>
            <div className="notes__list-item__title">
              <h1>{note.title}</h1>
              <TipIcon/>
            </div>
            <div className="note__details">
              <div className="email">
                <h2>email</h2>
                <span>{note.email}</span>
              </div>
              <div className="password">
                <h2>password</h2>
                <span>{note.password}</span>
              </div>
            </div>
          </li>
        ))}

        <li 
          className='notes__list-item notes__list-item--new'
          onClick={() => setIsModalOpen(true)}
        >
          <PlusIcon/>
        </li>
      </ul>

      <NoteModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onSubmit={handleCreateNote}
        folderId={folderId}
      />
    </>
  );
};

export default Notes;