import React from 'react';
import { useState } from 'react';

import './_homePage.scss';
import { LogoIcon, PlusIcon } from '../../components/icons/Icons';
import FolderList from '../../components/HomePage/Folders/FolderList'
import Notes from '../../components/HomePage/Notes/Notes';
import { useNavigate } from 'react-router-dom';

const folders = ["Work", "Personal", "Projects"];

const HomePage = () => {
  const [activeIndex, setActiveIndex] = useState(null);
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/');
  };

  return (
    <div className="home-page">
      <header className="header">
        <nav className="menu">
          <ul className="menu__list">
            <li className="menu__list-item">Main</li>
            <li className="menu__list-item">About us</li>
            <li className="menu__list-item">Contacts</li>
            <li className="menu__list-item">Settings</li>
          </ul>
        </nav>

        <button className="logout" onClick={handleLogout}>Log out</button>
      </header>

      <aside className="aside">
        <LogoIcon/>
        <FolderList 
          folders={folders} 
          activeIndex={activeIndex} 
          onSelect={(index) => setActiveIndex(index)} 
        />
        
      </aside>

      <main className="main">
        <li className='notes__list-item notes__list-item--new'>
          <PlusIcon/>
        </li>
      </main>
    </div>
  );
};

export default HomePage;