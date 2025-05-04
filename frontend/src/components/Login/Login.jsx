import React from 'react';
import { useState, useEffect } from 'react';

import './_login.scss';
import { LogoIcon, LoginIcon } from '../icons/Icons';

const useValidation = (value, validations) => {
  const [minLength, setMinLength] = useState(false);
  const [maxLength, setMaxLength] = useState(false);
  const [isEmpty, setEmpty] = useState(true);

  useEffect(() => {
    for (const validation in validations) {
      switch (validation) {
        case 'minLength':
          value.length < validations[validation] ? setMinLength(true) : setMinLength(false);
          break;

        case 'maxLength':
          value.length > validations[validation] ? setMaxLength(true) : setMaxLength(false);
          break;

        case 'isEmpty':
          value ? setEmpty(false) : setEmpty(true);
          break;

        default:
          break;
      }
    }
  }, [value, validations]);

  return { minLength, maxLength, isEmpty };
}

const useInput = (initialValue, validations) => {
  const [value, setValue] = useState(initialValue);
  const [isDirty, setDirty] = useState(false);
  const valid = useValidation(value, validations);

  const onChange = (e) => {
    setValue(e.target.value);
  };

  const onBlur = (e) => {
    setDirty(true);
  };

  return { value, onChange, onBlur, isDirty, ...valid };
}

const Login = () => {
  const username = useInput('', { isEmpty: true, minLength: 8, maxLength: 16 });
  const password = useInput('', { isEmpty: true, minLength: 8, maxLength: 20 });
  const [counter, setCounter] = useState(0);

  return (
    <div className="login">
      <LogoIcon/>
      <h1 className='login__title'>Sign up to SecureKeeper</h1>

      <div className='signup-form'>
        <form className='signup-form__form'>
          <label className="signup-form__label">Username</label>
          <input onChange={e => username.onChange(e)} onBlur={e => username.onBlur(e)} value={username.value} type='text' className="signup-form__input" minLength="8" maxLength="16" required />
          <div className="signup-form__error">
            {(username.isDirty && username.isEmpty && <span>Field cannot be empty</span>) ||
            (username.isDirty && (username.minLength || username.maxLength) && <span>Field must be between 8 and 16 characters</span>)}
          </div>
      
          <label htmlFor="password-login" className="signup-form__label">Password
            <span className='signup-form__input-counter' style={{ color: counter < 8 || counter > 20 ? "red" : "green" }}>{counter}/20</span>
          </label>
          <input onChange={e => password.onChange(e)} onBlur={e => password.onBlur(e)} value={password.value} onInput={e => setCounter(e.target.value.length)} type='password' className="signup-form__input" minLength="8" maxLength="20" required />
          <div className="signup-form__error">
            {(password.isDirty && password.isEmpty && <span>Field cannot be empty</span>) ||
            (password.isDirty && (password.minLength || password.maxLength) && <span>Field must be between 8 and 20 characters</span>)}
          </div>

          <button type='submit' className="signup-form__btn">Sign up</button>
        </form>

        <span className='or'>Or</span>

        <button className='signup-form__account-signin'><LoginIcon/>Already have account?</button>
      </div>
    </div>
  )
}

export default Login;