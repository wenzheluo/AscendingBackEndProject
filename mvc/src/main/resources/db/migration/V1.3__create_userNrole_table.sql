CREATE TABLE users_roles (
                             user_id     BIGINT NOT NULL,
                             role_id     BIGINT NOT NULL
);

ALTER TABLE users_roles
    ADD CONSTRAINT users_fk FOREIGN KEY ( user_id )
        REFERENCES users ( id ) ON DELETE CASCADE;

ALTER TABLE users_roles
    ADD CONSTRAINT roles_fk FOREIGN KEY ( role_id )
        REFERENCES roles ( id );

ALTER TABLE users_roles ADD CONSTRAINT users_roles_pk PRIMARY KEY (user_id, role_id);
ALTER TABLE users_roles ADD CONSTRAINT user_role_unique UNIQUE (user_id, role_id);