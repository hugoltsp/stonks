create schema if not exists stonks;

CREATE TABLE IF NOT EXISTS stonks.subscriber (
  id SERIAL PRIMARY KEY,
  telegram_user_id BIGINT NOT null
);
 
create UNIQUE INDEX subscriber_telegram_user_id_uq_idx ON stonks.subscriber (telegram_user_id);
create INDEX subscriber_telegram_user_id_idx ON stonks.subscriber (telegram_user_id);

CREATE TABLE IF NOT EXISTS stonks.stock  (
  id SERIAL primary KEY,
  name  VARCHAR(255) NOT NULL,
  price  DECIMAL(12,3) NULL,
  change  DECIMAL(12,3) NULL,
  change_percent  DECIMAL(12,3) NULL);
  
create UNIQUE INDEX stock_uq_idx ON stonks.stock (name);
create INDEX stock_idx ON stonks.stock (name);

CREATE TABLE IF NOT EXISTS stonks.subscriber_has_stock (
  subscriber_id BIGINT NOT NULL,
  stock_id BIGINT NOT NULL,
  PRIMARY KEY (subscriber_id, stock_id),
  CONSTRAINT fk_subscriber_has_share_subscriber
    FOREIGN KEY (subscriber_id)
    REFERENCES stonks.subscriber (id),
  CONSTRAINT fk_subscriber_has_share_share1
    FOREIGN KEY (stock_id)
    REFERENCES stonks.stock (id));

create INDEX fk_subscriber_has_stocks_stock1_idx on stonks.subscriber_has_stock (stock_id);
create INDEX fk_subscriber_has_stocks_subscriber_idx on stonks.subscriber_has_stock (subscriber_id);
