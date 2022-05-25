import createCacheState from "@/utils/utils";
import { queryAllQualityGateProfile } from "@/api/module/profile";

const useQualityGate = createCacheState(queryAllQualityGateProfile, []);
export default useQualityGate;
