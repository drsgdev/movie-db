-- Table: public.attr_values

-- DROP TABLE public.attr_values;

CREATE TABLE public.attr_values
(
    id integer NOT NULL DEFAULT nextval('attr_values_id_seq'::regclass),
    attr_id integer NOT NULL,
    obj_id integer NOT NULL,
    val character varying(500) COLLATE pg_catalog."default",
    date_val date,
    CONSTRAINT attr_values_pkey PRIMARY KEY (id),
    CONSTRAINT attr_values_attr_id_fkey FOREIGN KEY (attr_id)
        REFERENCES public.attr (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT attr_values_obj_id_fkey FOREIGN KEY (obj_id)
        REFERENCES public.objects (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE RESTRICT
)

TABLESPACE pg_default;

ALTER TABLE public.attr_values
    OWNER to postgres;