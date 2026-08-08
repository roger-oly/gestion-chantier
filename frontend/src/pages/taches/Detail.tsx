import {
  Card,
  CardContent,
  Typography,
  Box,
  Button,
} from "@mui/material";

import { useEffect, useState } from "react";

import {
  useParams,
  useNavigate,
} from "react-router-dom";

import {
  getTacheById,
  deleteTache,
} from "../../services/tache.service";

import type { Tache } from "../../types/tache";


export default function TacheDetail() {

  const { id } = useParams();

  const navigate = useNavigate();

  const [tache, setTache] = useState<Tache | null>(null);


  useEffect(() => {

    async function loadTache() {

      if (id) {

        try {

          const data = await getTacheById(
            Number(id)
          );

          setTache(data);

        } catch (error) {

          console.error(
            "Erreur chargement de la tâche :",
            error
          );

        }

      }

    }

    loadTache();

  }, [id]);


  if (!tache) {

    return (
      <Typography>
        Chargement de la tâche...
      </Typography>
    );

  }

async function handleDelete() {

   if (!id || !tache) return;

  const confirmation = window.confirm(
    `Voulez-vous vraiment supprimer la tâche "${tache.titre}" ?`
  );

  if (!confirmation) {
    return;
  }

  try {

    await deleteTache(Number(id));

    alert(
      "Tâche supprimée avec succès"
    );

    navigate("/taches");

  } catch (error) {

    console.error(
      "Erreur suppression tâche :",
      error
    );

    alert(
      "Erreur lors de la suppression de la tâche"
    );

  }

}

  return (

    <>

      <Typography
        variant="h4"
        gutterBottom
      >
        Tâche : {tache.titre}
      </Typography>


      <Card>

        <CardContent>

          <Typography variant="h5">
            {tache.titre}
          </Typography>


          <Typography sx={{ mt: 2 }}>
            Description : {tache.description}
          </Typography>


          <Typography sx={{ mt: 1 }}>
            Chantier : {tache.nomChantier}
          </Typography>


          <Typography sx={{ mt: 1 }}>
            Priorité : {tache.niveauPriorite}
          </Typography>


          <Typography sx={{ mt: 1 }}>
            Statut : {tache.statut}
          </Typography>


          <Box
            sx={{
              mt: 3,
              display: "flex",
              gap: 2,
            }}
          >

            <Button
              variant="contained"
              onClick={() =>
                navigate(
                  `/taches/${tache.idTache}/modifier`
                )
              }
            >
              Modifier
            </Button>


            <Button
              variant="outlined"
              color="error"
              onClick={handleDelete}
            >
              Supprimer
            </Button>

          </Box>

        </CardContent>

      </Card>

    </>

  );

}