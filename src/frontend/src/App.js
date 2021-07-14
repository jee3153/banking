// import './App.css';
import React, { useEffect, useState } from 'react';
import TransactionHistory from './components/TransactionHistory';
import Login from './components/Login';
function App() {
  const [count, setCount] = useState(0);

  // Similar to componentDidMount and componentDidUpdate:
  // useEffect(() => {
  //   // Update the document title using the browser API
  //   // document.title = `You clicked ${count} times`;

  //   const fetchTransactions = async () => {
  //     const response = await fetch('http://localhost:8080/transaction/all');

  //     const transactions = await response.json();
  //     console.log(transactions);
  //   };

  //   fetchTransactions();
  // });

  return (
    <div className="App">
      <header className="App-header">
        <TransactionHistory />
      </header>
      <Login />
    </div>
  );
}

export default App;
