import { Card, CardContent, Grid, Typography } from '@mui/material'
import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router';
import { getActivities } from '../services/api';

const ActivityList = () => {
  const [activities, setActivities] = useState([]);
  const navigate = useNavigate();

  const fetchActivities = async () => {
    try {
      const response = await getActivities();
      setActivities(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    fetchActivities();
  }, []);

  return (
    <Grid container spacing={3}>
      {activities.map((activity) => (
        <Grid item xs={12} sm={6} md={4} key={activity.id}>

          <Card
            onClick={() => navigate(`/activities/${activity.id}`)}
            sx={{
              cursor: "pointer",
              height: "100%",
              borderRadius: 3,
              transition: "0.3s",
              boxShadow: 3,
              "&:hover": {
                transform: "translateY(-6px)",
                boxShadow: 8,
              },
            }}
          >
            <CardContent sx={{ p: 3 }}>

              {/* Activity Type */}
              <Typography
                variant="h6"
                fontWeight="bold"
                gutterBottom
              >
                {activity.type}
              </Typography>

              {/* Duration */}
              <Typography
                variant="body2"
                sx={{ opacity: 0.8, mb: 1 }}
              >
                ⏱ Duration: {activity.duration} min
              </Typography>

              {/* Calories */}
              <Typography
                variant="body2"
                sx={{
                  color: "error.main",
                  fontWeight: 500
                }}
              >
                🔥 Calories Burned: {activity.caloriesBurned}
              </Typography>

            </CardContent>
          </Card>

        </Grid>
      ))}
    </Grid>
  )
}

export default ActivityList