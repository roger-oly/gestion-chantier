import {
  Box,
  Button,
  Card,
  CardContent,
  LinearProgress,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  TextField,
  Typography,
} from "@mui/material";

import { useEffect, useState } from "react";

import {
  createAvancement,
  getAvancementsByChantier, getPourcentageByChantier,
} from "../../../services/avancement.service";

import { useAuth } from "../../../contexts/AuthContext";

import type { Avancement } from "../../../types/avancement";

interface ChantierAvancementProps {
  idChantier: number;
}

export default function ChantierAvancement({
  idChantier,
}: ChantierAvancementProps) {
  const { user } = useAuth();

  const [avancements, setAvancements] =
    useState<Avancement[]>([]);

    const [pourcentage, setPourcentage] = useState(0);

  const [loading, setLoading] = useState(true);

  const [commentaire, setCommentaire] =
    useState("");

  const [saving, setSaving] = useState(false);

async function loadAvancements() {
  try {
    setLoading(true);

    const data =
      await getAvancementsByChantier(idChantier);

    const pourcentageActuel =
      await getPourcentageByChantier(idChantier);

    setAvancements(data);
    setPourcentage(pourcentageActuel);

  } catch (error) {
    console.error(
      "Erreur chargement des avancements :",
      error
    );
  } finally {
    setLoading(false);
  }
}

  useEffect(() => {
    loadAvancements();
  }, [idChantier]);

  async function handleValidation() {
    if (!user) {
      alert("Utilisateur non connecté.");
      return;
    }

    if (!commentaire.trim()) {
      alert("Veuillez saisir un commentaire.");
      return;
    }

    try {
      setSaving(true);

      await createAvancement(
        idChantier,
        user.idUtilisateur,
        commentaire.trim()
      );

      alert("Avancement validé avec succès.");

      setCommentaire("");

      await loadAvancements();
    } catch (error) {
      console.error(
        "Erreur validation avancement :",
        error
      );

      alert(
        "Erreur lors de la validation de l'avancement."
      );
    } finally {
      setSaving(false);
    }
  }

  if (loading) {
    return (
      <Typography>
        Chargement de l'avancement...
      </Typography>
    );
  }

  function formatDate(date: string) {
    return new Date(date).toLocaleString("fr-FR", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
  }

  const dernierAvancement = avancements[0];

  {dernierAvancement && (
  <>
    <Typography sx={{ mt: 2 }}>
      Dernière mise à jour :{" "}
      {formatDate(
        dernierAvancement.dateMiseAJour
      )}
    </Typography>

    <Typography sx={{ mt: 1 }}>
      Commentaire :{" "}
      {dernierAvancement.commentaire ||
        "Aucun commentaire"}
    </Typography>
  </>
)}

  return (
    <Box>
      <Typography
        variant="h5"
        gutterBottom
      >
        Suivi de l'avancement
      </Typography>

      <Card sx={{ mb: 3 }}>
        <CardContent>
          <Typography
            variant="h3"
            gutterBottom
          >
            {pourcentage} %
          </Typography>

          <LinearProgress
            variant="determinate"
            value={pourcentage}
            sx={{
              height: 10,
              borderRadius: 5,
            }}
          />

          {dernierAvancement && (
            <>
              <Typography sx={{ mt: 2 }}>
                Dernière mise à jour :{" "}
                {formatDate(
                  dernierAvancement.dateMiseAJour
                )}
              </Typography>

              <Typography sx={{ mt: 1 }}>
                Commentaire :{" "}
                {dernierAvancement.commentaire ||
                  "Aucun commentaire"}
              </Typography>
            </>
          )}
        </CardContent>
      </Card>

      <Card sx={{ mb: 3 }}>
        <CardContent>
          <Typography
            variant="h6"
            gutterBottom
          >
            Valider la mise à jour
          </Typography>

          <TextField
            label="Commentaire"
            fullWidth
            multiline
            rows={3}
            value={commentaire}
            onChange={(event) =>
              setCommentaire(event.target.value)
            }
            placeholder="Commentaire du responsable"
          />

          <Button
            variant="contained"
            sx={{ mt: 2 }}
            onClick={handleValidation}
            disabled={saving}
          >
            {saving
              ? "Validation..."
              : "Valider la mise à jour"}
          </Button>
        </CardContent>
      </Card>

      <Typography
        variant="h6"
        gutterBottom
      >
        Historique des validations
      </Typography>

      {avancements.length === 0 ? (
        <Typography>
          Aucun avancement enregistré pour ce chantier.
        </Typography>
      ) : (
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>
                Pourcentage
              </TableCell>

              <TableCell>
                Commentaire
              </TableCell>

              <TableCell>
                Date
              </TableCell>
            </TableRow>
          </TableHead>

          <TableBody>
            {avancements.map((avancement) => (
              <TableRow
                key={avancement.idAvancement}
              >
                <TableCell>
                  {avancement.pourcentage} %
                </TableCell>

                <TableCell>
                  {avancement.commentaire || "-"}
                </TableCell>

                <TableCell>
                  {formatDate(
                    avancement.dateMiseAJour
                  )}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      )}
    </Box>
  );
}