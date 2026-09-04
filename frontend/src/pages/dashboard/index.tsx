import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import {
  Card,
  CardContent,
  Typography,
  LinearProgress,
  CircularProgress,
  Box,
} from "@mui/material";

import Grid from "@mui/material/Grid";

import { getChantiers } from "../../services/chantier.service";
import { getTaches } from "../../services/tache.service";
import { getIncidents } from "../../services/incident.service";
import { getLivraisons } from "../../services/livraison.service";


const Dashboard = () => {

  const navigate = useNavigate();

  const [nombreChantiers, setNombreChantiers] =
    useState(0);

  const [nombreTaches, setNombreTaches] =
    useState(0);

  const [incidentsEnCours, setIncidentsEnCours] =
  useState(0);

const [livraisonsEnAttente, setLivraisonsEnAttente] =
  useState(0);

  const [avancementTaches, setAvancementTaches] =
    useState(0);

  const [avancementChantiers, setAvancementChantiers] =
    useState(0);

  const [loading, setLoading] =
    useState(true);

  const [error, setError] =
    useState<string | null>(null);


  useEffect(() => {

    async function loadDashboard() {

      try {

        setLoading(true);
        setError(null);


        const [
          chantiers,
          taches,
          incidents,
          livraisons,
        ] = await Promise.all([

          getChantiers(),

          getTaches(),

          getIncidents(),

          getLivraisons(),

        ]);


        /*
         * ================================
         * COMPTEURS
         * ================================
         */

        setNombreChantiers(
          chantiers.length
        );

        setNombreTaches(
          taches.length
        );

      const nombreIncidentsEnCours =
  incidents.filter((incident) => {
    return (
      incident.statut
        ?.trim()
        .toLowerCase() === "en cours"
    );
  }).length;

setIncidentsEnCours(
  nombreIncidentsEnCours
);


const nombreLivraisonsEnAttente =
  livraisons.filter((livraison) => {
    return (
      livraison.statut
        ?.trim()
        .toLowerCase() === "en attente"
    );
  }).length;

setLivraisonsEnAttente(
  nombreLivraisonsEnAttente
);


        /*
         * ================================
         * AVANCEMENT DES TÂCHES
         * ================================
         *
         * Tâches terminées / tâches totales
         */

        if (taches.length > 0) {

          const tachesTerminees =
            taches.filter((tache) => {

              const statut = tache.statut
                ?.trim()
                .toLowerCase();

              return (
                statut === "terminé" ||
                statut === "terminée"
              );

            }).length;


          const pourcentage =
            (tachesTerminees /
              taches.length) *
            100;


          setAvancementTaches(
            Number(
              pourcentage.toFixed(2)
            )
          );

        } else {

          setAvancementTaches(0);

        }


        /*
         * ================================
         * AVANCEMENT DES CHANTIERS
         * ================================
         *
         * Chantiers terminés / chantiers totaux
         */

        if (chantiers.length > 0) {

          const chantiersTermines =
            chantiers.filter((chantier) => {

              const statut = chantier.statut
                ?.trim()
                .toLowerCase();

              return (
                statut === "terminé" ||
                statut === "terminée"
              );

            }).length;


          const pourcentage =
            (chantiersTermines /
              chantiers.length) *
            100;


          setAvancementChantiers(
            Number(
              pourcentage.toFixed(2)
            )
          );

        } else {

          setAvancementChantiers(0);

        }


      } catch (error) {

        console.error(
          "Erreur chargement dashboard :",
          error
        );


        setError(
          "Impossible de charger les données du tableau de bord."
        );


      } finally {

        setLoading(false);

      }

    }


    loadDashboard();

  }, []);


  /*
   * ================================
   * CHARGEMENT
   * ================================
   */

  if (loading) {

    return (

      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          py: 5,
        }}
      >

        <CircularProgress />

      </Box>

    );

  }


  /*
   * ================================
   * ERREUR
   * ================================
   */

  if (error) {

    return (

      <Typography color="error">
        {error}
      </Typography>

    );

  }


  /*
   * ================================
   * DASHBOARD
   * ================================
   */

  return (

    <div>

      <Typography
        variant="h4"
        gutterBottom
      >
        Tableau de bord
      </Typography>


      <Grid
        container
        spacing={3}
      >


        {/* ================================
            CHANTIERS
        ================================= */}

        <Grid
          size={{
            xs: 12,
            md: 6,
          }}
        >

          <Card
  onClick={() => navigate("/chantiers")}
  sx={{
    cursor: "pointer",
    transition: "0.2s",
    "&:hover": {
      transform: "translateY(-3px)",
      boxShadow: 4,
    },
  }}
>

            <CardContent>

              <Typography variant="h6">
                Chantiers
              </Typography>


              <Typography variant="h3">
                {nombreChantiers}
              </Typography>

            </CardContent>

          </Card>

        </Grid>


        {/* ================================
            TÂCHES
        ================================= */}

        <Grid
          size={{
            xs: 12,
            md: 6,
          }}
        >

         <Card
  onClick={() => navigate("/taches")}
  sx={{
    cursor: "pointer",
    transition: "0.2s",
    "&:hover": {
      transform: "translateY(-3px)",
      boxShadow: 4,
    },
  }}
>

            <CardContent>

              <Typography variant="h6">
                Tâches
              </Typography>


              <Typography variant="h3">
                {nombreTaches}
              </Typography>

            </CardContent>

          </Card>

        </Grid>


        {/* ================================
            INCIDENTS
        ================================= */}

        <Grid
          size={{
            xs: 12,
            md: 6,
          }}
        >

          <Card
  onClick={() => navigate("/incidents")}
  sx={{
    cursor: "pointer",
    transition: "0.2s",
    "&:hover": {
      transform: "translateY(-3px)",
      boxShadow: 4,
    },
  }}
>

            <CardContent>


              <Typography variant="h6">
  Incidents en cours
</Typography>

<Typography variant="h3">
  {incidentsEnCours}
</Typography>

            </CardContent>

          </Card>

        </Grid>


        {/* ================================
            LIVRAISONS
        ================================= */}

        <Grid
          size={{
            xs: 12,
            md: 6,
          }}
        >

          <Card
  onClick={() => navigate("/livraisons")}
  sx={{
    cursor: "pointer",
    transition: "0.2s",
    "&:hover": {
      transform: "translateY(-3px)",
      boxShadow: 4,
    },
  }}
>

            <CardContent>


              <Typography variant="h6">
  Livraisons en attente
</Typography>

<Typography variant="h3">
  {livraisonsEnAttente}
</Typography>

            </CardContent>

          </Card>

        </Grid>


      </Grid>


      {/* ================================
          AVANCEMENT DES TÂCHES
      ================================= */}

      <Card sx={{ mt: 4 }}>

        <CardContent>

          <Typography variant="h6">
            Avancement des tâches
          </Typography>


          <LinearProgress
            variant="determinate"
            value={avancementTaches}
            sx={{ mt: 2 }}
          />


          <Typography sx={{ mt: 1 }}>
            {avancementTaches} %
          </Typography>

        </CardContent>

      </Card>


      {/* ================================
          AVANCEMENT DES CHANTIERS
      ================================= */}

      <Card sx={{ mt: 3 }}>

        <CardContent>

          <Typography variant="h6">
            Avancement des chantiers
          </Typography>


          <LinearProgress
            variant="determinate"
            value={avancementChantiers}
            sx={{ mt: 2 }}
          />


          <Typography sx={{ mt: 1 }}>
            {avancementChantiers} %
          </Typography>

        </CardContent>

      </Card>


    </div>

  );

};


export default Dashboard;