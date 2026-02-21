import React, { useEffect, useState } from "react";
import {
  Typography,
  Box,
  CircularProgress,
  Card,
  CardContent,
  Divider,
  List,
  ListItem,
  Stack,
  Chip,
  Button
} from "@mui/material";

import FitnessCenterIcon from "@mui/icons-material/FitnessCenter";
import TipsAndUpdatesIcon from "@mui/icons-material/TipsAndUpdates";
import TrendingUpIcon from "@mui/icons-material/TrendingUp";
import SecurityIcon from "@mui/icons-material/Security";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";

import { useParams, useNavigate } from "react-router-dom";
import { getActivityDetail } from "../services/api";

/* ---------- ICON MAP ---------- */

const iconMap = {
  Improvements: <TrendingUpIcon color="success" />,
  Suggestions: <TipsAndUpdatesIcon color="warning" />,
  "Safety Guidelines": <SecurityIcon color="error" />
};

/* ---------- HIGHLIGHT KEYS ---------- */

const highlightKeys = [
  "Calories Burned",
  "Overall",
  "Pace",
  "Heart Rate"
];

/* ---------- TEXT SPLITTER + HIGHLIGHT ---------- */

const HighlightText = ({ text }) => {

  if (!text.includes(":"))
    return (
      <Typography variant="body1" sx={{ lineHeight: 1.8 }}>
        • {text}
      </Typography>
    );

  const [head, ...rest] = text.split(":");
  const body = rest.join(":");
  const shouldHighlight = highlightKeys.includes(head.trim());

  return (
    <Typography variant="body1" sx={{ lineHeight: 1.8 }}>
      <Box
        component="span"
        sx={{
          fontWeight: 700,
          px: shouldHighlight ? 1 : 0,
          py: shouldHighlight ? 0.3 : 0,
          borderRadius: shouldHighlight ? 1 : 0,
          bgcolor: shouldHighlight ? "primary.light" : "transparent",
          color: shouldHighlight ? "primary.contrastText" : "inherit"
        }}
      >
        {head}:
      </Box>{" "}
      {body}
    </Typography>
  );
};

/* ---------- SECTION ---------- */

const Section = ({ title, items }) => {
  if (!items?.length) return null;

  return (
    <Box sx={{ mt: 4 }}>
      <Stack direction="row" spacing={1.5} alignItems="center" mb={1}>
        {iconMap[title]}
        <Typography variant="h5" sx={{ fontWeight: 700 }}>
          {title}
        </Typography>
      </Stack>

      <Divider sx={{ mb: 2 }} />

      <List dense>
        {items.map((item, i) => (
          <ListItem key={i} sx={{ pl: 0, py: 0.8 }}>
            <HighlightText text={item} />
          </ListItem>
        ))}
      </List>
    </Box>
  );
};

/* ---------- MAIN ---------- */

const ActivityDetail = () => {

  const { id } = useParams();
  const navigate = useNavigate();   // ⭐ added
  const [activity, setActivity] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {

    const fetchActivityDetail = async () => {
      try {
        const res = await getActivityDetail(id);
        setActivity(res.data);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchActivityDetail();

  }, [id]);

  if (loading)
    return (
      <Box sx={{ display: "flex", justifyContent: "center", mt: 12 }}>
        <CircularProgress size={40} />
      </Box>
    );

  if (!activity)
    return (
      <Typography align="center" sx={{ mt: 12 }} variant="h6">
        No activity found
      </Typography>
    );

  return (

    <Box sx={{ maxWidth: 920, mx: "auto", mt: 5, px: 2 }}>

      {/* ⭐ BACK BUTTON */}
      <Button
        startIcon={<ArrowBackIcon />}
        onClick={() => navigate(-1)}
        sx={{
          mb: 2,
          borderRadius: 3,
          textTransform: "none",
          fontWeight: 600
        }}
      >
        Back
      </Button>

      <Card elevation={3}>
        <CardContent sx={{ p: 4 }}>

          {/* HEADER */}
          <Stack direction="row" spacing={1.5} alignItems="center" mb={3}>
            <FitnessCenterIcon color="primary" sx={{ fontSize: 34 }} />

            <Typography variant="h4" sx={{ fontWeight: 800 }}>
              {activity.activityType || "General"} Activity
            </Typography>

            <Chip
              label="AI Plan"
              size="small"
              color="primary"
              variant="outlined"
              sx={{ ml: 1, fontWeight: 600 }}
            />
          </Stack>

          <Divider sx={{ mb: 3 }} />

          {/* RECOMMENDATION */}
          {activity.recommendation && (
            <Stack spacing={1.5} sx={{ mb: 3 }}>
              {activity.recommendation
                .split("\n")
                .filter(Boolean)
                .map((line, i) => (
                  <HighlightText key={i} text={line} />
                ))}
            </Stack>
          )}

          <Section title="Improvements" items={activity.improvement} />
          <Section title="Suggestions" items={activity.suggestion} />
          <Section title="Safety Guidelines" items={activity.safety} />

        </CardContent>
      </Card>
    </Box>

  );
};

export default ActivityDetail;