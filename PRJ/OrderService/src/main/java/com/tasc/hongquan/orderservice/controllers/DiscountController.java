package com.tasc.hongquan.orderservice.controllers;

import com.tasc.hongquan.orderservice.dto.ResponseObject;
import com.tasc.hongquan.orderservice.models.Discount;
import com.tasc.hongquan.orderservice.services.DiscountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/discounts")
@AllArgsConstructor
public class DiscountController {
    private final DiscountService discountService;

    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addDiscount(@RequestBody Discount discount){
        try{
            discountService.addDiscount(discount);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Add discount successfully", discount)
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("error", e.getMessage(), null)
            );
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseObject> updateDiscount(@RequestBody Discount discount){
        try{
            discountService.updateDiscount(discount);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Update discount successfully", discount)
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("error", e.getMessage(), null)
            );
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteDiscount(@PathVariable int id){
        try{
            discountService.deleteDiscount(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Delete discount successfully", null)
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("error", e.getMessage(), null)
            );
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseObject> getDiscount(@PathVariable int id){
        try{
            Discount discount = discountService.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Get discount successfully", discount)
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("error", e.getMessage(), null)
            );
        }
    }

    @GetMapping("/get-by-code/{code}")
    public ResponseEntity<ResponseObject> getDiscountByCode(@PathVariable String code){
        try{
            Discount discount = discountService.findByCode(code);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Get discount by code successfully", discount)
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("error", e.getMessage(), null)
            );
        }
    }

    @GetMapping("/")
    public ResponseEntity<ResponseObject> getAllDiscounts(){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Get all discounts successfully", discountService.getAllDiscounts())
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("error", e.getMessage(), null)
            );
        }
    }
}
