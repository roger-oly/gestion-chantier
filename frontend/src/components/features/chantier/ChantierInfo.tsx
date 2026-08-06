import {
  Typography,
  Divider
} from "@mui/material";


import type { Chantier } from "../../../types/chantier";


interface ChantierInfoProps {

  chantier: Chantier;

}



export default function ChantierInfo({
  chantier
}: ChantierInfoProps) {


  return (

    <>


      <Typography variant="h5">
        {chantier.nom}
      </Typography>


      <Divider sx={{my:2}} />



      <Typography>
        Description : {chantier.description}
      </Typography>


      <Typography>
        Localisation : {chantier.localisation}
      </Typography>


      <Typography>
        Budget : {chantier.budget} FCFA
      </Typography>


      <Typography>
        Date début : {chantier.dateDebut}
      </Typography>


      <Typography>
        Date fin prévue : {chantier.dateFinPrevue}
      </Typography>


      <Typography>
        Statut : {chantier.statut}
      </Typography>



      <Divider sx={{my:2}} />


      <Typography>
        Créé par : {chantier.nomUtilisateur}
      </Typography>



    </>

  );

}