-- Table: public.object_types

-- DROP TABLE public.object_types;

CREATE TABLE public.object_types
(
    id integer NOT NULL DEFAULT nextval('object_types_id_seq'::regclass),
    descr character varying(15) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT object_types_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.object_types
    OWNER to postgres;