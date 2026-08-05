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


const utilisateurs = [

  {
    id: 1,
    nom: "Koko",
    prenom: "Jacques",
    email: "jacques@mail.com",
    role: "Administrateur",
  },

  {
    id: 2,
    nom: "Diallo",
    prenom: "Ali",
    email: "ali@mail.com",
    role: "Chef chantier",
  },

  {
    id: 3,
    nom: "Kouassi",
    prenom: "Jean",
    email: "jean@mail.com",
    role: "Ouvrier",
  },

];



export default function Utilisateurs() {


  return (

    <>


      <Typography

        variant="h4"

        gutterBottom

      >

        Utilisateurs

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
                  Nom
                </TableCell>


                <TableCell>
                  Prénom
                </TableCell>


                <TableCell>
                  Email
                </TableCell>


                <TableCell>
                  Rôle
                </TableCell>


                <TableCell>
                  Action
                </TableCell>


              </TableRow>


            </TableHead>



            <TableBody>


              {utilisateurs.map((utilisateur)=>(


                <TableRow

                  key={utilisateur.id}

                >


                  <TableCell>

                    {utilisateur.nom}

                  </TableCell>



                  <TableCell>

                    {utilisateur.prenom}

                  </TableCell>



                  <TableCell>

                    {utilisateur.email}

                  </TableCell>



                  <TableCell>

                    {utilisateur.role}

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