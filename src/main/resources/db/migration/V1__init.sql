CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE app_user (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  username varchar(24) NOT NULL,
  password_hash varchar(255) NOT NULL,
  role varchar(32) NOT NULL DEFAULT 'ROLE_USER',
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX app_user_username_lower_uk ON app_user (lower(username));

CREATE TABLE refresh_token (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id uuid NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  token_hash varchar(128) NOT NULL,
  expires_at timestamptz NOT NULL,
  revoked boolean NOT NULL DEFAULT false,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX refresh_token_hash_uk ON refresh_token (token_hash);
CREATE INDEX refresh_token_user_idx ON refresh_token (user_id);

CREATE TABLE promotion (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  code varchar(16) NOT NULL,
  name varchar(100) NOT NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX promotion_code_uk ON promotion (code);

CREATE TABLE wrestler (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  slug varchar(120) NOT NULL,
  name varchar(160) NOT NULL,
  promotion_code varchar(16) NOT NULL,
  tag varchar(200),
  initials varchar(8),
  image_url varchar(500),
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX wrestler_slug_uk ON wrestler (slug);
CREATE INDEX wrestler_promotion_idx ON wrestler (promotion_code);

CREATE TABLE event (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  slug varchar(120) NOT NULL,
  name varchar(200) NOT NULL,
  promotion_code varchar(16) NOT NULL,
  type varchar(32) NOT NULL,
  event_date date NOT NULL,
  display_date varchar(64),
  venue varchar(200),
  location varchar(200),
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX event_slug_uk ON event (slug);
CREATE INDEX event_promotion_idx ON event (promotion_code);
CREATE INDEX event_date_idx ON event (event_date DESC);

CREATE TABLE feud (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  slug varchar(120) NOT NULL,
  a_name varchar(160),
  a_tag varchar(200),
  b_name varchar(160),
  b_tag varchar(200),
  status varchar(200) NOT NULL,
  heat smallint NOT NULL DEFAULT 0,
  updated_label varchar(120),
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX feud_slug_uk ON feud (slug);

CREATE TABLE match_card (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  slug varchar(120) NOT NULL,
  name varchar(220) NOT NULL,
  event_label varchar(200) NOT NULL,
  event_slug varchar(120),
  feud_slug varchar(120),
  date_label varchar(120) NOT NULL,
  promotion varchar(16) NOT NULL,
  type varchar(120) NOT NULL,
  stipulation varchar(240),
  duration varchar(32),
  winner varchar(160),
  card varchar(120),
  participants jsonb NOT NULL DEFAULT '[]'::jsonb,
  sides jsonb NOT NULL DEFAULT '[]'::jsonb,
  entrants jsonb NOT NULL DEFAULT '[]'::jsonb,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX match_card_slug_uk ON match_card (slug);
CREATE INDEX match_card_event_slug_idx ON match_card (event_slug);
CREATE INDEX match_card_feud_slug_idx ON match_card (feud_slug);
CREATE INDEX match_card_promotion_idx ON match_card (promotion);

CREATE TABLE match_rating (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id uuid NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  match_slug varchar(120) NOT NULL,
  crowd smallint NOT NULL DEFAULT 0,
  story smallint NOT NULL DEFAULT 0,
  difficulty smallint NOT NULL DEFAULT 0,
  technique smallint NOT NULL DEFAULT 0,
  personal_stars numeric(2,1) NOT NULL DEFAULT 0,
  review text NOT NULL DEFAULT '',
  notes jsonb NOT NULL DEFAULT '{}'::jsonb,
  updated_at timestamptz NOT NULL DEFAULT now(),
  created_at timestamptz NOT NULL DEFAULT now(),
  UNIQUE (user_id, match_slug)
);

CREATE INDEX match_rating_user_idx ON match_rating (user_id);

CREATE TABLE feud_dossier (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id uuid NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  feud_slug varchar(120) NOT NULL,
  participants text NOT NULL DEFAULT '',
  cause text NOT NULL DEFAULT '',
  motivation_a text NOT NULL DEFAULT '',
  motivation_b text NOT NULL DEFAULT '',
  background text NOT NULL DEFAULT '',
  stakes text NOT NULL DEFAULT '',
  current_status text NOT NULL DEFAULT '',
  resolution text NOT NULL DEFAULT '',
  key_developments jsonb NOT NULL DEFAULT '[]'::jsonb,
  heat smallint NOT NULL DEFAULT 0,
  updated_at timestamptz NOT NULL DEFAULT now(),
  created_at timestamptz NOT NULL DEFAULT now(),
  UNIQUE (user_id, feud_slug)
);

CREATE INDEX feud_dossier_user_idx ON feud_dossier (user_id);

CREATE TABLE feud_promo (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id uuid NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  feud_slug varchar(120) NOT NULL,
  promo_slug varchar(120) NOT NULL,
  title text NOT NULL DEFAULT '',
  speaker text NOT NULL DEFAULT '',
  promo_date date,
  venue text NOT NULL DEFAULT '',
  quote text NOT NULL DEFAULT '',
  transcript text NOT NULL DEFAULT '',
  impact text NOT NULL DEFAULT '',
  updated_at timestamptz NOT NULL DEFAULT now(),
  created_at timestamptz NOT NULL DEFAULT now(),
  UNIQUE (user_id, feud_slug, promo_slug)
);

CREATE INDEX feud_promo_user_idx ON feud_promo (user_id);

CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN
  NEW.updated_at = now();
  RETURN NEW;
END;
$$;

CREATE TRIGGER app_user_set_updated_at BEFORE UPDATE ON app_user FOR EACH ROW EXECUTE FUNCTION set_updated_at();
CREATE TRIGGER promotion_set_updated_at BEFORE UPDATE ON promotion FOR EACH ROW EXECUTE FUNCTION set_updated_at();
CREATE TRIGGER wrestler_set_updated_at BEFORE UPDATE ON wrestler FOR EACH ROW EXECUTE FUNCTION set_updated_at();
CREATE TRIGGER event_set_updated_at BEFORE UPDATE ON event FOR EACH ROW EXECUTE FUNCTION set_updated_at();
CREATE TRIGGER feud_set_updated_at BEFORE UPDATE ON feud FOR EACH ROW EXECUTE FUNCTION set_updated_at();
CREATE TRIGGER match_card_set_updated_at BEFORE UPDATE ON match_card FOR EACH ROW EXECUTE FUNCTION set_updated_at();
CREATE TRIGGER match_rating_set_updated_at BEFORE UPDATE ON match_rating FOR EACH ROW EXECUTE FUNCTION set_updated_at();
CREATE TRIGGER feud_dossier_set_updated_at BEFORE UPDATE ON feud_dossier FOR EACH ROW EXECUTE FUNCTION set_updated_at();
CREATE TRIGGER feud_promo_set_updated_at BEFORE UPDATE ON feud_promo FOR EACH ROW EXECUTE FUNCTION set_updated_at();
