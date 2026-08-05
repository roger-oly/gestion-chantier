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


const livraisons = [

  {
    id: 1,
    description: "Ciment",
    date: "15/06/2026",
    statut: "Livré",
  },

  {
    id: 2,
    description: "Fer",
    date: "17/06/2026",
    statut: "En attente",
  },

  {
    id: 3,
    description: "Sable",
    date: "19/06/2026",
    statut: "Livré",
  },

];


export default function Livraisons() {


  return (

    <>


      <Typography

        variant="h4"

        gutterBottom

      >

        Livraisons

      </Typography>



      <Button

        variant="contained"

        sx={{ mb: 2 }}

      >

        + Ajouter

      </Button>



      <Card>


        <CardContent>


          <Table>


            <TableHead>


              <TableRow>


                <TableCell>
                  Description
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


              {livraisons.map((livraison)=>(


                <TableRow

                  key={livraison.id}

                >


                  <TableCell>

                    {livraison.description}

                  </TableCell>



                  <TableCell>

                    {livraison.date}

                  </TableCell>



                  <TableCell>

                    {livraison.statut}

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