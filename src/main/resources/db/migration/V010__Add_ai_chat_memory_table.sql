CREATE TABLE IF NOT EXISTS SPRING_AI_CHAT_MEMORY
(
    conversation_id
                VARCHAR(36) NOT NULL,
    content     TEXT        NOT NULL,
    type        VARCHAR(10) NOT NULL CHECK
        (
        type
            IN
        (
         'USER',
         'ASSISTANT',
         'SYSTEM',
         'TOOL'
            )),
    "timestamp" TIMESTAMP   NOT NULL
);

CREATE INDEX IF NOT EXISTS SPRING_AI_CHAT_MEMORY_CONVERSATION_ID_TIMESTAMP_IDX
    ON SPRING_AI_CHAT_MEMORY (conversation_id, "timestamp");

CREATE VIEW v_chat_history AS
select conversation_id, min(timestamp) as start_timestamp, max(timestamp) as end_timestamp, count(*) as message_count from spring_ai_chat_memory group by conversation_id order by min(timestamp);
