import { useEffect, useState } from "react";
import {
  Box,
  Button,
  Card,
  CardContent,
  MenuItem,
  TextField,
  Typography,
} from "@mui/material";
import { useNavigate, useSearchParams } from "react-router-dom";

import { createIncident } from "../../services/incident.service";
import { getChantiers } from "../../services/chantier.service";
import { getUser } from "../../services/storage.service";

interface Chantier {
  idChantier: number;
  nom: string;
}

export default function CreateIncident() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  const chantierParam = searchParams.get("chantier");
  const chantierDepuisFiche = chantierParam !== null;
  const [chantiers, setChantiers] = useState<Chantier[]>([]);
  const [idChantier, setIdChantier] = useState(
    chantierParam ? Number(chantierParam) : ""
  );

  const [type, setType] = useState("");
  const [description, setDescription] = useState("");
  const [gravite, setGravite] = useState("");
  const [dateIncident, setDateIncident] = useState("");
  const [statut, setStatut] = useState("Ouvert");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    async function loadChantiers() {
      try {
        const data = await getChantiers();
        setChantiers(data);
      } catch (error) {
        console.error("Erreur chargement des chantiers :", error);
      }
    }

    loadChantiers();

    // Date/heure actuelle par défaut
    const now = new Date();
    const localDateTime = new Date(
      now.getTime() - now.getTimezoneOffset() * 60000
    )
      .toISOString()
      .slice(0, 16);

    setDateIncident(localDateTime);
  }, []);

  async function handleSubmit(event: React.FormEvent) {
    event.preventDefault();

    const user = getUser();

    if (!user) {
      alert("Utilisateur non connecté.");
      return;
    }

    if (!idChantier) {
      alert("Veuillez sélectionner un chantier.");
      return;
    }

    try {
      setLoading(true);

      await createIncident({
        type,
        description,
        gravite,
        dateIncident,
        statut,
        idChantier: Number(idChantier),
        idUtilisateur: user.idUtilisateur,
      });

      alert("Incident déclaré avec succès.");

      navigate("/incidents");
    } catch (error) {
      console.error("Erreur création incident :", error);
      alert("Impossible de déclarer l'incident.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <Box sx={{ p: 3 }}>
      <Card>
        <CardContent>
          <Typography variant="h5" gutterBottom>
            Déclarer un incident
          </Typography>

          <Box
            component="form"
            onSubmit={handleSubmit}
            sx={{
              display: "flex",
              flexDirection: "column",
              gap: 2,
              mt: 2,
            }}
          >
        <TextField
  select
  label="Chantier"
  value={idChantier}
  onChange={(e) =>
    setIdChantier(
      e.target.value ? Number(e.target.value) : ""
    )
  }
  required
  disabled={chantierDepuisFiche}
  helperText={
    chantierDepuisFiche
      ? "Chantier sélectionné depuis la fiche chantier."
      : undefined
  }
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
              label="Type d'incident"
              value={type}
              onChange={(e) => setType(e.target.value)}
              required
            />

            <TextField
              label="Description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              multiline
              rows={4}
              required
            />

            <TextField
              select
              label="Gravité"
              value={gravite}
              onChange={(e) => setGravite(e.target.value)}
              required
            >
              <MenuItem value="Faible">Faible</MenuItem>
              <MenuItem value="Moyenne">Moyenne</MenuItem>
              <MenuItem value="Élevée">Élevée</MenuItem>
              <MenuItem value="Critique">Critique</MenuItem>
            </TextField>

            <TextField
              label="Date et heure"
              type="datetime-local"
              value={dateIncident}
              onChange={(e) => setDateIncident(e.target.value)}
              InputLabelProps={{ shrink: true }}
              required
            />

            <TextField
              select
              label="Statut"
              value={statut}
              onChange={(e) => setStatut(e.target.value)}
              required
            >
              <MenuItem value="Ouvert">Ouvert</MenuItem>
              <MenuItem value="En cours">En cours</MenuItem>
              <MenuItem value="Résolu">Résolu</MenuItem>
              <MenuItem value="Clôturé">Clôturé</MenuItem>
            </TextField>

            <Box sx={{ display: "flex", gap: 2, mt: 2 }}>
              <Button
                type="submit"
                variant="contained"
                disabled={loading}
              >
                {loading ? "Enregistrement..." : "Déclarer l'incident"}
              </Button>

              <Button
                variant="outlined"
                onClick={() => navigate("/incidents")}
              >
                Annuler
              </Button>
            </Box>
          </Box>
        </CardContent>
      </Card>
    </Box>
  );
}