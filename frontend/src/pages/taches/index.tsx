import {
  Button,
  Card,
  CardContent,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Typography,
} from "@mui/material";


const taches = [

  {
    id: 1,
    titre: "Fondation",
    priorite: "Haute",
    statut: "En cours",
    responsable: "Jacques",
  },

  {
    id: 2,
    titre: "Béton",
    priorite: "Moyenne",
    statut: "À faire",
    responsable: "Ali",
  },

  {
    id: 3,
    titre: "Peinture",
    priorite: "Faible",
    statut: "Terminée",
    responsable: "Jean",
  },

];



export default function Taches() {


  return (

    <>


      <Typography
        variant="h4"
        gutterBottom
      >

        Tâches

      </Typography>



      <Button
        variant="contained"
        sx={{ mb: 2 }}
      >

        + Nouvelle

      </Button>



      <Card>

        <CardContent>


          <Table>


            <TableHead>

              <TableRow>

                <TableCell>
                  Titre
                </TableCell>

                <TableCell>
                  Priorité
                </TableCell>

                <TableCell>
                  Statut
                </TableCell>

                <TableCell>
                  Responsable
                </TableCell>

                <TableCell>
                  Action
                </TableCell>


              </TableRow>


            </TableHead>



            <TableBody>


              {taches.map((tache)=>(


                <TableRow key={tache.id}>


                  <TableCell>
                    {tache.titre}
                  </TableCell>


                  <TableCell>
                    {tache.priorite}
                  </TableCell>


                  <TableCell>
                    {tache.statut}
                  </TableCell>


                  <TableCell>
                    {tache.responsable}
                  </TableCell>


                  <TableCell>
                    Modifier
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