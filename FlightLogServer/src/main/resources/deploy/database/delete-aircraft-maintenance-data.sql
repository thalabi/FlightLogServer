delete from COMPONENT_COMPONENT_HISTORY;
delete from COMPONENT_HISTORY;
delete from COMPONENT;
delete from PART;
delete from DATABASECHANGELOG where filename like '%aircraft-maintenance%/data/%';
commit;