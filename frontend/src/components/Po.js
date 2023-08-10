import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams, Link } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';

function Po() {
    const [poDetails, setPoDetails] = useState({});
    const [invoiceDetails, setInvoiceDetails] = useState([]);
    const [submissionDetails,setSubmissionDetails] = useState([]);
    const [isEditing, setIsEditing] = useState(false);
    const [newPovendor, setNewPovendor] = useState('');
    const [isCreatingInvoice, setIsCreatingInvoice] = useState(false);
    const [newInvoiceName, setNewInvoiceName] = useState('');
    const [newInvoiceDetail, setNewInvoiceDetail] = useState('');
    const [newInvoiceAmount, setNewInvoiceAmount] = useState('');
    const [isCreatingSubmission, setIsCreatingSubmission] = useState(false);
    const [newSubmissionName, setNewSubmissionName] = useState('');
    const [newSubmissionAmount, setNewSubmissionAmount] = useState('');
    const { POref } = useParams();

    const handleDeleteSubmission = async (submissionId) => {
        const user = JSON.parse(localStorage.getItem('user'));
        const config = {
            headers: {
                Authorization: `Bearer ${user.accessToken}`
            }
        };
    
        try {
            await axios.delete(`http://localhost:8080/submissions/delete/${submissionId}`, config);
    
            // Fetch updated submission details
            const submissionResponse = await axios.get(`http://localhost:8080/submissions/get/${POref}`, config);
            setSubmissionDetails(submissionResponse.data);
        } catch (error) {
            console.error('Error deleting submission:', error);
        }
    };
    

    const handleDeleteInvoice = async (invoiceId) => {
        const user = JSON.parse(localStorage.getItem('user'));
        const config = {
            headers: {
                Authorization: `Bearer ${user.accessToken}`
            }
        };
    
        try {
            await axios.delete(`http://localhost:8080/invoices/delete/${invoiceId}`, config);
    
            // Fetch updated invoice details
            const invoiceResponse = await axios.get(`http://localhost:8080/invoices/${POref}`, config);
            setInvoiceDetails(invoiceResponse.data);
        } catch (error) {
            console.error('Error deleting invoice:', error);
        }
    };

    // Function to handle the "Create Invoice" button click
const handleCreateInvoice = () => {
    setIsCreatingInvoice(true);
    setNewInvoiceName('');
    setNewInvoiceDetail('');
    setNewInvoiceAmount('');
};

// Function to handle the "Create" button click when creating an invoice
const handleCreateInvoiceClick = () => {
    createNewInvoice(); // Call the createNewInvoice function
    setIsCreatingInvoice(false);
};

// Function to handle the "Cancel" button click when creating an invoice
const handleCancelInvoiceClick = () => {
    setIsCreatingInvoice(false);
};

const handleCreateSubmission = () => {
    setIsCreatingSubmission(true);
    setNewSubmissionName('');
    setNewSubmissionAmount('');
};

const handleCreateSubmissionClick = () => {
    createNewSubmission(); // 
    setIsCreatingSubmission(false);
};

// Function to handle the "Cancel" button click when creating an invoice
const handleCancelSubmissionClick = () => {
    setIsCreatingSubmission(false);
};

    const createNewInvoice = async () => {
        const user = JSON.parse(localStorage.getItem('user'));
        const config = {
            headers: {
                Authorization: `Bearer ${user.accessToken}`
            }
        };

        try {
            await axios.post(`http://localhost:8080/invoices/createinvoice/${POref}`, {
                invName: newInvoiceName,
                invDetail: newInvoiceDetail,
                invAmount: newInvoiceAmount
            }, config);

            // Fetch updated invoice details
            const invoiceResponse = await axios.get(`http://localhost:8080/invoices/${POref}`, config);
            setInvoiceDetails(invoiceResponse.data);

            setIsCreatingInvoice(false);
        } catch (error) {
            console.error('Error creating invoice:', error);
        }
    };

    const createNewSubmission = async () => {
        const user = JSON.parse(localStorage.getItem('user'));
        const config = {
            headers: {
                Authorization: `Bearer ${user.accessToken}`
            }
        };

        try {
            await axios.post(`http://localhost:8080/submissions/createsub/${POref}`, {
                username: newSubmissionName,
                subamount: newSubmissionAmount
            }, config);

            // Fetch updated submission details
            const submissionResponse = await axios.get(`http://localhost:8080/submissions/get/${POref}`, config);
            setSubmissionDetails(submissionResponse.data);

            setIsCreatingSubmission(false);
        } catch (error) {
            console.error('Error creating submission:', error);
        }
    };

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        const day = date.getDate().toString().padStart(2, '0');
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const year = date.getFullYear().toString().slice(-2);
        return `${day}${month}${year}`;
    };

    const handleEditClick = () => {
        setIsEditing(true);
        setNewPovendor(poDetails.povendor);
    };

    const handleSaveClick = async () => {
        const user = JSON.parse(localStorage.getItem('user'));
        const config = {
            headers: {
                Authorization: `Bearer ${user.accessToken}`
            }
        };

        try {
            await axios.put(`http://localhost:8080/api/po/updatePO/${POref}`, {
                povendor: newPovendor
            }, config);

            // Fetch updated PO details
            const response = await axios.get(`http://localhost:8080/api/po/getPObyRef/${POref}`, config);
            setPoDetails(response.data);

            setIsEditing(false);
        } catch (error) {
            console.error('Error updating PO details:', error);
        }
    };

    // const formatPoStatus = (status) => {
    //     return status ? 'Completed' : 'Pending';
    //   };

    // Calculate the Invoice total
  const calculateInvoiceTotal = () => {
    return invoiceDetails.reduce((total, invoice) => total + invoice.invAmount, 0);
  };

  // Calculate the Submission total
  const calculateSubmissionTotal = () => {
    return submissionDetails.reduce((total, submission) => total + submission.subamount, 0);
  };

  // Calculate the PO balance
  const calculatePOBalance = () => {
    return calculateInvoiceTotal() - calculateSubmissionTotal();
  };
  const updatePOStatusAndReload = async () => {
    const user = JSON.parse(localStorage.getItem('user'));
    const config = {
      headers: {
        Authorization: `Bearer ${user.accessToken}`
      }
    };

    try {
      await axios.post(`http://localhost:8080/api/po/updatePOStatus/${POref}`, null, config);
      window.location.reload(); // Reload the page
    } catch (error) {
      console.error('Error updating PO status:', error);
    }
  };
  

    useEffect(() => {
        const user = JSON.parse(localStorage.getItem('user'));
        const config = {
            headers: {
                Authorization: `Bearer ${user.accessToken}`
            }
        };

        const fetchData = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/api/po/getPObyRef/${POref}`, config);
                setPoDetails(response.data);

                const invoiceResponse = await axios.get(`http://localhost:8080/invoices/${POref}`, config);
                setInvoiceDetails(invoiceResponse.data);

                const submissionResponse = await axios.get(`http://localhost:8080/submissions/get/${POref}`, config);
                setSubmissionDetails(submissionResponse.data);
            } catch (error) {
                console.error('Error fetching details:', error);
            }
        };

        fetchData();
    }, [POref]);

    return (
        <div className="container mt-4">
            <div className="row">
                <div className="col-md-12">
                    <Link to="/home" className="btn btn-secondary mb-3">Back to Home</Link>
                </div>
                <div className="col-md-6">
                    <div className="card mb-4">
                        <div className="card-header d-flex justify-content-between align-items-center">
                        <div>
                                PO Details
                            </div>
                            {isEditing ? (
                                <button
                                    className="btn btn-sm btn-success"
                                    onClick={handleSaveClick}
                                >
                                    Save
                                </button>
                            ) : (
                                <button
                                    className="btn btn-sm btn-primary"
                                    onClick={handleEditClick}
                                >
                                    Update
                                </button>
                            )}
                        </div>
                        
                        <div className="card-body">
                            <p><strong>POref:</strong> {poDetails.poref}</p>
                            <p>
                                <strong>POvendor: </strong>
                                {isEditing ? (
                                    <input
                                        type="text"
                                        value={newPovendor}
                                        onChange={(e) => setNewPovendor(e.target.value)}
                                    />
                                ) : (
                                    poDetails.povendor
                                )}
                            </p>
                            <p><strong>Username:</strong> {poDetails.username}</p>
                            <p><strong>Created Date:</strong> {formatDate(poDetails.createdDate)}</p>
                            <p><strong>PO Status: </strong> <span
                                style={{
                                    color: poDetails.postatus ? "green" : "red",
                                    fontWeight: "bold"
                                }}
                            >
                                {poDetails.postatus ? "Completed" : "Pending"}
                            </span></p>
                        </div>
                    </div>
                </div>
                <div className="col-md-6">
                    <div className="card mb-4">
                        <div className="card-header">Financial Tally</div>
                        <div className="card-body">
                        <p><strong>Invoice total:</strong> ${calculateInvoiceTotal()}</p>
                        <p><strong>Submission total:</strong> ${calculateSubmissionTotal()}</p>
                        <p><strong>PO balance:</strong> <span
                    style={{
                        color: calculatePOBalance() > 0 ? "black" : calculatePOBalance() >= 0 ? "green" : "red",
                        fontWeight: "bold"
                    }}
                >
                    ${calculatePOBalance()}
                </span></p>
                        {calculatePOBalance() === 0 && !poDetails.postatus && ( // Only show if PO balance is 0
                            <button className="btn btn-success" onClick={updatePOStatusAndReload}>
                            Update PO Status and Reload
                            </button>
                        )}
                        </div>
                    </div>
                </div>
                <div className="col-md-6">
                    <div className="card mb-4">
                        <div className="card-header d-flex justify-content-between align-items-center ">
                            <div>PO Invoice</div>
                        {/* <button className="btn btn-sm btn-primary" onClick={() => console.log('Create Invoice')}>
                            Create Invoice
                        </button> */}
                        {isCreatingInvoice?(
                            <>
                            <span>
                            <button
                            className="btn btn-sm btn-success"
                            onClick={handleCreateInvoiceClick}
                        >Create</button>
                        <button
                            className="btn btn-sm btn-danger"
                            onClick={handleCancelInvoiceClick}
                        >Cancel</button></span>
                        </>
                        ):(
                            <button
                            className="btn btn-sm btn-success"
                            onClick={handleCreateInvoice}
                        >Create Invoice</button>
                        
                        )}
                        </div>
                        <div className="card-body">
                        <table className="table">
                            <thead>
                                <tr>
                                    <th className="text-center">Name</th>
                                    <th className="text-center">Detail</th>
                                    <th className="text-center">Amount</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                {/* Render input fields for new invoice */}
                                {isCreatingInvoice && (
                                    <tr>
                                        <td className="text-center">
                                            <input
                                                type="text"
                                                value={newInvoiceName}
                                                onChange={(e) =>
                                                    setNewInvoiceName(e.target.value)
                                                }
                                            />
                                        </td>
                                        <td className="text-center">
                                            <input
                                                type="text"
                                                value={newInvoiceDetail}
                                                onChange={(e) =>
                                                    setNewInvoiceDetail(e.target.value)
                                                }
                                            />
                                        </td>
                                        <td className="text-center">
                                            <input
                                                type="number"
                                                value={newInvoiceAmount}
                                                onChange={(e) =>
                                                    setNewInvoiceAmount(e.target.value)
                                                }
                                            />
                                        </td>
                                        <td></td>
                                    </tr>
                                )}
                                {/* Render existing invoice rows */}
                                {invoiceDetails.map((invoice, index) => (
                                    <tr key={index}>
                                        <td className="text-center">{invoice.invName}</td>
                                        <td className="text-center">{invoice.invDetail}</td>
                                        <td className="text-center">{invoice.invAmount}</td>
                                        <td className="text-center">
                                        {!isCreatingInvoice && ( // Conditionally render Delete button
                                            <button
                                                className="btn btn-sm btn-danger"
                                                onClick={() => handleDeleteInvoice(invoice.id)}
                                            >
                                                Delete
                                            </button>
                                        )}
                            </td>
                                    </tr>
                                ))}
                            </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div className="col-md-6">
    <div className="card mb-4">
        <div className="card-header d-flex justify-content-between align-items-center">
            <div>PO Submissions</div>
            {isCreatingSubmission ? (
                <span>
                    <button
                        className="btn btn-sm btn-success"
                        onClick={handleCreateSubmissionClick}
                    >
                        Create
                    </button>
                    <button
                        className="btn btn-sm btn-danger"
                        onClick={handleCancelSubmissionClick}
                    >
                        Cancel
                    </button>
                </span>
            ) : (
                <button
                    className="btn btn-sm btn-success"
                    onClick={handleCreateSubmission}
                >
                    Create Submission
                </button>
            )}
        </div>
        <div className="card-body">
            <table className="table">
                <thead>
                    <tr>
                        <th className="text-center">Name</th>
                        <th className="text-center">Amount</th>
                        {!isCreatingSubmission && <th></th>} {/* Conditional rendering for Delete button column */}
                    </tr>
                </thead>
                <tbody>
                    {isCreatingSubmission && (
                        <tr>
                            <td className="text-center">
                                <input
                                    type="text"
                                    value={newSubmissionName}
                                    onChange={(e) =>
                                        setNewSubmissionName(e.target.value)
                                    }
                                />
                            </td>
                            <td className="text-center">
                                <input
                                    type="text"
                                    value={newSubmissionAmount}
                                    onChange={(e) =>
                                        setNewSubmissionAmount(e.target.value)
                                    }
                                />
                            </td>
                        </tr>
                    )}
                    {submissionDetails.map((submissions, index) => (
                        <tr key={index}>
                            <td className="text-center">{submissions.username}</td>
                            <td className="text-center">{submissions.subamount}</td>
                            {!isCreatingSubmission && ( // Conditionally render Delete button
                                <td className="text-center">
                                    <button
                                        className="btn btn-sm btn-danger"
                                        onClick={() => handleDeleteSubmission(submissions.id)}
                                    >
                                        Delete
                                    </button>
                                </td>
                                            )}
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    );
}

export default Po;
