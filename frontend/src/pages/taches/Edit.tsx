import {
  Button,
  TextField,
  Typography,
  Paper,
  MenuItem,
} from "@mui/material";

import { useEffect, useState } from "react";

import {
  useForm,
} from "react-hook-form";

import {
  useNavigate,
  useParams,
} from "react-router-dom";

import {
  getTacheById,
  updateTache,
} from "../../services/tache.service";

import {
  getChantiers,
} from "../../services/chantier.service";

import type { Chantier } from "../../types/chantier";


interface TacheForm {

  titre: string;

  description: string;

  statut: string;

  niveauPriorite: string;

  idChantier: number;

}


export default function EditTache() {

  const {
    register,
    handleSubmit,
    reset,
  } = useForm<TacheForm>();


  const { id } = useParams();

  const navigate = useNavigate();


  const [chantiers, setChantiers] =
    useState<Chantier[]>([]);


  useEffect(() => {

    async function loadData() {

      if (!id) return;

      try {

        const [tache, chantiersData] =
          await Promise.all([

            getTacheById(
              Number(id)
            ),

            getChantiers(),

          ]);


        setChantiers(
          chantiersData
        );


        reset({

          titre: tache.titre,

          description: tache.description,

          statut: tache.statut,

          niveauPriorite:
            tache.niveauPriorite,

          idChantier:
            tache.idChantier,

        });

      } catch (error) {

        console.error(
          "Erreur chargement des données :",
          error
        );

      }

    }

    loadData();

  }, [id, reset]);


  async function onSubmit(
    data: TacheForm
  ) {

    if (!id) return;

    try {

      await updateTache(
        Number(id),
        {
          titre: data.titre,

          description:
            data.description,

          statut: data.statut,

          niveauPriorite:
            data.niveauPriorite,

          idChantier:
            Number(data.idChantier),
        }
      );


      alert(
        "Tâche modifiée avec succès"
      );


      navigate(
        `/taches/${id}`
      );

    } catch (error) {

      console.error(
        "Erreur modification tâche :",
        error
      );

      alert(
        "Erreur lors de la modification"
      );

    }

  }


  return (

    <Paper
      sx={{
        padding: 4,
      }}
    >

      <Typography
        variant="h4"
        gutterBottom
      >
        Modifier la tâche
      </Typography>


      <form
        onSubmit={
          handleSubmit(onSubmit)
        }
      >


        <TextField
          label="Titre"
          fullWidth
          margin="normal"
          {...register(
            "titre",
            {
              required: true,
            }
          )}
        />


        <TextField
          label="Description"
          fullWidth
          multiline
          rows={3}
          margin="normal"
          {...register(
            "description"
          )}
        />


        <TextField
          select
          label="Chantier"
          fullWidth
          margin="normal"
          defaultValue=""
          {...register(
            "idChantier",
            {
              required: true,
            }
          )}
        >

          {chantiers.map(
            (chantier) => (

              <MenuItem
                key={
                  chantier.idChantier
                }
                value={
                  chantier.idChantier
                }
              >
                {chantier.nom}
              </MenuItem>

            )
          )}

        </TextField>


        <TextField
          select
          label="Statut"
          fullWidth
          margin="normal"
          {...register(
            "statut",
            {
              required: true,
            }
          )}
        >

          <MenuItem value="En attente">
            En attente
          </MenuItem>

          <MenuItem value="En cours">
            En cours
          </MenuItem>

          <MenuItem value="Terminé">
            Terminé
          </MenuItem>

        </TextField>


        <TextField
          select
          label="Priorité"
          fullWidth
          margin="normal"
          {...register(
            "niveauPriorite",
            {
              required: true,
            }
          )}
        >

          <MenuItem value="Faible">
            Faible
          </MenuItem>

          <MenuItem value="Moyenne">
            Moyenne
          </MenuItem>

          <MenuItem value="Haute">
            Haute
          </MenuItem>

        </TextField>


        <Button
          type="submit"
          variant="contained"
          sx={{
            mt: 3,
          }}
        >
          Enregistrer
        </Button>


        <Button
          type="button"
          variant="outlined"
          sx={{
            mt: 3,
            ml: 2,
          }}
          onClick={() =>
            navigate(
              `/taches/${id}`
            )
          }
        >
          Annuler
        </Button>

      </form>

    </Paper>

  );

}