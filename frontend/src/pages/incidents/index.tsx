import { useEffect, useState } from "react";

import {
  Button,
  Card,
  CardContent,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Typography,
} from "@mui/material";

import { useNavigate } from "react-router-dom";

import { getIncidents } from "../../services/incident.service";
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

export default function Incidents() {

  const navigate = useNavigate();

  const [incidents, setIncidents] = useState<Incident[]>([]);

  useEffect(() => {
    const loadIncidents = async () => {
      try {
        const data = await getIncidents();
        setIncidents(data);
      } catch (error) {
        console.error(
          "Erreur chargement des incidents :",
          error
        );
      }
    };

    loadIncidents();
  }, []);

  return (
    <>
      <Typography
        variant="h4"
        gutterBottom
      >
        Incidents
      </Typography>

      <Button
        variant="contained"
        sx={{ mb: 2 }}
        onClick={() => navigate("/incidents/nouveau")}
      >
        + Déclarer
      </Button>

      <Card>
        <CardContent>
          <Table>

            <TableHead>
              <TableRow>

                <TableCell>
                  Type
                </TableCell>

                <TableCell>
                  Gravité
                </TableCell>

                <TableCell>
                  Date
                </TableCell>

                <TableCell>
                  Statut
                </TableCell>

                <TableCell>
                  Action
                </TableCell>

              </TableRow>
            </TableHead>

            <TableBody>

              {incidents.map((incident) => (

                <TableRow
                  key={incident.idIncident}
                >

                  <TableCell>
                    {incident.type}
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
        </CardContent>
      </Card>
    </>
  );
}