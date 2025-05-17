import React from 'react';
import { useState, useEffect } from 'react';
import { NavLink } from 'react-router-dom';

import './_login.scss';
import { LogoIcon, LoginIcon } from '../../components/icons/Icons';

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [usernameDirty, setUsernameDirty] = useState(false);
  const [passwordDirty, setPasswordDirty] = useState(false);
  const [usernameError, setUsernameError] = useState('Username cannot be empty');
  const [passwordError, setPasswordError] = useState('Password cannot be empty');
  const [counter, setCounter] = useState(0);
  const [formValid, setFormValid] = useState(false);

  useEffect(() => {
    if (usernameError !== '' || passwordError !== '') {
      setFormValid(false);
    } else {
      setFormValid(true);
    }
  }, [usernameError, passwordError]);

  const handleClick = (e) => {
    if (!formValid) {
      e.preventDefault();
    }
  };

  const usernameHandler = (e) => {
    const value = e.target.value;
    setUsername(value);
    const re = /^[a-zA-Z0-9]+([._]?[a-zA-Z0-9]+)*$/;
    if (value.length === 0) {
      setUsernameError('Username cannot be empty');
    } else if (!re.test(value)) {
      setUsernameError('Incorrect username');
    } else {
      setUsernameError('');
    }
  };

  const passwordHandler = (e) => {
    const value = e.target.value;
    setPassword(value);
    if (value.length === 0) {
      setPasswordError('Password cannot be empty');
    } else if (value.length < 8 || value.length > 20) {
      setPasswordError('Password must be between 8 and 20 characters');
    } else {
      setPasswordError('');
    }
  }

  const blurHandler = (e) => {
    switch (e.target.name) {
      case 'username':
        setUsernameDirty(true);
        break;

      case 'password':
        setPasswordDirty(true);
        break;

      default:
    }
  };

  return (
    <div className="login">
      <LogoIcon/>
      <h1 className='login__title'>Welcome to SecureKeeper!</h1>

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
            {isSignIn ? <SignInForm onSwitch={() => setIsSignIn(false)} /> : <RegisterForm onSwitch={() => setIsSignIn(true)} />}
          </div>

          <NavLink to="/Home" onClick={handleClick} type='submit' className="signup-form__btn" style={{ cursor: !formValid ? 'not-allowed' : 'pointer' }}>Sign up</NavLink>
        </form>

        <span className='or'>Or</span>

        <button className='signup-form__account-signin'><LoginIcon/>Already have account?</button>
      </div>
    </div>
  )
}

export default Login;