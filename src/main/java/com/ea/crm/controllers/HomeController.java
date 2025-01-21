package com.ea.crm.controllers;

import com.ea.crm.dataprovider.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeController {

    @GetMapping("/test")
    public ResponseEntity<ResponseDTO> test() {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData("test");
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
