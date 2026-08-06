import { useEffect, useState } from "react";

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

import { getChantiers } from "../../services/chantier.service";

import type { Chantier } from "../../types/chantier";


export default function Chantiers() {


  const [chantiers, setChantiers] = useState<Chantier[]>([]);


  useEffect(() => {

    async function loadChantiers() {

      try {

        const data = await getChantiers();

        console.log("Chantiers reçus :", data);

        setChantiers(data);


      } catch (error) {

        console.error(
          "Erreur chargement chantiers :",
          error
        );

      }

    }


    loadChantiers();

  }, []);



  return (

    <>

      <Typography
        variant="h4"
        gutterBottom
      >

        Chantiers

      </Typography>


      <Button
      component={Link}
      to="/chantiers/nouveau"
      variant="contained"
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
                  Localisation
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


              {chantiers.map((chantier) => (

                <TableRow
                  key={chantier.idChantier}
                >


                  <TableCell>
                    {chantier.nom}
                  </TableCell>


                  <TableCell>
                    {chantier.localisation}
                  </TableCell>


                  <TableCell>
                    {chantier.dateDebut}
                  </TableCell>


                  <TableCell>
                    {chantier.statut}
                  </TableCell>



                  <TableCell>

                    <Button
                      component={Link}
                      to={`/chantiers/${chantier.idChantier}`}
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