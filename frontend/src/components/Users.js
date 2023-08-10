import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

function Users() {
  const [users, setUsers] = useState([]);
  const [currentUser, setCurrentUser] = useState(null);
  const [newUser, setNewUser] = useState({
    username: '',
    email: '',
    password: '',
    roles: []
  });
  const [editMode, setEditMode] = useState(false);
  const [isSalesChecked, setIsSalesChecked] = useState(false);
    const [isFinanceChecked, setIsFinanceChecked] = useState(false);
    const [isModeratorChecked, setIsModeratorChecked] = useState(false);


  // Role name mapping
  const roleMappings = {
    ROLE_SALES: 'Sales',
    ROLE_FINANCE: 'Finance',
    ROLE_MODERATOR: 'Moderator',
    // Add more mappings as needed
  };

  // Fetch user data from local storage on component mount
  useEffect(() => {
    const user = JSON.parse(localStorage.getItem('user'));
    setCurrentUser(user);
  }, []);

  const fetchUsers = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/auth/getallaccounts'); // Replace with your API endpoint
      setUsers(response.data);
    } catch (error) {
      console.error('Error fetching users:', error);
    }
  };

  const handleDeleteUser = async (userId) => {
    try {
      await axios.delete(`http://localhost:8080/api/auth/deleteaccount/${userId}`); // Replace with your API endpoint
      fetchUsers(); // Fetch updated user data
    } catch (error) {
      console.error('Error deleting user:', error);
    }
  };

  const handleCreateUser = async () => {
    const user = JSON.parse(localStorage.getItem('user'));
    
    const config = {
      headers: {
        Authorization: `Bearer ${user.accessToken}`
      }
    };
    
    const roles = [];
    if (isSalesChecked) roles.push('sales');
    if (isFinanceChecked) roles.push('finance');
    if (isModeratorChecked) roles.push('mod');
  
    const newUserWithRoles = {
      ...newUser,
      roles: roles
    };
  
    try {
      const response = await axios.post('http://localhost:8080/api/auth/signup', newUserWithRoles, config);
      console.log('New user created:', response.data);
      fetchUsers(); // Fetch updated user data
      setNewUser({
        username: '',
        email: '',
        password: '',
        roles: []
      });
      setIsSalesChecked(false); // Reset checkbox states
      setIsFinanceChecked(false);
      setIsModeratorChecked(false);
      setEditMode(false); // Exit edit mode after creating user
      window.location.reload();
    } catch (error) {
      console.error('Error creating user:', error);
    }
  };
  
  
  

  // Toggle edit mode
  const toggleEditMode = () => {
    setEditMode(!editMode);
  };

  const handleNewUserChange = (event) => {
    const { name, value, type, checked } = event.target;

    if (type === 'checkbox') {
      // Handle checkbox changes
      if (checked) {
        setNewUser((prevUser) => ({
          ...prevUser,
          roles: [...prevUser.roles, value]
        }));
      } else {
        setNewUser((prevUser) => ({
          ...prevUser,
          roles: prevUser.roles.filter((role) => role !== value)
        }));
      }
    } else {
      // Handle other input changes
      setNewUser((prevUser) => ({
        ...prevUser,
        [name]: value
      }));
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  return (
    <div className="container mt-4">
        <div className="mb-3">
        <Link to="/home" className="btn btn-primary">
          Back to Home
        </Link>
      </div>
    {currentUser ? (
      <div className="card">
        
        <div className="card-header d-flex justify-content-between align-items-center">
        <div>
              User Dashboard
            </div>
            <div>
            <button
              className={`btn btn-sm ${editMode ? 'btn-danger' : 'btn-success'}`}
              onClick={toggleEditMode}
            >
              {editMode ? 'Cancel' : 'Create User'}
            </button>
            </div>
        </div>
        <div className="card-body">
          {editMode && (
            <table className="table">
              <tbody>
                <tr>
                  <td>
                    <input
                      type="text"
                      name="username"
                      value={newUser.username}
                      onChange={handleNewUserChange}
                    />
                  </td>
                  <td>
                    <input
                      type="email"
                      name="email"
                      value={newUser.email}
                      onChange={handleNewUserChange}
                    />
                  </td>
                  <td>
                    <input
                      type="password"
                      name="password"
                      value={newUser.password}
                      onChange={handleNewUserChange}
                    />
                  </td>
                  <td>
                  <label>
  <input
    type="checkbox"
    name="roles"
    value="sales"
    checked={isSalesChecked}
    onChange={() => setIsSalesChecked(!isSalesChecked)}
  />
  Sales
</label>
<br />
<label>
  <input
    type="checkbox"
    name="roles"
    value="ROLE_FINANCE"
    checked={isFinanceChecked}
    onChange={() => setIsFinanceChecked(!isFinanceChecked)}
  />
  Finance
</label>
<br />
<label>
  <input
    type="checkbox"
    name="roles"
    value="ROLE_MODERATOR"
    checked={isModeratorChecked}
    onChange={() => setIsModeratorChecked(!isModeratorChecked)}
  />
  Moderator
</label>


                  </td>
                  <td>
                    <button className="btn btn-sm btn-success"onClick={handleCreateUser}>Create</button>
                  </td>
                </tr>
              </tbody>
            </table>
          )}
          <table className="table">
            <tbody>
              {users.map((user) => (
                <tr key={user.id}>
                  <td>{user.username}</td>
                  <td>{user.email}</td>
                  <td>
                    {user.roles.map((role, index) => (
                      <span key={index}>
                        {roleMappings[role.name]}
                        {index !== user.roles.length - 1 && ', '}
                      </span>
                    ))}
                  </td>
                  <td>
                  {user.username !== currentUser.username && (
        <button
          className="btn btn-sm btn-danger"
          onClick={() => handleDeleteUser(user.userId)}
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
    ) : (
      <div className="alert alert-danger">
        You are not authenticated. Please log in to view this page.
      </div>
    )}
  </div>
  );
}

export default Users;
