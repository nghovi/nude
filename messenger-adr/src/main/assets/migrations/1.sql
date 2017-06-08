CREATE TABLE ss_stamp_category(
    category_id TEXT NOT NULL,
    category_name TEXT NOT NUll,
    category_key TEXT NOT NUll,
    category_url TEXT
);

CREATE TABLE ss_stamp(
    stamp_id TEXT NOT NULL,
    stamp_name TEXT NOT NUll,
    stamp_key TEXT NOT NUll,
    stamp_keyword TEXT,
    stamp_url TEXT,
    category_id TEXT NOT NUll
);





