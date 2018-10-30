package tools.demo.service.Impl;

import org.springframework.stereotype.Service;
import tools.demo.service.OneService;
@Service
public class OneServiceImpl implements OneService {
    @Override
    public String getName(String name) {
        return "this name  is :"+name;
    }
}
