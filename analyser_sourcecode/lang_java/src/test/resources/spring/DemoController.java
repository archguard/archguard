package com.macro.mall.demo.controller;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.demo.dto.PmsBrandDto;
import com.macro.mall.demo.service.DemoService;
import com.macro.mall.model.PmsBrand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 品牌管理示例controller
 * Created by macro on 2019/4/8.
 */
@Api(tags = "DemoController", description = "品牌管理示例接口")
@Controller
public class DemoController {
    @Autowired
    private DemoService demoService;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(com.macro.mall.demo.controller.DemoController.class);

    @RequestMapping(value = "/brand/listAll", method = RequestMethod.GET)
    public CommonResult<java.util.List<PmsBrand>> getBrandList() {
        return CommonResult.success(demoService.listAllBrand());
    }
}
