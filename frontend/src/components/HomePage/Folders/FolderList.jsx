import React, { useState } from 'react';
import '../../../Pages/HomePage/_homePage.scss';
import { FolderIcon, NewFolderIcon } from '../../icons/Icons';

const FolderList = ({ folders, activeIndex, onSelect, onCreateFolder }) => {
  const [newFolderName, setNewFolderName] = useState('');
  const [isCreating, setIsCreating] = useState(false);

  const handleCreateClick = () => {
    setIsCreating(true);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (newFolderName.trim()) {
      onCreateFolder(newFolderName);
      setNewFolderName('');
    }
    setIsCreating(false);
  };

  return (
    <ul className='folder__list'>
      {folders.map((folder, index) => (
        <li
            key={folder.id}
            className={`folder__list-item ${index === activeIndex ? "active" : ""}`}
            onClick={() => {
              console.log("Selected folder:", folder);
              onSelect(folder);
            }}
         >
          <FolderIcon className='folder-icon' />
          <span className='folder-name'>{folder}</span>
        </li>
      ))}

      {isCreating ? (
        <form onSubmit={handleSubmit} className="folder__list-item">
          <input
            type="text"
            value={newFolderName}
            onChange={(e) => setNewFolderName(e.target.value)}
            autoFocus
            className="folder-name-input"
            placeholder="Folder name"
          />
          <button type="submit" className="folder-submit-btn">
            Create
          </button>
        </form>
      ) : (
        <li className='folder__list-item new-folder' onClick={handleCreateClick}>
          <NewFolderIcon className='folder-icon' />
          <span className='folder-name'>New Folder</span>
        </li>
      )}
    </ul>
  );
};

export default FolderList;