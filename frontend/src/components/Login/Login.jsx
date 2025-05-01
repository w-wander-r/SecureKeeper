import React from 'react';
import { useState } from 'react';

import './_login.scss';
import { LogoIcon, LoginIcon } from '../icons/Icons';

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [usernameDirty, setUsernameDirty] = useState(false);
  const [passwordDirty, setPasswordDirty] = useState(false);
  const [usernameError, setUsernameError] = useState('Username cannot be empty');
  const [passwordError, setPasswordError] = useState('Password cannot be empty');

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
      <h1 className='login__title'>Sign up to SecureKeeper</h1>

      <div className='signup-form'>
        <form className='signup-form__form'>
          <label className="signup-form__label">Username
          {(usernameDirty && usernameError) && <span>{usernameError}</span>}
          </label>
          <input name="username" onBlur={e => blurHandler(e)} type='text' className="signup-form__input" maxLength={20} required />
      
          <label htmlFor="password-login" className="signup-form__label">Password
          {(passwordDirty && passwordError) && <span>{passwordError}</span>}
          </label>
          <input name="password" onBlur={e => blurHandler(e)} type='password' className="signup-form__input" maxLength={20} required />

          <button className="signup-form__btn">Sign up</button>
        </form>

        <span className='or'>Or</span>

        <button className='signup-form__account-signin'><LoginIcon/>Already have account?</button>
      </div>
    </div>
  )
}

export default Login;