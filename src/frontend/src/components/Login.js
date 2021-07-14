import React, { useState } from 'react';

const Login = () => {
  //   const userId = '';
  const [userId, setuserId] = useState('');
  const submit = (event) => {
    // store userId to localStorage
    event.preventDefault();
    localStorage.setItem(`userId`, userId);
    console.log(userId);
  };

  const handleChange = (event) => {
    setuserId(event.target.value);
  };

  return (
    <div className="Login">
      <form onSubmit={submit}>
        <label>
          Username:
          <input value={userId} onChange={handleChange} />
        </label>
        <input type="submit" value="Submit" />
      </form>
    </div>
  );
};

export default Login;
