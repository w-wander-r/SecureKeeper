import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

import "./_login.scss";
import { LoginIcon, NewAccIcon } from '../../components/icons/Icons';
import useInput from "../../components/Hooks/Validation";

export function SignInForm({ onSwitch }) {
  const username = useInput('', { isEmpty: true, minLength: 8, maxLength: 16 });
  const password = useInput('', { isEmpty: true, minLength: 8, maxLength: 20 });
  const [counter, setCounter] = useState(0);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();
  const isDisabled = !username.inputValid || !password.inputValid || isLoading;
  
  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError(null);
    
    try {
      const response = await axios.post('http://localhost:8090/login', {
        username: username.value,
        password: password.value
      });
      
      // Store the received token in localStorage
      localStorage.setItem('token', response.data.token);
      
      // Redirect to home page
      navigate('/home');
      
    } catch (err) {
      const errorMessage = err.response?.data?.message || 
                         err.response?.data || 
                         err.message || 
                         'Login failed. Please try again.';
      setError(errorMessage);
      
      // Clear password field on error
      // password.setValue('');
      // password.setIsDirty(false);
      setCounter(0);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <>
      <form className="auth-container__form" onSubmit={handleSubmit}>
        <label className="auth-container__label">Username</label>
        <input
          onChange={(e) => username.onChange(e)}
          onBlur={(e) => username.onBlur(e)}
          value={username.value}
          type="text"
          className="auth-container__input"
          maxLength="16"
          required
        />
        <div className="auth-container__error">
          {(username.isDirty && username.isEmpty && (
            <span>Field cannot be empty</span>
          )) ||
            (username.isDirty && (username.minLength || username.maxLength) && (
              <span>Field must be between 8 and 16 characters</span>
            ))}
        </div>

        <label htmlFor="password-login" className="auth-container__label">
          Password
          <span
            className="auth-container__input-counter"
            style={{ color: counter < 8 || counter > 20 ? "red" : "green" }}
          >
            {counter}/20
          </span>
        </label>
        <input
          onChange={(e) => {
            password.onChange(e);
            setCounter(e.target.value.length);
          }}
          onBlur={(e) => password.onBlur(e)}
          value={password.value}
          type="password"
          className="auth-container__input"
          maxLength="20"
          required
        />
        <div className="auth-container__error">
          {(password.isDirty && password.isEmpty && (
            <span>Field cannot be empty</span>
          )) ||
            (password.isDirty && (password.minLength || password.maxLength) && (
              <span>Field must be between 8 and 20 characters</span>
            ))}
        </div>

        {error && <div className="auth-container__error auth-container__error--main">{error}</div>}

        <button
          disabled={isDisabled}
          type="submit"
          className="auth-container__btn"
          style={{ cursor: isDisabled ? "not-allowed" : "pointer" }}
        >
          {isLoading ? 'Signing in...' : 'Sign in'}
        </button>

        <span className="or">or</span>
      </form>

      <button 
        className="auth-container__account-signin"
        onClick={onSwitch}
      >
        <LoginIcon />
        Already have an account?
      </button>
    </>
  );
};

export function RegisterForm({ onSwitch }) {
  const username = useInput('', { isEmpty: true, minLength: 8, maxLength: 16 });
  const password = useInput('', { isEmpty: true, minLength: 8, maxLength: 20 });
  const [counter, setCounter] = useState(0);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [success, setSuccess] = useState(false);
  const isDisabled = !username.inputValid || !password.inputValid || isLoading;

  useEffect(() => {
    if (success) {
      const timer = setTimeout(() => {
        onSwitch(); // Switch back to login form
        setSuccess(false); // Reset success state
        username.setValue(''); // Clear username
        password.setValue(''); // Clear password
        setCounter(0); // Reset counter
      }, 2000); // Wait 2 seconds before redirecting
      
      return () => clearTimeout(timer);
    }
  }, [success, onSwitch, username, password]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError(null);
    
    try {
      const response = await axios.post('http://localhost:8090/register', {
        username: username.value,
        password: password.value
      });
      
      setSuccess(true);
    } catch (err) {
      setError(err.response?.data?.message || err.response?.data || err.message || 'Registration failed');
      password.setValue('');
      setCounter(0);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <>
      <form className="auth-container__form" onSubmit={handleSubmit}>
        {success && (
          <div className="auth-container__success">
            Registration successful! Redirecting to login...
          </div>
        )}
        
        <label className="auth-container__label">Username</label>
        <input
          onChange={(e) => username.onChange(e)}
          onBlur={(e) => username.onBlur(e)}
          value={username.value}
          type="text"
          className="auth-container__input"
          minLength="8"
          maxLength="16"
          required
        />
        <div className="auth-container__error">
          {(username.isDirty && username.isEmpty && (
            <span>Field cannot be empty</span>
          )) ||
            (username.isDirty && (username.minLength || username.maxLength) && (
              <span>Field must be between 8 and 16 characters</span>
            ))}
        </div>

        <label htmlFor="password-login" className="auth-container__label">
          Password
          <span
            className="auth-container__input-counter"
            style={{ color: counter < 8 || counter > 20 ? "red" : "green" }}
          >
            {counter}/20
          </span>
        </label>
        <input
          onChange={(e) => {
            password.onChange(e);
            setCounter(e.target.value.length);
          }}
          onBlur={(e) => password.onBlur(e)}
          value={password.value}
          type="password"
          className="auth-container__input"
          minLength="8"
          maxLength="20"
          required
        />
        <div className="auth-container__error">
          {(password.isDirty && password.isEmpty && (
            <span>Field cannot be empty</span>
          )) ||
            (password.isDirty && (password.minLength || password.maxLength) && (
              <span>Field must be between 8 and 20 characters</span>
            ))}
        </div>

        {error && <div className="auth-container__error auth-container__error--main">{error}</div>}

        <button
          disabled={isDisabled}
          type="submit"
          className="auth-container__btn"
          style={{ cursor: isDisabled ? "not-allowed" : "pointer" }}
        >
          {isLoading ? 'Signing up...' : 'Sign up'}
        </button>

        <span className="or">or</span>
      </form>

      <button 
        className="auth-container__account-signin"
        onClick={onSwitch}
      >
        <NewAccIcon />
        Don't have an account?
      </button>
    </>
  );
}