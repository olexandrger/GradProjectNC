-- =============================================================================
-- Diagram Name: erd_v3_3
-- Created on: 29.04.2017 2:09:36
-- Diagram Version: 
-- =============================================================================
CREATE TABLE "role" (
	"role_id" SERIAL NOT NULL,
	"role_name" varchar(60) NOT NULL,
	PRIMARY KEY("role_id")
);

CREATE TABLE "user_role" (
	"user_id" int4 NOT NULL,
	"role_id" int4 NOT NULL,
	PRIMARY KEY("user_id","role_id")
);

CREATE TABLE "user" (
	"user_id" SERIAL NOT NULL,
	"email" varchar(120) NOT NULL,
	"password" varchar(60) NOT NULL,
	"first_name" varchar(60) NOT NULL,
	"last_name" varchar(60) NOT NULL,
	"phone_number" varchar(30),
	"account_id" int4 NOT NULL,
	PRIMARY KEY("user_id")
);

CREATE TABLE "domain" (
	"domain_id" SERIAL NOT NULL,
	"domain_name" varchar(120) NOT NULL,
	"address_id" int4 NOT NULL,
	"domain_type_id" int4 NOT NULL,
	PRIMARY KEY("domain_id")
);

CREATE TABLE "user_domain" (
	"user_id" int4 NOT NULL,
	"domain_id" int4 NOT NULL,
	PRIMARY KEY("user_id","domain_id")
);

CREATE TABLE "address" (
	"address_id" SERIAL NOT NULL,
	"apartment_number" varchar(30) NOT NULL,
	"location_id" int4 NOT NULL,
	PRIMARY KEY("address_id")
);

CREATE TABLE "location" (
	"location_id" SERIAL NOT NULL,
	"google_place_id" varchar(60) NOT NULL,
	"google_region_id" int4 NOT NULL,
	PRIMARY KEY("location_id")
);

CREATE TABLE "google_region" (
	"google_region_id" SERIAL NOT NULL,
	"google_region_name" varchar(120) NOT NULL,
	"region_id" int4 NOT NULL,
	PRIMARY KEY("google_region_id")
);

CREATE TABLE "region" (
	"region_id" SERIAL NOT NULL,
	"region_name" varchar(60) NOT NULL,
	PRIMARY KEY("region_id")
);

CREATE TABLE "product_region_price" (
	"price_id" SERIAL NOT NULL,
	"product_id" int4 NOT NULL,
	"region_id" int4 NOT NULL,
	"price" numeric,
	PRIMARY KEY("price_id")
);

CREATE TABLE "discount" (
	"discount_id" SERIAL NOT NULL,
	"discount_title" varchar(60) NOT NULL,
	"discount" numeric NOT NULL,
	"start_date" timestamp(0) NOT NULL,
	"end_date" timestamp(0) NOT NULL,
	PRIMARY KEY("discount_id")
);

CREATE TABLE "discount_price" (
	"discount_id" int4 NOT NULL,
	"price_id" int4 NOT NULL,
	PRIMARY KEY("discount_id","price_id")
);

CREATE TABLE "product_type" (
	"product_type_id" SERIAL NOT NULL,
	"product_type_name" varchar(60) NOT NULL,
	"product_type_description" varchar(240),
	PRIMARY KEY("product_type_id")
);

CREATE TABLE "product_characteristic" (
	"product_characteristic_id" SERIAL NOT NULL,
	"product_type_id" int4 NOT NULL,
	"characteristic_name" varchar(60) NOT NULL,
	"measure" varchar(30) NOT NULL,
	"data_type_id" int4 NOT NULL,
	PRIMARY KEY("product_characteristic_id")
);

CREATE TABLE "product" (
	"product_id" SERIAL NOT NULL,
	"product_name" varchar(60) NOT NULL,
	"product_description" varchar(240),
	"product_type_id" int4 NOT NULL,
	PRIMARY KEY("product_id")
);

CREATE TABLE "data_type" (
	"data_type_id" SERIAL NOT NULL,
	"data_type" varchar(30) NOT NULL,
	PRIMARY KEY("data_type_id")
);

CREATE TABLE "product_characteristic_value" (
	"product_id" int4 NOT NULL,
	"product_characteristic_id" int4 NOT NULL,
	"number_value" numeric,
	"date_value" timestamp(0),
	"string_value" varchar,
	PRIMARY KEY("product_id","product_characteristic_id")
);

CREATE TABLE "product_instance" (
	"instance_id" SERIAL NOT NULL,
	"price_id" int4 NOT NULL,
	"domain_id" int4 NOT NULL,
	"status_id" int4 NOT NULL,
	PRIMARY KEY("instance_id")
);

CREATE TABLE "complain" (
	"complain_id" SERIAL NOT NULL,
	"user_id" int4 NOT NULL,
	"product_instance_id" int4,
	"complain_title" varchar(60) NOT NULL,
	"content" varchar(240),
	"status_id" int4 NOT NULL,
	"responsible_id" int4,
	"response" varchar(240),
	"open_date" timestamp(0) NOT NULL,
	"close_date" timestamp(0),
	"complain_reason_id" int4 NOT NULL,
	PRIMARY KEY("complain_id")
);

CREATE TABLE "domain_type" (
	"domain_type_id" SERIAL NOT NULL,
	"domain_type_name" varchar(30) NOT NULL,
	PRIMARY KEY("domain_type_id")
);

CREATE TABLE "product_order" (
	"product_order_id" SERIAL NOT NULL,
	"product_instance_id" int4 NOT NULL,
	"user_id" int4 NOT NULL,
	"order_aim_id" int4 NOT NULL,
	"status_id" int4 NOT NULL,
	"responsible_id" int4,
	"open_date" timestamp(0) NOT NULL,
	"close_date" timestamp(0),
	PRIMARY KEY("product_order_id")
);

CREATE TABLE "category" (
	"category_id" SERIAL NOT NULL,
	"category_name" varchar(60) NOT NULL,
	"category_type_id" int4 NOT NULL,
	PRIMARY KEY("category_id")
);

CREATE TABLE "category_type" (
	"category_type_id" SERIAL NOT NULL,
	"category_type_name" varchar(30) NOT NULL,
	PRIMARY KEY("category_type_id")
);


ALTER TABLE "user_role" ADD CONSTRAINT "Ref_account_role_to_role" FOREIGN KEY ("role_id")
	REFERENCES "role"("role_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "user_role" ADD CONSTRAINT "Ref_user_role_to_user" FOREIGN KEY ("user_id")
	REFERENCES "user"("user_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "domain" ADD CONSTRAINT "Ref_domain_to_address" FOREIGN KEY ("address_id")
	REFERENCES "address"("address_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "domain" ADD CONSTRAINT "Ref_domain_to_domain_type" FOREIGN KEY ("domain_type_id")
	REFERENCES "domain_type"("domain_type_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "user_domain" ADD CONSTRAINT "Ref_user_domain_to_user" FOREIGN KEY ("user_id")
	REFERENCES "user"("user_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "user_domain" ADD CONSTRAINT "Ref_user_domain_to_domain" FOREIGN KEY ("domain_id")
	REFERENCES "domain"("domain_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "address" ADD CONSTRAINT "Ref_address_to_location" FOREIGN KEY ("location_id")
	REFERENCES "location"("location_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "location" ADD CONSTRAINT "Ref_location_to_google_region" FOREIGN KEY ("google_region_id")
	REFERENCES "google_region"("google_region_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "google_region" ADD CONSTRAINT "Ref_google_region_to_region" FOREIGN KEY ("region_id")
	REFERENCES "region"("region_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "product_region_price" ADD CONSTRAINT "Ref_product_region_price_to_region" FOREIGN KEY ("region_id")
	REFERENCES "region"("region_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "product_region_price" ADD CONSTRAINT "Ref_product_region_price_to_product" FOREIGN KEY ("product_id")
	REFERENCES "product"("product_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "discount_price" ADD CONSTRAINT "Ref_discount_price_to_product_region_price" FOREIGN KEY ("price_id")
	REFERENCES "product_region_price"("price_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "discount_price" ADD CONSTRAINT "Ref_discount_price_to_discount" FOREIGN KEY ("discount_id")
	REFERENCES "discount"("discount_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "product_characteristic" ADD CONSTRAINT "Ref_product_characteristic_to_product_type" FOREIGN KEY ("product_type_id")
	REFERENCES "product_type"("product_type_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "product_characteristic" ADD CONSTRAINT "Ref_product_characteristic_to_data_type" FOREIGN KEY ("data_type_id")
	REFERENCES "data_type"("data_type_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "product" ADD CONSTRAINT "Ref_product_to_product_type" FOREIGN KEY ("product_type_id")
	REFERENCES "product_type"("product_type_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "product_characteristic_value" ADD CONSTRAINT "Ref_product_characteristic_value_to_product" FOREIGN KEY ("product_id")
	REFERENCES "product"("product_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "product_characteristic_value" ADD CONSTRAINT "Ref_product_characteristic_value_to_product_characteristic" FOREIGN KEY ("product_characteristic_id")
	REFERENCES "product_characteristic"("product_characteristic_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "product_instance" ADD CONSTRAINT "Ref_product_instance_to_domain" FOREIGN KEY ("domain_id")
	REFERENCES "domain"("domain_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "product_instance" ADD CONSTRAINT "Ref_product_instance_to_product_region_price" FOREIGN KEY ("price_id")
	REFERENCES "product_region_price"("price_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "product_instance" ADD CONSTRAINT "Ref_product_instance_to_category" FOREIGN KEY ("status_id")
	REFERENCES "category"("category_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "complain" ADD CONSTRAINT "Ref_complain_to_user" FOREIGN KEY ("user_id")
	REFERENCES "user"("user_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "complain" ADD CONSTRAINT "Ref_complain_to_responsible" FOREIGN KEY ("responsible_id")
	REFERENCES "user"("user_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "complain" ADD CONSTRAINT "Ref_complain_to_product_instance" FOREIGN KEY ("product_instance_id")
	REFERENCES "product_instance"("instance_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "complain" ADD CONSTRAINT "Ref_complain_status_to_category" FOREIGN KEY ("status_id")
	REFERENCES "category"("category_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "complain" ADD CONSTRAINT "Ref_complain_to_category" FOREIGN KEY ("complain_reason_id")
	REFERENCES "category"("category_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "product_order" ADD CONSTRAINT "Ref_product_order_to_user" FOREIGN KEY ("user_id")
	REFERENCES "user"("user_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "product_order" ADD CONSTRAINT "Ref_product_order_to_responsible" FOREIGN KEY ("responsible_id")
	REFERENCES "user"("user_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "product_order" ADD CONSTRAINT "Ref_product_order_to_product_instance" FOREIGN KEY ("product_instance_id")
	REFERENCES "product_instance"("instance_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "product_order" ADD CONSTRAINT "Ref_product_order_aim_to_category" FOREIGN KEY ("order_aim_id")
	REFERENCES "category"("category_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "product_order" ADD CONSTRAINT "Ref_product_order_status_to_category" FOREIGN KEY ("status_id")
	REFERENCES "category"("category_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE "category" ADD CONSTRAINT "Ref_category_to_category_type" FOREIGN KEY ("category_type_id")
	REFERENCES "category_type"("category_type_id")
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;


