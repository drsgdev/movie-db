-- Table: public.attr

-- DROP TABLE public.attr;

CREATE TABLE public.attr
(
    id integer NOT NULL DEFAULT nextval('attr_id_seq'::regclass),
    type_id integer NOT NULL,
    descr character varying(15) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT attr_pkey PRIMARY KEY (id),
    CONSTRAINT attr_type_id_fkey FOREIGN KEY (type_id)
        REFERENCES public.attr_types (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE RESTRICT
)

TABLESPACE pg_default;

ALTER TABLE public.attr
    OWNER to postgres;