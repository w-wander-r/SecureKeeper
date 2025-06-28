import React from 'react';

import './_notes.scss';
import { TipIcon } from '../../icons/Icons';

const Notes = ({ notes }) => {
  return (
    <ul className='notes__list'>
      {notes.map(( note, index ) => (
        <li
          key={index}
          className='notes__list-item'
        >

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
    </ul>
  );
}

export default Notes;