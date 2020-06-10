-- Table: public.attr_types

-- DROP TABLE public.attr_types;

CREATE TABLE public.attr_types
(
    id integer NOT NULL DEFAULT nextval('attr_types_id_seq'::regclass),
    descr character varying(15) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT attr_types_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.attr_types
    OWNER to postgres;