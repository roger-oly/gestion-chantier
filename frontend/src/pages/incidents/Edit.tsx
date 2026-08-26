import { useEffect, useState } from "react";
import {
  Box,
  Button,
  Card,
  CardContent,
  CircularProgress,
  MenuItem,
  TextField,
  Typography,
} from "@mui/material";
import { useNavigate, useParams } from "react-router-dom";

import {
  getIncidentById,
  updateIncident,
} from "../../services/incident.service";

import { getChantiers } from "../../services/chantier.service";
import { getUser } from "../../services/storage.service";

interface Chantier {
  idChantier: number;
  nom: string;
}

export default function EditIncident() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const [chantiers, setChantiers] = useState<Chantier[]>([]);

  const [idChantier, setIdChantier] = useState<number | "">("");
  const [type, setType] = useState("");
  const [description, setDescription] = useState("");
  const [gravite, setGravite] = useState("");
  const [dateIncident, setDateIncident] = useState("");
  const [statut, setStatut] = useState("");

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);

  /*
   * Chargement de l'incident et des chantiers
   */
  useEffect(() => {
    const loadData = async () => {
      try {
        setLoading(true);
        setError(null);

        if (!id) {
          throw new Error("Identifiant de l'incident manquant.");
        }

        const [incident, chantiersData] = await Promise.all([
          getIncidentById(Number(id)),
          getChantiers(),
        ]);

        setChantiers(chantiersData);

        setType(incident.type);
        setDescription(incident.description);
        setGravite(incident.gravite);
        setStatut(incident.statut);

        /*
         * Le backend ne renvoie pas le chantier dans le JSON
         * car la relation est @JsonIgnore.
         *
         * Nous allons donc récupérer le chantier autrement
         * si nécessaire.
         */
        if (incident.idChantier) {
          setIdChantier(incident.idChantier);
        }

        /*
         * datetime-local attend le format :
         * YYYY-MM-DDTHH:mm
         */
        if (incident.dateIncident) {
          setDateIncident(
            incident.dateIncident.slice(0, 16)
          );
        }
      } catch (err) {
        console.error(
          "Erreur chargement de l'incident :",
          err
        );

        setError(
          "Impossible de charger l'incident."
        );
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, [id]);

  /*
   * Enregistrement des modifications
   */
  async function handleSubmit(
    event: React.FormEvent
  ) {
    event.preventDefault();

    const user = getUser();

    if (!user) {
      alert("Utilisateur non connecté.");
      return;
    }

    if (!id) {
      alert("Identifiant de l'incident manquant.");
      return;
    }

    if (!idChantier) {
      alert("Veuillez sélectionner un chantier.");
      return;
    }

    try {
      setSaving(true);

      await updateIncident(
        Number(id),
        {
          type,
          description,
          gravite,
          dateIncident,
          statut,
          idChantier: Number(idChantier),
          idUtilisateur: user.idUtilisateur,
        }
      );

      alert(
        "Incident modifié avec succès."
      );

      navigate(`/incidents/${id}`);
    } catch (err) {
      console.error(
        "Erreur modification incident :",
        err
      );

      alert(
        "Impossible de modifier l'incident."
      );
    } finally {
      setSaving(false);
    }
  }

  /*
   * Chargement
   */
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

  /*
   * Erreur
   */
  if (error) {
    return (
      <Box sx={{ p: 3 }}>
        <Typography color="error">
          {error}
        </Typography>

        <Button
          variant="outlined"
          sx={{ mt: 2 }}
          onClick={() =>
            navigate("/incidents")
          }
        >
          Retour aux incidents
        </Button>
      </Box>
    );
  }

  return (
    <Box sx={{ p: 3 }}>
      <Card>
        <CardContent>

          <Typography
            variant="h5"
            gutterBottom
          >
            Modifier l'incident
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

            {/* Chantier */}
        <TextField
  select
  label="Chantier"
  value={idChantier}
  required
  disabled
  helperText="Le chantier d'un incident ne peut pas être modifié."
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

            {/* Type */}
            <TextField
              label="Type d'incident"
              value={type}
              onChange={(e) =>
                setType(e.target.value)
              }
              required
            />

            {/* Description */}
            <TextField
              label="Description"
              value={description}
              onChange={(e) =>
                setDescription(e.target.value)
              }
              multiline
              rows={4}
              required
            />

            {/* Gravité */}
            <TextField
              select
              label="Gravité"
              value={gravite}
              onChange={(e) =>
                setGravite(e.target.value)
              }
              required
            >
              <MenuItem value="Faible">
                Faible
              </MenuItem>

              <MenuItem value="Moyenne">
                Moyenne
              </MenuItem>

              <MenuItem value="Élevée">
                Élevée
              </MenuItem>

              <MenuItem value="Critique">
                Critique
              </MenuItem>
            </TextField>

            {/* Date */}
            <TextField
              label="Date et heure"
              type="datetime-local"
              value={dateIncident}
              onChange={(e) =>
                setDateIncident(e.target.value)
              }
              InputLabelProps={{
                shrink: true,
              }}
              required
            />

            {/* Statut */}
            <TextField
              select
              label="Statut"
              value={statut}
              onChange={(e) =>
                setStatut(e.target.value)
              }
              required
            >
              <MenuItem value="Ouvert">
                Ouvert
              </MenuItem>

              <MenuItem value="En cours">
                En cours
              </MenuItem>

              <MenuItem value="Résolu">
                Résolu
              </MenuItem>

              <MenuItem value="Clôturé">
                Clôturé
              </MenuItem>
            </TextField>

            {/* Actions */}
            <Box
              sx={{
                display: "flex",
                gap: 2,
                mt: 2,
              }}
            >
              <Button
                type="submit"
                variant="contained"
                disabled={saving}
              >
                {saving
                  ? "Enregistrement..."
                  : "Enregistrer les modifications"}
              </Button>

              <Button
                variant="outlined"
                onClick={() =>
                  navigate(`/incidents/${id}`)
                }
                disabled={saving}
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