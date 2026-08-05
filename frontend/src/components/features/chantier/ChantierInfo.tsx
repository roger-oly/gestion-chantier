import {
  Typography,
} from "@mui/material";


interface ChantierInfoProps {

  chantier: {

    nom: string;

    description: string;

    localisation: string;

    budget: string;

    dateDebut: string;

    dateFin: string;

    statut: string;

  };

}


export default function ChantierInfo({
  chantier
}: ChantierInfoProps) {


  return (

    <>


      <Typography>
        Description : {chantier.description}
      </Typography>


      <Typography>
        Localisation : {chantier.localisation}
      </Typography>


      <Typography>
        Budget : {chantier.budget}
      </Typography>


      <Typography>
        Date début : {chantier.dateDebut}
      </Typography>


      <Typography>
        Date fin : {chantier.dateFin}
      </Typography>


      <Typography>
        Statut : {chantier.statut}
      </Typography>


    </>

  );

}