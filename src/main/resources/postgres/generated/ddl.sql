
    create table inventory.inventory_group (
        id uuid not null,
        name varchar(255),
        recorded timestamp(6),
        state varchar(255) check (state in ('TRACKING','ISSUE','DELIVERED')),
        primary key (id)
    );

    create table inventory.inventory_part (
        id uuid not null,
        name varchar(255),
        encodedName varchar(255),
        state varchar(255) check (state in ('TRACKING','REPLACING','RETURNING','DISCARDED','DELIVERED')),
        recorded timestamp(6),
        primary key (id)
    );

    create table inventory_group_inventory_part (
        InventoryGroup_id uuid not null,
        parts_id uuid not null unique
    );

    alter table if exists inventory_group_inventory_part 
       add constraint FKa2crrk8qock3d4ou0qxl9k2cm 
       foreign key (parts_id) 
       references inventory.inventory_part;

    alter table if exists inventory_group_inventory_part 
       add constraint FK9ho1twi4f3fusc3pl0wlqvt98 
       foreign key (InventoryGroup_id) 
       references inventory.inventory_group;
