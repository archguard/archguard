package cc.unitmesh.untitled.demo.controller;

import cc.unitmesh.untitled.demo.base.CommonResult;
import cc.unitmesh.untitled.demo.entity.Advertise;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/advertise")
public class AdvertiseController {
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<Advertise>> list() {
        List<Advertise> companyAddressList = new ArrayList<>();
        return CommonResult.success(companyAddressList);
    }
}
