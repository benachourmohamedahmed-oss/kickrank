# KickRank

Plateforme web de gestion et de classement de matchs de football amateur avec ELO, creation de matchs casual/ranked, generation automatique d'equipes equilibrees et validation par observateur.

## Structure

- `backend/` : API REST Spring Boot 3, Spring Security, JWT, JPA.
- `frontend/` : application Angular standalone organisee par features.

## Lancement backend

```bash
cd backend
mvn spring-boot:run
```

Par defaut, l'API demarre sur `http://localhost:8080/api`.

La configuration utilise PostgreSQL si les variables suivantes existent, sinon vous pouvez adapter `backend/src/main/resources/application.yml` :

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET`

## Lancement frontend

```bash
cd frontend
npm install
npm start
```

L'application Angular demarre sur `http://localhost:4200`.

## Comptes et roles

Les roles prevus sont :

- `PLAYER`
- `ORGANIZER`
- `OBSERVER`
- `ADMIN`

Les organisateurs et observateurs doivent etre verifies pour les actions ranked.

## Admin de developpement

Au premier lancement, le backend cree un administrateur si aucun compte avec cet email n'existe :

- email : `admin@kickrank.local`
- mot de passe : `Admin12345`

Vous pouvez changer ces valeurs avec `ADMIN_EMAIL` et `ADMIN_PASSWORD`.

## Partager le projet avec un autre PC

1. Envoyez le dossier `kickrank` via GitHub ou ZIP.
2. Ne partagez jamais `backend/.env` s'il contient un vrai mot de passe.
3. Sur l'autre PC, dans `backend`, copiez `.env.example` vers `.env`.
4. Remplissez `DB_PASSWORD` avec le mot de passe Supabase.
5. Lancez le backend :

```cmd
cd backend
run-dev.cmd
```

6. Dans un deuxieme terminal, lancez Angular :

```cmd
cd frontend
run-dev.cmd
```

Le backend tourne sur `http://localhost:8080/api` et le frontend sur `http://localhost:4200`.
