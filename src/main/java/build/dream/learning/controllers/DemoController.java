package build.dream.learning.controllers;

import build.dream.common.utils.GsonUtils;
import build.dream.learning.services.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/demo")
public class DemoController {
    @Autowired
    private BranchService branchService;

    @RequestMapping(value = "/test")
    @ResponseBody
    public String test() {
        return GsonUtils.toJson(branchService.obtainBranchInfo());
    }
}
