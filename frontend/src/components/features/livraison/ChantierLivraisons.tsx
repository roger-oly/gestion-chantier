import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  Box,
  Button,
  Card,
  CardContent,
  CircularProgress,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Typography,
} from "@mui/material";

import {
  getLivraisonsByChantier,
  Livraison,
} from "../../../services/livraison.service";

interface ChantierLivraisonsProps {
  idChantier: number;
}

export default function ChantierLivraisons({
  idChantier,
}: ChantierLivraisonsProps) {
const navigate = useNavigate();
  const [livraisons, setLivraisons] = useState<Livraison[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadLivraisons = async () => {
      try {
        setLoading(true);
        setError(null);

        console.log("ID chantier envoyé :", idChantier);

const data = await getLivraisonsByChantier(idChantier);

console.log("Livraisons reçues :", data);

        setLivraisons(data);
      } catch (error) {
        console.error(
          "Erreur chargement des livraisons :",
          error
        );

        setError(
          "Impossible de charger les livraisons."
        );
      } finally {
        setLoading(false);
      }
    };

    loadLivraisons();
  }, [idChantier]);

  if (loading) {
    return (
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          py: 4,
        }}
      >
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Typography color="error">
        {error}
      </Typography>
    );
  }

  return (
    <Card>
      <CardContent>
        <Box
          sx={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            mb: 2,
          }}
        >
          <Typography variant="h6">
            Livraisons du chantier
          </Typography>

         <Button
  variant="contained"
  onClick={() =>
    navigate(
      `/chantiers/${idChantier}/livraisons/nouveau`
    )
  }
>
  + Ajouter
</Button>
        </Box>

        {livraisons.length === 0 ? (
          <Typography color="text.secondary">
            Aucune livraison enregistrée pour ce chantier.
          </Typography>
        ) : (
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>
                  Description
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
              {livraisons.map((livraison) => (
                <TableRow
                  key={livraison.idLivraison}
                >
                  <TableCell>
                    {livraison.description}
                  </TableCell>

                  <TableCell>
                    {new Date(
                      livraison.dateLivraison
                    ).toLocaleDateString("fr-FR")}
                  </TableCell>

                  <TableCell>
                    {livraison.statut}
                  </TableCell>

<TableCell>
  <Button
    size="small"
    variant="outlined"
    onClick={() =>
      navigate(
        `/livraisons/${livraison.idLivraison}`,
        {
          state: {
            from: "chantier",
            chantierId: idChantier,
          },
        }
      )
    }
  >
    Voir
  </Button>
</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        )}
      </CardContent>
    </Card>
  );
}