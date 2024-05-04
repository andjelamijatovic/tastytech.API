CREATE TABLE category_translation (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    created_at datetime DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    translation varchar(255),
    description varchar(255),
    language_id bigint(20),
    category_id bigint(20),
    PRIMARY KEY (id),
    CONSTRAINT fk_language_in_category_translation FOREIGN KEY (language_id) REFERENCES language (id),
    CONSTRAINT fk_category_in_category_translation FOREIGN KEY (category_id) REFERENCES menu_category (id)
);