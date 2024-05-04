CREATE TABLE menu_category (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    name varchar(255),
    description varchar(255),
    created_at datetime DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    parent_category_id bigint(20),
    menu_id bigint(20),
    PRIMARY KEY (id),
    CONSTRAINT fk_parent_category FOREIGN KEY (parent_category_id) REFERENCES menu_category (id),
    CONSTRAINT fk_menu_in_category FOREIGN KEY (menu_id) REFERENCES menu (id)
);