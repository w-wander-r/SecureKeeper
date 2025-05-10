import { useState, useEffect } from 'react';

const useValidation = (value, validations) => {
  const [minLength, setMinLength] = useState(false);
  const [maxLength, setMaxLength] = useState(false);
  const [isEmpty, setEmpty] = useState(true);
  const [inputValid, setInputValid] = useState(false);

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

  useEffect(() => {
    if (minLength || maxLength || isEmpty) {
      setInputValid(false);
    } else {
      setInputValid(true);
    }
  }, [minLength, maxLength, isEmpty]);

  return { minLength, maxLength, isEmpty, inputValid };
};

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
};

export default useInput;