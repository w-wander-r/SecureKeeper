import React from 'react';
import { useState } from 'react';

import './_login.scss';
import { LogoIcon } from '../icons/Icons';
import { SignInForm, RegisterForm } from './Forms';

const Login = () => {
  const [isSignIn, setIsSignIn] = useState(true);

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
        </div>
    </div>
  )
}

export default Login;