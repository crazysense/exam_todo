package myyuk.exam.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Welcome Controller.
 */
@Controller
public class WebController {
    /**
     * The home to be viewed at fist visit.
     * @return redirect.
     */
    @GetMapping(value = "/")
    public String home() {
        return "todo";
    }
}
