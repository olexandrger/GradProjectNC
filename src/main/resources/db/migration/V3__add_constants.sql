INSERT INTO "role" ("role_name") VALUES
    ('ADMIN'),
    ('CLIENT'),
    ('CSR'),
    ('PMG');

INSERT INTO "domain_type" ("domain_type_name") VALUES
    ('PRIVATE'),
    ('CORPORATIVE');

INSERT INTO "data_type" ("data_type") VALUES
    ('NUMBER'),
    ('DATE'),
    ('STRING');

INSERT INTO "status_type" ("status_type_name") VALUES
    ('PRODUCT_INSTANCE_STATUS'),
    ('COMPLAIN_STATUS'),
    ('PRODUCT_ORDER_STATUS');

INSERT INTO "status" ("status_name", "status_type_id") VALUES
    ('CREATED', 3),
    ('IN_PROGRESS', 3),
    ('CANCELLED', 3),
    ('COMPLETED', 3);

INSERT INTO "status" ("status_name", "status_type_id") VALUES
    ('CREATED', 2),
    ('UNDER_CONSIDERATION', 2),
    ('CONSIDERATION_COMPLETED', 2),
    ('REJECTED', 2);

INSERT INTO "status" ("status_name", "status_type_id") VALUES
    ('CREATED', 1),
    ('ACTIVATED', 1),
    ('SUSPENDED', 1),
    ('DEACTIVATED', 1);

INSERT INTO "category_type" ("category_type_name") VALUES
    ('PRODUCT_ORDER_CATEGORY'),
    ('COMPLAIN_CATEGORY');

INSERT INTO "category" ("category_name", "category_type_id") VALUES
    ('SERVICE_PROBLEMS', 2),
    ('ORDER_PROBLEMS', 2),
    ('PRODUCT_CHARACTERISTICS_PROBLEMS', 2),
    ('TECHNICAL_PROBLEMS', 2);