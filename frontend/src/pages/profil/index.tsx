import { useState } from "react";

import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  Divider,
  TextField,
  Typography,
} from "@mui/material";

import { useAuth } from "../../contexts/AuthContext";
import { changePassword } from "../../services/profil.service";

export default function Profil() {

  const { user } = useAuth();

  const [ancienMotDePasse, setAncienMotDePasse] =
    useState("");

  const [nouveauMotDePasse, setNouveauMotDePasse] =
    useState("");

  const [confirmationMotDePasse, setConfirmationMotDePasse] =
    useState("");

  const [message, setMessage] =
    useState<string | null>(null);

  const [error, setError] =
    useState<string | null>(null);

  const [loading, setLoading] =
    useState(false);


  if (!user) {

    return (
      <Typography color="error">
        Aucun utilisateur connecté.
      </Typography>
    );

  }


  const handleChangePassword = async () => {

    setMessage(null);
    setError(null);


    if (!ancienMotDePasse || !nouveauMotDePasse || !confirmationMotDePasse) {

      setError(
        "Veuillez remplir tous les champs."
      );

      return;

    }


    if (nouveauMotDePasse !== confirmationMotDePasse) {

      setError(
        "Les nouveaux mots de passe ne correspondent pas."
      );

      return;

    }


    if (nouveauMotDePasse.length < 6) {

      setError(
        "Le nouveau mot de passe doit contenir au moins 6 caractères."
      );

      return;

    }


    try {

      setLoading(true);

      await changePassword(
        user.idUtilisateur,
        {
          ancienMotDePasse,
          nouveauMotDePasse,
        }
      );


      setMessage(
        "Votre mot de passe a été modifié avec succès."
      );


      setAncienMotDePasse("");
      setNouveauMotDePasse("");
      setConfirmationMotDePasse("");


    } catch (error) {

      console.error(
        "Erreur modification mot de passe :",
        error
      );

      setError(
        "Impossible de modifier le mot de passe. Vérifiez votre ancien mot de passe."
      );


    } finally {

      setLoading(false);

    }

  };


  return (

    <Box>

      <Typography
        variant="h4"
        gutterBottom
      >
        Mon profil
      </Typography>


      <Card sx={{ mb: 3 }}>

        <CardContent>

          <Typography
            variant="h6"
            gutterBottom
          >
            Informations personnelles
          </Typography>


          <Divider sx={{ mb: 3 }} />


          <Box
            sx={{
              display: "grid",
              gridTemplateColumns: {
                xs: "1fr",
                md: "1fr 1fr",
              },
              gap: 2,
            }}
          >

            <TextField
              label="Nom"
              value={user.nom}
              fullWidth
              slotProps={{
                input: {
                  readOnly: true,
                },
              }}
            />


            <TextField
              label="Prénom"
              value={user.prenom}
              fullWidth
              slotProps={{
                input: {
                  readOnly: true,
                },
              }}
            />


            <TextField
              label="Email"
              value={user.email}
              fullWidth
              slotProps={{
                input: {
                  readOnly: true,
                },
              }}
            />


            <TextField
              label="Téléphone"
              value={user.telephone || "Non renseigné"}
              fullWidth
              slotProps={{
                input: {
                  readOnly: true,
                },
              }}
            />


            <TextField
              label="Rôle"
              value={user.role.libelle}
              fullWidth
              slotProps={{
                input: {
                  readOnly: true,
                },
              }}
            />


            <TextField
              label="Statut"
              value={user.statut}
              fullWidth
              slotProps={{
                input: {
                  readOnly: true,
                },
              }}
            />

          </Box>


          <Typography
            variant="body2"
            color="text.secondary"
            sx={{ mt: 3 }}
          >
            Pour toute modification de vos informations personnelles,
            veuillez contacter un administrateur.
          </Typography>

        </CardContent>

      </Card>


      <Card>

        <CardContent>

          <Typography
            variant="h6"
            gutterBottom
          >
            Sécurité
          </Typography>


          <Divider sx={{ mb: 3 }} />


          {message && (

            <Alert
              severity="success"
              sx={{ mb: 2 }}
            >
              {message}
            </Alert>

          )}


          {error && (

            <Alert
              severity="error"
              sx={{ mb: 2 }}
            >
              {error}
            </Alert>

          )}


          <Box
            sx={{
              maxWidth: 500,
              display: "flex",
              flexDirection: "column",
              gap: 2,
            }}
          >

            <TextField
              label="Ancien mot de passe"
              type="password"
              value={ancienMotDePasse}
              onChange={(event) =>
                setAncienMotDePasse(event.target.value)
              }
              fullWidth
            />


            <TextField
              label="Nouveau mot de passe"
              type="password"
              value={nouveauMotDePasse}
              onChange={(event) =>
                setNouveauMotDePasse(event.target.value)
              }
              fullWidth
            />


            <TextField
              label="Confirmer le nouveau mot de passe"
              type="password"
              value={confirmationMotDePasse}
              onChange={(event) =>
                setConfirmationMotDePasse(event.target.value)
              }
              fullWidth
            />


            <Button
              variant="contained"
              onClick={handleChangePassword}
              disabled={loading}
            >
              {loading
                ? "Modification..."
                : "Modifier mon mot de passe"}
            </Button>

          </Box>

        </CardContent>

      </Card>

    </Box>

  );

}