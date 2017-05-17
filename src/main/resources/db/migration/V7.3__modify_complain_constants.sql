DELETE FROM "category" WHERE "category_name" = 'REJECTED' AND "category_type_id" = 2;

INSERT INTO "category" ("category_name", "category_type_id") VALUES
  ('OTHER', 5);