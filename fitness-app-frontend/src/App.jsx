import { Button, Box } from '@mui/material';
import './App.css'
import { BrowserRouter as Router, Navigate, Route, Routes } from "react-router-dom";
import { AuthContext } from 'react-oauth2-code-pkce';
import { useDispatch } from 'react-redux';
import { setCredential } from './store/authSlice';
import ActivityForm from './components/ActivityForm';
import ActivityList from './components/ActivityList';
import ActivityDetail from './components/ActivityDetail';
import { useContext, useState, useEffect } from 'react';

const ActivitiesPage = () => {
  return (
    <Box component="section" sx={{ p: 2, border: '1px dashed grey' }}>
      <ActivityForm onActivitesAdded={() => window.location.reload()} />
      <ActivityList />
    </Box>
  );
}

function App() {

  const { token, tokenData, logIn } = useContext(AuthContext);
  const dispatch = useDispatch();
  const [authReady, setAuthReady] = useState(false);

  useEffect(() => {
    if (token) {
      dispatch(setCredential({ token, user: tokenData }))
      setAuthReady(true);
    }
  }, [token, tokenData, dispatch]);

  return (
    <Router>

      {!token ? (
        <Button
          variant="contained"
          onClick={() => logIn()}
        >
          Login
        </Button>
      ) : (

        <Box component="section" sx={{ p: 2, border: '1px dashed grey' }}>
          <Routes>

            <Route path="/activities" element={<ActivitiesPage />} />
            <Route path="/activities/:id" element={<ActivityDetail />} />

            <Route
              path="/"
              element={<Navigate to="/activities" replace />}
            />

          </Routes>
        </Box>

      )}

    </Router>
  )
}

export default App;