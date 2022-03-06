ALTER TABLE dubbo_bean ADD COLUMN module_id varchar(36);
ALTER TABLE dubbo_reference_config ADD COLUMN module_id varchar(36);
ALTER TABLE dubbo_service_config ADD COLUMN module_id varchar(36);