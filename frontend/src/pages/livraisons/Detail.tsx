import { useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import {
  Box,
  Button,
  Card,
  CardContent,
  CircularProgress,
  Divider,
  Typography,
} from "@mui/material";

import {
  getLivraisonById,
  Livraison,
} from "../../services/livraison.service";

export default function LivraisonDetail() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const location = useLocation();

  const [livraison, setLivraison] =
    useState<Livraison | null>(null);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function loadLivraison() {
      if (!id) {
        setError("Identifiant de la livraison introuvable.");
        setLoading(false);
        return;
      }

      try {
        const data = await getLivraisonById(Number(id));

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

  function handleRetour() {
    const state = location.state as {
      from?: string;
      chantierId?: number;
    } | null;

    if (
      state?.from === "chantier" &&
      state?.chantierId
    ) {
      navigate(
        `/chantiers/${state.chantierId}?tab=5`
      );

      return;
    }

    navigate("/livraisons");
  }

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

  if (!livraison) {
    return (
      <Typography>
        Livraison introuvable.
      </Typography>
    );
  }

  return (
    <Box sx={{ p: 3 }}>

      {/* Retour */}
      <Button
        variant="outlined"
        onClick={handleRetour}
        sx={{ mb: 3 }}
      >
        ← Retour
      </Button>

      <Typography
        variant="h4"
        gutterBottom
      >
        Détails de la livraison
      </Typography>

      <Card>
        <CardContent>

          <Typography
            variant="h6"
            gutterBottom
          >
            Informations
          </Typography>

          <Divider sx={{ mb: 3 }} />

          <Typography sx={{ mb: 2 }}>
            <strong>Description :</strong>{" "}
            {livraison.description}
          </Typography>

          <Typography sx={{ mb: 2 }}>
            <strong>Date de livraison :</strong>{" "}
            {new Date(
              livraison.dateLivraison
            ).toLocaleDateString("fr-FR")}
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

      {/* Actions */}
      <Box
        sx={{
          display: "flex",
          gap: 2,
          mt: 3,
        }}
      >
        <Button
          variant="contained"
          onClick={() =>
            navigate(
              `/livraisons/${livraison.idLivraison}/modifier`,
              {
                state: location.state,
              }
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