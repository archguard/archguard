package dev.evolution.complexity;

import com.google.gson.Gson;
import dev.evolution.utils.HttpUtil;

public class RecognizeComplexityService {

    public RecognizeComplexityCheckResult check(String clz, String code) {
        if(code == null && code.isEmpty()) {
            return new RecognizeComplexityCheckResult("code is empty");
        }
        String url = "http://10.127.151.13:8095/api/checks/"+clz;
        //String url = "http://localhost:9000/api/checks/"+clz;
        String res = HttpUtil.post(url,code);
        System.out.println(res);
        Gson gson = new Gson();

        RecognizeComplexityCheckResult checkResult = new RecognizeComplexityCheckResult(res);
        try {
            checkResult = gson.fromJson(res,RecognizeComplexityCheckResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkResult;
    }


}
