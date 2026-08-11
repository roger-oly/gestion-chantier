import {
  Card,
  CardContent,
  Typography,
  Box,
  Button,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
} from "@mui/material";

import { useState, useEffect } from "react";
import { useParams, useNavigate, useSearchParams,} from "react-router-dom";

import ChantierInfo 
from "../../components/features/chantier/ChantierInfo";

import ChantierDocuments
from "../../components/features/document/ChantierDocuments";

import ChantierTabs 
from "../../components/features/chantier/ChantierTabs";

import ChantierAvancement
  from "../../components/features/chantier/ChantierAvancement";

import {
  getChantierById,
  deleteChantier,
} from "../../services/chantier.service";

import {
  getTachesByChantier,
} from "../../services/tache.service";

import type { Chantier } 
from "../../types/chantier";
import type { Tache } from "../../types/tache";

export default function ChantierDetail() {


const { id } = useParams();

const [searchParams] = useSearchParams();

const initialTab = Number(
  searchParams.get("tab") || 0
);

const [tab, setTab] = useState(initialTab);



const [chantier, setChantier] = useState<Chantier | null>(null);

const [taches, setTaches] =
  useState<Tache[]>([]);

const navigate = useNavigate();



useEffect(() => {

  async function loadData() {

    if (!id) return;

    try {

      const chantierData =
        await getChantierById(
          Number(id)
        );

      const tachesData =
        await getTachesByChantier(
          Number(id)
        );

      setChantier(chantierData);

      setTaches(tachesData);

    } catch (error) {

      console.error(
        "Erreur chargement du chantier et des tâches :",
        error
      );

    }

  }

  loadData();

}, [id]);


if(!chantier){

    return (
        <Typography>
            Chargement du chantier...
        </Typography>
    );

}

async function handleDelete() {

  if (!id || !chantier) return;

  const confirmation = window.confirm(
    `Voulez-vous vraiment supprimer le chantier "${chantier.nom}" ?`
  );

  if (!confirmation) {
    return;
  }

  try {

    await deleteChantier(Number(id));

    alert("Chantier supprimé avec succès");

    navigate("/chantiers");

  } catch (error) {

    console.error(
      "Erreur suppression chantier :",
      error
    );

    alert(
      "Erreur lors de la suppression du chantier"
    );

  }

}

  return (

    <>


      <Typography
        variant="h4"
        gutterBottom
      >

        Chantier : {chantier.nom}

      </Typography>



      <Card>


        <CardContent>


          <ChantierTabs

        value={tab}

         onChange={
         (_, value)=>setTab(value)
            }

            />


          <Box sx={{ mt: 3 }}>


           {tab === 0 && (
  <>
    <ChantierInfo
      chantier={chantier}
    />

    {/* Actions du chantier */}
    <Box sx={{ mt: 3, display: "flex", gap: 2 }}>
      <Button
        variant="contained"
        onClick={() =>
          navigate(`/chantiers/${chantier.idChantier}/modifier`)
        }
      >
        Modifier
      </Button>

      <Button
  variant="outlined"
  color="error"
  onClick={handleDelete}
>
  Supprimer
</Button>
    </Box>
  </>
)}


{tab === 1 && (

  <>
    <Typography
      variant="h6"
      gutterBottom
    >
      Liste des tâches du chantier
    </Typography>

    <Box
  sx={{
    mb: 2,
    display: "flex",
    justifyContent: "flex-end",
  }}
>
  <Button
    variant="contained"
    onClick={() =>
      navigate(
        `/taches/nouveau?chantier=${chantier.idChantier}`
      )
    }
  >
    + Nouvelle tâche
  </Button>
</Box>

    {taches.length === 0 ? (

      <Typography>
        Aucune tâche associée à ce chantier.
      </Typography>

    ) : (

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
                {tache.niveauPriorite}
              </TableCell>

              <TableCell>
                {tache.statut}
              </TableCell>

              <TableCell>

  <Button
    variant="outlined"
    size="small"
    onClick={() =>
      navigate(`/taches/${tache.idTache}`)
    }
  >
    Voir
  </Button>

</TableCell>

            </TableRow>

          ))}

        </TableBody>

      </Table>

    )}

  </>

)}


           {tab === 2 && (
  <ChantierAvancement
    idChantier={chantier.idChantier}
  />
)}



            {tab === 3 && (
  <ChantierDocuments
    idChantier={chantier.idChantier}
  />
)}



            {tab === 4 && (

              <Typography>

                Incidents déclarés

              </Typography>

            )}



            {tab === 5 && (

              <Typography>

                Livraisons du chantier

              </Typography>

            )}


          </Box>


        </CardContent>


      </Card>


    </>

  );

}