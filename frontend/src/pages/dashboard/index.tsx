import {
  Card,
  CardContent,
  Typography,
  LinearProgress,
} from "@mui/material";
import Grid from "@mui/material/Grid";


const Dashboard = () => {


  return (

    <div>


      <Typography variant="h4" gutterBottom>

        Tableau de bord

      </Typography>



      <Grid container spacing={3}>


          <Grid size={{ xs: 12, md: 6 }}>

          <Card>

            <CardContent>

              <Typography variant="h6">

                Chantiers

              </Typography>


              <Typography variant="h3">

                12

              </Typography>


            </CardContent>

          </Card>

        </Grid>



        <Grid size={{ xs: 12, md: 6 }}>

          <Card>

            <CardContent>

              <Typography variant="h6">

                Tâches

              </Typography>


              <Typography variant="h3">

                45

              </Typography>


            </CardContent>

          </Card>


        </Grid>



        <Grid size={{ xs: 12, md: 6 }}>


          <Card>

            <CardContent>


              <Typography variant="h6">

                Incidents

              </Typography>


              <Typography variant="h3">

                3

              </Typography>


            </CardContent>

          </Card>


        </Grid>



        <Grid size={{ xs: 12, md: 6 }}>


          <Card>

            <CardContent>


              <Typography variant="h6">

                Livraisons

              </Typography>


              <Typography variant="h3">

                8

              </Typography>


            </CardContent>

          </Card>


        </Grid>


      </Grid>



      <Card sx={{ mt: 4 }}>

        <CardContent>


          <Typography variant="h6">

            Avancement moyen

          </Typography>


          <LinearProgress

            variant="determinate"

            value={75}

            sx={{ mt: 2 }}

          />


          <Typography sx={{ mt: 1 }}>

            75 %

          </Typography>


        </CardContent>


      </Card>



    </div>

  );

};


export default Dashboard;