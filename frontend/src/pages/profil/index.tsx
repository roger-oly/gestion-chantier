import {
  Button,
  Card,
  CardContent,
  TextField,
  Typography,
} from "@mui/material";


export default function Profil() {


  return (

    <>


      <Typography

        variant="h4"

        gutterBottom

      >

        Mon Profil

      </Typography>



      <Card

        sx={{

          maxWidth: 600

        }}

      >


        <CardContent>


          <TextField

            fullWidth

            label="Nom"

            margin="normal"

          />



          <TextField

            fullWidth

            label="Prénom"

            margin="normal"

          />



          <TextField

            fullWidth

            label="Téléphone"

            margin="normal"

          />



          <TextField

            fullWidth

            label="Email"

            type="email"

            margin="normal"

          />



          <TextField

            fullWidth

            label="Mot de passe"

            type="password"

            margin="normal"

          />



          <Button

            variant="contained"

            sx={{

              mt: 2

            }}

          >

            Enregistrer

          </Button>



        </CardContent>


      </Card>


    </>

  );

}