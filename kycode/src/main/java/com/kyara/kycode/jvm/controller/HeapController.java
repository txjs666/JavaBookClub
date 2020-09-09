
package com.kyara.kycode.jvm.controller;

import com.kyara.kycode.jvm.domain.User;
import org.assertj.core.util.Lists;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ：Kyara
 * @date ：Created in 2020/2/8 11:49
 * @description： TODO
 * @modified By：
 * @version: 1.0
 */
@RestController
public class HeapController {
    List<User> list = Lists.newArrayList();

    @GetMapping("/heap")
    public String heap() throws InterruptedException {
        while (true) {
            list.add(new User());
            Thread.sleep(1);
//            Thread.sleep(1);
        }
    }

}

