-- ===========================================
-- Base de donnees - Tables - PostgreSQL 17
-- ===========================================

DROP TABLE IF EXISTS document CASCADE;
DROP TABLE IF EXISTS livraison CASCADE;
DROP TABLE IF EXISTS incident CASCADE;
DROP TABLE IF EXISTS avancement CASCADE;
DROP TABLE IF EXISTS tache CASCADE;
DROP TABLE IF EXISTS chantier CASCADE;
DROP TABLE IF EXISTS utilisateur CASCADE;
DROP TABLE IF EXISTS role CASCADE;


-- ===========================================
-- TABLE : role
-- ===========================================

CREATE TABLE role (
    id_role SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL UNIQUE
);

-- ===========================================
-- TABLE : utilisateur
-- ===========================================

CREATE TABLE utilisateur (
    id_utilisateur SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(255) NOT NULL,
    telephone VARCHAR(20),
    statut VARCHAR(20) NOT NULL,
    id_role INTEGER NOT NULL,

    CONSTRAINT fk_utilisateur_role
        FOREIGN KEY (id_role)
        REFERENCES role(id_role)
);

-- ===========================================
-- TABLE : chantier
-- ===========================================

CREATE TABLE chantier (
    id_chantier SERIAL PRIMARY KEY,
    nom VARCHAR(150) NOT NULL,
    description TEXT,
    localisation VARCHAR(255) NOT NULL,
    budget NUMERIC(12,2),
    date_debut DATE NOT NULL,
    date_fin_prevue DATE,
    statut VARCHAR(30) NOT NULL,

    id_utilisateur INTEGER NOT NULL,

    CONSTRAINT fk_chantier_responsable
        FOREIGN KEY (id_utilisateur)
        REFERENCES utilisateur(id_utilisateur)
);

-- ===========================================
-- TABLE : tache
-- ===========================================

CREATE TABLE tache (
    id_tache SERIAL PRIMARY KEY,
    titre VARCHAR(150) NOT NULL,
    description TEXT,
    statut VARCHAR(30) NOT NULL,
    niveau_priorite VARCHAR(20),

    id_chantier INTEGER NOT NULL,

    CONSTRAINT fk_tache_chantier
        FOREIGN KEY (id_chantier)
        REFERENCES chantier(id_chantier)
);

-- ===========================================
-- TABLE : avancement
-- ===========================================

CREATE TABLE avancement (
    id_avancement SERIAL PRIMARY KEY,
    pourcentage INTEGER NOT NULL CHECK (pourcentage BETWEEN 0 AND 100),
    commentaire TEXT,
    date_mise_a_jour TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    id_tache INTEGER NOT NULL,
    id_utilisateur INTEGER NOT NULL,

    CONSTRAINT fk_avancement_tache
        FOREIGN KEY (id_tache)
        REFERENCES tache(id_tache),

    CONSTRAINT fk_avancement_utilisateur
        FOREIGN KEY (id_utilisateur)
        REFERENCES utilisateur(id_utilisateur)
);

-- ===========================================
-- TABLE : incident
-- ===========================================

CREATE TABLE incident (
    id_incident SERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    gravite VARCHAR(20),
    date_incident TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    statut VARCHAR(30) NOT NULL,

    id_chantier INTEGER NOT NULL,
    id_utilisateur INTEGER NOT NULL,

    CONSTRAINT fk_incident_chantier
        FOREIGN KEY (id_chantier)
        REFERENCES chantier(id_chantier),

    CONSTRAINT fk_incident_utilisateur
        FOREIGN KEY (id_utilisateur)
        REFERENCES utilisateur(id_utilisateur)
);

-- ===========================================
-- TABLE : livraison
-- ===========================================

CREATE TABLE livraison (
    id_livraison SERIAL PRIMARY KEY,
    description TEXT,
    date_livraison DATE NOT NULL,
    statut VARCHAR(30) NOT NULL,

    id_chantier INTEGER NOT NULL,
    id_utilisateur INTEGER NOT NULL,

    CONSTRAINT fk_livraison_chantier
        FOREIGN KEY (id_chantier)
        REFERENCES chantier(id_chantier),

    CONSTRAINT fk_livraison_utilisateur
        FOREIGN KEY (id_utilisateur)
        REFERENCES utilisateur(id_utilisateur)
);

-- ===========================================
-- TABLE : document
-- ===========================================

CREATE TABLE document (
    id_document SERIAL PRIMARY KEY,
    nom VARCHAR(150) NOT NULL,
    type VARCHAR(50) NOT NULL,
    chemin_fichier VARCHAR(255) NOT NULL,
    date_upload TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    id_chantier INTEGER NOT NULL,
    id_utilisateur INTEGER NOT NULL,

    CONSTRAINT fk_document_chantier
        FOREIGN KEY (id_chantier)
        REFERENCES chantier(id_chantier),

    CONSTRAINT fk_document_utilisateur
        FOREIGN KEY (id_utilisateur)
        REFERENCES utilisateur(id_utilisateur)
);
