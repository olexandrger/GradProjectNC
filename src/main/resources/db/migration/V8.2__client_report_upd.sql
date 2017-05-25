CREATE OR REPLACE FUNCTION posithion_of_resum_in_this_mounths(order_id   NUMERIC, product_id NUMERIC,
                                                              order_date TIMESTAMP)
  RETURNS NUMERIC AS $$
DECLARE i NUMERIC;
BEGIN
  WITH resum_orders_by_product_of_this_mounth AS (
      SELECT
        row_number()
        OVER () number,
        product_order_id
      FROM product_order po
      WHERE po.product_instance_id = product_id
            AND date_part('month', order_date) = date_part('month', po.close_date)
            AND date_part('year', order_date) = date_part('year', po.close_date)
            AND po.order_aim_id = 25
            AND po.status_id = 4
      ORDER BY po.close_date ASC
  )
  SELECT ro.number
  INTO i
  FROM resum_orders_by_product_of_this_mounth ro
  WHERE ro.product_order_id = order_id;
  RETURN i;
END;$$
LANGUAGE plpgsql;

SELECT *
FROM report;

SELECT ('2200-01-01 00:00:00' :: TIMESTAMP);

SELECT *
FROM product_order
WHERE open_date <= '2200-01-01 00:00:00' :: TIMESTAMP;

DELETE FROM report
WHERE report_name = 'Client orders report';

INSERT INTO report (report_name, report_description, report_script, parameters) VALUES
  ('Client orders report',

   'The report
should contain following information: total amount of orders and list of
orders with: order number; product ordered; price; date of order.',

   'WITH completed_user_orders AS (
    SELECT
      po.product_order_id       AS "order_number",
      aim.category_id           AS "order_aim_Id",
      aim.category_name         AS "order_aim",
      po.open_date              AS "open_date",
      po.close_date             AS "close_date",
      prod.product_name         AS "product",
      pi.instance_id            AS "instance_id",
      CASE
      WHEN (po.order_aim_id = 13)
        THEN prp.price
      WHEN (po.order_aim_id = 25 AND
            posithion_of_resum_in_this_mounths(po.product_order_id, po.product_instance_id, po.close_date) = 1)
        THEN prp.price
      ELSE 0
      END                       AS "default_prise",
      COALESCE(dis.discount, 0) AS "discount"
    FROM product_order po
      INNER JOIN "user" usr
        ON usr.user_id = po.user_id
      INNER JOIN CATEGORY aim
        ON aim.category_id = po.order_aim_id
      INNER JOIN CATEGORY status
        ON po.status_id = status.category_id
      INNER JOIN product_instance pi
        ON pi.instance_id = po.product_instance_id
      INNER JOIN DOMAIN D
        ON D.domain_id = pi.domain_id
      INNER JOIN product_region_price prp
        ON pi.price_id = prp.price_id
      INNER JOIN product prod
        ON prp.product_id = prod.product_id
      LEFT JOIN discount_price dp
        ON dp.price_id = prp.price_id
      LEFT JOIN discount dis
        ON dis.discount_id = dp.discount_id AND po.close_date >= dis.start_date AND po.close_date <= dis.end_date
    WHERE po.status_id = 4 AND usr.email = ? AND (po.open_date >= ? :: TIMESTAMP) AND (po.close_date <= ? :: TIMESTAMP)
)
SELECT
  cuo.order_number                                             AS "Number",
  cuo.order_aim                                                AS "Aim",
  cuo.product                                                  AS "Product",
  cuo.open_date                                                AS "Open",
  cuo.close_date                                               AS "Close",
  cuo.default_prise                                            AS "Default prise",
  (cuo.default_prise - cuo.default_prise * cuo.discount / 100) AS "Discounted prise",
  cuo.discount || ''%''                                          AS "Discount"
FROM completed_user_orders cuo
ORDER BY cuo."order_aim_Id";',
   '[
     {
       "id": 0,
       "name": "User e-mail",
       "type": "STRING"
     },
     {
       "id": 1,
       "name": "Start date",
       "type": "DATETIME"
     },
     {
       "id": 2,
       "name": "End date",
       "type": "DATETIME"
     }
   ]' :: JSON);