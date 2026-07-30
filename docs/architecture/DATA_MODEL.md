# TopRopes Backend Data Model

## Design Principle

Data ownership moves to backend-controlled PostgreSQL schemas. Frontend is a consumer, not an authority.

## Schema Split

- `catalog` schema: canonical wrestling content.
- `identity` schema: users and access metadata.
- `community` schema: user-generated ratings and feud authoring content.

## Entity Relationship Diagram

```mermaid
%%{init: {'theme': 'neutral'}}%%
erDiagram
  users ||--o{ match_ratings : owns
  users ||--o{ feud_dossiers : owns
  users ||--o{ feud_promos : owns

  promotions ||--o{ events : organizes
  events ||--o{ matches : contains
  feuds ||--o{ feud_participants : has
  wrestlers ||--o{ feud_participants : appears_in
  matches ||--o{ match_participants : has
  wrestlers ||--o{ match_participants : appears_in
  feuds ||--o{ matches : linked_to

  users {
    uuid id PK
    varchar username UK
    varchar email UK
    varchar password_hash
    varchar role
    timestamptz created_at
    timestamptz updated_at
  }

  promotions {
    uuid id PK
    varchar code UK
    varchar name
  }

  wrestlers {
    uuid id PK
    varchar slug UK
    varchar name
    varchar promotion_code
    varchar tag
  }

  events {
    uuid id PK
    varchar slug UK
    varchar name
    date event_date
    varchar type
    uuid promotion_id FK
  }

  feuds {
    uuid id PK
    varchar slug UK
    varchar status
    smallint heat
    date updated_on
  }

  matches {
    uuid id PK
    varchar slug UK
    varchar name
    uuid event_id FK
    uuid feud_id FK
    varchar match_type
    varchar stipulation
    varchar winner
    integer duration_seconds
  }

  match_ratings {
    uuid id PK
    uuid user_id FK
    uuid match_id FK
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
    uuid feud_id FK
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
    uuid feud_id FK
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

## Constraints

- `match_ratings`: unique `(user_id, match_id)`.
- `feud_dossiers`: unique `(user_id, feud_id)`.
- `feud_promos`: unique `(user_id, feud_id, promo_slug)`.
- Slugs are unique for external API identity.

## Migration from Frontend Data

- Move `roster`, `events`, `matches`, and `feuds` from frontend modules into `catalog` tables.
- Preserve existing slugs to avoid route breaks.
- Backfill user records from existing auth provider during transition if needed.
