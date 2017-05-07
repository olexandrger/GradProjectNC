ALTER TABLE "product_characteristic"
  DROP CONSTRAINT "Ref_product_characteristic_to_data_type";

DROP TABLE "data_type";

ALTER TABLE "product_characteristic" ADD CONSTRAINT "Ref_product_characteristic_to_data_type" FOREIGN KEY ("data_type_id")
	REFERENCES "category" ("category_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

INSERT INTO "category_type" ("category_type_name") VALUES
  ('DATA_TYPE');

INSERT INTO "category" ("category_name", "category_type_id") VALUES
    ('NUMBER', 7),
    ('DATE', 7),
    ('STRING', 7);


ALTER TABLE "location"
  DROP CONSTRAINT "Ref_location_to_google_region";

DROP TABLE "google_region";

ALTER TABLE "location"
  RENAME COLUMN "google_region_id" TO "region_id";

ALTER TABLE "location" ADD CONSTRAINT "Ref_location_to_region" FOREIGN KEY ("region_id")
REFERENCES "region"("region_id")
MATCH SIMPLE
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;


CREATE TABLE "report" (
  "report_id" SERIAL NOT NULL,
  "report_name" VARCHAR(60) NOT NULL UNIQUE,
  "report_description" VARCHAR(240),
  "report_script" VARCHAR(480) NOT NULL,
  "parameters" JSON,
  PRIMARY KEY("report_id")
);


ALTER TABLE "role" ADD UNIQUE("role_name");
ALTER TABLE "region" ADD UNIQUE("region_name");
ALTER TABLE "product" ADD UNIQUE("product_name");
ALTER TABLE "product_type" ADD UNIQUE("product_type_name");
ALTER TABLE "category_type" ADD UNIQUE("category_type_name");


ALTER TABLE "discount"
    ALTER COLUMN "start_date" TYPE timestamp(0) with time zone,
    ALTER COLUMN "end_date" TYPE timestamp(0) with time zone;

ALTER TABLE "complain"
  ALTER COLUMN "open_date" TYPE timestamp(0) with time zone,
  ALTER COLUMN "close_date" TYPE timestamp(0) with time zone;

ALTER TABLE "product_order"
  ALTER COLUMN "open_date" TYPE timestamp(0) with time zone,
  ALTER COLUMN "close_date" TYPE timestamp(0) with time zone;


ALTER TABLE "product"
  ALTER COLUMN "is_active" SET NOT NULL;


DELETE FROM "category"
  WHERE "category_name"='CANCEL' AND "category_type_id"=4;

INSERT INTO "category" ("category_name", "category_type_id") VALUES
  ('RESUME', 4),
  ('MODIFY', 4);


ALTER TABLE "product_type"
  ADD COLUMN "is_active" BOOLEAN NOT NULL;