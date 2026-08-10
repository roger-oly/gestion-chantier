import {
  Button,
  MenuItem,
  Paper,
  TextField,
  Typography,
} from "@mui/material";

import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import {
  useNavigate, useSearchParams,} from "react-router-dom";

import { useAuth } from "../../contexts/AuthContext";

import {
  createDocument,
  type DocumentRequest,
} from "../../services/document.service";

import { getChantiers } from "../../services/chantier.service";

import type { Chantier } from "../../types/chantier";

export default function CreateDocument() {
  const navigate = useNavigate();
  const { user } = useAuth();

  const [searchParams] = useSearchParams();

const idChantier = searchParams.get("chantier");

  const [chantiers, setChantiers] = useState<Chantier[]>([]);
  const [loadingChantiers, setLoadingChantiers] = useState(true);
  const [fichier, setFichier] = useState<File | null>(null);

  const {
    register,
    handleSubmit,
    reset,
  } = useForm<DocumentRequest>();

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

async function onSubmit(data: DocumentRequest) {

  try {

    if (!fichier) {
      alert("Veuillez sélectionner un fichier");
      return;
    }

    if (!user) {
      alert("Utilisateur non connecté");
      return;
    }

    if (!idChantier && !data.idChantier) {
      alert("Veuillez sélectionner un chantier");
      return;
    }

    const chantierId = Number(
      idChantier || data.idChantier
    );

    await createDocument(
      {
        ...data,
        idChantier: chantierId,
        idUtilisateur: user.idUtilisateur,
      },
      fichier
    );

    alert("Document ajouté avec succès");

    reset();
    setFichier(null);

    if (idChantier) {

      navigate(
        `/chantiers/${idChantier}?tab=3`
      );

    } else {

      navigate("/documents");

    }

  } catch (error) {

    console.error(
      "Erreur création document :",
      error
    );

    alert(
      "Erreur lors de la création du document"
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
        variant="h5"
        gutterBottom
      >
        Ajouter un document
      </Typography>

      <form
        onSubmit={handleSubmit(onSubmit)}
      >
        <TextField
          label="Nom du document"
          fullWidth
          margin="normal"
          {...register("nom", {
            required: true,
          })}
        />

        <TextField
          select
          label="Type"
          fullWidth
          margin="normal"
          defaultValue=""
          {...register("type", {
            required: true,
          })}
        >
          <MenuItem value="">
            Sélectionner un type
          </MenuItem>

          <MenuItem value="PDF">
            PDF
          </MenuItem>

          <MenuItem value="Word">
            Word
          </MenuItem>

          <MenuItem value="Excel">
            Excel
          </MenuItem>

          <MenuItem value="Image">
            Image
          </MenuItem>

          <MenuItem value="Autre">
            Autre
          </MenuItem>
        </TextField>
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

        <Button
          variant="outlined"
          component="label"
          sx={{ mt: 2 }}
        >
          Choisir un fichier

          <input
            type="file"
            hidden
            onChange={(event) => {
              const selectedFile =
                event.target.files?.[0] ?? null;

              setFichier(selectedFile);
            }}
          />
        </Button>

        <Typography sx={{ mt: 1 }}>
          {fichier
            ? fichier.name
            : "Aucun fichier sélectionné"}
        </Typography>

        <Button
          type="submit"
          variant="contained"
          sx={{ mt: 3 }}
        >
          Enregistrer
        </Button>

        <Button
          variant="outlined"
          sx={{
            mt: 3,
            ml: 2,
          }}
          onClick={() => navigate("/documents")}
        >
          Annuler
        </Button>
      </form>
    </Paper>
  );
}