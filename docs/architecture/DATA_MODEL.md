# TopRopes Backend Data Model

## Current Runtime

The current `ring-rumble-react` runtime uses Supabase. The tables below are the frontend's active persistence contract; they are not the same schema as the Spring Boot database.

Authentication is username-first in the UI, but Supabase Auth receives an internal derived email address. The Supabase user UUID is used in all ownership columns.

## Active Supabase Schema

- `profiles`: public username mapped to a Supabase Auth user ID.
- `user_roles`: role assignments using `admin`, `moderator`, or `user`.
- `admin_events`, `admin_matches`, `admin_wrestlers`: public editorial catalogue records.
- `catalogue_seed`: one-time marker for importing the in-repo editorial seed.
- `match_ratings`, `feud_dossiers`, `feud_promos`: user-owned content.

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

- Catalogue tables are publicly readable through Supabase RLS.
- In the current development mode, authenticated users can insert their own catalogue records. Updates and deletes require being the original `created_by` user or holding the `admin` role.
- `match_ratings`, `feud_dossiers`, and `feud_promos` are RLS-scoped to `auth.uid()`.
- User-content keys are `(user_id, match_slug)`, `(user_id, feud_slug)`, and `(user_id, feud_slug, promo_slug)`.
- Catalogue slugs are unique. `participants` and `key_developments` are JSON values.

## UI-only and Fallback Data

- `events-data`, `matches-data`, `roster-data`, and `feud-data` still provide static editorial fallback data for SSR/offline behavior.
- The Supabase catalogue deliberately does not store every UI field. Wrestler portrait color and initials, match sides and entrants, and some detailed wrestler and feud presentation data may be enriched from static modules.
- The application imports the static event, match, and wrestler catalogue once through the `catalogue_seed` marker. Deleting an imported row removes it from subsequent live catalogue reads.

## Backend Migration Requirements

- Preserve all active table fields and the slug-based user-content keys in the REST DTOs.
- Add the missing editorial CRUD and wrestler fields before changing the frontend data source.
- Define a Supabase-user-to-backend-user mapping or accept validated Supabase tokens before migrating authenticated content.
