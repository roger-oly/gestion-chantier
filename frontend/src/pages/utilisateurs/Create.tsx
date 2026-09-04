import { useEffect, useState } from "react";

import {
  Box,
  Button,
  MenuItem,
  TextField,
  Typography,
} from "@mui/material";

import { useNavigate } from "react-router-dom";

import {
  createUtilisateur,
} from "../../services/utilisateur.service";

import {
  getRoles,
} from "../../services/role.service";

import type { Role } from "../../services/role.service";


export default function CreateUtilisateur() {

  const navigate = useNavigate();


  const [roles, setRoles] = useState<Role[]>([]);

  const [nom, setNom] = useState("");
  const [prenom, setPrenom] = useState("");
  const [email, setEmail] = useState("");
  const [motDePasse, setMotDePasse] = useState("");
  const [telephone, setTelephone] = useState("");
  const [statut, setStatut] = useState("ACTIF");
  const [idRole, setIdRole] = useState("");


  const [loading, setLoading] = useState(false);
  const [loadingRoles, setLoadingRoles] = useState(true);

  const [error, setError] =
    useState<string | null>(null);


  /*
   * Chargement des rôles
   */
  useEffect(() => {

    async function loadRoles() {

      try {

        const data = await getRoles();

        console.log(
          "Rôles reçus :",
          data
        );

        setRoles(data);

      } catch (error) {

        console.error(
          "Erreur chargement des rôles :",
          error
        );

        setError(
          "Impossible de charger les rôles."
        );

      } finally {

        setLoadingRoles(false);

      }

    }

    loadRoles();

  }, []);


  /*
   * Création utilisateur
   */
  async function handleSubmit(
    event: React.FormEvent
  ) {

    event.preventDefault();


    if (!idRole) {

      setError(
        "Veuillez sélectionner un rôle."
      );

      return;

    }


    try {

      setLoading(true);

      setError(null);


      await createUtilisateur({

        nom,
        prenom,
        email,
        motDePasse,
        telephone,
        statut,

        role: {
          idRole: Number(idRole),
        },

      });


      navigate("/utilisateurs");


    } catch (error) {

      console.error(
        "Erreur création utilisateur :",
        error
      );

      setError(
        "Impossible de créer l'utilisateur."
      );


    } finally {

      setLoading(false);

    }

  }


  return (

    <Box sx={{ p: 3 }}>

      <Typography
        variant="h4"
        gutterBottom
      >
        Nouvel utilisateur
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
          label="Mot de passe"
          type="password"
          value={motDePasse}
          onChange={(event) =>
            setMotDePasse(event.target.value)
          }
          required
          fullWidth
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
          disabled={loadingRoles}
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
            disabled={
              loading ||
              loadingRoles
            }
          >
            {loading
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