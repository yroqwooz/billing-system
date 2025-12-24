CREATE TABLE subscriptions (
                               id UUID PRIMARY KEY,
                               user_id UUID NOT NULL,
                               plan_id UUID NOT NULL,
                               status VARCHAR(20) NOT NULL,
                               start_date DATE NOT NULL,
                               end_date DATE NOT NULL,
                               created_at TIMESTAMP NOT NULL,
                               updated_at TIMESTAMP NOT NULL,
                               version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_subscriptions_user_id ON subscriptions(user_id);
CREATE INDEX idx_subscriptions_plan_id ON subscriptions(plan_id);
CREATE INDEX idx_subscriptions_status ON subscriptions(status);
CREATE INDEX idx_subscriptions_end_date ON subscriptions(end_date);
CREATE INDEX idx_subscriptions_user_status ON subscriptions(user_id, status);

COMMENT ON TABLE subscriptions IS 'User subscriptions to billing plans';
COMMENT ON COLUMN subscriptions.id IS 'Unique subscription identifier';
COMMENT ON COLUMN subscriptions.user_id IS 'User who owns this subscription';
COMMENT ON COLUMN subscriptions.plan_id IS 'Billing plan for this subscription';
COMMENT ON COLUMN subscriptions.status IS 'Subscription status: CREATED, ACTIVE, CANCELLED, EXPIRED';
COMMENT ON COLUMN subscriptions.start_date IS 'Subscription start date';
COMMENT ON COLUMN subscriptions.end_date IS 'Subscription end date';
COMMENT ON COLUMN subscriptions.version IS 'Optimistic locking version';