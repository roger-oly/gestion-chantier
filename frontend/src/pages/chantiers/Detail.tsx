import {
  Card,
  CardContent,
  Typography,
  Box,
} from "@mui/material";

import { useState, useEffect } from "react";

import { useParams } from "react-router-dom";

import ChantierInfo 
from "../../components/features/chantier/ChantierInfo";

import ChantierTabs 
from "../../components/features/chantier/ChantierTabs";

import { getChantierById } 
from "../../services/chantier.service";

import type { Chantier } 
from "../../types/chantier";

export default function ChantierDetail() {


  const [tab, setTab] = useState(0);


const { id } = useParams();


const [chantier, setChantier] = useState<Chantier | null>(null);



useEffect(() => {

    async function loadChantier(){

        if(id){

            const data = await getChantierById(
                Number(id)
            );

            setChantier(data);

        }

    }


    loadChantier();


}, [id]);


if(!chantier){

    return (
        <Typography>
            Chargement du chantier...
        </Typography>
    );

}
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