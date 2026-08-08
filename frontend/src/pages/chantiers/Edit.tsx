import {
  Button,
  TextField,
  Typography,
  Paper,
  MenuItem,
} from "@mui/material";

import { useEffect } from "react";
import { useForm } from "react-hook-form";
import { useNavigate, useParams } from "react-router-dom";

import {
  getChantierById,
  updateChantier,
} from "../../services/chantier.service";

interface ChantierForm {
  nom: string;
  description: string;
  localisation: string;
  budget: number;
  dateDebut: string;
  dateFinPrevue: string;
  statut: string;
}

export default function EditChantier() {

  const { id } = useParams();

  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    reset,
  } = useForm<ChantierForm>();


  useEffect(() => {

    async function loadChantier() {

      if (!id) return;

      try {

        const chantier = await getChantierById(Number(id));

        reset({
          nom: chantier.nom,
          description: chantier.description,
          localisation: chantier.localisation,
          budget: chantier.budget,
          dateDebut: chantier.dateDebut,
          dateFinPrevue: chantier.dateFinPrevue,
          statut: chantier.statut,
        });

      } catch (error) {

        console.error(
          "Erreur chargement chantier :",
          error
        );

      }

    }

    loadChantier();

  }, [id, reset]);


async function onSubmit(data: ChantierForm) {

  if (!id) return;

  try {

    const chantier = {
      ...data,
      budget: Number(data.budget),
      idUtilisateur: 1,
    };

    console.log(
      "Données modification envoyées :",
      chantier
    );

    await updateChantier(
      Number(id),
      chantier
    );

    alert(
      "Chantier modifié avec succès"
    );

    navigate(`/chantiers/${id}`);

  } catch (error) {

    console.error(
      "Erreur modification chantier :",
      error
    );

    alert(
      "Erreur lors de la modification du chantier"
    );

  }

}


  return (

    <Paper sx={{ padding: 4 }}>

      <Typography
        variant="h4"
        gutterBottom
      >
        Modifier le chantier
      </Typography>


      <form onSubmit={handleSubmit(onSubmit)}>


        <TextField
          label="Nom du chantier"
          fullWidth
          margin="normal"
          {...register("nom", {
            required: true
          })}
        />


        <TextField
          label="Description"
          fullWidth
          multiline
          rows={3}
          margin="normal"
          {...register("description")}
        />


        <TextField
          label="Localisation"
          fullWidth
          margin="normal"
          {...register("localisation", {
            required: true
          })}
        />


        <TextField
          label="Budget"
          type="number"
          fullWidth
          margin="normal"
          {...register("budget", {
            required: true,
            valueAsNumber: true
          })}
        />


        <TextField
          label="Date début"
          type="date"
          fullWidth
          margin="normal"
          InputLabelProps={{
            shrink: true
          }}
          {...register("dateDebut", {
            required: true
          })}
        />


        <TextField
          label="Date fin prévue"
          type="date"
          fullWidth
          margin="normal"
          InputLabelProps={{
            shrink: true
          }}
          {...register("dateFinPrevue")}
        />


        <TextField
          select
          label="Statut"
          fullWidth
          margin="normal"
          {...register("statut")}
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


        <Button
          type="submit"
          variant="contained"
          sx={{ mt: 3 }}
        >
          Enregistrer les modifications
        </Button>


        <Button
          variant="outlined"
          sx={{ mt: 3, ml: 2 }}
          onClick={() =>
            navigate(`/chantiers/${id}`)
          }
        >
          Annuler
        </Button>


      </form>

    </Paper>

  );
}