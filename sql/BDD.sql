CREATE DATABASE IF NOT EXISTS BDD;
                USE BDD;

                CREATE TABLE Utilisateur (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    nom VARCHAR(255),
                    ptsFidelite INT,
                    codeAcces INT,
                    role VARCHAR(50) DEFAULT 'utilisateur' -- 'utilisateur' ou 'admin'
                );

                -- Centre de tri
                CREATE TABLE CentreDeTri (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    nom VARCHAR(255),
                    adresse VARCHAR(255)
                );

                -- Poubelle
                CREATE TABLE Poubelle (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    capaciteMax INT,
                    emplacement VARCHAR(255),
                    typePoubelle VARCHAR(255),
                    quantiteActuelle INT,
                    seuilAlerte INT
                );

                -- Dépôt
                CREATE TABLE Depot (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    type VARCHAR(255),
                    poids FLOAT,
    quantite INT,
    heureDepot DATETIME,
    points INT,
    poubelleID INT,
    utilisateurID INT,
    FOREIGN KEY (poubelleID) REFERENCES Poubelle(id),
    FOREIGN KEY (utilisateurID) REFERENCES Utilisateur(id)
);

-- Catégorie de produits
CREATE TABLE CategorieProduit (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255),
    pointNecessaire INT,
    bonReduction FLOAT
);

-- Produits dans une catégorie
CREATE TABLE ProduitCategorie (
    id INT AUTO_INCREMENT PRIMARY KEY,
    produit VARCHAR(255),
    categorieID INT,
    FOREIGN KEY (categorieID) REFERENCES CategorieProduit(id)
);

-- Commerce
CREATE TABLE Commerce (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255),
    centreID INT,
    FOREIGN KEY (centreID) REFERENCES CentreDeTri(id)
);

-- Contrat de partenariat
CREATE TABLE ContratPartenariat (
    id INT PRIMARY KEY AUTO_INCREMENT,
    dateDebut DATE,
    dateFin DATE,
    centreID INT,
    commerceID INT,
    FOREIGN KEY (centreID) REFERENCES CentreDeTri(id),
    FOREIGN KEY (commerceID) REFERENCES Commerce(id)
);

-- Lien Commerce - Catégorie de produit
CREATE TABLE CommerceCategorieProduit (
    commerceID INT,
    categorieID INT,
    PRIMARY KEY (commerceID, categorieID),
    FOREIGN KEY (commerceID) REFERENCES Commerce(id),
    FOREIGN KEY (categorieID) REFERENCES CategorieProduit(id)
);

-- Bon de commande
CREATE TABLE BonDeCommande (
    id INT PRIMARY KEY AUTO_INCREMENT,
    utilisateurID INT,
    etatCommande VARCHAR(255),
    dateCommande DATE,
    commerceID INT,
    pointsUtilises INT,
    FOREIGN KEY (utilisateurID) REFERENCES Utilisateur(id),
    FOREIGN KEY (commerceID) REFERENCES Commerce(id)
);

-- Commande liée à des catégories de produits
CREATE TABLE CommandeCategorieProduit (
    bonDeCommandeID INT,
    categorieProduitID INT,
    PRIMARY KEY (bonDeCommandeID, categorieProduitID),
    FOREIGN KEY (bonDeCommandeID) REFERENCES BonDeCommande(id),
    FOREIGN KEY (categorieProduitID) REFERENCES CategorieProduit(id)
);

-- Lien Centre ↔ Poubelle
CREATE TABLE CentrePoubelle (
    centreID INT,
    poubelleID INT,
    PRIMARY KEY (centreID, poubelleID),
    FOREIGN KEY (centreID) REFERENCES CentreDeTri(id),
    FOREIGN KEY (poubelleID) REFERENCES Poubelle(id)
);

-- Lien Centre ↔ Commerce
CREATE TABLE CentreCommerce (
    centreID INT,
    commerceID INT,
    PRIMARY KEY (centreID, commerceID),
    FOREIGN KEY (centreID) REFERENCES CentreDeTri(id),
    FOREIGN KEY (commerceID) REFERENCES Commerce(id)
);

-- Lien Centre ↔ Utilisateur
CREATE TABLE CentreUtilisateur (
    centreID INT,
    utilisateurID INT,
    PRIMARY KEY (centreID, utilisateurID),
    FOREIGN KEY (centreID) REFERENCES CentreDeTri(id),
    FOREIGN KEY (utilisateurID) REFERENCES Utilisateur(id)
);


-- Historique des dépôts
CREATE TABLE HistoriqueDepot (
    utilisateurID INT,
    depotID INT,
    dateDepot DATETIME,
    typeDechet VARCHAR(50),
    pointsGagnes INT,
    PRIMARY KEY (utilisateurID, depotID),
    FOREIGN KEY (utilisateurID) REFERENCES Utilisateur(id),
    FOREIGN KEY (depotID) REFERENCES Depot(id)
);
