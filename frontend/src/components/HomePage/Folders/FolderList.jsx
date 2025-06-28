import React from 'react';

import '../../../Pages/HomePage/_homePage.scss';
import { FolderIcon, NewFolderIcon } from '../../icons/Icons';

const FolderList = ({ folders, activeIndex, onSelect }) => {
  return (
    <ul className='folder__list'>
      {folders.map(( folder, index ) => (
        <li
          key={index}
          className={`folder__list-item ${index === activeIndex ? "active" : ""}`}
          onClick={() => onSelect(index)}
        >
          <FolderIcon className='folder-icon' />
          <span className='folder-name'>{folder}</span>
        </li>
      ))}

      <li className='folder__list-item new-folder'>
        <NewFolderIcon className='folder-icon' />
        <span className='folder-name'>New Folder</span>
      </li>
    </ul>
  );
}

export default FolderList