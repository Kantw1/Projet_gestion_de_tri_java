# 📄 README – Projet Gestion de Tri Sélectif

## 🌍 Présentation

Ce projet a pour but de **gérer le tri sélectif** de manière intelligente à l'aide d'une **application JavaFX** connectée à une base de données MySQL.

**Objectif :**
- Encourager les ménages à mieux trier leurs déchets via un système de **points de fidélité**.
- Permettre aux utilisateurs de consulter leurs **dépôts**, **points gagnés**, et **réductions** proposées par des **commerces partenaires**.
- Gérer les **centres de tri**, **poubelles connectées**, **catégories de produits** et **commandes**.

---

## 🛠️ Technologies utilisées

- **Java 21**
- **JavaFX 21**
- **MySQL**
- **JDBC** (Java Database Connectivity)
- **FXML** pour les interfaces graphiques
- **CSS** pour la personnalisation graphique

---

## 📚 Structure du projet

```
/src
 ├── /dao                  # DAO : accès base de données (UtilisateurDAO, etc.)
 ├── /ihm                  # Interface graphique (controllers JavaFX)
 ├── /model                # Modèles de données (Utilisateur, CentreDeTri, etc.)
 ├── /utils                # Utilitaires (connexion base, outils scènes)
 └── HelloApplication.java # Point d'entrée principal

/resources
 ├── /views                # Fichiers FXML (interfaces visuelles)
 └── style.css             # Feuille de style générale CSS

/remplissage.sql           # Script de remplissage de la base de données
/BDD_structure.sql         # (Structure SQL si fournie)
/README.md                 # (Ton fichier README)
/LICENSE                   # (Optionnel, licence)
```

---

## 💄 Base de données

- Script de création de la BDD : fourni (`BDD_structure.sql`).
- Script de remplissage : fourni (`remplissage.sql`).

---

## 🚀 Instructions d'installation

1. **Cloner le projet** ou télécharger les fichiers.
2. **Importer le projet** dans votre IDE favori (**IntelliJ**, **Eclipse**, **VS Code**...).
3. **Créer la base de données** en exécutant `BDD_structure.sql`.
4. **Remplir la base** en exécutant `remplissage.sql`.
5. **Configurer** la connexion dans `utils/DatabaseConnection.java`.
6. **Lancer** l'application avec `HelloApplication.java`.

---

## 🎨 Style graphique

- Design clair, épuré, moderne.
- Boutons, champs, tableaux stylisés.
- Responsive et adapté au tri sélectif (couleurs par type de poubelle).

---

## 👥 Fonctionnalités principales

- **Inscription / Connexion** utilisateur avec code d'accès sécurisé.
- **Visualisation** des points de fidélité et historique de dépôts.
- **Dépôt de déchets** et attribution automatique de points.
- **Consultation des commerces partenaires** et utilisation des points.
- **Gestion des centres, poubelles et partenariats** (administrateurs).

---

## 📊 Données générées

- Utilisateurs réalistes (avec rôles `utilisateur` ou `admin`).
- Centres de tri répartis sur plusieurs villes.
- Poubelles de différents types et capacités.
- Dépôts variés avec points attribués.
- Contrats de partenariat actifs entre commerces et centres.

---

## ✏️ Auteurs

Projet réalisé par :

- Auteurs : Quentin Fourrier, Ahmed Metwally.

- Collaborateurs : Vincent Poulin, Capucine Lardiere, Clément Aouali.

---

## 📄 Licence

Projet académique — Usage éducatif uniquement.

---

