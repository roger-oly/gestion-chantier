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


const incidents = [

  {
    id: 1,
    type: "Accident",
    gravite: "Haute",
    date: "15/06/2026",
    statut: "Ouvert",
  },

  {
    id: 2,
    type: "Retard",
    gravite: "Moyenne",
    date: "18/06/2026",
    statut: "En cours",
  },

  {
    id: 3,
    type: "Panne",
    gravite: "Faible",
    date: "20/06/2026",
    statut: "Résolu",
  },

];


export default function Incidents() {


  return (

    <>


      <Typography

        variant="h4"

        gutterBottom

      >

        Incidents

      </Typography>



      <Button

        variant="contained"

        sx={{ mb: 2 }}

      >

        + Déclarer

      </Button>



      <Card>


        <CardContent>


          <Table>


            <TableHead>


              <TableRow>


                <TableCell>
                  Type
                </TableCell>


                <TableCell>
                  Gravité
                </TableCell>


                <TableCell>
                  Date
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


              {incidents.map((incident)=>(


                <TableRow

                  key={incident.id}

                >


                  <TableCell>

                    {incident.type}

                  </TableCell>



                  <TableCell>

                    {incident.gravite}

                  </TableCell>



                  <TableCell>

                    {incident.date}

                  </TableCell>



                  <TableCell>

                    {incident.statut}

                  </TableCell>



                  <TableCell>

                    Voir

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