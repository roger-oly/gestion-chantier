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


const documents = [

  {
    id: 1,
    nom: "Plan.pdf",
    type: "PDF",
    date: "15/06/2026",
    action: "Télécharger",
  },

  {
    id: 2,
    nom: "Devis.docx",
    type: "Word",
    date: "17/06/2026",
    action: "Télécharger",
  },

  {
    id: 3,
    nom: "Photo.png",
    type: "Image",
    date: "18/06/2026",
    action: "Voir",
  },

];



export default function Documents() {


  return (

    <>


      <Typography
        variant="h4"
        gutterBottom
      >

        Documents

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
                  Type
                </TableCell>


                <TableCell>
                  Date
                </TableCell>


                <TableCell>
                  Action
                </TableCell>


              </TableRow>


            </TableHead>



            <TableBody>


              {documents.map((document)=>(


                <TableRow

                  key={document.id}

                >


                  <TableCell>

                    {document.nom}

                  </TableCell>



                  <TableCell>

                    {document.type}

                  </TableCell>



                  <TableCell>

                    {document.date}

                  </TableCell>



                  <TableCell>

                    {document.action}

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