import React, { useState, useEffect } from 'react';
import Login from './components/Login';
import Register from './components/Register';
import Dashboard from './components/Dashboard';
import { isAuthenticated } from './services/api';
import './App.css';

function App() {
  const [isAuth, setIsAuth] = useState(false);
  const [showRegister, setShowRegister] = useState(false);

  useEffect(() => {
    const authStatus = isAuthenticated();
    setIsAuth(authStatus);
  }, []);

  const handleLoginSuccess = () => {
    setIsAuth(true);
  };

  const handleRegisterSuccess = () => {
    setIsAuth(true);
  };

  if (isAuth) {
    return <Dashboard />;
  }

  return (
    <div className="App">
      {showRegister ? (
        <Register
          onRegisterSuccess={handleRegisterSuccess}
          switchToLogin={() => setShowRegister(false)}
        />
      ) : (
        <Login
          onLoginSuccess={handleLoginSuccess}
          switchToRegister={() => setShowRegister(true)}
        />
      )}
    </div>
  );
}

export default App;