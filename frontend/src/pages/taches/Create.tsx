import {
  Button,
  MenuItem,
  Paper,
  TextField,
  Typography,
} from "@mui/material";

import { useEffect, useState } from "react";

import { useForm } from "react-hook-form";

import { useNavigate } from "react-router-dom";

import { useSearchParams } from "react-router-dom";

import {
  createTache,
  type TacheRequest,
} from "../../services/tache.service";

import {
  getChantiers,
} from "../../services/chantier.service";

import type { Chantier } from "../../types/chantier";


export default function CreateTache() {

  const [searchParams] = useSearchParams();

  const idChantier = searchParams.get("chantier");

  const navigate = useNavigate();

  const [chantiers, setChantiers] = useState<Chantier[]>([]);

  const [loadingChantiers, setLoadingChantiers] = useState(true);


  const {
    register,
    handleSubmit,
    reset,
  } = useForm<TacheRequest>({
    defaultValues: {
      statut: "En attente",
      niveauPriorite: "Moyenne",
    },
  });


  useEffect(() => {

    async function loadChantiers() {

      try {

        const data = await getChantiers();

        setChantiers(data);

      } catch (error) {

        console.error(
          "Erreur chargement des chantiers :",
          error
        );

      } finally {

        setLoadingChantiers(false);

      }

    }

    loadChantiers();

  }, []);


  async function onSubmit(data: TacheRequest) {

    try {

      if (!idChantier && !data.idChantier) {
  alert("Veuillez sélectionner un chantier.");
  return;
}

await createTache({
  ...data,
  idChantier: Number(idChantier || data.idChantier),
});

      alert("Tâche créée avec succès");

      reset();

      if (idChantier) {
  navigate(`/chantiers/${idChantier}?tab=1`);
} else {
  navigate("/taches");
}

    } catch (error) {

      console.error(
        "Erreur création tâche :",
        error
      );

      alert(
        "Erreur lors de la création de la tâche"
      );

    }

  }


  return (

    <Paper
      sx={{
        p: 4,
      }}
    >

      <Typography
        variant="h4"
        gutterBottom
      >
        Nouvelle tâche
      </Typography>


      <form
        onSubmit={handleSubmit(onSubmit)}
      >

        <TextField
          label="Titre"
          fullWidth
          margin="normal"
          {...register("titre", {
            required: true,
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
  select
  label="Chantier"
  fullWidth
  margin="normal"
  defaultValue={idChantier || ""}
  disabled={loadingChantiers || !!idChantier}
  {...register("idChantier", {
    required: true,
  })}
>

          <MenuItem value="">
            Sélectionner un chantier
          </MenuItem>


          {chantiers.map((chantier) => (

            <MenuItem
              key={chantier.idChantier}
              value={chantier.idChantier}
            >
              {chantier.nom}
            </MenuItem>

          ))}

        </TextField>


        <TextField
          select
          label="Priorité"
          fullWidth
          margin="normal"
          {...register("niveauPriorite")}
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
            Terminée
          </MenuItem>

        </TextField>


        <Button
          type="submit"
          variant="contained"
          sx={{ mt: 3 }}
        >
          Enregistrer
        </Button>


        <Button
          variant="outlined"
          sx={{ mt: 3, ml: 2 }}
          onClick={() => navigate("/taches")}
        >
          Annuler
        </Button>

      </form>

    </Paper>

  );

}