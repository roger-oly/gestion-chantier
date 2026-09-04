import { useEffect, useState } from "react";

import {
  Box,
  Button,
  MenuItem,
  TextField,
  Typography,
  CircularProgress,
} from "@mui/material";

import {
  useNavigate,
  useParams,
} from "react-router-dom";

import {
  getUtilisateurById,
  updateUtilisateur,
} from "../../services/utilisateur.service";

import {
  getRoles,
} from "../../services/role.service";

import type { Role } from "../../services/role.service";


export default function EditUtilisateur() {

  const { id } = useParams<{ id: string }>();

  const navigate = useNavigate();


  const [nom, setNom] = useState("");
  const [prenom, setPrenom] = useState("");
  const [email, setEmail] = useState("");
  const [motDePasse, setMotDePasse] = useState("");
  const [telephone, setTelephone] = useState("");
  const [statut, setStatut] = useState("ACTIF");
  const [idRole, setIdRole] = useState("");


  const [roles, setRoles] = useState<Role[]>([]);

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  const [error, setError] =
    useState<string | null>(null);


  /*
   * Chargement utilisateur + rôles
   */
  useEffect(() => {

    async function loadData() {

      if (!id) {
        setError(
          "Identifiant utilisateur introuvable."
        );

        setLoading(false);

        return;
      }


      try {

        const [
          utilisateur,
          rolesData,
        ] = await Promise.all([
          getUtilisateurById(Number(id)),
          getRoles(),
        ]);


        setNom(utilisateur.nom);
        setPrenom(utilisateur.prenom);
        setEmail(utilisateur.email);
        setTelephone(
          utilisateur.telephone || ""
        );
        setStatut(utilisateur.statut);
        setIdRole(
          String(utilisateur.role.idRole)
        );

        setRoles(rolesData);


      } catch (error) {

        console.error(
          "Erreur chargement utilisateur :",
          error
        );

        setError(
          "Impossible de charger l'utilisateur."
        );


      } finally {

        setLoading(false);

      }

    }


    loadData();

  }, [id]);


  /*
   * Enregistrement des modifications
   */
  async function handleSubmit(
    event: React.FormEvent
  ) {

    event.preventDefault();


    if (!id) {

      setError(
        "Identifiant utilisateur introuvable."
      );

      return;

    }


    if (!idRole) {

      setError(
        "Veuillez sélectionner un rôle."
      );

      return;

    }


    try {

      setSaving(true);

      setError(null);


      await updateUtilisateur(
        Number(id),
        {
          nom,
          prenom,
          email,
          motDePasse,
          telephone,
          statut,

          role: {
            idRole: Number(idRole),
          },
        }
      );


      navigate("/utilisateurs");


    } catch (error) {

      console.error(
        "Erreur modification utilisateur :",
        error
      );

      setError(
        "Impossible de modifier l'utilisateur."
      );


    } finally {

      setSaving(false);

    }

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


  if (error && !nom) {

    return (
      <Typography color="error">
        {error}
      </Typography>
    );

  }


  return (

    <Box sx={{ p: 3 }}>

      <Typography
        variant="h4"
        gutterBottom
      >
        Modifier l'utilisateur
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
          label="Nom"
          value={nom}
          onChange={(event) =>
            setNom(event.target.value)
          }
          required
          fullWidth
        />


        <TextField
          label="Prénom"
          value={prenom}
          onChange={(event) =>
            setPrenom(event.target.value)
          }
          required
          fullWidth
        />


        <TextField
          label="Email"
          type="email"
          value={email}
          onChange={(event) =>
            setEmail(event.target.value)
          }
          required
          fullWidth
        />


        <TextField
          label="Nouveau mot de passe"
          type="password"
          value={motDePasse}
          onChange={(event) =>
            setMotDePasse(event.target.value)
          }
          fullWidth
          helperText="Laisser vide si le mot de passe ne doit pas être modifié."
        />


        <TextField
          label="Téléphone"
          value={telephone}
          onChange={(event) =>
            setTelephone(event.target.value)
          }
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

          <MenuItem value="ACTIF">
            ACTIF
          </MenuItem>

          <MenuItem value="INACTIF">
            INACTIF
          </MenuItem>

        </TextField>


        <TextField
          select
          label="Rôle"
          value={idRole}
          onChange={(event) =>
            setIdRole(event.target.value)
          }
          required
          fullWidth
        >

          {roles.map((role) => (

            <MenuItem
              key={role.idRole}
              value={role.idRole}
            >
              {role.libelle}
            </MenuItem>

          ))}

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
            disabled={saving}
          >
            {saving
              ? "Enregistrement..."
              : "Enregistrer"}
          </Button>


          <Button
            type="button"
            variant="outlined"
            onClick={() =>
              navigate("/utilisateurs")
            }
          >
            Annuler
          </Button>

        </Box>

      </Box>

    </Box>

  );

}