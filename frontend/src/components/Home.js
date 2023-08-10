import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';

function Home() {
  const [pendingPOs, setPendingPOs] = useState([]);
  const [completedPOs, setCompletedPOs] = useState([]);
  const [poVendor, setPoVendor] = useState('');
    const [isModalOpen, setIsModalOpen] = useState(false);

    const openModal = () => {
        setIsModalOpen(true);
      };
      
      const closeModal = () => {
        setIsModalOpen(false);
        setPoVendor(''); // Clear the input when closing the modal
      };

      const handleCreatePO = async () => {
        const user = JSON.parse(localStorage.getItem('user'));
      
        const config = {
          headers: {
            Authorization: `Bearer ${user.accessToken}`
          }
        };
      
        const newPO = {
          povendor: poVendor
        };
      
        try {
          const response = await axios.post('http://localhost:8080/api/po/createPO', newPO, config);
          console.log('New PO created:', response.data);
          closeModal(); // Close the modal
          navigate(`/po/${response.data.poref}`); // Redirect to the newly created PO page
        } catch (error) {
          console.error('Error creating PO:', error);
        }
      };
      
      
  const user = JSON.parse(localStorage.getItem('user'));
  const navigate = useNavigate();

  const handleDelete = async (POref) => {
    const user = JSON.parse(localStorage.getItem('user'));

    const config = {
      headers: {
        Authorization: `Bearer ${user.accessToken}`
      }
    };

    try {
      await axios.delete(`http://localhost:8080/api/po/deletePO/${POref}`, config);
      // Remove the deleted PO from the state
      setPendingPOs(pendingPOs.filter(po => po.poref !== POref));
      setCompletedPOs(completedPOs.filter(po => po.poref !== POref));
      console.log(`PO with POref ${POref} has been deleted.`);
    } catch (error) {
      console.error(`Error deleting PO with POref ${POref}:`, error);
    }
  };

  function formatDate(dateString) {
    const date = new Date(dateString);
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear().toString().slice(-2);
    return `${day}${month}${year}`;
  }
  

//   // Check if the user is authenticated
//   const isAuthenticated = () => {
//     const user = JSON.parse(localStorage.getItem('user'));
//     return user && user.accessToken;
//   };

  useEffect(() => {
    const user = JSON.parse(localStorage.getItem('user'));
    console.log('User Roles:', user.roles); // Log user roles
    const config = {
      headers: {
        Authorization: `Bearer ${user.accessToken}`
      }
    };
  
    axios.get('http://localhost:8080/api/po/getPOsWithStatusFalse', config)
      .then(response => setPendingPOs(response.data))
      .catch(error => console.error('Error fetching pending POs:', error));
  
    axios.get('http://localhost:8080/api/po/getPOsWithStatusTrue', config)
      .then(response => setCompletedPOs(response.data))
      .catch(error => console.error('Error fetching completed POs:', error));
  }, []);

  const handleLogout = () => {
    // Remove user from local storage and navigate to root URL
    localStorage.removeItem('user');
    navigate('/');
  };

  const renderPOTable = (POs) => {
    if (!Array.isArray(POs)) {
      return <p>No data available</p>;
    }
  
    if (POs.length === 0) {
      return <p>No POs to display</p>;
    }
    return (
      <table className="table mt-4">
        <thead>
          <tr>
            <th>POref</th>
            <th>POvendor</th>
            <th>Username</th>
            <th>POdate</th>
            <th></th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {POs.map(po => (
            <tr key={po.poref}>
              <td>{po.poref}</td>
              <td>{po.povendor}</td>
              <td>{po.username}</td>
              <td>{formatDate(po.createdDate)}</td>
              <td>
              <Link to={`/po/${po.poref}`} className="btn btn-info btn-sm">
                View
              </Link>
            </td>
            <td>
  {(user.roles.includes('ROLE_MODERATOR') || user.username === po.username) && (
    <button
      className="btn btn-danger btn-sm"
      onClick={() => handleDelete(po.poref)}
    >
      Delete
    </button>
  )}
</td>


            </tr>
          ))}
        </tbody>
      </table>
    );
  };

  return (
    <div className="container mt-4">
      <div className="row">
        <div className="col-md-12 mb-4">
          <div className="card">
            <div className="card-body d-flex justify-content-between">
            <button onClick={openModal} className="btn btn-success mr-2">Create PO</button>
              <Link to="/pendingpo" className="btn btn-primary">View Pending POs</Link>
              <Link to="/completedpo" className="btn btn-primary">View Completed POs</Link>
              {user.roles.includes('ROLE_MODERATOR') && ( 
                <Link to="/users" className="btn btn-info">User Dashboard</Link>
              )}
              <button onClick={handleLogout} className="btn btn-danger">Logout</button>
            </div>
          </div>
        </div>
        {/* Modal */}
<div className={`modal ${isModalOpen ? 'show' : ''}`} tabIndex="-1" role="dialog" style={{ display: isModalOpen ? 'block' : 'none' }}>
  <div className="modal-dialog" role="document">
    <div className="modal-content">
      <div className="modal-header">
        <h5 className="modal-title">Create New PO</h5>
        <button type="button" className="close" onClick={closeModal} aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div className="modal-body">
        <div className="form-group">
          <label htmlFor="poVendor">PO Vendor</label>
          <input
            type="text"
            className="form-control"
            id="poVendor"
            value={poVendor}
            onChange={(e) => setPoVendor(e.target.value)}
          />
        </div>
      </div>
      <div className="modal-footer">
        <button type="button" className="btn btn-secondary" onClick={closeModal}>Cancel</button>
        <button type="button" className="btn btn-primary" onClick={handleCreatePO}>Create</button>
      </div>
    </div>
  </div>
</div>
        <div className="col-md-6">
          <div className="card">
            <div className="card-body">
              <div className="card-title">Pending POs</div>
              {renderPOTable(pendingPOs)}
            </div>
          </div>
        </div>
        <div className="col-md-6">
          <div className="card">
            <div className="card-body">
             <div className="card-title">Completed POs</div>
              {renderPOTable(completedPOs)}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Home;
