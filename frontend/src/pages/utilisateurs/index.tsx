import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import {
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

import { getUtilisateurs, deleteUtilisateur } from "../../services/utilisateur.service";

import type { Utilisateur } from "../../types/utilisateur";

export default function Utilisateurs() {

  const navigate = useNavigate();
  const [utilisateurs, setUtilisateurs] = useState<Utilisateur[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function loadUtilisateurs() {
      try {
        const data = await getUtilisateurs();

        console.log("Utilisateurs reçus :", data);

        setUtilisateurs(data);
      } catch (error) {
        console.error(
          "Erreur chargement des utilisateurs :",
          error
        );

        setError(
          "Impossible de charger les utilisateurs."
        );
      } finally {
        setLoading(false);
      }
    }

    loadUtilisateurs();
  }, []);

  if (loading) {
    return (
      <div
        style={{
          display: "flex",
          justifyContent: "center",
          padding: "40px",
        }}
      >
        <CircularProgress />
      </div>
    );
  }

  if (error) {
    return (
      <Typography color="error">
        {error}
      </Typography>
    );
  }

  async function handleDelete(id: number) {
  const confirmation = window.confirm(
    "Voulez-vous vraiment supprimer cet utilisateur ?"
  );

  if (!confirmation) {
    return;
  }

  try {
    await deleteUtilisateur(id);

    setUtilisateurs((utilisateurs) =>
      utilisateurs.filter(
        (utilisateur) =>
          utilisateur.idUtilisateur !== id
      )
    );

    alert("Utilisateur supprimé avec succès");

  } catch (error) {
    console.error(
      "Erreur suppression utilisateur :",
      error
    );

    alert(
      "Impossible de supprimer l'utilisateur."
    );
  }
}

  return (
    <>
      <Typography
        variant="h4"
        gutterBottom
      >
        Utilisateurs
      </Typography>

     <Button
  variant="contained"
  sx={{ mb: 2 }}
  onClick={() =>
    navigate("/utilisateurs/nouveau")
  }
>
  + Ajouter
</Button>

      <Card>
        <CardContent>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>
                  Nom
                </TableCell>

                <TableCell>
                  Prénom
                </TableCell>

                <TableCell>
                  Email
                </TableCell>

                <TableCell>
                  Téléphone
                </TableCell>

                <TableCell>
                  Statut
                </TableCell>

                <TableCell>
                  Rôle
                </TableCell>

                <TableCell>
                  Action
                </TableCell>
              </TableRow>
            </TableHead>

            <TableBody>
              {utilisateurs.map((utilisateur) => (
                <TableRow
                  key={utilisateur.idUtilisateur}
                >
                  <TableCell>
                    {utilisateur.nom}
                  </TableCell>

                  <TableCell>
                    {utilisateur.prenom}
                  </TableCell>

                  <TableCell>
                    {utilisateur.email}
                  </TableCell>

                  <TableCell>
                    {utilisateur.telephone}
                  </TableCell>

                  <TableCell>
                    {utilisateur.statut}
                  </TableCell>

                  <TableCell>
                    {utilisateur.role?.libelle ?? "-"}
                  </TableCell>

 <TableCell>
  <Button
    variant="outlined"
    size="small"
    sx={{ mr: 1 }}
    onClick={() =>
      navigate(
        `/utilisateurs/${utilisateur.idUtilisateur}/modifier`
      )
    }
  >
    Modifier
  </Button>

  <Button
    variant="outlined"
    color="error"
    size="small"
    onClick={() =>
      handleDelete(
        utilisateur.idUtilisateur
      )
    }
  >
    Supprimer
  </Button>
</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </>
  );
}