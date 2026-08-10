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

import { downloadDocument, deleteDocument, } from "../../services/document.service";

import { useEffect, useState } from "react";

import { useNavigate } from "react-router-dom";

import { getDocuments } from "../../services/document.service";

import type { Document } from "../../types/document";

export default function Documents() {
  const navigate = useNavigate();
  const [documents, setDocuments] = useState<Document[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadDocuments() {
      try {
        const data = await getDocuments();

        console.log("Documents reçus :", data);

        setDocuments(data);
      } catch (error) {
        console.error(
          "Erreur chargement des documents :",
          error
        );
      } finally {
        setLoading(false);
      }
    }

    loadDocuments();
  }, []);

  async function handleDownload(id: number, nom: string) {
  try {
    const blob = await downloadDocument(id);

    const url = window.URL.createObjectURL(blob);

    const link = document.createElement("a");
    link.href = url;
    link.download = nom;

    document.body.appendChild(link);
    link.click();

    link.remove();
    window.URL.revokeObjectURL(url);

  } catch (error) {
    console.error(
      "Erreur téléchargement document :",
      error
    );

    alert(
      "Impossible de télécharger le document"
    );
  }
}

async function handleView(id: number) {
  try {
    const blob = await downloadDocument(id);

    const url = window.URL.createObjectURL(blob);

    window.open(url, "_blank");

    setTimeout(() => {
      window.URL.revokeObjectURL(url);
    }, 1000);

  } catch (error) {
    console.error(
      "Erreur affichage document :",
      error
    );

    alert(
      "Impossible d'afficher le document"
    );
  }
}

async function handleDelete(id: number) {
  const confirmation = window.confirm(
    "Voulez-vous vraiment supprimer ce document ?"
  );

  if (!confirmation) {
    return;
  }

  try {
    await deleteDocument(id);

    setDocuments((documents) =>
      documents.filter(
        (document) =>
          document.idDocument !== id
      )
    );

    alert("Document supprimé avec succès");

  } catch (error) {
    console.error(
      "Erreur suppression document :",
      error
    );

    alert(
      "Impossible de supprimer le document"
    );
  }
}

  if (loading) {
    return (
      <Typography>
        Chargement des documents...
      </Typography>
    );
  }

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
  onClick={() => navigate("/documents/nouveau")}
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
                  Chantier
                </TableCell>

                <TableCell>
                  Date
                </TableCell>

                <TableCell>
                  Auteur du dépôt
                </TableCell>

                <TableCell>
                Actions
                </TableCell>

              </TableRow>
            </TableHead>

            <TableBody>
              {documents.map((document) => (
                <TableRow
                  key={document.idDocument}
                >
                  <TableCell>
                    {document.nom}
                  </TableCell>

                  <TableCell>
                    {document.type}
                  </TableCell>

                  <TableCell>
                    {document.nomChantier}
                  </TableCell>

                  <TableCell>
                    {new Date(
                      document.dateUpload
                    ).toLocaleDateString("fr-FR")}
                  </TableCell>

                  <TableCell>
                    {document.nomUtilisateur}
                  </TableCell>

                  <TableCell>
  
  <Button
  variant="outlined"
  size="small"
  sx={{ mr: 1 }}
  onClick={() =>
    handleView(document.idDocument)
  }
>
  Voir
</Button>

  <Button
    variant="outlined"
    size="small"
    onClick={() =>
      handleDownload(
        document.idDocument,
        document.nom
      )
    }
  >
    Télécharger
  </Button>

  <Button
  variant="outlined"
  color="error"
  size="small"
  sx={{ ml: 1 }}
  onClick={() =>
    handleDelete(document.idDocument)
  }
>
  Supprimer
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