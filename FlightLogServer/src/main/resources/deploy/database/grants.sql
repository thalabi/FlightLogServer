--
-- must run from system account
--
define legacy_schema=flightlog;
--define new_schema_name=flightlogv4;

-- significant_event
grant all on &&legacy_schema..significant_event to &&new_schema_name;

grant all on &&new_schema_name..significant_event to &&legacy_schema;
grant all on &&new_schema_name..significant_event_seq to &&legacy_schema;

-- make_model
grant all on &&legacy_schema..make_model to &&new_schema_name;

grant all on &&new_schema_name..make_model to &&legacy_schema;
grant all on &&new_schema_name..make_model_seq to &&legacy_schema;


-- flight_log
grant all on &&legacy_schema..flight_log to &&new_schema_name;
grant all on &&legacy_schema..flight_log_seq to &&new_schema_name;

grant all on &&new_schema_name..flight_log to &&legacy_schema;
grant all on &&new_schema_name..flight_log_seq to &&legacy_schema;
