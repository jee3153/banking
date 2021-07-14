import './TransactionHistory.css';
import React, { useEffect, useState } from 'react';

const TransactionHistory = () => {
  let [transaction, setTransaction] = useState([]);
  let [loaded, setloaded] = useState(false);
  const userId = localStorage.getItem('userId');

  // Similar to componentDidMount and componentDidUpdate:
  useEffect(() => {
    // Update the document title using the browser API
    // document.title = `You clicked ${count} times`;

    const fetchTransactions = async () => {
      const response = await fetch(
        `http://localhost:8080/transaction/${userId}`
      );

      const transactions = await response.json();
      setTransaction(transactions);
      setloaded(true);
      // console.log(transactions);

      // return transactions;
    };
    fetchTransactions();

    // console.log(transactions);
  }, []);

  const listTransactions = transaction.map((item, index) => (
    <tr key={index}>
      <td>{item.transactionKey.transactionId}</td>
      <td>{item.account.accountName}</td>
      <td>{item.participant.id}</td>
      <td>£{item.payment}</td>
      <td>{item.transactionKey.type}</td>
      <td>{item.paymentMadeAt}</td>
    </tr>
  ));

  return (
    <div className="TransactionHistory">
      <table className="transactionTable">
        <thead>
          <tr className="tableHeaders">
            <th>transactionID</th>
            <th>accountName</th>
            <th>recipient</th>
            <th>payment</th>
            <th>transactionType</th>
            <th>paymentMadeAt</th>
          </tr>
        </thead>
        <tbody>{loaded ? listTransactions : null}</tbody>
      </table>
    </div>
  );
};

export default TransactionHistory;
