package kr.cometkim.jdbcjettrenderer.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MainCotroller {
    @GetMapping('/')
    public String index() { 'index' }
}
