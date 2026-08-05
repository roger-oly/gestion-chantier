import {
  Card,
  CardContent,
  Typography,
  Box,
} from "@mui/material";

import { useState } from "react";
import ChantierInfo 
from "../../components/features/chantier/ChantierInfo";
import ChantierTabs from "../../components/features/chantier/ChantierTabs";


export default function ChantierDetail() {


  const [tab, setTab] = useState(0);


  const chantier = {

    nom: "Construction Lycée",

    description:
      "Construction d'un établissement scolaire moderne",

    localisation:
      "Abidjan",

    budget:
      "150 000 000 FCFA",

    dateDebut:
      "01/05/2026",

    dateFin:
      "30/09/2026",

    statut:
      "En cours",

  };



  return (

    <>


      <Typography
        variant="h4"
        gutterBottom
      >

        Chantier : {chantier.nom}

      </Typography>



      <Card>


        <CardContent>


          <ChantierTabs

        value={tab}

         onChange={
         (_, value)=>setTab(value)
            }

            />


          <Box sx={{ mt: 3 }}>


            {tab === 0 && (

            <ChantierInfo
             chantier={chantier}
            />

            )}



            {tab === 1 && (

              <Typography>

                Liste des tâches du chantier

              </Typography>

            )}



            {tab === 2 && (

              <Typography>

                Suivi de l'avancement

              </Typography>

            )}



            {tab === 3 && (

              <Typography>

                Documents associés

              </Typography>

            )}



            {tab === 4 && (

              <Typography>

                Incidents déclarés

              </Typography>

            )}



            {tab === 5 && (

              <Typography>

                Livraisons du chantier

              </Typography>

            )}


          </Box>


        </CardContent>


      </Card>


    </>

  );

}