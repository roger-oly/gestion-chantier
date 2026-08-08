
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

import { useEffect, useState } from "react";

import { Link } from "react-router-dom";

import { getTaches } from "../../services/tache.service";

import type { Tache } from "../../types/tache";


export default function Taches() {

  const [taches, setTaches] = useState<Tache[]>([]);

  const [loading, setLoading] = useState(true);


  useEffect(() => {

    async function loadTaches() {

      try {

        const data = await getTaches();

        console.log("Tâches reçues :", data);

        setTaches(data);

      } catch (error) {

        console.error(
          "Erreur chargement des tâches :",
          error
        );

      } finally {

        setLoading(false);

      }

    }

    loadTaches();

  }, []);


  if (loading) {

    return (
      <Typography>
        Chargement des tâches...
      </Typography>
    );

  }


  return (

    <>

      <Typography
        variant="h4"
        gutterBottom
      >
        Tâches
      </Typography>


      <Button
        component={Link}
        to="/taches/nouveau"
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
                  Chantier
                </TableCell>

                <TableCell>
                  Priorité
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

              {taches.map((tache) => (

                <TableRow
                  key={tache.idTache}
                >

                  <TableCell>
                    {tache.titre}
                  </TableCell>

                  <TableCell>
                    {tache.nomChantier}
                  </TableCell>

                  <TableCell>
                    {tache.niveauPriorite}
                  </TableCell>

                  <TableCell>
                    {tache.statut}
                  </TableCell>

                  <TableCell>

                    <Button
                      component={Link}
                      to={`/taches/${tache.idTache}`}
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
