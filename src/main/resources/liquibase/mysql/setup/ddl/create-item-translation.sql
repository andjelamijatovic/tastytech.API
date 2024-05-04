create table item_translation (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    created_at datetime DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    name varchar(255),
    description varchar(255),
    language_id bigint(20),
    item_id bigint(20),
    PRIMARY KEY (id),
    CONSTRAINT fk_language_in_item_translation FOREIGN KEY (language_id) REFERENCES language (id),
    CONSTRAINT fk_item_in_item_translation FOREIGN KEY (item_id) REFERENCES menu_item (id)
);