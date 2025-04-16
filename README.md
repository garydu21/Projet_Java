# 🕵️ Gestion d'Affaires Criminelles

Projet Java avec interface graphique (Swing) permettant de gérer une base de données de criminels et d'affaires criminelles.

## 🎯 Objectifs
- Ajouter, modifier, supprimer des criminels
- Créer des affaires, leur associer des criminels
- Visualiser les détails des affaires et des suspects
- Sauvegarder automatiquement les données au format JSON
- Affichage clair avec interfaces Swing

## 📦 Structure du projet

src/
├── Criminel/
│   ├── Criminel.java
│   ├── Affaire.java
│   └── Crime.java (si utilisé)
├── Modele/
│   └── Modele.java
├── Vue/
│   ├── Vue.java
│   └── VueAffaires.java
├── Controleur/
│   └── Controleur.java

criminels.json        ← Données sauvegardées des criminels
affaires.json         ← Données sauvegardées des affaires


## 📄 Fonctionnalités

- ✅ Ajout/modification/suppression de criminels
- ✅ Création d'affaires avec date, lieu, description
- ✅ Association et dissociation de criminels aux affaires
- ✅ Interfaces dynamiques et intuitives
- ✅ Sauvegarde automatique au format JSON
- ✅ Récupération des liens entre données à l'ouverture

## 🔧 Dépendances

- `Gson` pour sérialisation JSON
- Java 17+ (ou 11+ si bien configuré)