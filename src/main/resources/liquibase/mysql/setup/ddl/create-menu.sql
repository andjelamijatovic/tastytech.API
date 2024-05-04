CREATE TABLE menu (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    created_at datetime DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    name varchar(255),
    brand_id bigint(20),
    PRIMARY KEY (id),
    CONSTRAINT fk_brand_in_menu FOREIGN KEY (brand_id) REFERENCES brand (id)
);