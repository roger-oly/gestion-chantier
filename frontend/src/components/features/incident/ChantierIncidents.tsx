import { useEffect, useState } from "react";
import {
  Box,
  Button,
  CircularProgress,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
} from "@mui/material";

import { Incident } from "../../../types/incident";
import { getIncidentsByChantier } from "../../../services/incident.service";
import { useNavigate } from "react-router-dom";

interface ChantierIncidentsProps {
  idChantier: number;
}

const formatDate = (date: string) => {
  return new Date(date).toLocaleString("fr-FR", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
};

const ChantierIncidents = ({
  idChantier,
}: ChantierIncidentsProps) => {
  const [incidents, setIncidents] = useState<Incident[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const navigate = useNavigate();

  useEffect(() => {
    const loadIncidents = async () => {
      try {
        setLoading(true);
        setError(null);

        const data = await getIncidentsByChantier(idChantier);

        setIncidents(data);
      } catch (err) {
        console.error(
          "Erreur chargement des incidents :",
          err
        );

        setError(
          "Impossible de charger les incidents du chantier."
        );
      } finally {
        setLoading(false);
      }
    };

    loadIncidents();
  }, [idChantier]);

  return (
    <Box sx={{ mt: 2 }}>

      {/* En-tête */}
      <Box
        sx={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          mb: 2,
        }}
      >
        <Typography variant="h6">
          Liste des incidents du chantier
        </Typography>

        <Button
          variant="contained"
          onClick={() =>
            navigate(`/incidents/nouveau?chantier=${idChantier}`)
          }
        >
          + Nouvel incident
        </Button>
      </Box>

      {/* Chargement */}
      {loading && (
        <Box
          sx={{
            display: "flex",
            justifyContent: "center",
            py: 4,
          }}
        >
          <CircularProgress />
        </Box>
      )}

      {/* Erreur */}
      {!loading && error && (
        <Typography color="error">
          {error}
        </Typography>
      )}

      {/* Aucun incident */}
      {!loading && !error && incidents.length === 0 && (
        <Paper
          sx={{
            p: 4,
            textAlign: "center",
          }}
        >
          <Typography color="text.secondary">
            Aucun incident renseigné pour ce chantier.
          </Typography>
        </Paper>
      )}

      {/* Liste */}
      {!loading && !error && incidents.length > 0 && (
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Type</TableCell>
                <TableCell>Description</TableCell>
                <TableCell>Gravité</TableCell>
                <TableCell>Date</TableCell>
                <TableCell>Statut</TableCell>
                <TableCell>Action</TableCell>
              </TableRow>
            </TableHead>

            <TableBody>
              {incidents.map((incident) => (
                <TableRow key={incident.idIncident}>

                  <TableCell>
                    {incident.type}
                  </TableCell>

                  <TableCell>
                    {incident.description}
                  </TableCell>

                  <TableCell>
                    {incident.gravite}
                  </TableCell>

                 <TableCell>
  {formatDate(incident.dateIncident)}
</TableCell>

                  <TableCell>
                    {incident.statut}
                  </TableCell>

                  <TableCell>
                   <Button
  size="small"
  onClick={() =>
    navigate(`/incidents/${incident.idIncident}`)
  }
>
  Voir
</Button>
                  </TableCell>

                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}

    </Box>
  );
};

export default ChantierIncidents;