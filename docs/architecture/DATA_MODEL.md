# TopRopes Backend Data Model

## Runtime

The `ring-rumble-react` runtime uses the Spring Boot API and the tables below are its persistence contract.

Authentication is username-first and the backend user UUID is used in all ownership columns.

## Active Schema

- `app_user`: public username, BCrypt password hash, and role.
- `promotion`, `event`, `match_card`, `wrestler`, and `feud`: editorial catalogue records.
- `match_rating`, `feud_dossier`, and `feud_promo`: user-owned content.

erDiagram
  profiles ||--o{ user_roles : has
  profiles ||--o{ admin_events : authors
  profiles ||--o{ admin_matches : authors
  profiles ||--o{ admin_wrestlers : authors
  profiles ||--o{ match_ratings : owns
  profiles ||--o{ feud_dossiers : owns
  profiles ||--o{ feud_promos : owns

  profiles {
  wrestlers ||--o{ match_participants : appears_in
    text username
    uuid id PK
    varchar password_hash
    varchar role
  user_roles {
    timestamptz updated_at
    uuid user_id FK
    app_role role
  promotions {
    uuid id PK
  admin_wrestlers {
    varchar name
    uuid created_by FK
  }

    uuid id PK
    varchar promotion
    varchar height
    varchar weight
    varchar hometown
    varchar finisher
    text bio
    text image_url
    varchar slug UK
    varchar name
  admin_events {
    varchar tag
    uuid created_by FK
  }

  events {
    varchar promotion
    uuid id PK
    varchar venue
    varchar location
    varchar broadcast_type
    varchar broadcast_date
    varchar network
    varchar commentary
    varchar name
    date event_date
  admin_matches {
    uuid promotion_id FK
    uuid created_by FK
  }

    varchar event_slug
    varchar event_name
    varchar promotion
    varchar match_type
    uuid event_id FK
    varchar duration
    uuid feud_id FK
    varchar card
    varchar match_date
    jsonb participants
    varchar stipulation
    varchar winner
    integer duration_seconds
  }

  match_ratings {
    uuid id PK
    uuid user_id FK
    varchar match_slug
    smallint crowd
    smallint story
    smallint difficulty
    smallint technique
    numeric personal_stars
    text review
    jsonb notes
    timestamptz updated_at
  }

  feud_dossiers {
    uuid id PK
    uuid user_id FK
    varchar feud_slug
    text participants
    text cause
    text motivation_a
    text motivation_b
    text background
    text stakes
    text current_status
    text resolution
    jsonb key_developments
    smallint heat
    timestamptz updated_at
  }

  feud_promos {
    uuid id PK
    uuid user_id FK
    varchar feud_slug
    varchar promo_slug
    varchar title
    varchar speaker
    date promo_date
    varchar venue
    text quote
    text transcript
    text impact
    timestamptz updated_at
  }
```

## Access and Constraints

- Catalogue data is publicly readable through REST endpoints.
- Catalogue mutation requires the backend `ROLE_ADMIN` role.
- User content is scoped to the authenticated user's UUID in service operations.
- User-content keys are `(user_id, match_slug)`, `(user_id, feud_slug)`, and `(user_id, feud_slug, promo_slug)`.
- Catalogue slugs are unique. `participants` and `key_developments` are JSON values.

## UI-only and Fallback Data

- `events-data`, `matches-data`, `roster-data`, and `feud-data` still provide static editorial fallback data for SSR/offline behavior.
- Some presentation fields, such as wrestler portrait colors, remain enriched from static modules.

## Backend Contract

- REST DTOs preserve the slug-based user-content keys.
- Editorial CRUD, extended wrestler fields, and catalog image uploads are implemented by the backend.
