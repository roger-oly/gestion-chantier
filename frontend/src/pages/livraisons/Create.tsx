import { useEffect, useState } from "react";
import {
  Box,
  Button,
  MenuItem,
  TextField,
  Typography,
} from "@mui/material";
import { useNavigate, useParams } from "react-router-dom";

import { createLivraison } from "../../services/livraison.service";
import {
  getChantiers,
} from "../../services/chantier.service";
import { useAuth } from "../../contexts/AuthContext";

import type { Chantier } from "../../types/chantier";

export default function CreateLivraison() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { user } = useAuth();

  const [chantiers, setChantiers] = useState<Chantier[]>([]);
  const [idChantier, setIdChantier] = useState(
    id ? Number(id) : ""
  );

  const [description, setDescription] = useState("");
  const [dateLivraison, setDateLivraison] = useState("");
  const [statut, setStatut] = useState("En attente");

  const [loading, setLoading] = useState(false);
  const [loadingChantiers, setLoadingChantiers] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadChantiers = async () => {
      try {
        const data = await getChantiers();
        setChantiers(data);
      } catch (error) {
        console.error(
          "Erreur chargement des chantiers :",
          error
        );

        setError(
          "Impossible de charger les chantiers."
        );
      } finally {
        setLoadingChantiers(false);
      }
    };

    loadChantiers();
  }, []);

  const handleSubmit = async (
    event: React.FormEvent
  ) => {
    event.preventDefault();

    if (!user) {
      setError("Utilisateur non connecté.");
      return;
    }

    if (!idChantier) {
      setError("Veuillez sélectionner un chantier.");
      return;
    }

    try {
      setLoading(true);
      setError(null);

      await createLivraison({
        description,
        dateLivraison,
        statut,
        idChantier: Number(idChantier),
        idUtilisateur: user.idUtilisateur,
      });

      if (id) {
        navigate(`/chantiers/${id}?tab=5`);
      } else {
        navigate("/livraisons");
      }
    } catch (error) {
      console.error(
        "Erreur création livraison :",
        error
      );

      setError(
        "Impossible de créer la livraison."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box sx={{ p: 3 }}>
      <Typography
        variant="h4"
        gutterBottom
      >
        Nouvelle livraison
      </Typography>

      <Box
        component="form"
        onSubmit={handleSubmit}
        sx={{
          maxWidth: 600,
          display: "flex",
          flexDirection: "column",
          gap: 2,
        }}
      >

        <TextField
          select
          label="Chantier"
          value={idChantier}
          onChange={(event) =>
            setIdChantier(
              event.target.value
                ? Number(event.target.value)
                : ""
            )
          }
          required
          fullWidth
          disabled={Boolean(id) || loadingChantiers}
        >
          {chantiers.map((chantier) => (
            <MenuItem
              key={chantier.idChantier}
              value={chantier.idChantier}
            >
              {chantier.nom}
            </MenuItem>
          ))}
        </TextField>

        <TextField
          label="Description"
          value={description}
          onChange={(event) =>
            setDescription(event.target.value)
          }
          required
          fullWidth
        />

        <TextField
          label="Date de livraison"
          type="date"
          value={dateLivraison}
          onChange={(event) =>
            setDateLivraison(event.target.value)
          }
          InputLabelProps={{
            shrink: true,
          }}
          required
          fullWidth
        />

        <TextField
          select
          label="Statut"
          value={statut}
          onChange={(event) =>
            setStatut(event.target.value)
          }
          fullWidth
        >
          <MenuItem value="En attente">
            En attente
          </MenuItem>

          <MenuItem value="En cours">
            En cours
          </MenuItem>

          <MenuItem value="Livrée">
            Livrée
          </MenuItem>
        </TextField>

        {error && (
          <Typography color="error">
            {error}
          </Typography>
        )}

        <Box
          sx={{
            display: "flex",
            gap: 2,
          }}
        >
          <Button
            type="submit"
            variant="contained"
            disabled={
              loading ||
              loadingChantiers
            }
          >
            {loading
              ? "Enregistrement..."
              : "Enregistrer"}
          </Button>

          <Button
            variant="outlined"
            onClick={() =>
              id
                ? navigate(`/chantiers/${id}?tab=5`)
                : navigate("/livraisons")
            }
          >
            Annuler
          </Button>
        </Box>

      </Box>
    </Box>
  );
}