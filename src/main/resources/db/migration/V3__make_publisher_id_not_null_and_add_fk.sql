-- 1) Create Dummy Publisher if it does not exist
INSERT INTO publishers (name)
SELECT 'Dummy Publisher'
    WHERE NOT EXISTS (
    SELECT 1 FROM publishers WHERE name = 'Dummy Publisher'
);

-- 2) Set publisher_id of existing books to Dummy Publisher
UPDATE books
SET publisher_id = (
    SELECT id FROM publishers WHERE name = 'Dummy Publisher'
)
WHERE publisher_id IS NULL;

-- 3) Enforce NOT NULL
ALTER TABLE books
    ALTER COLUMN publisher_id SET NOT NULL;

-- 4) Add FK constraint
ALTER TABLE books
    ADD CONSTRAINT book_fk_publisher_id
        FOREIGN KEY (publisher_id)
            REFERENCES publishers (id)
            ON DELETE RESTRICT;
