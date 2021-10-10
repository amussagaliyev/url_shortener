--liquibase formatted sql
--changeset author:arman logicalFilePath:create_table_url_short_path
CREATE TABLE url_hash (
    id BIGINT(8) UNSIGNED NOT NULL AUTO_INCREMENT,
    url VARCHAR(2048) NOT NULL,
    hash VARCHAR(50) NOT NULL,
    last_access TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (id),
    UNIQUE KEY unique_hash (hash)
);
--rollback drop table url_short_path
