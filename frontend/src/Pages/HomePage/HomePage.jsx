import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './_homePage.scss';
import { LogoIcon, PlusIcon } from '../../components/icons/Icons';
import FolderList from '../../components/HomePage/Folders/FolderList';
import { useNavigate } from 'react-router-dom';

const HomePage = () => {
  const [activeIndex, setActiveIndex] = useState(null);
  const [folders, setFolders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

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
      setFolders(response.data);
      setError(null);
    } catch (err) {
      console.error('Error fetching folders:', err);
      setError(err.response?.data?.message || 'Failed to load folders');
      if (err.response?.status === 401) {
        localStorage.removeItem('token');
        navigate('/');
      }
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
          onSelect={(index) => setActiveIndex(index)}
          onCreateFolder={handleCreateFolder}
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