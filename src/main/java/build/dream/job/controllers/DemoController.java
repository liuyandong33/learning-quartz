package build.dream.job.controllers;

import build.dream.common.utils.GsonUtils;
import build.dream.job.services.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/demo")
public class DemoController {
    @Autowired
    private DemoService demoService;

    @RequestMapping(value = "/obtainBranch")
    @ResponseBody
    public String obtainBranch() {
        return GsonUtils.toJson(demoService.obtainBranch());
    }
}
