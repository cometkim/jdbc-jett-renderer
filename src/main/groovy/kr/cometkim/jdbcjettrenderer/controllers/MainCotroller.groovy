package kr.cometkim.jdbcjettrenderer.controllers

import net.sf.jett.transform.ExcelTransformer
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

@Controller
class MainCotroller {
    @GetMapping('/')
    public String index() { 'index' }

    @PostMapping('/render')
    public ResponseEntity<ByteArrayResource> renderTemplate(@RequestParam("template") MultipartFile template) {
        def fileIn = template.getInputStream()
        def fileOut = new ByteArrayOutputStream()

        def context = [:]

        new ExcelTransformer()
            .transform(fileIn, context)
            .write(fileOut)

        ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"${template.originalFilename}\"")
            .body(new ByteArrayResource(fileOut.toByteArray()))
    }
}
