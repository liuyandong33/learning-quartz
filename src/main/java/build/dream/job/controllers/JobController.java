package build.dream.job.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.job.models.job.RefreshJobsModel;
import build.dream.job.services.JobService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/job")
public class JobController {
    @RequestMapping(value = "/refreshJobs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = RefreshJobsModel.class, serviceClass = JobService.class, serviceMethodName = "refreshJobs", error = "刷新定时任务失败")
    public String refreshJobs() {
        return null;
    }
}
