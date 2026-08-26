import { useEffect, useState } from "react";
import {
  Box,
  Button,
  Card,
  CardContent,
  CircularProgress,
  Typography,
} from "@mui/material";
import { useNavigate, useParams } from "react-router-dom";

import { getIncidentById } from "../../services/incident.service";
import { Incident } from "../../types/incident";

    const formatDate = (date: string) => {
  return new Date(date).toLocaleString("fr-FR", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
};

export default function ShowIncident() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const [incident, setIncident] = useState<Incident | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {

    const loadIncident = async () => {
      try {
        if (!id) {
          throw new Error("Identifiant de l'incident manquant.");
        }

        const data = await getIncidentById(Number(id));

        setIncident(data);
      } catch (error) {
        console.error(
          "Erreur chargement de l'incident :",
          error
        );

        setError(
          "Impossible de charger les informations de l'incident."
        );
      } finally {
        setLoading(false);
      }
    };

    loadIncident();
  }, [id]);

  if (loading) {
    return (
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          py: 5,
        }}
      >
        <CircularProgress />
      </Box>
    );
  }

  if (error || !incident) {
    return (
      <Box sx={{ p: 3 }}>
        <Typography color="error">
          {error ?? "Incident introuvable."}
        </Typography>

        <Button
          variant="outlined"
          sx={{ mt: 2 }}
          onClick={() => navigate("/incidents")}
        >
          Retour aux incidents
        </Button>
      </Box>
    );
  }

  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h4" gutterBottom>
        Détail de l'incident
      </Typography>

      <Button
  variant="contained"
  onClick={() => navigate(`/incidents/${incident.idIncident}/modifier`)}
>
  Modifier
</Button>

      <Card>
        <CardContent>

          <Typography variant="h6" gutterBottom>
            {incident.type}
          </Typography>

          <Typography sx={{ mb: 2 }}>
            <strong>Description :</strong>{" "}
            {incident.description}
          </Typography>

          <Typography sx={{ mb: 2 }}>
            <strong>Gravité :</strong>{" "}
            {incident.gravite}
          </Typography>

          <Typography sx={{ mb: 2 }}>
  <strong>Date :</strong>{" "}
  {formatDate(incident.dateIncident)}
</Typography>

<Typography sx={{ mb: 2 }}>
  <strong>Date de modification :</strong>{" "}
  {formatDate(incident.dateModification)}
</Typography>

          <Typography sx={{ mb: 2 }}>
            <strong>Statut :</strong>{" "}
            {incident.statut}
          </Typography>

          <Button
            variant="outlined"
            onClick={() => navigate("/incidents")}
          >
            Retour
          </Button>

        </CardContent>
      </Card>
    </Box>
  );
}