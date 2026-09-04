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

import {
  getLivraisonById,
  Livraison,
} from "../../services/livraison.service";

export default function ShowLivraison() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const [livraison, setLivraison] =
    useState<Livraison | null>(null);

  const [loading, setLoading] = useState(true);
  const [error, setError] =
    useState<string | null>(null);

  useEffect(() => {
    async function loadLivraison() {
      if (!id) {
        setError(
          "Identifiant de la livraison introuvable."
        );
        setLoading(false);
        return;
      }

      try {
        const data = await getLivraisonById(
          Number(id)
        );

        setLivraison(data);
      } catch (error) {
        console.error(
          "Erreur chargement livraison :",
          error
        );

        setError(
          "Impossible de charger la livraison."
        );
      } finally {
        setLoading(false);
      }
    }

    loadLivraison();
  }, [id]);

  if (loading) {
    return (
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          p: 4,
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

  if (!livraison) {
    return (
      <Typography>
        Livraison introuvable.
      </Typography>
    );
  }

  return (
    <Box sx={{ p: 3 }}>

      <Button
        variant="outlined"
        sx={{ mb: 2 }}
        onClick={() =>
          navigate("/livraisons")
        }
      >
        ← Retour
      </Button>

      <Card>
        <CardContent>

          <Typography
            variant="h4"
            gutterBottom
          >
            Détails de la livraison
          </Typography>

          <Typography sx={{ mb: 2 }}>
            <strong>Description :</strong>{" "}
            {livraison.description}
          </Typography>

          <Typography sx={{ mb: 2 }}>
            <strong>Date de livraison :</strong>{" "}
            {livraison.dateLivraison}
          </Typography>

          <Typography sx={{ mb: 2 }}>
            <strong>Statut :</strong>{" "}
            {livraison.statut}
          </Typography>

          <Typography sx={{ mb: 2 }}>
            <strong>Chantier :</strong>{" "}
            {livraison.idChantier}
          </Typography>

          <Typography>
            <strong>Utilisateur :</strong>{" "}
            {livraison.idUtilisateur}
          </Typography>

        </CardContent>
      </Card>

      <Box
        sx={{
          display: "flex",
          gap: 2,
          mt: 2,
        }}
      >
        <Button
          variant="contained"
          onClick={() =>
            navigate(
              `/livraisons/${livraison.idLivraison}/modifier`
            )
          }
        >
          Modifier
        </Button>

        <Button
          variant="outlined"
          color="error"
        >
          Supprimer
        </Button>
      </Box>

    </Box>
  );
}