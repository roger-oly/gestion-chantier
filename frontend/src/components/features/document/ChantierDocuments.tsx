import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import {
  Box,
  Typography,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Button,
} from "@mui/material";

import {
  getDocumentsByChantier,
  downloadDocument,
  deleteDocument,
} from "../../../services/document.service";

import type { Document } from "../../../types/document";

interface ChantierDocumentsProps {
  idChantier: number;
}

export default function ChantierDocuments({
  idChantier,
}: ChantierDocumentsProps) {

  const navigate = useNavigate();

  const [documents, setDocuments] = useState<Document[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {

    async function loadDocuments() {

      try {

        const data =
          await getDocumentsByChantier(idChantier);

        console.log(
          "Documents du chantier :",
          data
        );

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

  }, [idChantier]);

  async function handleDownload(document: Document) {

    try {

      const blob =
        await downloadDocument(
          document.idDocument
        );

      const url =
        window.URL.createObjectURL(blob);

      const link =
        window.document.createElement("a");

      link.href = url;
      link.download = document.nom;

      window.document.body.appendChild(link);

      link.click();

      link.remove();

      window.URL.revokeObjectURL(url);

    } catch (error) {

      console.error(
        "Erreur téléchargement du document :",
        error
      );

      alert(
        "Impossible de télécharger le document."
      );

    }
  }

  async function handleView(document: Document) {

    try {

      const blob =
        await downloadDocument(
          document.idDocument
        );

      const url =
        window.URL.createObjectURL(blob);

      window.open(url, "_blank");

      setTimeout(() => {
        window.URL.revokeObjectURL(url);
      }, 1000);

    } catch (error) {

      console.error(
        "Erreur affichage du document :",
        error
      );

      alert(
        "Impossible d'afficher le document."
      );

    }
  }

  function formatDate(date: string) {

    return new Date(date).toLocaleString(
      "fr-FR",
      {
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
        hour: "2-digit",
        minute: "2-digit",
      }
    );
  }

  async function handleDelete(document: Document) {

    const confirmation =
      window.confirm(
        `Voulez-vous vraiment supprimer le document "${document.nom}" ?`
      );

    if (!confirmation) {
      return;
    }

    try {

      await deleteDocument(
        document.idDocument
      );

      setDocuments(
        (documentsActuels) =>
          documentsActuels.filter(
            (item) =>
              item.idDocument !==
              document.idDocument
          )
      );

      alert(
        "Document supprimé avec succès"
      );

    } catch (error) {

      console.error(
        "Erreur suppression du document :",
        error
      );

      alert(
        "Erreur lors de la suppression du document."
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
              `/documents/nouveau?chantier=${idChantier}`
            )
          }
        >
          + Ajouter un document
        </Button>
      </Box>

      {documents.length === 0 ? (

        <Typography>
          Aucun document associé à ce chantier.
        </Typography>

      ) : (

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
                Date d'ajout
              </TableCell>

              <TableCell>
                Ajouté par
              </TableCell>

              <TableCell>
                Action
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
                  {formatDate(
                    document.dateUpload
                  )}
                </TableCell>

                <TableCell>
                  {document.nomUtilisateur}
                </TableCell>

                <TableCell>

                  <Button
                    variant="outlined"
                    size="small"
                    onClick={() =>
                      handleView(document)
                    }
                  >
                    Voir
                  </Button>

                  <Button
                    variant="outlined"
                    size="small"
                    sx={{ ml: 1 }}
                    onClick={() =>
                      handleDownload(document)
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
                      handleDelete(document)
                    }
                  >
                    Supprimer
                  </Button>

                </TableCell>

              </TableRow>

            ))}

          </TableBody>

        </Table>

      )}

    </>
  );
}
