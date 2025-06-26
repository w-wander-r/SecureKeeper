import React from "react";
import { useState } from "react";
import axios from "axios";

import "./_login.scss";
import { LoginIcon, NewAccIcon } from '../../components/icons/Icons';
import useInput from "../../components/Hooks/Validation";

export function SignInForm({ onSwitch }) {
  const username = useInput('', { isEmpty: true, minLength: 8, maxLength: 16 });
  const password = useInput('', { isEmpty: true, minLength: 8, maxLength: 20 });
  const [counter, setCounter] = useState(0);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
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
      
      console.log('Login successful:', response.data);
      // Handle successful login (store token, redirect, etc.)
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Login failed');
      console.error('Login error:', err);
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
          onChange={(e) => password.onChange(e)}
          onBlur={(e) => password.onBlur(e)}
          value={password.value}
          onInput={(e) => setCounter(e.target.value.length)}
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

        {error && <div className="auth-container__error">{error}</div>}

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
  const isDisabled = !username.inputValid || !password.inputValid || isLoading;

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError(null);
    
    try {
      const response = await axios.post('http://localhost:8090/register', {
        username: username.value,
        password: password.value
      });
      
      console.log('Registration successful:', response.data);
      // Handle successful registration (redirect to login, show success message, etc.)
      onSwitch(); // Optionally switch to login form after successful registration
    } catch (err) {
      setError(err.response?.data?.message || err.response?.data || err.message || 'Registration failed');
      console.error('Registration error:', err);
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
          onChange={(e) => password.onChange(e)}
          onBlur={(e) => password.onBlur(e)}
          value={password.value}
          onInput={(e) => setCounter(e.target.value.length)}
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

        {error && <div className="auth-container__error">{error}</div>}

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