-- Table: public.objects

-- DROP TABLE public.objects;

CREATE TABLE public.objects
(
    id integer NOT NULL DEFAULT nextval('objects_id_seq'::regclass),
    type_id integer NOT NULL,
    name character varying(30) COLLATE pg_catalog."default",
    CONSTRAINT objects_pkey PRIMARY KEY (id),
    CONSTRAINT objects_type_id_fkey FOREIGN KEY (type_id)
        REFERENCES public.object_types (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE RESTRICT
        NOT VALID
)

TABLESPACE pg_default;

ALTER TABLE public.objects
    OWNER to postgres;