ALTER TABLE code_framework_dubbo_bean ADD COLUMN module_id varchar(36);
ALTER TABLE code_framework_dubbo_reference_config ADD COLUMN module_id varchar(36);
ALTER TABLE code_framework_dubbo_service_config ADD COLUMN module_id varchar(36);