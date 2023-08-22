CREATE TABLE IF NOT EXISTS public.type
(
    id   serial NOT NULL,
    type text   NOT NULL,
    CONSTRAINT pk_type_id PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.dictionary
(
    id    serial NOT NULL,
    title text   NOT NULL,
    type  integer,
    CONSTRAINT pk_dictionary_id PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.note
(
    id         serial NOT NULL,
    word       text   NOT NULL,
    definition text   NOT NULL,
    dictionary integer,
    CONSTRAINT pk_note_id PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.validator
(
    id    serial NOT NULL,
    regex text   NOT NULL,
    type  integer   NOT NULL,
    CONSTRAINT pk_validator_id PRIMARY KEY (id)
);
