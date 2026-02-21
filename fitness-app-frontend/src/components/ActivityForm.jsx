import {
  Box,
  Button,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  TextField,
  Typography,
  Paper,
  Stack,
  InputAdornment
} from "@mui/material";

import DirectionsRunIcon from "@mui/icons-material/DirectionsRun";
import LocalFireDepartmentIcon from "@mui/icons-material/LocalFireDepartment";
import FitnessCenterIcon from "@mui/icons-material/FitnessCenter";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";

import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { addActivity } from "../services/api";

const ActivityForm = ({ onActivityAdded }) => {

  const navigate = useNavigate();   // ⭐ for back navigation

  const [activity, setActivity] = useState({
    type: "RUNNING",
    duration: "",
    caloriesBurned: "",
    additionalMetrics: {}
  });

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      await addActivity(activity);
      onActivityAdded?.();

      setActivity({
        type: "RUNNING",
        duration: "",
        caloriesBurned: "",
        additionalMetrics: {}
      });

    } catch (error) {
      console.error(error);
    }
  };

  return (

    <Paper
      elevation={8}
      sx={{
        p: 4,
        mb: 5,
        borderRadius: 4,
        background:
          "linear-gradient(135deg, rgba(255,255,255,0.95), rgba(255,255,255,0.85))",
        backdropFilter: "blur(12px)",
      }}
    >

      {/* ⭐ BACK BUTTON */}
      <Button
        startIcon={<ArrowBackIcon />}
        onClick={() => navigate('/')}
        sx={{
          mb: 2,
          borderRadius: 3,
          textTransform: "none",
          fontWeight: 600,
          alignSelf: "flex-start",
          "&:hover": { transform: "translateX(-3px)" }
        }}
      >
        Back
      </Button>

      {/* HEADER */}
      <Stack direction="row" spacing={1.5} alignItems="center" mb={3}>
        <FitnessCenterIcon color="primary" />
        <Typography variant="h5" fontWeight={800}>
          Add New Activity
        </Typography>
      </Stack>

      <Box component="form" onSubmit={handleSubmit}>

        {/* TYPE */}
        <FormControl fullWidth sx={{ mb: 3 }}>
          <InputLabel>Activity Type</InputLabel>

          <Select
            value={activity.type}
            label="Activity Type"
            onChange={(e) =>
              setActivity({ ...activity, type: e.target.value })
            }
            sx={{ borderRadius: 3 }}
          >
            <MenuItem value="RUNNING">🏃 Running</MenuItem>
            <MenuItem value="WALKING">🚶 Walking</MenuItem>
            <MenuItem value="CYCLING">🚴 Cycling</MenuItem>
            <MenuItem value="RIDING">🏍 Riding</MenuItem>
            <MenuItem value="GYM">🏋️ Gym</MenuItem>
            <MenuItem value="CARDIO">❤️ Cardio</MenuItem>
          </Select>
        </FormControl>

        {/* DURATION */}
        <TextField
          fullWidth
          label="Duration (Minutes)"
          type="number"
          sx={{ mb: 3 }}
          value={activity.duration}
          onChange={(e) =>
            setActivity({ ...activity, duration: e.target.value })
          }
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <DirectionsRunIcon color="primary" />
              </InputAdornment>
            ),
            sx: { borderRadius: 3 }
          }}
        />

        {/* CALORIES */}
        <TextField
          fullWidth
          label="Calories Burned"
          type="number"
          sx={{ mb: 4 }}
          value={activity.caloriesBurned}
          onChange={(e) =>
            setActivity({ ...activity, caloriesBurned: e.target.value })
          }
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <LocalFireDepartmentIcon color="error" />
              </InputAdornment>
            ),
            sx: { borderRadius: 3 }
          }}
        />

        {/* SUBMIT */}
        <Button
          type="submit"
          fullWidth
          variant="contained"
          size="large"
          sx={{
            py: 1.6,
            borderRadius: 3,
            fontSize: 16,
            fontWeight: 700,
            background: "linear-gradient(45deg,#ff512f,#dd2476)",
            boxShadow: "0 8px 20px rgba(221,36,118,0.4)",
            "&:hover": {
              background: "linear-gradient(45deg,#ff512f,#c31432)",
              transform: "translateY(-2px)"
            }
          }}
        >
          Add Activity
        </Button>

      </Box>
    </Paper>
  );
};

export default ActivityForm;