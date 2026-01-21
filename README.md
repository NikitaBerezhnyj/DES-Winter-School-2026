# DES Winter School 2026

This repository includes both [english :uk:](#des-winter-school-2026-uk) and [ukrainian :ukraine:](#des-winter-school-2026-ukraine) versions.

## DES Winter School 2026 :uk:

...

### Overview

...

### Features

- ...

### Technologies Used

#### FrontEnd

- React
- HTML
- CSS
- JS

#### BackEnd

- ...

#### Tools

- Docker

### Getting Started

To get started with DES Winter School 2026, follow this steps:

1. Clone the repo

```
git clone https://github.com/NikitaBerezhnyj/DES-Winter-School-2026.git
cd DES-Winter-School-2026
```

2. Create an .env file following the example in .env.example

3. Run it using Docker:

- For development environment:

  ```bash
  docker compose -f docker-compose.dev.yaml up --build
  ```

  - Frontend runs on: [http://localhost:5173](http://localhost:5173)
  - Backend runs on: [http://localhost:8080](http://localhost:8080)
  - Changes in the source code are reflected immediately (live reload enabled)

- For production environment:

  ```bash
  docker compose -f docker-compose.prod.yaml up --build
  ```

  - Frontend runs on: [http://localhost:80](http://localhost:80)
  - Backend runs on: [http://localhost:8080](http://localhost:8080)
  - Serves static files from the optimized build (/dist)

### Commit Conventions

We follow **Conventional Commits**:

```
<type>(<scope>): <subject>
```

Where **type** can be:

- `feat` — a new feature
- `fix` — a bug fix
- `docs` — documentation changes
- `style` — formatting, missing semicolons, etc.
- `refactor` — code refactoring
- `test` — adding or updating tests
- `chore` — build process or auxiliary tools

And **scope** can be:

- `frontend` - changes in FrontEnd
- `backend` - changes in BackEnd

**Examples:**

```
feat/navbar: add responsive navigation component
```

```
fix/login: correct validation error handling
```

```
docs: update README with Docker instructions
```

### Branch Naming

- Feature branches: `feat/<short-description>`
- Bug fixes: `fix/<short-description>`
- Hotfixes: `hotfix/<short-description>`
- Release branches: `release/<version>`

Main branch: `main` (production-ready)

### Usage

Once the project is running, you can:

1. ...

### License & Community Guidelines

- [License](LICENSE) — project license.
- [Code of Conduct](CODE_OF_CONDUCT.md) — expected behavior for contributors.
- [Contributing Guide](CONTRIBUTING.md) — how to help the project.
- [Security Policy](SECURITY.md) — reporting security issues.

---

## DES Winter School 2026 :ukraine:

...

### Огляд

...

### Функціонал

- ...

### Використані Технології

#### FrontEnd

- React
- HTML
- CSS
- JS

#### BackEnd

- ...

#### Інструменти

- Docker

### Початок роботи

Щоб почати працювати з DES Winter School 2026, слідуйте наступним крокам:

1. Клонуйте репозиторій:

```
git clone https://github.com/NikitaBerezhnyj/DES-Winter-School-2026.git
cd DES-Winter-School-2026
```

2. Створіть .env файл за прикладом .env.example

3. Запустіть проєкт за допомогою Docker:

- Для середовища розробки:

  ```bash
  docker compose -f docker-compose.dev.yaml up --build
  ```

  - Frontend запускається на: [http://localhost:5173](http://localhost:5173)
  - Backend запускається на: [http://localhost:8080](http://localhost:8080)
  - Зміни в коді відображаються одразу (live reload увімкнено)

- Для продакшн середовища:

  ```bash
  docker compose -f docker-compose.prod.yaml up --build
  ```

  - Frontend запускається на: [http://localhost:80](http://localhost:80)
  - Backend запускається на: [http://localhost:8080](http://localhost:8080)
  - Віддає статичні файли з оптимізованого збірки (/dist)

### Правила комітів

Ми дотримуємось **Conventional Commits**:

```
<type>(<scope>): <subject>
```

Де **type** може бути:

- `feat` — нова функціональність
- `fix` — виправлення багу
- `docs` — зміни в документації
- `style` — форматування, пропущені крапки з комою тощо
- `refactor` — рефакторинг коду
- `test` — додавання або оновлення тестів
- `chore` — процес збірки або допоміжні інструменти

А **scope** може бути:

- `frontend` — зміни у FrontEnd
- `backend` — зміни у BackEnd

**Приклади:**

```
feat(frontend):add responsive navigation component
```

```
fix(backend): correct validation error handling
```

```
docs: update README with Docker instructions
```

### Назви гілок

- Feature branches: `feat/<short-description>`
- Bug fixes: `fix/<short-description>`
- Hotfixes: `hotfix/<short-description>`
- Release branches: `release/<version>`

Головна гілка: `main` (готова до продакшну)

### Використання

Після запуску проєкту ви можете:

1. ...

### Ліцензія та правила спільноти

- [License](LICENSE) — ліцензія проєкту.
- [Code of Conduct](CODE_OF_CONDUCT.md) — очікувана поведінка для контриб’юторів.
- [Contributing Guide](CONTRIBUTING.md) — як допомогти проєкту.
- [Security Policy](SECURITY.md) — повідомлення про проблеми безпеки.

```

```
