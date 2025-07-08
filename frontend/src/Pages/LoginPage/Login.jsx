import React from 'react';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

import './_login.scss';
import { RegisterForm, SignInForm } from './Forms';
import { LogoIcon  } from '../../components/icons/Icons'

const Login = () => {
  const [isSignIn, setIsSignIn] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleLogin = async (credentials) => {
    try {
      const response = await axios.post('http://localhost:8090/login', credentials);
      
      // Store the JWT token in localStorage or sessionStorage
      localStorage.setItem('token', response.data.token);
      
      // Redirect to home page
      navigate('/home');
    } catch (err) {
      setError(err.response?.data || 'Login failed');
    }
  };

  return (
    <div className="login">
      <LogoIcon/>
      <h1 className='login__title'>Welcome to SecureKeeper!</h1>

      {error && <div className="error-message">{error}</div>}

      <div className="auth-container">
          <div className="tabs">
            <button
              className={isSignIn ? 'tab active' : 'tab'}
              onClick={() => setIsSignIn(true)}
            >
              SIGN IN
            </button>
            <button
              className={!isSignIn ? 'tab active' : 'tab'}
              onClick={() => setIsSignIn(false)}
            >
              REGISTER
            </button>
          </div>
          <div className="form-container">
            {isSignIn ? 
              <SignInForm 
                onSwitch={() => setIsSignIn(false)} 
                onLogin={handleLogin}
              /> : 
              <RegisterForm 
                onSwitch={() => setIsSignIn(true)} 
              />
            }
          </div>
      </div>
    </div>
  )
}

export default Login;