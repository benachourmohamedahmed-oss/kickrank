# Guide complet pour lancer KickRank sur un autre PC

Ce dossier contient le projet KickRank complet :

- Backend : Spring Boot
- Frontend : Angular
- Base de donnees : Supabase PostgreSQL

Le frontend ne se connecte pas directement a Supabase. Le fonctionnement est :

```text
Angular http://localhost:4200
        |
        v
Spring Boot http://localhost:8080/api
        |
        v
Supabase PostgreSQL
```

## 1. Logiciels necessaires

Sur le PC, il faut installer :

1. Java 21
2. Maven
3. Node.js
4. Angular CLI, optionnel mais recommande

Pour verifier :

```cmd
java -version
mvn -v
node -v
npm -v
```

Si Angular CLI n'est pas installe :

```cmd
npm install -g @angular/cli
```

## 2. Ouvrir le projet

Decompresser le fichier ZIP, puis ouvrir le dossier :

```text
kickrank
```

dans VS Code ou IntelliJ.

## 3. Configurer Supabase

Cette etape est obligatoire seulement si vous voulez utiliser la base Supabase.
Sans fichier `.env`, le backend demarre avec une base H2 locale temporaire pour tester rapidement.

Dans le dossier :

```text
kickrank\backend
```

copier le fichier :

```text
.env.example
```

et le renommer :

```text
.env
```

Ensuite remplir `.env` avec les informations Supabase.

Exemple :

```env
DB_URL=jdbc:postgresql://aws-0-eu-west-1.pooler.supabase.com:5432/postgres?sslmode=require
DB_USERNAME=postgres.fctqwiffnajktbyngpjb
DB_PASSWORD=mettre_ici_le_mot_de_passe_supabase
DB_DRIVER=org.postgresql.Driver

JWT_SECRET=change-this-secret-with-at-least-32-characters
JWT_EXPIRATION_MINUTES=1440

ADMIN_EMAIL=admin@kickrank.local
ADMIN_PASSWORD=Admin12345
```

Important :

- `DB_PASSWORD` est le mot de passe database Supabase.
- Si le mot de passe est oublie, il faut le reset depuis Supabase.
- Ne pas mettre les infos Supabase dans Angular.

## 4. Lancer le backend

Ouvrir un terminal :

```cmd
cd chemin\vers\kickrank\backend
run-dev.cmd
```

Si tout marche, Spring Boot demarre sur :

```text
http://localhost:8080/api
```

Test rapide dans le navigateur :

```text
http://localhost:8080/api/leaderboard
```

## 5. Lancer le frontend

Ouvrir un deuxieme terminal :

```cmd
cd chemin\vers\kickrank\frontend
run-dev.cmd
```

Le frontend demarre sur :

```text
http://localhost:4200
```

## 6. Compte admin

Au premier lancement du backend, un compte admin est cree automatiquement :

```text
email: admin@kickrank.local
password: Admin12345
```

Avec ce compte, on peut :

- voir le classement
- valider les candidatures organisateur/observateur
- tester les roles

## 7. Verifier les tables Supabase

Dans Supabase :

1. Ouvrir le projet.
2. Aller dans Table Editor.
3. Verifier que les tables existent :

```text
users
user_roles
matches
participations
role_applications
```

Ces tables sont creees automatiquement par Spring Boot/Hibernate.

## 8. Probleme courant : port 8080 deja utilise

Si le backend affiche :

```text
Port 8080 was already in use
```

Faire :

```cmd
netstat -ano | findstr :8080
```

Lire le PID a droite, puis :

```cmd
taskkill /PID LE_PID /F
```

Exemple :

```cmd
taskkill /PID 1876 /F
```

Puis relancer :

```cmd
run-dev.cmd
```

## 9. Probleme courant : mauvais mot de passe Supabase

Si le backend refuse la connexion a PostgreSQL :

1. Verifier `DB_PASSWORD` dans `backend\.env`.
2. Si le mot de passe est oublie, aller dans Supabase > Project Settings > Database.
3. Reset le database password.
4. Remettre le nouveau mot de passe dans `.env`.
5. Relancer le backend.

## 10. Tester le projet

Scenario simple :

1. Lancer backend.
2. Lancer frontend.
3. Ouvrir `http://localhost:4200`.
4. Se connecter avec l'admin.
5. Creer un compte joueur.
6. Envoyer une candidature organisateur ou observateur.
7. L'admin approuve la candidature.
8. Creer un match.
9. Les joueurs rejoignent le match.
10. Quand le match est complet, les equipes sont generees.

## 11. Structure du projet

Backend :

```text
backend/src/main/java/com/kickrank
  auth
  common
  config
  match
  security
  user
```

Frontend :

```text
frontend/src/app
  core
  features
```

## 12. Commandes rapides

Backend :

```cmd
cd kickrank\backend
run-dev.cmd
```

Frontend :

```cmd
cd kickrank\frontend
run-dev.cmd
```

URLs :

```text
Backend:  http://localhost:8080/api
Frontend: http://localhost:4200
```
