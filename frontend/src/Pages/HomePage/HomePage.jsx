import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './_homePage.scss';
import { LogoIcon, PlusIcon } from '../../components/icons/Icons';
import FolderList from '../../components/HomePage/Folders/FolderList';
import { useNavigate } from 'react-router-dom';
import Notes from '../../components/HomePage/Notes/Notes';

const HomePage = () => {
  const [activeIndex, setActiveIndex] = useState(null);
  const [folders, setFolders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const [activeFolder, setActiveFolder] = useState(null);
  const [notes, setNotes] = useState([]);

  useEffect(() => {
    if (activeFolder) {
      fetchNotes(activeFolder.id);
    }
  }, [activeFolder]);

  const fetchNotes = async (folderId) => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get(`http://localhost:8090/api/notes/folder/${folderId}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      setNotes(response.data);
    } catch (err) {
      console.error('Error fetching notes:', err);
    }
  };

  const handleCreateNote = async () => {
    if (!activeFolder) return;
    
    try {
      const token = localStorage.getItem('token');
      await axios.post('http://localhost:8090/api/notes', {
        title: "New Note",
        username: "",
        email: "",
        password: "",
        folderId: activeFolder.id
      }, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      fetchNotes(activeFolder.id); // Refresh notes
    } catch (err) {
      console.error('Error creating note:', err);
    }
  };

  // Fetch user's folders from backend
  const fetchFolders = async () => {
    try {
      setLoading(true);
      const token = localStorage.getItem('token');
      if (!token) {
        navigate('/');
        return;
      }
  
      const response = await axios.get('http://localhost:8090/api/folders', {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });
      console.log("API Response:", response.data); // Debug log
      setFolders(response.data); // Keep full objects
    } catch (err) {
      console.log("error while fetching folders");
    } finally {
      setLoading(false);
    }
  };

  // Fetch folders on component mount
  useEffect(() => {
    fetchFolders();
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/');
  };

  const handleCreateFolder = async (folderName) => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.post(
        'http://localhost:8090/api/folders',
        { name: folderName },
        {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        }
      );
      // Refresh the folder list after creation
      await fetchFolders();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to create folder');
    }
  };

  // if (loading) return <div className="loading">Loading folders...</div>;
  // if (error) return <div className="error">Error: {error}</div>;

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
          onSelect={(index) => {
            const selectedFolder = folders[index];
            console.log("Selected folder:", selectedFolder); // Debug log
            setActiveIndex(index);
            setActiveFolder(selectedFolder);
          }}
        />
      </aside>

      <main className="main">
        <Notes 
          notes={notes}
          onNoteCreated={() => fetchNotes(activeFolder.id)}
          onNoteClick={(note) => {console.log("chosen note: "+note)}}
          folderId={activeFolder?.id}
        />
      </main>
    </div>
  );
};

export default HomePage;