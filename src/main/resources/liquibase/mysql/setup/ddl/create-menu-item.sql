CREATE TABLE menu_item (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    created_at datetime DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    price double,
    category_id bigint(20),
    PRIMARY KEY (id),
    CONSTRAINT fk_category_in_item FOREIGN KEY (category_id) REFERENCES menu_category (id)
);