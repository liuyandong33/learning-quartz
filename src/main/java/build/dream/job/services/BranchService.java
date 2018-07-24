package build.dream.job.services;

import build.dream.common.api.ApiRest;
import build.dream.job.repositories.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Service
public class BranchService {
    @Autowired
    private BranchRepository branchRepository;

    @Transactional(readOnly = true)
    public ApiRest obtainBranchInfo() {
        return new ApiRest(branchRepository.findById(BigInteger.ONE), "获取门店信息成功！");
    }
}
