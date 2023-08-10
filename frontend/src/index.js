import React from 'react';
import { render } from 'react-dom';
import { BrowserRouter as Router, Routes, Route, useNavigate } from 'react-router-dom';

import Login from './components/Login';
import Home from './components/Home';
import Pending from './components/Pending';
import Completed from './components/Completed';
import Po from './components/Po';
import Users from './components/Users';
import 'bootstrap/dist/css/bootstrap.min.css';



function App(){
  return(
    <Router>
      <Routes>
        <Route path="/*" element={<Login/>}/>
        <Route path="/home" element={<Home/>}/>
        <Route path="/pendingpo" element={<Pending/>}/>
        <Route path="/completedpo" element={<Completed/>}/>
        <Route path="/users" element={<Users/>}/>
        <Route path="/po/:POref" element={<Po/>}/>
      </Routes>
    </Router>
  );
}

render(<App />, document.getElementById('root'));
