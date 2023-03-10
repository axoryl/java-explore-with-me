DROP TABLE IF EXISTS stats;

CREATE TABLE IF NOT EXISTS stats
(
    stats_id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app       VARCHAR(30) NOT NULL,
    uri       VARCHAR(500) NOT NULL,
    ip        varchar(15) NOT NULL,
    timestamp TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_stats_id PRIMARY KEY (stats_id)
);
