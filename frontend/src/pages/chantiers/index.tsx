import {
  Button,
  Card,
  CardContent,
  Typography,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
} from "@mui/material";
import { Link } from "react-router-dom";


const chantiers = [
  {
    id: 1,
    nom: "Construction Lycée",
    ville: "Abidjan",
    debut: "01/05/2026",
    statut: "En cours",
  },
  {
    id: 2,
    nom: "Pont Moderne",
    ville: "Bouaké",
    debut: "10/06/2026",
    statut: "En attente",
  },
  {
    id: 3,
    nom: "Centre administratif",
    ville: "Yamoussoukro",
    debut: "15/07/2026",
    statut: "Terminé",
  },
];


export default function Chantiers() {


  return (

    <>

      <Typography variant="h4" gutterBottom>

        Chantiers

      </Typography>


      <Button
        variant="contained"
        sx={{ mb: 2 }}
      >

        + Nouveau

      </Button>


      <Card>

        <CardContent>


          <Table>


            <TableHead>

              <TableRow>

                <TableCell>
                  Nom
                </TableCell>

                <TableCell>
                  Ville
                </TableCell>

                <TableCell>
                  Début
                </TableCell>

                <TableCell>
                  Statut
                </TableCell>

                <TableCell>
                  Action
                </TableCell>


              </TableRow>

            </TableHead>



            <TableBody>


              {chantiers.map((chantier)=>(

                <TableRow key={chantier.id}>


                  <TableCell>
                    {chantier.nom}
                  </TableCell>


                  <TableCell>
                    {chantier.ville}
                  </TableCell>


                  <TableCell>
                    {chantier.debut}
                  </TableCell>


                  <TableCell>
                    {chantier.statut}
                  </TableCell>


                <TableCell>

                    <Button
                    component={Link}
                     to={`/chantiers/${chantier.id}`}
                    >

                     Voir

                    </Button>

                </TableCell>


                </TableRow>

              ))}


            </TableBody>


          </Table>


        </CardContent>


      </Card>


    </>

  );

}