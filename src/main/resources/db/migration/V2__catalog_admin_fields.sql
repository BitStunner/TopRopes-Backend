ALTER TABLE event
  ADD COLUMN broadcast_type varchar(120),
  ADD COLUMN broadcast_date varchar(64),
  ADD COLUMN network varchar(120),
  ADD COLUMN commentary varchar(500);

ALTER TABLE wrestler
  ADD COLUMN height varchar(64),
  ADD COLUMN weight varchar(64),
  ADD COLUMN hometown varchar(160),
  ADD COLUMN finisher varchar(200),
  ADD COLUMN bio text;