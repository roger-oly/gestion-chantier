import {
  Button,
  TextField,
  Typography,
  Paper,
  MenuItem,
} from "@mui/material";

import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";

import { createChantier } from "../../services/chantier.service";


interface ChantierForm {

  nom: string;

  description: string;

  localisation: string;

  budget: number;

  dateDebut: string;

  dateFinPrevue: string;

  statut: string;

}



export default function CreateChantier() {


  const {
    register,
    handleSubmit,
    reset,
  } = useForm<ChantierForm>();


  const navigate = useNavigate();



  async function onSubmit(data: ChantierForm) {


    try {


      const chantier = {

        ...data,

        budget: Number(data.budget),

        //utilisateur: {

          //idUtilisateur: 1

       // }
       idUtilisateur:1

      };



      console.log(
        "Données envoyées :",
        chantier
      );



      await createChantier(chantier);



      alert(
        "Chantier créé avec succès"
      );



      reset();



      navigate("/chantiers");



    } catch(error) {


      console.error(
        "Erreur création chantier :",
        error
      );


    }

  }



  return (

    <Paper
      sx={{
        padding:4
      }}
    >


      <Typography
        variant="h4"
        gutterBottom
      >

        Nouveau chantier

      </Typography>



      <form
        onSubmit={
          handleSubmit(onSubmit)
        }
      >


        <TextField

          label="Nom du chantier"

          fullWidth

          margin="normal"

          {...register(
            "nom",
            {
              required:true
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

          label="Localisation"

          fullWidth

          margin="normal"

          {...register(
            "localisation",
            {
              required:true
            }
          )}

        />



        <TextField

          label="Budget"

          type="number"

          fullWidth

          margin="normal"

          {...register(
            "budget",
            {
              required:true
            }
          )}

        />



        <TextField

          label="Date début"

          type="date"

          fullWidth

          margin="normal"

          InputLabelProps={{
            shrink:true
          }}

          {...register(
            "dateDebut",
            {
              required:true
            }
          )}

        />



        <TextField

          label="Date fin prévue"

          type="date"

          fullWidth

          margin="normal"

          InputLabelProps={{
            shrink:true
          }}

          {...register(
            "dateFinPrevue"
          )}

        />



        <TextField

          select

          label="Statut"

          fullWidth

          margin="normal"

          defaultValue="En attente"

          {...register(
            "statut"
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



        <Button

          type="submit"

          variant="contained"

          sx={{
            mt:3
          }}

        >

          Enregistrer

        </Button>



      </form>


    </Paper>

  );

}