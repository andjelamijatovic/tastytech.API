CREATE TABLE location (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    created_at datetime DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    name varchar(255) NOT NULL,
    description varchar(255),
    longitude double,
    latitude double,
    phone varchar(255),
    address varchar(255),
    brand_id bigint(20),
    PRIMARY KEY (id),
    CONSTRAINT fk_brand_in_location FOREIGN KEY (brand_id) REFERENCES brand (id)
);