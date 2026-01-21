# DES Winter School 2026

This repository includes both [english :uk:](#des-winter-school-2026-uk) and [ukrainian :ukraine:](#des-winter-school-2026-ukraine) versions.

## DES Winter School 2026 :uk:

Project within the framework of [Data Engineering and Security Winter School 2026](https://des.lnu.edu.ua/)

MetricMind is a SaaS web platform for simplified Google Analytics 4 (GA4) data analysis for small businesses. It provides users with key traffic metrics and automatically generates a clear text analysis report based on GA4 data. The goal of the project is to reduce the time spent analyzing standard reports and provide basic recommendations for decision-making.

### Overview

MetricMind allows you to log in via Google, connect to GA4 property, and get up-to-date data for the last 7 or 30 days. The data is aggregated on the backend, analyzed, and accompanied by an AI report with explanations of changes and basic recommendations. All user actions are performed on a single page — the dashboard.

### Features

- Authorization via Google OAuth 2.0
- Connection to one GA4 property
- Retrieval and aggregation of key metrics: users, sessions, events, conversions, average engagement time, traffic sources
- Comparison with the previous period
- AI-generated text analytics report with insights and basic recommendations
- Display of results on a simplified dashboard

### Technologies Used

#### FrontEnd

- React
- HTML
- CSS
- JS

#### BackEnd

- Java
- Spring Boot

#### Tools

- Docker
- Docker Compose

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

1. Click "Login with Google" on the landing page.
2. Select the GA4 property for analysis.
3. Receive aggregated metrics and an AI report on the dashboard.
4. View data for the last 7 or 30 days.
5. All requests to Google Analytics 4 are executed on the backend; the frontend only receives aggregated and secure data.

### License & Community Guidelines

- [Docs](docs/) - project documentation.
- [License](LICENSE) — project license.
- [Code of Conduct](CODE_OF_CONDUCT.md) — expected behavior for contributors.
- [Contributing Guide](CONTRIBUTING.md) — how to help the project.
- [Security Policy](SECURITY.md) — reporting security issues.

---

## DES Winter School 2026 :ukraine:

Проєкт у межах [Data Engineering and Security Winter School 2026](https://des.lnu.edu.ua/)

MetricMind — це SaaS-веб-платформа для спрощеного аналізу даних Google Analytics 4 (GA4) для малого бізнесу. Вона надає користувачу ключові метрики відвідуваності та автоматично формує зрозумілий текстовий аналітичний звіт на основі даних GA4. Мета проєкту — скоротити час на аналіз стандартних звітів і дати базові рекомендації для прийняття рішень.

### Огляд

MetricMind дозволяє авторизуватися через Google, підключитися до GA4 property і отримати актуальні дані за останні 7 або 30 днів. Дані агрегуються на бекенді, аналізуються та супроводжуються AI-звітом з поясненнями змін і базовими рекомендаціями. Усі дії користувача виконуються на одній сторінці — дашборді.

### Функціонал

- Авторизація через Google OAuth 2.0
- Підключення до однієї GA4 property
- Отримання та агрегація основних метрик: користувачі, сесії, події, конверсії, середній час залученості, джерела трафіку
- Порівняння з попереднім періодом
- AI-генерація текстового аналітичного звіту з інсайтами та базовими рекомендаціями
- Відображення результатів на спрощеному дашборді

### Використані Технології

#### FrontEnd

- React
- HTML
- CSS
- JS

#### BackEnd

- Java
- Spring Boot

#### Інструменти

- Docker
- Docker Compose

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

1. Натискає «Login with Google» на лендінгу.
2. Обирає GA4 property для аналізу.
3. Отримує агреговані метрики та AI-звіт на дашборді.
4. Може переглядати дані за останні 7 або 30 днів.
5. Усі запити до Google Analytics 4 виконуються на бекенді, фронтенд отримує лише агреговані та безпечні дані.

### Ліцензія та правила спільноти

- [Документація](docs/) - документація проєкту.
- [License](LICENSE) — ліцензія проєкту.
- [Code of Conduct](CODE_OF_CONDUCT.md) — очікувана поведінка для контриб’юторів.
- [Contributing Guide](CONTRIBUTING.md) — як допомогти проєкту.
- [Security Policy](SECURITY.md) — повідомлення про проблеми безпеки.
