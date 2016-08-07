CREATE TABLE IF NOT EXISTS entry (
  id serial PRIMARY KEY,
  content varchar(1024),
  creation_date timestamp
);