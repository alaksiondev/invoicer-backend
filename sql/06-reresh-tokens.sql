CREATE TABLE IF NOT EXISTS t_refresh_tokens(
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id uuid not null,
    enabled boolean not null default true,
    constraint fk_user
        foreign key (user_id)
            references t_user(id)
            ON DELETE CASCADE
)