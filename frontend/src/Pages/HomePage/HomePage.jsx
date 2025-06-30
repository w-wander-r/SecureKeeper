import React from 'react';
import { useState } from 'react';

import './_homePage.scss';
import { LogoIcon } from '../../components/icons/Icons';
import FolderList from '../../components/HomePage/Folders/FolderList'
import Notes from '../../components/HomePage/Notes/Notes';

const folders = ["Work", "Personal", "Projects"];

const note1 = {
  id: 1,
  title: "Project z",
  email: "projectz@gmail.com",
  username: "ProjectZ",
  password: "X_!#@SSwaStiKA"
}

const note2 = {
  id: 2,
  title: "Project X",
  email: "projectx@gmail.com",
  username: "ProjectX",
  password: "X_!#@SSwaStiKA"
}

const notes = [note1, note2];

const HomePage = () => {
  const [activeIndex, setActiveIndex] = useState(null);

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

        <button className="logout">Log out</button>
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
        <Notes notes={notes} />
      </main>
    </div>
  );
};

export default HomePage;